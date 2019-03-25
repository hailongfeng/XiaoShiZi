package zblibrary.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import zblibrary.demo.R;
import zblibrary.demo.model.db.Student;
import zblibrary.demo.model.db.Student_Table;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.Log;

public class DbFlowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_flow);
        add();
        find();
        change();
        find();
        delete();
        find();
    }

    void add(){
        Student mUser = new Student("fhl",30);
        boolean save = mUser.save();
        showShortToast(save ? "保存成功" : "添加失败");
    }
    void find(){
        List<Student> users = SQLite.select().from(Student.class).queryList();// 查询所有记录
        for (Student s:users){
            Log.d(TAG,s.toString());
        }
    }
    void change(){
        SQLite.update(Student.class)
                .set(Student_Table.age.eq(20))
                .where(Student_Table.name.eq("fhl"))
                .query();
    }
    void delete(){
        SQLite.delete()
                .from(Student.class)
                .where(Student_Table.name.eq("fhl"))
                .execute();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
