package com.sampling.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.sampling.R;

public class ImeiFrag extends UltimateFragment {
    EditText edImei;

    @Override
    protected int setContentView() {
        return R.layout.frag_imei;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("设备信息");
        getFlexibleBar().setRightText("保存");
        setOnFlexibleClickListener();
        edImei = findViewById(R.id.ed_imei);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        String imei = (String) UltimatePreferenceHelper.get("CommonInfo", new String[]{"simei"}).get("simei");
        edImei.setText("" + imei);
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UltimatePreferenceHelper.editPreference("CommonInfo", new String[]{"simei"}, new Object[]{edImei.getText().toString().trim()});
                toast("保存成功！");
                onLeftClickListener();
            }
        });
    }
}
