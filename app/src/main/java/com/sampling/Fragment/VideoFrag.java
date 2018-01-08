package com.sampling.Fragment;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.bill.ultimatefram.net.RequestFileParams;
import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.JsonFormat;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.dialog.IOSAlertDialog;
import com.bill.ultimatefram.view.dialog.IOSProgressDialog;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.sampling.Common.CommonInfo;
import com.sampling.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zzf on 17-11-20.
 */

public class VideoFrag extends UltimateNetFrag {
    JCameraView jCameraView;
    private String qr_code;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    IOSProgressDialog progressDialog;

    @Override
    public void onConnComplete(String result, int flag, Object... tag) {
        Log.d(TAG, result);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        Map<String, Object> res = JsonFormat.formatJson(result, new String[]{"code", "message"});
        if (Integer.valueOf(res.get("code").toString()) == 200) {
            toast("上传成功");
        } else {
            try {
                toast("" + res.get("message"));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.frag_video;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setVisibility(View.GONE);
        Map<String, Object> argument = getArgument(new String[]{"sqr_code"});
        qr_code = argument.get("sqr_code").toString().trim();
        jCameraView = findViewById(R.id.jcameraview);
        jCameraView.setSaveVideoPath(ExternalFileHelper.getStorageDirectory(true).getPath());
        Log.d(TAG, ExternalFileHelper.getStorageDirectory(true).getPath());
        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_POOR);
        jCameraView.setTip("长按开始录制");
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                onLeftClickListener();
            }
        });
        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {

            }

            @Override
            public void AudioPermissionError() {
                toast("给点录音权限可以?");
            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {

            }

            @Override
            public void recordSuccess(final String url, Bitmap firstFrame) {
                Log.d(TAG, "sess:" + url);
                final IOSAlertDialog dialog = new IOSAlertDialog(getActivity());
                dialog.setTitle("是否上传视频?");
                dialog.setCancelable(true);
                dialog.setOnIOSAlertClickListener(new IOSAlertDialog.OnIOSAlertClickListener() {
                    @Override
                    public void onIOSClick(View v, Object tag, int flag) {
                        if (v.getId() == R.id.tv_positive) {
//                            toast("上传");
                            progressDialog = new IOSProgressDialog(getActivity());
                            progressDialog.setMessage("正在上传...");
                            progressDialog.show();
                            Map<String, Object> userinfo = UltimatePreferenceHelper.get("userinfo", new String[]{"susername", "spwd"});
                            openUrl(CommonInfo.ybdestory, new RequestParams(new String[]{"user", "pw", "样本编号", "销毁时间", "销毁状态"},
                                            new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString(), qr_code, dateFormat.format(new Date()), "已销毁"}),
                                    new RequestFileParams(new String[]{"销毁视频"},
                                            new RequestFileParams.FileParams[]{new RequestFileParams.FileParams(url)}));
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        jCameraView.onPause();
    }
}
