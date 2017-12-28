
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class DocBean {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("body")
    private List<Doc> mDocs;
    @SerializedName("pageIndex")
    private Long mPageIndex;
    @SerializedName("pageSize")
    private Long mPageSize;
    @SerializedName("totalCount")
    private Long mTotalCount;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public List<Doc> getDocs() {
        return mDocs;
    }

    public void setDocs(List<Doc> docs) {
        mDocs = docs;
    }

    public Long getPageIndex() {
        return mPageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        mPageIndex = pageIndex;
    }

    public Long getPageSize() {
        return mPageSize;
    }

    public void setPageSize(Long pageSize) {
        mPageSize = pageSize;
    }

    public Long getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(Long totalCount) {
        mTotalCount = totalCount;
    }

}
