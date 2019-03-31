package edu.children.xiaoshizi.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.School_Table;
import edu.children.xiaoshizi.bean.SearchWorldHistory;

public class DbUtils {

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

    public static List<SearchWorldHistory> getNewHistoryWord(int num){
        return SQLite.select().from(SearchWorldHistory.class)
                .where()
                .queryList();
    }

    public static <T extends BaseModel>  void saveModel(T model){
        model.save();
    }
    public static <T extends BaseModel> void saveModelList(List<T> datas){
        for (T data:datas)
            data.save();
    }
}
