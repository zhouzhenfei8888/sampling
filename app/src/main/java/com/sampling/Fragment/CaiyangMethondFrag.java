package com.sampling.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.view.recycleview.UltimateMaterialRecyclerView;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleAdapter;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleHolder;
import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.sampling.Beans.Body;
import com.sampling.Beans.ClassifyBean;
import com.sampling.Beans.MethondBean;
import com.sampling.Common.CommonInfo;
import com.sampling.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zzf on 17-10-23.
 */

public class CaiyangMethondFrag extends UltimateFragment {
    UltimateMaterialRecyclerView recyclerView;
    String classifyStr, methondStr;//assets中的json文件
    ClassifyBean classifyBean;
    MethondBean methondBean;
    Gson gson;
    List<Body> bodyList;

    @Override
    protected int setContentView() {
        return R.layout.frag_methond;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("采样方法");
        setOnFlexibleClickListener();
        recyclerView = findViewById(R.id.recycleview);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
      /*  openUrl(CommonInfo.fanlogin,new RequestParams(new String[]{"account","pw","imei","deviceType","provider"},
                new String[]{"admin","123456", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID),"1","SCKJ"}),1);*/
        getClassify();
        gson = new Gson();
        classifyBean = gson.fromJson(classifyStr,ClassifyBean.class);
        bodyList = new ArrayList<>();
        bodyList.addAll(classifyBean.getBody());
        recyclerView.setAdapter(new MethondAdapter(getActivity(),bodyList,R.layout.item_method));
        recyclerView.setOnItemClickListener(new UltimateRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRecycleItemClickListener(Object o, View view, int position, long id, int type) {
                Log.d(TAG,((Body)o).getTitle());
                replaceFragment(new CaiyangMethondFrag2().setArgument(new String[]{"stitle"},new Object[]{((Body)o).getTitle()}),true);
            }
        });
    }

    private void getClassify() {
        try {
            InputStream inputStream = getActivity().getAssets().open("classify.json");
            BufferedSource buffer = Okio.buffer(Okio.source(inputStream));
            classifyStr = buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MethondAdapter extends UltimateRecycleAdapter<Body> {
        public MethondAdapter(Context context, List<Body> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(Body body, UltimateRecycleHolder holder, int position) {
            holder.setText(R.id.tv_title,body.getTitle());
        }
    }
}
