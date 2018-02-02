package com.sampling.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.view.dialog.IOSProgressDialog;
import com.bill.ultimatefram.view.recycleview.UltimateMaterialRecyclerView;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleAdapter;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleHolder;
import com.google.gson.Gson;
import com.sampling.Beans.MarketList;
import com.sampling.Common.CommonInfo;
import com.sampling.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzf on 18-2-2.
 */

public class MarketNameFrag extends UltimateNetFrag {
    UltimateMaterialRecyclerView recyclerView;
    MarketAdapter marketAdapter;
    List<List<String>> list;
    Gson gson;
    IOSProgressDialog dialog;

    @Override
    public void onConnComplete(String result, int flag, Object... tag) {
        switch (flag) {
            case 1:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (!TextUtils.isEmpty(result)) {
                    MarketList marketList = gson.fromJson(result, MarketList.class);
                    list.addAll(marketList.getData());
                    marketAdapter.notifyDataSetChanged();
                } else {
//                    toast("暂无数据");
                }

                break;
        }
    }

    @Override
    public void onConnError(String result, int flag, Object... tag) {
        super.onConnError(result, flag, tag);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.frag_market_name;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("市场名称");
        setOnFlexibleClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        gson = new Gson();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.lv_market_name);
        recyclerView.setEmptyView(R.layout.empty_view, R.layout.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        marketAdapter = new MarketAdapter(getActivity(), list, R.layout.item_text);
        recyclerView.setAdapter(marketAdapter);
        dialog = new IOSProgressDialog(getActivity());
        dialog.show();
        openUrl(CommonInfo.getdata, new RequestParams(new String[]{"user", "pw", "json"}, new String[]{"caiyang1", "123456", "{table:T_FOOD_MARKET}"}), 1);
        recyclerView.setOnItemClickListener(new UltimateRecycleAdapter.OnItemClickListener() {
            @Override
            public void onRecycleItemClickListener(Object o, View view, int position, long id, int type) {
                String marketid = ((List<String>) o).get(2);
                String marketname = ((List<String>) o).get(3);
                editPreference("marketinfo", new String[]{"smarketname", "smarketid"}, new Object[]{marketname, marketid});
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("marketname", marketname);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
//                onLeftClickListener();
            }
        });
    }

    class MarketAdapter extends UltimateRecycleAdapter<List<String>> {

        public MarketAdapter(Context context, List<List<String>> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(List<String> s, UltimateRecycleHolder holder, int position) {
            holder.setText(R.id.tv_market_name_item, s.get(3));
        }
    }
}
