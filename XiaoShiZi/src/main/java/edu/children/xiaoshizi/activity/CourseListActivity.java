package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gyf.barlibrary.ImmersionBar;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.CourseListItemView;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseAdapter;

public class CourseListActivity extends XszBaseActivity {

    @BindView(R.id.multiStatusLayout)
    MultiStatusLayout multiStatusLayout;
    @BindView(R.id.lvBaseList)
    GridView gridView;
    BaseAdapter adapter;
    public static final String ARTICLETYPE = "articleType";
    private ArticleType articleType;
    private List<Article> articles;

    public static Intent createIntent(Context context,ArticleType articleType) {
        return new Intent(context, CourseListActivity.class).putExtra(ARTICLETYPE, articleType);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        articleType = (ArticleType) intent.getSerializableExtra(ARTICLETYPE);
        setContentView(R.layout.activity_course_list);
    }

    @Override
    public void initView() {//必须在onCreate方法内调用
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        adapter= new BaseAdapter<Article, CourseListItemView>(context){

            @Override
            public CourseListItemView createView(int viewType, ViewGroup parent) {
                return new CourseListItemView(context, R.layout.list_item_course_view);
            }
        };
        gridView.setAdapter(adapter);
    }

    @Override
    public void initData() {//必须在onCreate方法内调用
        tvBaseTitle.setText(articleType.getTitle());
        getListCourse();
    }

    private void getListCourse() {
        showLoading(R.string.loading);
        TreeMap sm = new TreeMap<String,String>();
        sm.put("categoryId",articleType.getCategoryId());
        LogicService.post(context, APIMethod.loadSeClassRoomContentByCategory,sm, new ApiSubscriber<Response<List<Article>>>() {
            @Override
            public void onSuccess(Response<List<Article>> respon) {
                hideLoading();
                articles=respon.getResult();
                if (articles.size()==0){
                    multiStatusLayout.showEmpty();
                }else {
                    multiStatusLayout.showContent();
                    adapter.refresh(articles);
                }
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    @Override
    public void initEvent() {//必须在onCreate方法内调用
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
    }

}