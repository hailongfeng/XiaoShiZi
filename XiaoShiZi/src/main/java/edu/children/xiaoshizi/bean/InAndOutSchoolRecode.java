package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class InAndOutSchoolRecode extends BaseModel {

    @PrimaryKey
    public String id;
    @Column
    public String snapPicUrl;
    @Column
    public String imgPicUrl;
    @Column
    public long similarity;
    @Column
    public long lastTime;
    @Column
    public String triggerTime;
}
