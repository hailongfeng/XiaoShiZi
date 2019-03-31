package edu.children.xiaoshizi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.SearchWordHistoryAdapter;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import edu.children.xiaoshizi.db.DbUtils;

public class SearchArticleActivity extends XszBaseActivity {

    @BindView(R.id.lvBaseList)
    ListView lv_search_word_history;
    SearchWordHistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article);
    }

    @Override
    public void initView() {
        adapter = new SearchWordHistoryAdapter(context);
        lv_search_word_history.setAdapter(adapter);
    }

    @Override
    public void initData() {
        List<SearchWorldHistory> data=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SearchWorldHistory d=new SearchWorldHistory();
            d.name="关键字"+i;
            d.time=new Date().getTime();
            d.save();
            data.add(d);
        }
        adapter.refresh(data);
    }

    @Override
    public void initEvent() {

    }
}
