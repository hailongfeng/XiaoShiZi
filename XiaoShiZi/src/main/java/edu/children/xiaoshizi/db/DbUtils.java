package edu.children.xiaoshizi.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.School_Table;

public class DbUtils {
    public static void saveSchool(School school){
        school.save();
    }
    public static void saveSchool(List<School> schools){
        for (School school:schools)
            school.save();
    }
    public static List<School> getSchoolByType(int type,String pid){
        if (type==1){
            return SQLite.select().from(School.class)
                    .where(School_Table.type.eq(type))
                    .queryList();
        }else {
            return SQLite.select().from(School.class)
                    .where(School_Table.type.eq(type),School_Table.parentId.eq(pid))
                    .queryList();
        }

    }

}
