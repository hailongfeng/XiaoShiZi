package edu.children.xiaoshizi.bean.request;

import zuo.biao.library.base.BaseModel;

public class RequestBindStudentParam extends BaseModel {

    /**
     * timestamp : 1558423511000
     * noncestr : noncestr
     * sign : ECA78F26B61B69E70522D3C329B64A67
     * token : e462a2cf-099f-4992-9493-42300aaf86b6
     * parentId : 1
     * userName : 张三
     * sex : M
     * schoolId : suA38j1AxGBjWcUJP4h
     * schoolGradeId : 5eaZmroMMgYZu0kX1nB
     * schoolClassId : uQ8jhJZCPEP1vqA9lQZ
     * birthday : 2019-03-20
     * parentCustody : 爸爸
     * bindingPassword : 1
     */

    private String timestamp;
    private String noncestr;
    private String sign;
    private String token;
    private String parentId;
    private String userName;
    private String sex;
    private String schoolId;
    private String schoolGradeId;
    private String schoolClassId;
    private String birthday;
    private String parentCustody;
    private String bindingPassword;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getParentCustody() {
        return parentCustody;
    }

    public void setParentCustody(String parentCustody) {
        this.parentCustody = parentCustody;
    }

    public String getBindingPassword() {
        return bindingPassword;
    }

    public void setBindingPassword(String bindingPassword) {
        this.bindingPassword = bindingPassword;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
