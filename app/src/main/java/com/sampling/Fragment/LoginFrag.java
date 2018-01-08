package com.sampling.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.util.JsonFormat;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.dialog.IOSProgressDialog;
import com.sampling.Common.CommonInfo;
import com.sampling.MainActivity;
import com.sampling.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by zzf on 17-10-23.
 */

public class LoginFrag extends UltimateNetFrag {
    EditText edName, edPw;
    CheckBox checkBox;
    IOSProgressDialog iosProgressDialog;

    @Override
    public void onConnComplete(String result, int flag, Object... tag) {
        Log.d(TAG, result);
        Map responseBean = JsonFormat.formatJson(result, new String[]{"code", "message", "body"});
        int code = (int) responseBean.get("code");
        String sessionid = (String) responseBean.get("body");
        if (iosProgressDialog.isShowing()) {
            iosProgressDialog.dismiss();
        }
        if (code == 200) {
            UltimatePreferenceHelper.editPreference("userinfo", new String[]{"susername", "spwd", "sessionid"},
                    new String[]{edName.getText().toString(), edPw.getText().toString(), sessionid});
            startActivity(MainActivity.class, true);
        } else {
            toast("用户名或密码不正确");
        }
    }

    @Override
    public void onConnError(String result, int flag, Object... tag) {
        if (iosProgressDialog.isShowing()) {
            iosProgressDialog.dismiss();
            toast("请检查网络连接");
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.frag_login;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setVisibility(View.GONE);
        edName = findViewById(R.id.ed_name);
        edPw = findViewById(R.id.ed_pw);
        checkBox = findViewById(R.id.checkbox);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        super.initEvent(savedInstanceState);
        new RxPermissions(getActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "权限已同意");
                    }
                }
        );
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edName.getText()) && !TextUtils.isEmpty(edPw.getText())) {
                    iosProgressDialog = new IOSProgressDialog(getActivity());
                    iosProgressDialog.show();
                    openUrl(CommonInfo.sessionlogin, new RequestParams(new String[]{"appuser", "apppw"}, new String[]{edName.getText().toString(), edPw.getText().toString()}));
                } else {
                    toast("用户名和密码不能为空");
                }
            }
        });
        final Map<String, Object> userinfo = UltimatePreferenceHelper.get("userinfo", new String[]{"susername", "spwd"});
        if (checkBox.isChecked()) {
            edName.setText(userinfo.get("susername").toString());
            edPw.setText(userinfo.get("spwd").toString());
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    edName.setText(userinfo.get("susername").toString());
                    edPw.setText(userinfo.get("spwd").toString());
                } else {
                    edName.setText(userinfo.get("susername").toString());
                    edPw.setText("");
                }
            }
        });
    }
}
