
package com.sampling.Beans;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponseBean {

    @SerializedName("body")
    private SiteInfo mSiteInfo;
    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;

    public SiteInfo getBody() {
        return mSiteInfo;
    }

    public void setBody(SiteInfo body) {
        mSiteInfo = body;
    }

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

}
