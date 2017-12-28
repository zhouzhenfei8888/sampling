package com.sampling;

import android.os.Bundle;
import android.util.Log;

import com.bill.ultimatefram.ui.UltimateActivity;
import com.sampling.Fragment.VideoFrag;

import java.util.Map;

/**
 * Created by zzf on 17-11-20.
 */

public class VideoActivity extends UltimateActivity {
    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        Map<String, Object> map = getExtras("sqr_code");
//        Log.d("VideoActivity++++++",map.get("sqr_code").toString());
        String sqr_code = map.get("sqr_code").toString();
        startFragment(new VideoFrag().setArgument(new String[]{"sqr_code"},new Object[]{sqr_code}), true);
    }

    @Override
    protected boolean canSetSystemBarOnFragment() {
        return false;
    }
}
