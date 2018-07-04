package com.sampling.Beans;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by zzf on 17-10-26.
 * 采样单信息
 */
@Entity
public class SamplingBean {
    @Id
    Long id;
    String isupload;
    String caiyanno;
    String renwuno;
    String yangpinglb;
    String yangpingmc;
    String yangpingmc2;
    String gps;
    String caiyangshuliang;
    String time;
    String strogemethond;
    String user;
    String pwd;
    String candi;
    String signpath;
    String givesignpath;
    String images;
    String cscm;//菜市场名
    String twh;//摊位号
    String detail;//样本详情
    @Generated(hash = 12590058)
    public SamplingBean(Long id, String isupload, String caiyanno, String renwuno,
            String yangpinglb, String yangpingmc, String yangpingmc2, String gps,
            String caiyangshuliang, String time, String strogemethond, String user,
            String pwd, String candi, String signpath, String givesignpath,
            String images, String cscm, String twh, String detail) {
        this.id = id;
        this.isupload = isupload;
        this.caiyanno = caiyanno;
        this.renwuno = renwuno;
        this.yangpinglb = yangpinglb;
        this.yangpingmc = yangpingmc;
        this.yangpingmc2 = yangpingmc2;
        this.gps = gps;
        this.caiyangshuliang = caiyangshuliang;
        this.time = time;
        this.strogemethond = strogemethond;
        this.user = user;
        this.pwd = pwd;
        this.candi = candi;
        this.signpath = signpath;
        this.givesignpath = givesignpath;
        this.images = images;
        this.cscm = cscm;
        this.twh = twh;
        this.detail = detail;
    }
    @Generated(hash = 1614502257)
    public SamplingBean() {
    }
    public String getCaiyanno() {
        return this.caiyanno;
    }
    public void setCaiyanno(String caiyanno) {
        this.caiyanno = caiyanno;
    }
    public String getRenwuno() {
        return this.renwuno;
    }
    public void setRenwuno(String renwuno) {
        this.renwuno = renwuno;
    }
    public String getYangpingmc() {
        return this.yangpingmc;
    }
    public void setYangpingmc(String yangpingmc) {
        this.yangpingmc = yangpingmc;
    }
    public String getGps() {
        return this.gps;
    }
    public void setGps(String gps) {
        this.gps = gps;
    }
    public String getCaiyangshuliang() {
        return this.caiyangshuliang;
    }
    public void setCaiyangshuliang(String caiyangshuliang) {
        this.caiyangshuliang = caiyangshuliang;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getStrogemethond() {
        return this.strogemethond;
    }
    public void setStrogemethond(String strogemethond) {
        this.strogemethond = strogemethond;
    }
    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getCandi() {
        return this.candi;
    }
    public void setCandi(String candi) {
        this.candi = candi;
    }
    public String getSignpath() {
        return this.signpath;
    }
    public void setSignpath(String signpath) {
        this.signpath = signpath;
    }
    public String getGivesignpath() {
        return this.givesignpath;
    }
    public void setGivesignpath(String givesignpath) {
        this.givesignpath = givesignpath;
    }
    public String getImages() {
        return this.images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public String getIsupload() {
        return this.isupload;
    }
    public void setIsupload(String isupload) {
        this.isupload = isupload;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getYangpinglb() {
        return this.yangpinglb;
    }
    public void setYangpinglb(String yangpinglb) {
        this.yangpinglb = yangpinglb;
    }

    @Override
    public String toString() {
        return "SamplingBean{" +
                "isupload='" + isupload + '\'' +
                ", caiyanno='" + caiyanno + '\'' +
                ", renwuno='" + renwuno + '\'' +
                ", yangpinglb='" + yangpinglb + '\'' +
                ", yangpingmc='" + yangpingmc + '\'' +
                ", gps='" + gps + '\'' +
                ", caiyangshuliang='" + caiyangshuliang + '\'' +
                ", time='" + time + '\'' +
                ", strogemethond='" + strogemethond + '\'' +
                ", user='" + user + '\'' +
                ", pwd='" + pwd + '\'' +
                ", candi='" + candi + '\'' +
                ", signpath='" + signpath + '\'' +
                ", givesignpath='" + givesignpath + '\'' +
                ", images='" + images + '\'' +
                '}';
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCscm() {
        return this.cscm;
    }
    public void setCscm(String cscm) {
        this.cscm = cscm;
    }
    public String getTwh() {
        return this.twh;
    }
    public void setTwh(String twh) {
        this.twh = twh;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getYangpingmc2() {
        return this.yangpingmc2;
    }
    public void setYangpingmc2(String yangpingmc2) {
        this.yangpingmc2 = yangpingmc2;
    }
}
