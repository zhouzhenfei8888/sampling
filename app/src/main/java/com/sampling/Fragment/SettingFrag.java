package com.sampling.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.sampling.R;

/**
 * Created by zzf on 18-2-2.
 */

public class SettingFrag extends UltimateFragment {
    LinearLayout layoutImeiName, layoutYangpinDestory;
    TextView tvImei;

    @Override
    protected int setContentView() {
        return R.layout.frag_setting;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("用户设置");
        setOnFlexibleClickListener();
        layoutImeiName = findViewById(R.id.lay_imei_name);
        layoutYangpinDestory = findViewById(R.id.lay_yangpin_desroty);
        tvImei = findViewById(R.id.tv_imei);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
/*        Object marketname = getPreference("marketinfo", new String[]{"smarketname"}).get("smarketname");
        if (marketname != null && !marketname.equals("")) {
            tvImei.setText("市场名称(" + marketname.toString() + ")");
        }
        layoutImeiName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentForResult(new MarketNameFrag(), 100);
            }
        });*/

        layoutYangpinDestory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(XiaohuiFrag.class, true);
            }
        });

        layoutImeiName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ImeiFrag(),true);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//            String marketname = data.getStringExtra("marketname");
            String marketname = data.getExtras().getString("marketname");
            tvImei.setText("市场名称(" + marketname + ")");
        }
    }
}
