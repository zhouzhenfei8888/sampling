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
    LinearLayout layoutMarketName, layoutYangpinDestory;
    TextView tvMarketName;

    @Override
    protected int setContentView() {
        return R.layout.frag_setting;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("用户设置");
        setOnFlexibleClickListener();
        layoutMarketName = findViewById(R.id.lay_market_name);
        layoutYangpinDestory = findViewById(R.id.lay_yangpin_desroty);
        tvMarketName = findViewById(R.id.tv_market_name);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        Object marketname = getPreference("marketinfo", new String[]{"smarketname"}).get("smarketname");
        if (marketname != null && !marketname.equals("")) {
            tvMarketName.setText("市场名称(" + marketname.toString() + ")");
        }
        layoutMarketName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragmentForResult(new MarketNameFrag(), 100);
            }
        });

        layoutYangpinDestory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(XiaohuiFrag.class, true);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//            String marketname = data.getStringExtra("marketname");
            String marketname = data.getExtras().getString("marketname");
            tvMarketName.setText("市场名称(" + marketname + ")");
        }
    }
}
