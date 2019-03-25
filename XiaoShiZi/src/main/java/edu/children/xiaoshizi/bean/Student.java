package edu.children.xiaoshizi.bean;

import zuo.biao.library.base.BaseModel;

public class Student extends BaseModel {
    private String studentId	;
    private String custody	;
    private String birthday	;
    private String sex	;
    private String studentName	;
    private String schoolId	;
    private String schoolGradeId	;
    private String schoolClassId	;
    private String schoolName	;
    private String schoolClassName;
    private String schoolGradeName	;
    private String headPortrait	;

    @Override
    protected boolean isCorrect() {
        return true;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCustody() {
        return custody;
    }

    public void setCustody(String custody) {
        this.custody = custody;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolGradeId() {
        return schoolGradeId;
    }

    public void setSchoolGradeId(String schoolGradeId) {
        this.schoolGradeId = schoolGradeId;
    }

    public String getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(String schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolClassName() {
        return schoolClassName;
    }

    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
    }

    public String getSchoolGradeName() {
        return schoolGradeName;
    }

    public void setSchoolGradeName(String schoolGradeName) {
        this.schoolGradeName = schoolGradeName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
