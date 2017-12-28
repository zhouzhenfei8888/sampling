
package com.sampling.Beans;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Body {

    @SerializedName("children")
    private List<Child> mChildren;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("value")
    private String mValue;

    public List<Child> getChildren() {
        return mChildren;
    }

    public void setChildren(List<Child> children) {
        mChildren = children;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
