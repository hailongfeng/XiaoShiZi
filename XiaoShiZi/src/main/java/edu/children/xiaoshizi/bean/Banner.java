package edu.children.xiaoshizi.bean;

import java.io.Serializable;

import zuo.biao.library.base.BaseModel;

public class Banner implements Serializable {
    private String id	;
    private String sortNum	;
    private String bannerImage	;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }
}
