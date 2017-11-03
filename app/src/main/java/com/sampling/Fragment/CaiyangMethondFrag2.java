package com.sampling.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.view.recycleview.UltimateMaterialRecyclerView;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleAdapter;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleHolder;
import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.sampling.Beans.Body;
import com.sampling.Beans.Child;
import com.sampling.Beans.ClassifyBean;
import com.sampling.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zzf on 17-10-30.
 */

public class CaiyangMethondFrag2 extends UltimateFragment {
    UltimateMaterialRecyclerView recyclerView;
    private String classifyStr;
    Gson gson;
    ClassifyBean classifyBean;
    List<Child> childList;

    @Override
    protected int setContentView() {
        return R.layout.frag_caiyangmethond;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycleview);
        getFlexibleBar().setTitle("采样方法");
        setOnFlexibleClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        childList = new ArrayList<>();
        getClassify();
        String stitle = (String) getArgument(new String[]{"stitle"}).get("stitle");
        gson = new Gson();
        classifyBean = gson.fromJson(classifyStr, ClassifyBean.class);
        List<Body> bodyList = classifyBean.getBody();
        for (Body body : bodyList) {
            if (body.getTitle().equals(stitle)) {
                childList.clear();
                childList.addAll(body.getChildren());
            }
        }
        recyclerView.setAdapter(new MethodAdapter(getActivity(),childList,R.layout.item_method));
        recyclerView.setOnItemClickListener(new UltimateRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRecycleItemClickListener(Object o, View view, int position, long id, int type) {
                replaceFragment(new CaiyangMethondFrag3().setArgument(new String[]{"stitle"},new Object[]{((Child)o).getTitle()}),true);
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

    private class MethodAdapter extends UltimateRecycleAdapter<Child> {
        public MethodAdapter(Context context, List<Child> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(Child child, UltimateRecycleHolder holder, int position) {
            holder.setText(R.id.tv_title,child.getTitle());
        }
    }
}
