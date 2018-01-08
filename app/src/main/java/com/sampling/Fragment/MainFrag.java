package com.sampling.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.view.dialog.IOSAlertDialog;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sampling.Beans.AppVersionInfo;
import com.sampling.Beans.EventBean;
import com.sampling.Beans.ResponseBeans;
import com.sampling.Common.CommonInfo;
import com.sampling.HttpClient;
import com.sampling.R;
import com.sampling.Service.ScanService;
import com.sampling.Utils;
import com.sampling.VideoActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Response;

/**
 * Created by zzf on 17-10-23.
 */

public class MainFrag extends UltimateFragment implements View.OnClickListener, View.OnTouchListener {
    FlowingDrawer drawer;
    TextView tvHome, tvOrderlist, tvCaiyangList, tvCaiyangMethond, tvLawer, tvDestory;
    LinearLayout linOrderlist, linCaiyanglist, linCaiyangMethond, linLawer, linDestory;

    private byte mGpioTrig = 0x29;//PC9
    private Disposable genxindisposable;
    private Disposable downdisposable;
    private String versionName;

    @Override
    protected int setContentView() {
        return R.layout.frag_main;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("中德生物");
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        getFlexibleBar().getLeftTextView().setTypeface(typeface);
        getFlexibleBar().getLeftTextView().setText(R.string.text_left_menu);
        drawer = findViewById(R.id.drawerlayout);
        tvHome = findViewById(R.id.tv_home);
        tvOrderlist = findViewById(R.id.tv_orderlist);
        tvCaiyangList = findViewById(R.id.tv_caiyanglist);
        tvCaiyangMethond = findViewById(R.id.tv_caiyangmethond);
        tvLawer = findViewById(R.id.tv_lawer);
        linOrderlist = findViewById(R.id.lin_orderlist);
        linCaiyanglist = findViewById(R.id.lin_caiyanglist);
        linCaiyangMethond = findViewById(R.id.lin_caiyangMethond);
        linLawer = findViewById(R.id.lin_lawer);
        linDestory = findViewById(R.id.lin_destory);
        tvDestory = findViewById(R.id.tv_destory);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        try {
            versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);
        drawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        drawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {

            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {

            }
        });
        tvHome.setOnClickListener(this);
        tvOrderlist.setOnClickListener(this);
        tvCaiyangList.setOnClickListener(this);
        tvCaiyangMethond.setOnClickListener(this);
        tvLawer.setOnClickListener(this);
        tvDestory.setOnClickListener(this);
        linOrderlist.setOnClickListener(this);
        linCaiyanglist.setOnClickListener(this);
        linCaiyangMethond.setOnClickListener(this);
        linLawer.setOnClickListener(this);
        linDestory.setOnClickListener(this);
        checkVersion();
        linDestory.setOnTouchListener(this);
        tvDestory.setOnTouchListener(this);
    }

    private void checkVersion() {
        HttpClient.getInstance().getVersionInfo("caiyang", new Observer<ResponseBeans<AppVersionInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                genxindisposable = d;
            }

            @Override
            public void onNext(ResponseBeans<AppVersionInfo> appVersionInfoResponseBean) {
                String version = appVersionInfoResponseBean.getBody().getVer();//服务器上最新版本
                final String downloadfile = appVersionInfoResponseBean.getBody().getFile();
                float fcurver = Float.valueOf(versionName);
                float fver = Float.valueOf(version);
                Log.d(TAG,""+fcurver+"::::"+fver);
                if (fcurver < fver) {
                    new MaterialDialog.Builder(getActivity())
                            .title("版本更新")
                            .content("有新版本，是否更新?")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    doDownLoad(downloadfile);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    toast("已是最新版本！");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                toast(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void doDownLoad(String file) {
        final String downloadurl = CommonInfo.url2 + file;
        Log.d(TAG, "url:::" + downloadurl);
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .content("正在下载...")
                .canceledOnTouchOutside(false)
                .progress(false, 100,true)
                .progressNumberFormat("%1d/%2d")
                .show();
        ProgressManager.getInstance().addResponseListener(downloadurl, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                dialog.setProgress(progressInfo.getPercent());
                Log.d(TAG,""+progressInfo.getPercent());
            }

            @Override
            public void onError(long id, Exception e) {
                e.printStackTrace();
                Log.d(TAG,e.getMessage());
            }
        });
        HttpClient.getInstance().downLoad(downloadurl, new Observer<Response<ResponseBody>>() {
            @Override
            public void onSubscribe(Disposable d) {
                downdisposable = d;
            }

            @Override
            public void onNext(Response<ResponseBody> responseBodyResponse) {
                BufferedSource bufferedSource = responseBodyResponse.body().source();
                Long total = responseBodyResponse.body().contentLength();
                BufferedSink bufferedSink = null;
                try {
                    bufferedSink = Okio.buffer(Okio.sink(new File(Utils.getApkpath())));
                    bufferedSink.write(bufferedSource.readByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedSink.close();
                        bufferedSource.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                Log.d(TAG, "" + responseBodyResponse.body().contentLength());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                dialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new MaterialDialog.Builder(getActivity())
                                .title("版本更新")
                                .content("是否安装caiyang.apk?")
                                .canceledOnTouchOutside(false)
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        installApk();
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
            }
        });
    }

    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(
                new File(Utils.getApkpath())),
                "application/vnd.android.package-archive");
        getActivity().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home:
                drawer.closeMenu(true);
                break;
            case R.id.tv_orderlist:
                drawer.closeMenu(true);
                replaceFragment(OrderListFrag.class, true);
                break;
            case R.id.tv_caiyanglist:
                drawer.closeMenu(true);
                replaceFragment(CaiyangListFrag.class, true);
                break;
            case R.id.tv_caiyangmethond:
                drawer.closeMenu(true);
                replaceFragment(CaiyangMethondFrag.class, true);
                break;
            case R.id.tv_lawer:
                drawer.closeMenu(true);
                replaceFragment(LawerFrag.class, true);
                break;
            case R.id.tv_destory:
                drawer.closeMenu(true);
//                startActivityForResult(new Intent(getActivity(), ScanActivity.class), 10001);
                break;
            case R.id.lin_orderlist:
                drawer.closeMenu(true);
                replaceFragment(OrderListFrag.class, true);
                break;
            case R.id.lin_caiyanglist:
                drawer.closeMenu(true);
                replaceFragment(CaiyangListFrag.class, true);
                break;
            case R.id.lin_caiyangMethond:
                drawer.closeMenu(true);
                replaceFragment(CaiyangMethondFrag.class, true);
                break;
            case R.id.lin_lawer:
                drawer.closeMenu(true);
                replaceFragment(LawerFrag.class, true);
                break;
            case R.id.lin_destory:
                drawer.closeMenu(true);
//                startActivityForResult(new Intent(getActivity(), ScanActivity.class), 10001);
                //测试
//                startActivity(new Intent(getActivity(),VideoActivity.class));
//                toast("请长按扫码！");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean bean) {
        final String str = bean.getMsg();
        IOSAlertDialog dialog = new IOSAlertDialog(getActivity());
        dialog.setTitle("是否销毁");
        dialog.setMessage(str);
        dialog.setCancelable(true);
        dialog.setOnIOSAlertClickListener(new IOSAlertDialog.OnIOSAlertClickListener() {
            @Override
            public void onIOSClick(View v, Object tag, int flag) {
                if (v.getId() == R.id.tv_positive) {
                    startActivity(VideoActivity.class, new String[]{"sqr_code"}, new Object[]{str}, false);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
/*        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            // handle scan result
            Log.d(TAG, scanResult.toString());
        }*/
   /*     if(requestCode == 10001){
            if(data != null){
                final String str = data.getStringExtra("qr_code");
                IOSAlertDialog dialog = new IOSAlertDialog(getActivity());
                dialog.setTitle("是否销毁");
                dialog.setMessage(str);
                dialog.setCancelable(true);
                dialog.setOnIOSAlertClickListener(new IOSAlertDialog.OnIOSAlertClickListener() {
                    @Override
                    public void onIOSClick(View v, Object tag, int flag) {
                        if(v.getId() == R.id.tv_positive){
                            startActivity(VideoActivity.class,new String[]{"sqr_code"},new Object[]{str},false);
                        }
                    }
                });
                dialog.show();
            }
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (genxindisposable != null && !genxindisposable.isDisposed()) {
            genxindisposable.dispose();
        }
        if (downdisposable != null && !downdisposable.isDisposed()) {
            downdisposable.dispose();
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ScanService.mApi.gpioControl(mGpioTrig, 0, 0);
                break;
            }

            case MotionEvent.ACTION_UP: {
                ScanService.mApi.gpioControl(mGpioTrig, 0, 1);
                break;
            }

            default:

                break;
        }
        return false;
    }
}
