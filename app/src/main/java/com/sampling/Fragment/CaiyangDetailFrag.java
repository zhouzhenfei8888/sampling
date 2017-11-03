package com.sampling.Fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.UltimateImageLoaderHelper;
import com.bill.ultimatefram.view.listview.adapter.CommonAdapter;
import com.bill.ultimatefram.view.listview.adapter.Holder;
import com.sampling.App;
import com.sampling.Beans.SamplingBean;
import com.sampling.Common.BarcodeCreater;
import com.sampling.R;
import com.sampling.dao.DaoSession;
import com.sampling.dao.SamplingBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by zzf on 17-10-30.
 */

public class CaiyangDetailFrag extends UltimateFragment {
    TextView tvOrderNo, tvLeibie, tvMingchen, tvGps, tvCaiyangyuan;
    EditText edShuliang, edCandi;
    ImageView ivSign, ivGiveSign;
    RadioGroup mRadioGroup;
    GridView gv;//现场图片添加

    public DaoSession mdaoSession;
    QueryBuilder<SamplingBean> queryBuilder;
    SamplingBean samplingBean;
    List<String> imagelist;

    private IWoyouService woyouService;

    @Override
    protected int setContentView() {
        return R.layout.frag_caiyangdetail;
    }

    @Override
    protected void initView() {
        tvOrderNo = findViewById(R.id.tv_orderno);
        tvLeibie = findViewById(R.id.tv_leibie);
        tvMingchen = findViewById(R.id.tv_mingchen);
        tvGps = findViewById(R.id.tv_gps);
        tvCaiyangyuan = findViewById(R.id.tv_caiyangyuan);
        edShuliang = findViewById(R.id.ed_shuliang);
        edCandi = findViewById(R.id.ed_candi);
        mRadioGroup = findViewById(R.id.radio_group);
        ivSign = findViewById(R.id.iv_sign);
        ivGiveSign = findViewById(R.id.iv_give_sign);
        gv = findViewById(R.id.gv_photo);
        getFlexibleBar().setTitle("采样单详情");
        getFlexibleBar().setRightText("打印");
        setOnFlexibleClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInsGnceState) {
        Long id = (Long) getArgument(new String[]{"lsamplebeanId"}).get("lsamplebeanId");
        Log.d(TAG, "" + id);
        mdaoSession = ((App) getActivity().getApplication()).mdaoSession;
        queryBuilder = mdaoSession.getSamplingBeanDao().queryBuilder();
        queryBuilder.where(SamplingBeanDao.Properties.Id.eq(id));
        samplingBean = queryBuilder.list().get(0);
        Log.d(TAG, samplingBean.toString());
        tvOrderNo.setText(samplingBean.getRenwuno());
        tvLeibie.setText(samplingBean.getYangpinglb());
        tvMingchen.setText(samplingBean.getYangpingmc());
        tvGps.setText(samplingBean.getGps());
        tvCaiyangyuan.setText(samplingBean.getUser());
        edShuliang.setText(samplingBean.getCaiyangshuliang());
        edCandi.setText(samplingBean.getCandi());
        if (samplingBean.getStrogemethond().equals("常温")) {
            Log.d(TAG, "常温");
            ((RadioButton) findViewById(R.id.radio_normal)).setChecked(true);
            ((RadioButton) findViewById(R.id.radio_cool)).setChecked(false);
        } else {
            ((RadioButton) findViewById(R.id.radio_cool)).setChecked(true);
            ((RadioButton) findViewById(R.id.radio_normal)).setChecked(false);
            Log.d(TAG, "冷藏");
        }
        UltimateImageLoaderHelper.loadImage(ExternalFileHelper.getStorageDirectory().getPath() + File.separator + samplingBean.getSignpath(), ivSign, UltimateImageLoaderHelper.LoadType.STORAGE);
        UltimateImageLoaderHelper.loadImage(ExternalFileHelper.getStorageDirectory().getPath() + File.separator + samplingBean.getGivesignpath(), ivGiveSign, UltimateImageLoaderHelper.LoadType.STORAGE);
        String imagestr = samplingBean.getImages();
        String[] images = imagestr.split(",");
        imagelist = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            imagelist.add(images[i]);
        }
        initPrint();
        gv.setAdapter(new MyImageAdapter(getActivity(), imagelist, R.layout.myphoto));
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.print_view, null);
                ((TextView) view.findViewById(R.id.tv_caiyangno)).setText(samplingBean.getCaiyanno());
                ((TextView) view.findViewById(R.id.tv_orderno)).setText(samplingBean.getRenwuno());
                ((TextView) view.findViewById(R.id.tv_leibie)).setText(samplingBean.getYangpinglb());
                ((TextView) view.findViewById(R.id.tv_mingchen)).setText(samplingBean.getYangpingmc());
                ((TextView) view.findViewById(R.id.tv_gps)).setText(samplingBean.getGps());
                ((TextView) view.findViewById(R.id.tv_shuliang)).setText(samplingBean.getCaiyangshuliang());
                ((TextView) view.findViewById(R.id.tv_storge)).setText(samplingBean.getStrogemethond());
                ((TextView) view.findViewById(R.id.tv_caiyangyuan)).setText(samplingBean.getUser());
                ((TextView) view.findViewById(R.id.tv_candi)).setText(samplingBean.getCandi());
                final Bitmap mBitmap = BarcodeCreater.encode2dAsBitmap(samplingBean.getCaiyanno(), 200, 200, 2);

                ((ImageView) view.findViewById(R.id.iv_qr)).setImageBitmap(mBitmap);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view).setCancelable(true).create().show();
                ((TextView) view.findViewById(R.id.tv_print)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        printBitmap(image);
                        StringBuilder sb = new StringBuilder();
                        sb.append("-------------------------------");
                        sb.append("采样编号：" + samplingBean.getCaiyanno());
                        sb.append("\n");
                        sb.append("任务编号：" + samplingBean.getRenwuno());
                        sb.append("\n");
                        sb.append("样品类别：" + samplingBean.getYangpinglb());
                        sb.append("\n");
                        sb.append("样品名称：" + samplingBean.getYangpingmc());
                        sb.append("\n");
                        sb.append("GPS：" + samplingBean.getGps());
                        sb.append("\n");
                        sb.append("采样数量：" + samplingBean.getCaiyangshuliang());
                        sb.append("\n");
                        sb.append("存储方式：" + samplingBean.getStrogemethond());
                        sb.append("\n");
                        sb.append("采样员：" + samplingBean.getUser());
                        sb.append("\n");
                        sb.append("来源产地：" + samplingBean.getCandi());
                        sb.append("\n");
                        sb.append("\n");
                        if(woyouService != null){
                            try {
                                woyouService.setAlignment(0,null);
                                woyouService.printText(sb.toString(),callback);
                                woyouService.lineWrap(1,null);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private void initPrint() {
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        getActivity().startService(intent);//启动打印服务
        getActivity().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    ICallback callback = new ICallback.Stub() {

        @Override
        public void onRunResult(boolean success) throws RemoteException {
            toast("success");
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
            toast(value);
        }

        @Override
        public void onRaiseException(int code, final String msg)
                throws RemoteException {
            toast(msg);
        }
    };


    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        if (width >= newWidth) {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {

            Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
                    bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

            return bitmap2;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyImageAdapter extends CommonAdapter<String> {
        public MyImageAdapter(Context context, List<String> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(String s, Holder holder, int position) {
            Log.d(TAG, ExternalFileHelper.getPath(s, true));
            UltimateImageLoaderHelper.loadImage(ExternalFileHelper.getPath(s, true), (ImageView) holder.getView(R.id.iv), UltimateImageLoaderHelper.LoadType.STORAGE);
        }
    }
}
