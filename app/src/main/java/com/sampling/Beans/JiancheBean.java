
package com.sampling.Beans;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class JiancheBean {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("body")
    private Sitinfo mSitinfo;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Sitinfo getSitinfo() {
        return mSitinfo;
    }

    public void setSitinfo(Sitinfo sitinfo) {
        mSitinfo = sitinfo;
    }

}
