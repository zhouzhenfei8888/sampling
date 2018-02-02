package com.sampling.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.sampling.R;

import java.util.Map;

/**
 * Created by zzf on 18-2-2.
 */

public class XiaohuiFrag extends UltimateFragment {

    EditText edXiaohuiPerson, edXiaohuiChenjie, edXiaohuiAddress;

    @Override
    protected int setContentView() {
        return R.layout.frag_xiaohui;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("样品销毁");
        getFlexibleBar().setRightText("保存");
        setOnFlexibleClickListener();
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });
        edXiaohuiPerson = findViewById(R.id.ed_xiaohui_person);
        edXiaohuiChenjie = findViewById(R.id.ed_xiaohui_chenjie);
        edXiaohuiAddress = findViewById(R.id.ed_xiaohui_address);
    }

    private void doSave() {
        editPreference("xiaohui", new String[]{"sxiaohuiperson", "sxiaohuichenjie", "sxiaohuiaddress"},
                new Object[]{getTextViewText(edXiaohuiPerson, ""), getTextViewText(edXiaohuiChenjie, ""),
                        getTextViewText(edXiaohuiAddress, "")});
        onLeftClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        Map<String, Object> map = getPreference("xiaohui", new String[]{"sxiaohuiperson", "sxiaohuichenjie", "sxiaohuiaddress"});
        edXiaohuiAddress.setText(map.get("sxiaohuiaddress").toString());
        edXiaohuiPerson.setText(map.get("sxiaohuiperson").toString());
        edXiaohuiChenjie.setText(map.get("sxiaohuichenjie").toString());
    }
}
