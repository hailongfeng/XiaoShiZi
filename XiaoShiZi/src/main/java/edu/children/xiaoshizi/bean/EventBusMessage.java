package edu.children.xiaoshizi.bean;

import java.io.Serializable;

public class EventBusMessage<T> implements Serializable {
    public static final int Type_User_info_change=0;
    public static final int Type_binding_student =1;
    public static final int Type_user_login=2;
    public static final int Type_user_logout=3;
    public static final int Type_user_real_name_auth=4;
    public static final int Type_unbinding_student=5;
    public static final int Type_message_new=6;
    public static final int Type_message_FeedBack=7;
    public static final int Type_article_comment=8;
    public static final int Type_login_ineffective=9;
    private int type;
    private String message;
    private T data;

    public EventBusMessage() {
    }

    public EventBusMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public EventBusMessage(int type, String message, T data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
