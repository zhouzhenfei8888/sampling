
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ClassifyBean {

    @SerializedName("body")
    private List<Body> mBody;
    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;

    public List<Body> getBody() {
        return mBody;
    }

    public void setBody(List<Body> body) {
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
