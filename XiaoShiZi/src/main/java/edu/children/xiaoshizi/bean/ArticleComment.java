package edu.children.xiaoshizi.bean;

import java.io.Serializable;

public class ArticleComment implements Serializable {

    /**
     * commentId : 1
     * commentParentId : 0
     * contentId : AuLrL81CQQ6aiDIqVIr
     * headPortrait : https://obs-shine-creativity.obs.cn-east-2.myhuaweicloud.com:443/app_pic/head_portrait/1/2019041315344646110.png?x-image-process=image/sharpen,100/quality,Q_90/resize,m_lfit,w_80,h_80
     * userName : 李大鹏
     * createTime : 2019-03-25 14:41:26
     * commentContent : 评论第一条
     */

    public static final String comment_type_Liked="Liked";//评论类型。Liked 点赞，Comment 内容回复，Share 分享
    public static final String comment_type_Comment="Comment";
    public static final String comment_type_Share="Share";

    private int commentId;
    private int commentParentId;
    private String contentId;
    private String headPortrait;
    private String userName;
    private String createTime;
    private String commentContent;



    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(int commentParentId) {
        this.commentParentId = commentParentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
