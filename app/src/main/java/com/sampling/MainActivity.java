package com.sampling;

import android.os.Bundle;

import com.bill.ultimatefram.ui.UltimateActivity;
import com.sampling.Fragment.MainFrag;

public class MainActivity extends UltimateActivity {

    private String TAG = "MainActivity";

    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        startFragment(new MainFrag(), true);
    }

    @Override
    protected boolean canSetSystemBarOnFragment() {
        return false;
    }
}
