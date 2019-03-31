package edu.children.xiaoshizi.bean;

import java.io.Serializable;
import java.util.List;

public class LoginRespon implements Serializable {

    private List<Student> students;
    private List<Parent> parents;
    private User loginResp;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    public User getLoginResp() {
        return loginResp;
    }

    public void setLoginResp(User loginResp) {
        this.loginResp = loginResp;
    }
}
