package edu.children.xiaoshizi.bean;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.List;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class Article extends BaseModel implements Serializable {

    /**
     * contentId : AuLrL81CQQ6aiDIqVIr
     * pushAppTitle : 安全课件测试
     * activityVideoUrl : https://single-obs.obs.cn-east-2.myhuaweicloud.com/app_media/2019031214193256206.mp4
     * activityVideoImageUrl : https://single-obs.obs.cn-east-2.myhuaweicloud.com/app_pic/2019031214364271658.png
     * contentType : VT
     */
    @PrimaryKey(autoincrement = true)
    private long cid;
    @Column
    private String contentId;
    @Column
    private int categoryId;//类别id
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
    private String contentType;//内容类型。IT 图文，VT 视频
    @Column
    private int likedNumber;//点赞数
    @Column
    private int commentsNumber; //评论数
    @Column
    private int shareNumber; //分享数
    @Column
    private String shareUrl;

    private List<ArticleComment> commentResps;
    private boolean isSelected=false;
    private boolean isShow=false;

    public Article() {
    }



    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public List<ArticleComment> getCommentResps() {
        return commentResps;
    }

    public void setCommentResps(List<ArticleComment> commentResps) {
        this.commentResps = commentResps;
    }

    public int getLikedNumber() {
        return likedNumber;
    }

    public void setLikedNumber(int likedNumber) {
        this.likedNumber = likedNumber;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                "cid=" + cid +
                ", contentId='" + contentId + '\'' +
                ", categoryId=" + categoryId +
                ", pushAppTitle='" + title + '\'' +
                ", bannerImage='" + bannerImage + '\'' +
                ", introduce='" + introduce + '\'' +
                ", activityVideoUrl='" + activityVideoUrl + '\'' +
                ", activityVideoImageUrl='" + activityVideoImageUrl + '\'' +
                ", contentType='" + contentType + '\'' +
                ", likedNumber=" + likedNumber +
                ", shareNumber=" + shareNumber +
                ", shareUrl='" + shareUrl + '\'' +
                ", commentResps=" + commentResps +
                ", isSelected=" + isSelected +
                ", isShow=" + isShow +
                '}';
    }


}
