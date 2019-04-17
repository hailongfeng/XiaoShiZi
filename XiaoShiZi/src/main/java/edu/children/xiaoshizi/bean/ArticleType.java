package edu.children.xiaoshizi.bean;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class ArticleType extends BaseModel implements Serializable {
    /**
     * categoryId : 4
     * sortNum : 1
     * type : VT
     * title : 视频的文章
     */
    @PrimaryKey(autoincrement = true)
    private long cid;
    @Column
    private int categoryId;
    @Column
    private int sortNum;
    @Column
    private String type;//分类类型。IT图文列表展示 VT 视频列表展示
    @Column
    private String title;
    @Column
    private String bannerImage;

    @Column
    private int belongTo; //1，首页，2，安全课堂，3，安全实验室

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
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

    public int getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(int belongTo) {
        this.belongTo = belongTo;
    }
}
