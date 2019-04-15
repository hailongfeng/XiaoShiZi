package edu.children.xiaoshizi.db;

import com.raizlabs.android.dbflow.annotation.Database;


@Database(version = XSZDatabase.VERSION, name = XSZDatabase.NAME)
public class XSZDatabase {
    public static final String NAME = "xiaoshizi";
    public static final int VERSION = 2;

}
