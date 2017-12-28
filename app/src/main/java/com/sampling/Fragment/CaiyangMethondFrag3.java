package com.sampling.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.google.gson.Gson;
import com.sampling.Beans.MethondBean;
import com.sampling.Beans.抽样方法;
import com.sampling.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zzf on 17-10-30.
 */

public class CaiyangMethondFrag3 extends UltimateFragment {
    TextView tvTitle, tvContent;
    private String methondStr;
    MethondBean methondBean;

    @Override
    protected int setContentView() {
        return R.layout.frag_caiyangmethond3;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("采样方法详情");
        setOnFlexibleClickListener();
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        String stitle = (String) getArgument(new String[]{"stitle"}).get("stitle");
        tvTitle.setText(stitle);
        getMethond();
        Gson gson = new Gson();
        methondBean = gson.fromJson(methondStr, MethondBean.class);
        抽样方法 cyff = methondBean.get抽样方法();
        try {
            Method method = cyff.getClass().getMethod("get"+stitle);
            String content= (String) method.invoke(cyff);
            tvContent.setText(content);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void getMethond() {
        try {
            InputStream inputStream = getActivity().getAssets().open("methond.json");
            BufferedSource buffer = Okio.buffer(Okio.source(inputStream));
            methondStr = buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
