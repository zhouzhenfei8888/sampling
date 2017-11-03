
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class OrderListBean {

    @SerializedName("body")
    private List<OrderInfo> mOrderInfo;
    @SerializedName("code")
    private Long mCode;
    @SerializedName("message")
    private String mMessage;

    public List<OrderInfo> getBody() {
        return mOrderInfo;
    }

    public void setBody(List<OrderInfo> orderInfo) {
        mOrderInfo = orderInfo;
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
