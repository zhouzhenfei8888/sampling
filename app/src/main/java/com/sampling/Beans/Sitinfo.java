
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Sitinfo {

    @SerializedName("\u540d\u79f0")
    private String m名称;
    @SerializedName("\u7c7b\u578b")
    private String m类型;
    @SerializedName("\u7f16\u7801")
    private String m编码;
    @SerializedName("\u4e0b\u5c5e")
    private List<com.sampling.Beans.菜市场> m菜市场;

    public String get名称() {
        return m名称;
    }

    public void set名称(String 名称) {
        m名称 = 名称;
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

    public List<com.sampling.Beans.菜市场> get菜市场() {
        return m菜市场;
    }

    public void set菜市场(List<com.sampling.Beans.菜市场> 菜市场) {
        m菜市场 = 菜市场;
    }

}
