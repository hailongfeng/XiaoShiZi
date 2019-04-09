package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.flyco.roundview.RoundTextView;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.adapter.SearchWordHistoryAdapter;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseView;

public class SearchArticleActivity extends XszBaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.edit_query)
    EditText edit_query;
    @BindView(R.id.txt_cancle)
    RoundTextView txt_cancle;

    @BindView(R.id.multiStatusLayout)
    MultiStatusLayout multiStatusLayout;

    @BindView(R.id.rvBaseRecycler)
    RecyclerView rvBaseRecycler;
    private ArticleAdapter articleAdapter;
    private List<Article> articles=new ArrayList<>();
    private ArticleType articleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_article);
    }

    class HistoryHoleder {
        List<SearchWorldHistory> data;
        SearchWordHistoryAdapter adapter;
        @BindView(R.id.lvBaseList)
        ListView lv_search_word_history;
        @BindView(R.id.iv_delete_all)
        ImageView iv_delete_all;
        @BindView(R.id.ll_search_history)
        LinearLayout ll_search_history;

        public void bindView() {
            data = DbUtils.getModelList(SearchWorldHistory.class);
            adapter = new SearchWordHistoryAdapter(context);

            adapter.setOnViewClickListener(new BaseView.OnViewClickListener() {
                @Override
                public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
                    showShortToast(((SearchWorldHistory) bv.data).getName());
                    ((SearchWorldHistory) bv.data).delete();
                    data.remove(bv.data);
                    adapter.refresh(data);
                    adapter.notifyDataSetChanged();
                }
            });

            lv_search_word_history.setAdapter(adapter);
            iv_delete_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    DbUtils.deleteModel(SearchWorldHistory.class);
                }
            });
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    window.dismiss();
                    String searchkeyWord = data.get(position).getName();
                    edit_query.setText(searchkeyWord);
                    searchArticleByKeyWorld(searchkeyWord);
                }
            });

            adapter.refresh(data);
        }
    }

    private PopupWindow window;
    private HistoryHoleder historyHoleder;

    void showHistoryList() {
        historyHoleder = new HistoryHoleder();
        View contentView = LayoutInflater.from(context).inflate(R.layout.article_search_word_layout, null, false);
        ButterKnife.bind(historyHoleder, contentView);
        historyHoleder.bindView();
        window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 300, true);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAsDropDown(ll_top, 0, 0);
    }

    @Override
    public void initView() {

        edit_query.setOnEditorActionListener(new mEditorActionListener());
        txt_cancle.setOnClickListener(this);
        edit_query.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showHistoryList();
            }
        });

        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.list_view_divider));
        rvBaseRecycler.addItemDecoration(divider);
        articleAdapter = new ArticleAdapter(context, ArticleAdapter.Type_Shoye_Article);
        rvBaseRecycler.setAdapter(articleAdapter);
        articleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击查看某个文章
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                Article article = articles.get(position);
                intent.putExtra("article", article);
                List<ArticleType> list = DemoApplication.getInstance().getContentCategoryResponse().getCategoryResps();
                for (int i = 0; i < list.size(); i++) {
                    if (article.getContentType().equals(list.get(i))) {
                        articleType = list.get(i);
                        break;
                    }
                }
                intent.putExtra("articleType", articleType);
                String title = articleType.getTitle() + " | " + article.getTitle();
                intent.putExtra(INTENT_TITLE, title);
                toActivity(intent);
            }
        });
        updataList(new ArrayList<>());
    }


    private class mEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (v.getId()) {
                case R.id.edit_query://搜索
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        String key = edit_query.getText().toString();
                        searchArticleByKeyWorld(key);
                    }
                    break;
            }
            return false;
        }
    }

    private void searchArticleByKeyWorld(String searchkeyWord) {
        showLoading("正在搜索");
        TreeMap sm = new TreeMap<String, String>();
        sm.put("SearchkeyWord", searchkeyWord);
        LogicService.post(context, APIMethod.searchContentByTitle, sm, new ApiSubscriber<Response<List<Article>>>() {
            @Override
            public void onSuccess(Response<List<Article>> respon) {
                SearchWorldHistory history = new SearchWorldHistory();
                history.setName(searchkeyWord);
                history.setTime(SystemClock.currentThreadTimeMillis());
                DbUtils.saveModel(history);
                hideLoading();
                updataList(respon.getResult());
            }

            @Override
            protected void onFail(NetErrorException error) {
                hideLoading();
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    void updataList(List<Article> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        if (articles.size() != 0) {
            multiStatusLayout.showContent();
        } else {
            multiStatusLayout.showEmpty();
        }
        articleAdapter.refresh(this.articles);
    }

    @Override
    public void initData() {
        /*
        List<SearchWorldHistory> data=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SearchWorldHistory d=new SearchWorldHistory();
            d.name="关键字"+i;
            d.time=new Date().getTime();
            d.save();
            data.add(d);
        }
        */

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete_all:
                DbUtils.deleteModel(SearchWorldHistory.class);
                initData();
                break;
            case R.id.txt_cancle:
                finish();
                break;
            default:
                break;
        }
    }
}
