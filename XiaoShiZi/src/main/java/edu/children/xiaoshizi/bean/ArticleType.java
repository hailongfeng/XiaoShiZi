package edu.children.xiaoshizi.bean;

import zuo.biao.library.base.BaseModel;

public class ArticleType extends BaseModel {
    /**
     * categoryId : 4
     * sortNum : 1
     * type : VT
     * title : 视频的文章
     */

    private int categoryId;
    private int sortNum;
    private String type;
    private String title;
    private String bannerImage;

    @Override
    protected boolean isCorrect() {
        return true;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }
}
