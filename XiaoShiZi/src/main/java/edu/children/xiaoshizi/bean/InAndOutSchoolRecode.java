package edu.children.xiaoshizi.bean;


import zuo.biao.library.base.BaseModel;

public class InAndOutSchoolRecode extends BaseModel {

    public String id;
    public String snapPicUrl;
    public String imgPicUrl;
    public long similarity;
    public long lastTime;
    public String triggerTime;

    @Override
    protected boolean isCorrect() {
        return true;
    }


}
