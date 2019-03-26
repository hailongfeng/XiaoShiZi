package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

import edu.children.xiaoshizi.db.XSZDatabase;

@Table(database = XSZDatabase.class)
public class School extends BaseModel {

    @PrimaryKey
    public String id;
    @Column
    public String pid;
    @Column
    public String schoolName;
    @Column
    public int type;

    @Override
    public String toString() {
        return "School{" +
                "schoolName='" + schoolName + '\'' +
                ", type=" + type +
                '}';
    }
}
