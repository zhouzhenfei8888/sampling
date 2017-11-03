
package com.sampling.Beans;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MethondBean {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("\u62bd\u6837\u65b9\u6cd5")
    private com.sampling.Beans.抽样方法 m抽样方法;

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

    public com.sampling.Beans.抽样方法 get抽样方法() {
        return m抽样方法;
    }

    public void set抽样方法(com.sampling.Beans.抽样方法 抽样方法) {
        m抽样方法 = 抽样方法;
    }

}
