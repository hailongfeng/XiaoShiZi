package edu.children.xiaoshizi.bean;

import java.io.Serializable;

import zuo.biao.library.base.BaseModel;

/**
 * 监护人
 */
public class Guardian implements Serializable{
    private String parentId	;
    private String parentUserName	;
    private String custody	;
    private String studentName	;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentUserName() {
        return parentUserName;
    }

    public void setParentUserName(String parentUserName) {
        this.parentUserName = parentUserName;
    }

    public String getCustody() {
        return custody;
    }

    public void setCustody(String custody) {
        this.custody = custody;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
