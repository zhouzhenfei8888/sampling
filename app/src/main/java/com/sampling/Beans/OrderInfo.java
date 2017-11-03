
package com.sampling.Beans;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class OrderInfo {

    @SerializedName("xid")
    private String mXid;
    @SerializedName("\u4e0b\u8fbe\u65e5\u671f")
    private String m下达日期;
    @SerializedName("\u4efb\u52a1\u76ee\u6807")
    private String m任务目标;
    @SerializedName("\u4efb\u52a1\u7f16\u53f7")
    private String m任务编号;
    @SerializedName("\u5907\u6ce8")
    private String m备注;
    @SerializedName("\u62bd\u68c0\u6570\u91cf")
    private Long m抽检数量;
    @SerializedName("\u62bd\u68c0\u6837\u672c")
    private String m抽检样本;
    @SerializedName("\u62bd\u68c0\u9879\u76ee")
    private String m抽检项目;
    @SerializedName("\u72b6\u6001")
    private String m状态;
    @SerializedName("\u76ee\u6807\u5b8c\u6210\u65e5\u671f")
    private String m目标完成日期;

    public String getXid() {
        return mXid;
    }

    public void setXid(String xid) {
        mXid = xid;
    }

    public String get下达日期() {
        return m下达日期;
    }

    public void set下达日期(String 下达日期) {
        m下达日期 = 下达日期;
    }

    public String get任务目标() {
        return m任务目标;
    }

    public void set任务目标(String 任务目标) {
        m任务目标 = 任务目标;
    }

    public String get任务编号() {
        return m任务编号;
    }

    public void set任务编号(String 任务编号) {
        m任务编号 = 任务编号;
    }

    public String get备注() {
        return m备注;
    }

    public void set备注(String 备注) {
        m备注 = 备注;
    }

    public Long get抽检数量() {
        return m抽检数量;
    }

    public void set抽检数量(Long 抽检数量) {
        m抽检数量 = 抽检数量;
    }

    public String get抽检样本() {
        return m抽检样本;
    }

    public void set抽检样本(String 抽检样本) {
        m抽检样本 = 抽检样本;
    }

    public String get抽检项目() {
        return m抽检项目;
    }

    public void set抽检项目(String 抽检项目) {
        m抽检项目 = 抽检项目;
    }

    public String get状态() {
        return m状态;
    }

    public void set状态(String 状态) {
        m状态 = 状态;
    }

    public String get目标完成日期() {
        return m目标完成日期;
    }

    public void set目标完成日期(String 目标完成日期) {
        m目标完成日期 = 目标完成日期;
    }

}
