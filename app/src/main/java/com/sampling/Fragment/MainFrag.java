package com.sampling.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.posapi.PosApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.sampling.R;

/**
 * Created by zzf on 17-10-23.
 */

public class MainFrag extends UltimateFragment implements View.OnClickListener{
    FlowingDrawer drawer;
    TextView tvHome,tvOrderlist,tvCaiyangList,tvCaiyangMethond,tvLawer;
    LinearLayout linOrderlist,linCaiyanglist,linCaiyangMethond,linLawer;

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
        linOrderlist.setOnClickListener(this);
        linCaiyanglist.setOnClickListener(this);
        linCaiyangMethond.setOnClickListener(this);
        linLawer.setOnClickListener(this);
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
        }
    }
}
