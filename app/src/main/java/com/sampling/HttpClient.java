package com.sampling;

import com.sampling.Beans.AppVersionInfo;
import com.sampling.Beans.ResponseBeans;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zzf on 18-1-2.
 */

public class HttpClient {
    private static final String BASE_URL = "https://fsplatform.com/";
    private static final int DEFAULT_TIMEOUT = 2;
    private Retrofit retrofit;
    private ApiService apiService;

    public HttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = ProgressManager.getInstance().with(builder).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private static class sinalInstance {
        public static final HttpClient instance = new HttpClient();
    }

    public static HttpClient getInstance() {
        return sinalInstance.instance;
    }

    public void getVersionInfo(String id, Observer<ResponseBeans<AppVersionInfo>> observer) {
        apiService.getVersionInfo(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void downLoad(String url, Observer<Response<ResponseBody>> observer) {
        apiService.downLoad(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(observer);
    }
}
