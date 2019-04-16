package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class Banner extends BaseModel implements Serializable {

    @PrimaryKey
    private String id	;
    @Column
    private String sortNum	;
    @Column
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
