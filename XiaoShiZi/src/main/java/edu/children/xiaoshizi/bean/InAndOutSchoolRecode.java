package edu.children.xiaoshizi.bean;


import java.io.Serializable;

import zuo.biao.library.base.BaseModel;

public class InAndOutSchoolRecode  implements Serializable {

    public String id;
    public String snapPicUrl;
    public String imgPicUrl;
    public long similarity;
    public long lastTime;
    public String triggerTime;

    public Student student;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnapPicUrl() {
        return snapPicUrl;
    }

    public void setSnapPicUrl(String snapPicUrl) {
        this.snapPicUrl = snapPicUrl;
    }

    public String getImgPicUrl() {
        return imgPicUrl;
    }

    public void setImgPicUrl(String imgPicUrl) {
        this.imgPicUrl = imgPicUrl;
    }

    public long getSimilarity() {
        return similarity;
    }

    public void setSimilarity(long similarity) {
        this.similarity = similarity;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
