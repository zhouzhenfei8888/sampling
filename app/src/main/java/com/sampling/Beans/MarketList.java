
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MarketList {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("column")
    private List<String> mColumn;
    @SerializedName("count")
    private Long mCount;
    @SerializedName("data")
    private List<List<String>> mData;
    @SerializedName("table")
    private String mTable;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public List<String> getColumn() {
        return mColumn;
    }

    public void setColumn(List<String> column) {
        mColumn = column;
    }

    public Long getCount() {
        return mCount;
    }

    public void setCount(Long count) {
        mCount = count;
    }

    public List<List<String>> getData() {
        return mData;
    }

    public void setData(List<List<String>> data) {
        mData = data;
    }

    public String getTable() {
        return mTable;
    }

    public void setTable(String table) {
        mTable = table;
    }

}
