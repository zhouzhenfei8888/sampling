
package com.sampling.Beans;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponseBeans<T> {

    @SerializedName("body")
    private T mBody;
    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;

    public T getBody() {
        return mBody;
    }

    public void setBody(T body) {
        mBody = body;
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
