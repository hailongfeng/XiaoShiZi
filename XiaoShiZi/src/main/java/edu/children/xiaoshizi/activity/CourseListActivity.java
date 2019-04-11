package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.CourseListAdapter;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.model.Entry;
import zuo.biao.library.ui.GridAdapter;
import zuo.biao.library.util.Log;

public class CourseListActivity extends BaseListActivity<Article, GridView, CourseListAdapter> {

    public static final String ARTICLETYPE = "articleType";
    public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";
    private ArticleType articleType;
    private List<Article> articles;

    public static Intent createIntent(Context context,ArticleType articleType) {
        return new Intent(context, CourseListActivity.class).putExtra(ARTICLETYPE, articleType);
    }
    private int range = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        intent = getIntent();
        articleType = (ArticleType) intent.getSerializableExtra(ARTICLETYPE);
        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        onRefresh();
    }

    @Override
    public void initView() {//必须在onCreate方法内调用
        super.initView();

    }

    @Override
    public void setList(List<Article> list) {
        setList(new AdapterCallBack<CourseListAdapter>() {

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }

            @Override
            public CourseListAdapter createAdapter() {
                return new CourseListAdapter(context);
            }
        });
    }

    @Override
    public void initData() {//必须在onCreate方法内调用
        super.initData();
        tvBaseTitle.setText(articleType.getTitle());
        showShortToast("range = " + range);
    }

    @Override
    public void getListAsync(int page) {
        showProgressDialog(R.string.loading);
        TreeMap sm = new TreeMap<String,String>();
        sm.put("categoryId",articleType.getCategoryId());
        LogicService.post(context, APIMethod.loadSeClassRoomContentByCategory,sm, new ApiSubscriber<Response<List<Article>>>() {
            @Override
            public void onSuccess(Response<List<Article>> respon) {
                articles=respon.getResult();
                onLoadSucceed(page, articles);
            }

            @Override
            protected void onFail(Throwable  error) {
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    @Override
    public void initEvent() {//必须在onCreate方法内调用
        super.initEvent();

        //如果adapter类型是zuo.biao.library.base.BaseAdapter，这两句就不用写了
        lvBaseList.setOnItemClickListener(this);
        lvBaseList.setOnItemLongClickListener(this);
    }

    //示例代码：ItemView点击和长按事件处理 <<<<<<<<<<<<<<<<<<<
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(context,ArticleDetailActivity.class);
        Article article= articles.get(position);
        intent.putExtra("article",article);
        intent.putExtra("articleType",articleType);
        String title=article.getTitle();
        intent.putExtra(INTENT_TITLE,title);
        toActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showShortToast("长按了 " + position);
        return true;
    }
}