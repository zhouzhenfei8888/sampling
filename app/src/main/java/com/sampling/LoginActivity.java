package com.sampling;

import android.os.Bundle;

import com.bill.ultimatefram.ui.UltimateActivity;
import com.sampling.Fragment.LoginFrag;

/**
 * Created by zzf on 17-10-23.
 */

public class LoginActivity extends UltimateActivity {
    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        startFragment(new LoginFrag(),true);
    }

    @Override
    protected boolean canSetSystemBarOnFragment() {
        return false;
    }
}
