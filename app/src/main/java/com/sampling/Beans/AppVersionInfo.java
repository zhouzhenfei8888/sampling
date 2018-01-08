package com.sampling.Beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zzf on 18-1-2.
 */

public class AppVersionInfo {
    @SerializedName("ver")
    String ver;
    @SerializedName("file")
    String file;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
