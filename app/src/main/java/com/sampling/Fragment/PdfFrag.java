package com.sampling.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.dialog.IOSProgressDialog;
import com.github.barteksc.pdfviewer.PDFView;
import com.sampling.Common.CommonInfo;
import com.sampling.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zzf on 17-10-31.
 */

public class PdfFrag extends UltimateFragment {
    private String sessionid;
    private Call callPdf;
    private OkHttpClient okHttpClient;
    PDFView pdfView;
    IOSProgressDialog progressDialog;

    @Override
    protected int setContentView() {
        return R.layout.frag_pdf;
    }

    @Override
    protected void initView() {
        pdfView = findViewById(R.id.pdfview);
        getFlexibleBar().setTitle("法律法规");
        setOnFlexibleClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        sessionid = (String) UltimatePreferenceHelper.get("userinfo", new String[]{"sessionid"}).get("sessionid");
        String spdf = (String) getArgument(new String[]{"spdf"}).get("spdf");
        progressDialog = new IOSProgressDialog(getActivity());
        progressDialog.show();
        pdfView.setEnabled(false);
        getPdf(spdf);
    }

    private void getPdf(String pdfname) {
        okHttpClient = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder()
                .add("k", pdfname)
                .build();
        Request request = new Request.Builder()
                .url(CommonInfo.getpdf)
                .post(formbody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("cookie", "_SID_=" + sessionid)
                .build();
        callPdf = okHttpClient.newCall(request);
        callPdf.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, e.getMessage());
                toast(e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final byte[] bytes = response.body().bytes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pdfView.fromBytes(bytes)
                                    .defaultPage(0)
                                    .enableSwipe(true)
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .defaultPage(0)
                                    .enableAnnotationRendering(false)
                                    .password(null)
                                    .scrollHandle(null)
                                    .load();
                            pdfView.setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callPdf.cancel();
    }
}
