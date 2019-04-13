package edu.children.xiaoshizi.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.School_Table;
import edu.children.xiaoshizi.bean.SearchWorldHistory;

public class DbUtils {

    public static List<School> getSchoolByType(int type,String pid){
        if (type==1){
            return SQLite.select().from(School.class)
                    .where(School_Table.type.eq(type))
                    .orderBy(School_Table.sortNum, true)
                    .queryList();
        }else  if (type==2){
            return SQLite.select().from(School.class)
                    .where(School_Table.type.eq(type),School_Table.parentId.eq(pid))
                    .orderBy(School_Table.sortNum, true)
                    .queryList();
        }else if (type==3){
            return SQLite.select().from(School.class)
                    .where(School_Table.type.eq(type),School_Table.gradeId.eq(pid))
                    .orderBy(School_Table.sortNum, true)
                    .queryList();
        }else {
            return new ArrayList<>();
        }
    }

    public static List<SearchWorldHistory> getNewHistoryWord(int num){
        return SQLite.select().from(SearchWorldHistory.class)
                .where()
                .queryList();
    }

    public static  <T extends BaseModel>  List<T> getModelList(Class<T> clazz, SQLOperator... where){
        return SQLite.select().from(clazz)
                .where(where)
                .queryList();
    }

    public static <T extends BaseModel>  void deleteModel(Class<T> clazz){
        SQLite.delete(clazz);
    }
    public static <T extends BaseModel>  void saveModel(T model){
        model.save();
    }
    public static <T extends BaseModel> void saveModelList(List<T> datas){
        for (T data:datas)
            data.save();
    }
}
