package edu.children.xiaoshizi.bean;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class Article extends BaseModel implements Serializable {

    /**
     * contentId : AuLrL81CQQ6aiDIqVIr
     * title : 安全课件测试
     * activityVideoUrl : https://single-obs.obs.cn-east-2.myhuaweicloud.com/app_media/2019031214193256206.mp4
     * activityVideoImageUrl : https://single-obs.obs.cn-east-2.myhuaweicloud.com/app_pic/2019031214364271658.png
     * contentType : VT
     */
    @PrimaryKey
    private String contentId;
    @Column
    private String title;
    @Column
    private String bannerImage;
    @Column
    private String introduce;
    @Column
    private String activityVideoUrl;
    @Column
    private String activityVideoImageUrl;
    @Column
    private String contentType;

    private boolean isSelected=false;
    private boolean isShow=false;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public String toString() {
        return "Article{" +
                "contentId='" + contentId + '\'' +
                ", title='" + title + '\'' +
                ", bannerImage='" + bannerImage + '\'' +
                ", introduce='" + introduce + '\'' +
                ", activityVideoUrl='" + activityVideoUrl + '\'' +
                ", activityVideoImageUrl='" + activityVideoImageUrl + '\'' +
                ", contentType='" + contentType + '\'' +
                ", isSelected=" + isSelected +
                ", isShow=" + isShow +
                '}';
    }
}
