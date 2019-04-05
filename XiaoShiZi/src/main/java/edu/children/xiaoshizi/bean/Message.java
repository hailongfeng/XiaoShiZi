package edu.children.xiaoshizi.bean;

public class Message extends InAndOutSchoolRecode {
    public String title;

    public Message() {
    }
    public Message(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
