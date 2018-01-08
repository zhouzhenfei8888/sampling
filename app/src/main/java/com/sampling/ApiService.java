package com.sampling;

import com.sampling.Beans.AppVersionInfo;
import com.sampling.Beans.ResponseBeans;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zzf on 18-1-2.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("update/do/anon.latest")
    Observable<ResponseBeans<AppVersionInfo>> getVersionInfo(@Field("id") String id);

    @GET
    @Streaming
    Observable<Response<ResponseBody>> downLoad(@Url String url);
}
