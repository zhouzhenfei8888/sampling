package com.sampling.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleAdapter;
import com.bill.ultimatefram.view.recycleview.adapter.UltimateRecycleHolder;
import com.google.gson.Gson;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.sampling.Beans.Doc;
import com.sampling.Beans.DocBean;
import com.sampling.Common.CommonInfo;
import com.sampling.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zzf on 17-10-23.
 */

public class LawerFrag extends UltimateFragment implements EasyRefreshLayout.EasyEvent {
    RecyclerView recyclerView;
    EasyRefreshLayout easyRefreshLayout;
    OkHttpClient okHttpClient;
    Call call, callPdf;
    Gson gson;
    DocBean docBean;
    List<Doc> docList;
    DocAdapter docAdapter;
    String sessionid;
    Long mPageIndex = 1l;
    Long mTotalCount;

    @Override
    protected int setContentView() {
        return R.layout.frag_lawer;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("法律法规");
        setOnFlexibleClickListener();
        easyRefreshLayout = findViewById(R.id.easylayout);
        recyclerView = findViewById(R.id.recycleview);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        docList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(docAdapter = new DocAdapter(getActivity(), docList, R.layout.item_lawer));
        sessionid = (String) UltimatePreferenceHelper.get("userinfo", new String[]{"sessionid"}).get("sessionid");
        gson = new Gson();
        Log.d(TAG, sessionid);
        RequestMsg(mPageIndex);
        easyRefreshLayout.addEasyEvent(this);
        docAdapter.setOnItemClickListener(new UltimateRecycleAdapter.OnItemClickListener<Doc>() {
            @Override
            public void onRecycleItemClickListener(Doc doc, View view, int position, long id, int type) {
                try {
                    if ("pdf".equals(doc.get文件().split("\\.")[1].trim())) {
                        replaceFragment(new PdfFrag().setArgument(new String[]{"spdf"}, new Object[]{((Doc) doc).get文件()}), true);
                    } else {
                        toast("请确定是pdf文件");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void RequestMsg(Long pageIndex) {
        okHttpClient = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder()
                .add("entity", "文档")
                .add("cat", "法律法规")
                .add("pageSize", "20")
                .add("pageIndex", String.valueOf(pageIndex))
                .build();

        Request request = new Request.Builder()
                .url(CommonInfo.getdoclist)
                .post(formbody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("cookie", "_SID_=" + sessionid)
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, json);
                        try {
                            docBean = gson.fromJson(json, DocBean.class);
                            Log.d(TAG, "" + docBean.getDocs().size());
                            mPageIndex = docBean.getPageIndex();
                            mPageIndex++;
                            mTotalCount = docBean.getTotalCount();
                            docList.addAll(docBean.getDocs());
                            docAdapter.notifyDataSetChanged();
                            easyRefreshLayout.loadMoreComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
        }
        if (callPdf != null) {
            callPdf.cancel();
        }
    }

    @Override
    public void onLoadMore() {
        RequestMsg(mPageIndex);
    }

    @Override
    public void onRefreshing() {
        easyRefreshLayout.refreshComplete();
    }

    private class DocAdapter extends UltimateRecycleAdapter<Doc> {
        public DocAdapter(Context context, List<Doc> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(Doc doc, UltimateRecycleHolder holder, int position) {
            try {
                holder.setText(R.id.tv_title, doc.get文件().split("\\.")[0] + "." + doc.get文件().split("\\.")[1]);
                holder.setText(R.id.tv_date, doc.get上传时间().split(" ")[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
