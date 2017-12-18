
package com.sampling.Beans;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Doc {

    @SerializedName("xid")
    private String mXid;
    @SerializedName("\u4e0a\u4f20\u65f6\u95f4")
    private String m上传时间;
    @SerializedName("\u6587\u4ef6")
    private String m文件;

    public String getXid() {
        return mXid;
    }

    public void setXid(String xid) {
        mXid = xid;
    }

    public String get上传时间() {
        return m上传时间;
    }

    public void set上传时间(String 上传时间) {
        m上传时间 = 上传时间;
    }

    public String get文件() {
        return m文件;
    }

    public void set文件(String 文件) {
        m文件 = 文件;
    }

}
