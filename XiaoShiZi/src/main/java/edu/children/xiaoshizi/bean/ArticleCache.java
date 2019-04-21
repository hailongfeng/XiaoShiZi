package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.annotation.Table;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class ArticleCache extends Article{

//    private String bannerImage;
//    private String introduce;
//    private String activityVideoUrl;
//    private String activityVideoImageUrl;
//    private String contentType;
//    private int likedNumber;
//    private int shareNumber;

    public ArticleCache() {
    }
    public ArticleCache(Article article) {
        super();
        this.setContentId(article.getContentId());
        this.setCategoryId(article.getCategoryId());
        this.setTitle(article.getTitle());
        this.setBannerImage(article.getBannerImage());
        this.setIntroduce(article.getIntroduce());
        this.setActivityVideoUrl(article.getActivityVideoUrl());
        this.setActivityVideoImageUrl(article.getActivityVideoImageUrl());
        this.setContentType(article.getContentType());
        this.setLikedNumber(article.getLikedNumber());
        this.setShareNumber(article.getShareNumber());
    }
}
