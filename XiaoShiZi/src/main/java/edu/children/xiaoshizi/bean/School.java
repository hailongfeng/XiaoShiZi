package edu.children.xiaoshizi.bean;

import java.io.Serializable;

import zuo.biao.library.base.BaseModel;

public class School implements Serializable  {
    private String id;
    private String schoolName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
