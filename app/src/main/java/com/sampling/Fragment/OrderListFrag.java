package com.sampling.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.listview.OnRefreshListener;
import com.bill.ultimatefram.view.recycleview.UltimateMaterialRecyclerView;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleAdapter;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.sampling.Beans.OrderInfo;
import com.sampling.Beans.OrderListBean;
import com.sampling.Common.CommonInfo;
import com.sampling.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zzf on 17-10-23.
 */

public class OrderListFrag extends UltimateNetFrag implements OnRefreshListener {
    UltimateMaterialRecyclerView recyclerView;
    List<OrderInfo> orderInfoList;
    Map<String, Object> userinfo;
    OrderAdapter orderAdapter;

    @Override
    protected int setContentView() {
        return R.layout.frag_orderlist;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("任务信息");
        recyclerView = findViewById(R.id.lv);
        setOnFlexibleClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        userinfo = UltimatePreferenceHelper.get("userinfo", new String[]{"susername", "spwd"});
        openUrl(CommonInfo.getOrderList, new RequestParams(new String[]{"user", "pw"}, new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString()}));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        orderInfoList = new ArrayList<>();
        recyclerView.setAdapter(orderAdapter = new OrderAdapter(getActivity(), orderInfoList, R.layout.order_item));
        recyclerView.setOnRefreshListener(this);
        recyclerView.setOnItemClickListener(new UltimateRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRecycleItemClickListener(Object o, View view, int position, long id, int type) {
                replaceFragment(new AddCaiyangFrag().setArgument(new String[]{"sorderno"},new Object[]{((OrderInfo)o).get任务编号()}),true);
            }
        });
//        recyclerView.setEmptyView(R.layout.empty_view,R.layout.empty_view);
    }

    @Override
    public void onConnComplete(String result, int flag, Object... tag) {
        Log.d(TAG, result);
        Gson gson = new GsonBuilder().create();
        OrderListBean orderListBean = gson.fromJson(result, OrderListBean.class);
        if (orderListBean.getCode() == 200) {
            orderInfoList.clear();
            orderInfoList.addAll(orderListBean.getBody());
            Log.d(TAG,"listsize:"+orderInfoList.size());
            recyclerView.onRefreshComplete();
            orderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        openUrl(CommonInfo.getOrderList, new RequestParams(new String[]{"user", "pw"}, new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString()}));
    }

    private class OrderAdapter extends UltimateRecycleAdapter<OrderInfo> {
        public OrderAdapter(Context context, List<OrderInfo> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(OrderInfo orderInfo, UltimateRecycleHolder holder, int position) {
            holder.setText(R.id.tv_orderno, "任务编号: " + orderInfo.get任务编号());
            holder.setText(R.id.tv_yangben, "抽检样本: " + orderInfo.get抽检样本());
            holder.setText(R.id.tv_xiangmu, "抽检项目: " + orderInfo.get抽检项目());
            holder.setText(R.id.tv_num, "抽检数量: " + orderInfo.get抽检数量());
            holder.setText(R.id.tv_time, "发布时间: " + orderInfo.get下达日期().split(" ")[0] + " | 到期时间:" + orderInfo.get目标完成日期().split(" ")[0]);
            holder.setText(R.id.tv_order_target, "发布人: " + orderInfo.get任务目标());
        }
    }
}
