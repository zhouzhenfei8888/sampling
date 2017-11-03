
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class 下属 {

    @SerializedName("\u4e0b\u5c5e")
    private List<下属> m下属;
    @SerializedName("\u540d\u79f0")
    private String m名称;
    @SerializedName("\u644a\u4f4d\u53f7")
    private String m摊位号;
    @SerializedName("\u7c7b\u578b")
    private String m类型;
    @SerializedName("\u7f16\u7801")
    private String m编码;

    public List<下属> get下属() {
        return m下属;
    }

    public void set下属(List<下属> 下属) {
        m下属 = 下属;
    }

    public String get名称() {
        return m名称;
    }

    public void set名称(String 名称) {
        m名称 = 名称;
    }

    public String get摊位号() {
        return m摊位号;
    }

    public void set摊位号(String 摊位号) {
        m摊位号 = 摊位号;
    }

    public String get类型() {
        return m类型;
    }

    public void set类型(String 类型) {
        m类型 = 类型;
    }

    public String get编码() {
        return m编码;
    }

    public void set编码(String 编码) {
        m编码 = 编码;
    }

}
