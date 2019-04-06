package edu.children.xiaoshizi.bean;

import zuo.biao.library.base.BaseModel;

public class Article extends BaseModel {

    /**
     * contentId : AuLrL81CQQ6aiDIqVIr
     * title : 安全课件测试
     * activityVideoUrl : https://single-obs.obs.cn-east-2.myhuaweicloud.com/app_media/2019031214193256206.mp4
     * activityVideoImageUrl : https://single-obs.obs.cn-east-2.myhuaweicloud.com/app_pic/2019031214364271658.png
     * contentType : VT
     */

    private String contentId;
    private String title;
    private String bannerImage;
    private String introduce;
    private String activityVideoUrl;
    private String activityVideoImageUrl;
    private String contentType;

    @Override
    protected boolean isCorrect() {
        return true;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActivityVideoUrl() {
        return activityVideoUrl;
    }

    public void setActivityVideoUrl(String activityVideoUrl) {
        this.activityVideoUrl = activityVideoUrl;
    }

    public String getActivityVideoImageUrl() {
        return activityVideoImageUrl;
    }

    public void setActivityVideoImageUrl(String activityVideoImageUrl) {
        this.activityVideoImageUrl = activityVideoImageUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
