package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.structure.BaseModel;

public class QianDao extends BaseModel {
    public int day;
    public int jiFen;
    public int count;
    public int signedDayNum;//签到天数

    public QianDao() {
    }

    public QianDao(int day, int jiFen, int count) {
        this.day = day;
        this.jiFen = jiFen;
        this.count = count;
    }
}
