package com.sampling.Common;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zzf on 17-10-24.
 * "account","pw","imei","deviceType","provider"
 */

public class CommonUtils {
    public static String getToken(Activity activity){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("account", "admin")
                .add("pw","1234")
                .add("imei", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID))
                .build();
        Request request = new Request.Builder()
                .url(CommonInfo.sessionlogin)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
        return "";
    }
}
