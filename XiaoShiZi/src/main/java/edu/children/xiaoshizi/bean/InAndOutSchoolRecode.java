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
    public String studentId;
    public String studentName;
    public String snapRemark;//消息描述
    public String snapStatus;//消息状态。normal 正常，error 异常

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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSnapRemark() {
        return snapRemark;
    }

    public void setSnapRemark(String snapRemark) {
        this.snapRemark = snapRemark;
    }

    public String getSnapStatus() {
        return snapStatus;
    }

    public void setSnapStatus(String snapStatus) {
        this.snapStatus = snapStatus;
    }
}
