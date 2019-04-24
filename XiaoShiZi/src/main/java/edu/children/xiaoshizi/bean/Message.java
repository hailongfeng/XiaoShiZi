package edu.children.xiaoshizi.bean;

public class Message extends InAndOutSchoolRecode {
    public String pushAppTitle;
    public String pushAppTime;

    public Message() {
    }
    public Message(String id, String pushAppTitle) {
        this.id = id;
        this.pushAppTitle = pushAppTitle;
    }

    public String getPushAppTitle() {
        return pushAppTitle;
    }

    public void setPushAppTitle(String pushAppTitle) {
        this.pushAppTitle = pushAppTitle;
    }

    public String getPushAppTime() {
        return pushAppTime;
    }

    public void setPushAppTime(String pushAppTime) {
        this.pushAppTime = pushAppTime;
    }
}
