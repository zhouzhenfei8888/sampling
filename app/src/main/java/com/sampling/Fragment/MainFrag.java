package com.sampling.Fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.view.dialog.IOSAlertDialog;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sampling.R;
import com.sampling.ScanActivity;
import com.sampling.VideoActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by zzf on 17-10-23.
 */

public class MainFrag extends UltimateFragment implements View.OnClickListener{
    FlowingDrawer drawer;
    TextView tvHome,tvOrderlist,tvCaiyangList,tvCaiyangMethond,tvLawer,tvDestory;
    LinearLayout linOrderlist,linCaiyanglist,linCaiyangMethond,linLawer,linDestory;

    @Override
    protected int setContentView() {
        return R.layout.frag_main;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("中德生物");
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"iconfont.ttf");
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
        new RxPermissions(getActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.ACCESS_FINE_LOCATION).subscribe(
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG,"权限已同意");
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_home:
                drawer.closeMenu(true);
                break;
            case R.id.tv_orderlist:
                drawer.closeMenu(true);
                replaceFragment(OrderListFrag.class,true);
                break;
            case R.id.tv_caiyanglist:
                drawer.closeMenu(true);
                replaceFragment(CaiyangListFrag.class,true);
                break;
            case R.id.tv_caiyangmethond:
                drawer.closeMenu(true);
                replaceFragment(CaiyangMethondFrag.class,true);
                break;
            case R.id.tv_lawer:
                drawer.closeMenu(true);
                replaceFragment(LawerFrag.class,true);
                break;
            case R.id.tv_destory:
                drawer.closeMenu(true);
                startActivityForResult(new Intent(getActivity(), ScanActivity.class), 10001);
                break;
            case R.id.lin_orderlist:
                drawer.closeMenu(true);
                replaceFragment(OrderListFrag.class,true);
                break;
            case R.id.lin_caiyanglist:
                drawer.closeMenu(true);
                replaceFragment(CaiyangListFrag.class,true);
                break;
            case R.id.lin_caiyangMethond:
                drawer.closeMenu(true);
                replaceFragment(CaiyangMethondFrag.class,true);
                break;
            case R.id.lin_lawer:
                drawer.closeMenu(true);
                replaceFragment(LawerFrag.class,true);
                break;
            case R.id.lin_destory:
                drawer.closeMenu(true);
                startActivityForResult(new Intent(getActivity(), ScanActivity.class), 10001);
                //测试
//                startActivity(new Intent(getActivity(),VideoActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10001){
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
        }
    }
}
