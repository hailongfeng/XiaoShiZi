package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.structure.BaseModel;

public class QianDao extends BaseModel {
    public int day;
    public int jiFen;

    public QianDao() {
    }

    public QianDao(int day, int jiFen) {
        this.day = day;
        this.jiFen = jiFen;
    }
}
