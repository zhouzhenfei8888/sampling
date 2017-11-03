package com.sampling.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.view.listview.OnRefreshListener;
import com.bill.ultimatefram.view.recycleview.UltimateMaterialRecyclerView;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleAdapter;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleHolder;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.sampling.Beans.SamplingBean;
import com.sampling.App;
import com.sampling.R;
import com.sampling.dao.DaoSession;

import java.util.List;

/**
 * Created by zzf on 17-10-23.
 */

public class CaiyangListFrag extends UltimateFragment implements OnRefreshListener {
    UltimateMaterialRecyclerView recyclerView;
    public DaoSession mdaoSession;
    List<SamplingBean> samplingBeanList;
    CaiyangListAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.frag_caiyanglist;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("采样单列表");
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        getFlexibleBar().getRightTextView().setTypeface(typeface);
        getFlexibleBar().getRightTextView().setText("新增采样单");
        getFlexibleBar().getRightTextView().setTextSize(40f);
        setOnFlexibleClickListener();
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(AddCaiyangFrag.class, true);
            }
        });
        recyclerView = findViewById(R.id.recycleview);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        mdaoSession = ((App) getActivity().getApplication()).mdaoSession;
        samplingBeanList = mdaoSession.getSamplingBeanDao().loadAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter = new CaiyangListAdapter(getActivity(), samplingBeanList, R.layout.list_caiyang_item));
        recyclerView.setOnRefreshListener(this);
        recyclerView.setOnItemClickListener(new UltimateRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRecycleItemClickListener(Object o, View view, int position, long id, int type) {
                replaceFragment(new CaiyangDetailFrag().setArgument(new String[]{"lsamplebeanId"},new Object[]{samplingBeanList.get(position).getId()}),true);
            }
        });
//        recyclerView.setEmptyView(R.layout.empty_view,R.layout.empty_view);
    }

    @Override
    public void onRefresh() {
        samplingBeanList.clear();
        samplingBeanList.addAll(mdaoSession.getSamplingBeanDao().loadAll());
        Log.d(TAG, "" + samplingBeanList.size());
        adapter.notifyDataSetChanged();
        recyclerView.onRefreshComplete();
    }

    private class CaiyangListAdapter extends UltimateRecycleAdapter<SamplingBean> {
        public CaiyangListAdapter(Context context, List<SamplingBean> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(SamplingBean samplingBean, UltimateRecycleHolder holder, int position) {
            holder.setText(R.id.tv_caiyangno, "采样单编号:" + samplingBean.getCaiyanno());
            holder.setText(R.id.tv_orderno, "任务编号:" + samplingBean.getRenwuno());
            if (samplingBean.getIsupload().equals("0")) {
                holder.setText(R.id.tv_done, "未上传");
                holder.setTextColor(R.id.tv_done, Color.parseColor("#ff0000"));
            } else {
                holder.setText(R.id.tv_done, "已上传");
                holder.setTextColor(R.id.tv_done, Color.parseColor("#a8dcf6"));
            }
            holder.setText(R.id.tv_person_date, "采样人: " + samplingBean.getUser() + "   日期: " + samplingBean.getTime());
        }
    }
}
