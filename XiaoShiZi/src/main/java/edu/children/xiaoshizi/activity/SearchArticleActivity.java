package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.adapter.SearchWordHistoryAdapter;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import edu.children.xiaoshizi.bean.SearchWorldHistory_Table;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseView;

public class SearchArticleActivity extends XszBaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.edit_query)
    EditText edit_query;
    @BindView(R.id.iv_delete_input_text)
    ImageView iv_delete_input_text;
    @BindView(R.id.txt_cancle)
    RoundTextView txt_cancle;

    @BindView(R.id.ll_article_search_result_empty)
    LinearLayout ll_article_search_result_empty;
    @BindView(R.id.ivbtn_tcjy)
    ImageButton ivbtn_tcjy;

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

        public HistoryHoleder(List<SearchWorldHistory> data) {
            this.data = data;
        }

        public void bindView() {
            adapter = new SearchWordHistoryAdapter(context);

            adapter.setOnViewClickListener(new BaseView.OnViewClickListener() {
                @Override
                public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
//                    showShortToast(((SearchWorldHistory) bv.data).getName());
                    ((SearchWorldHistory) bv.data).delete();
                    data.remove(bv.data);
                    adapter.refresh(data);
//                    adapter.notifyDataSetChanged();
                }
            });

            lv_search_word_history.setAdapter(adapter);
            iv_delete_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyPopupWindow.dismiss();
                    DbUtils.deleteModel(SearchWorldHistory.class);
                }
            });
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    historyPopupWindow.dismiss();
                    String searchkeyWord = data.get(position).getName();
                    edit_query.setText(searchkeyWord);
                    searchArticleByKeyWorld(searchkeyWord);
                }
            });

            adapter.refresh(data);
        }
    }

    private PopupWindow historyPopupWindow;
    private HistoryHoleder historyHoleder;

    void showHistoryList() {
        List<SearchWorldHistory> data=DbUtils.getModelList(SearchWorldHistory.class);
        if (data!=null&&data.size()>0){
            historyHoleder = new HistoryHoleder(data);
            View contentView = LayoutInflater.from(context).inflate(R.layout.article_search_word_layout, null, false);
            ButterKnife.bind(historyHoleder, contentView);
            historyHoleder.bindView();
            historyPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,  1000, true);
            historyPopupWindow.setOutsideTouchable(true);
            historyPopupWindow.setTouchable(true);
            historyPopupWindow.showAsDropDown(ll_top, 0, 0);
        }

    }

    @Override
    public void initView() {

        txt_cancle.setOnClickListener(this);
        edit_query.setOnEditorActionListener(new mEditorActionListener());
//        edit_query.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                showHistoryList();
//            }
//        });
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
                List<ArticleType> list = DbUtils.getArticleTypeList(1);
                for (int i = 0; i < list.size(); i++) {
                    if (article.getCategoryId()==list.get(i).getCategoryId()) {
                        articleType = list.get(i);
                        break;
                    }
                }
                if (article!=null) {
                    intent.putExtra("articleType", articleType);
                    String title = articleType.getTitle() + " | " + article.getTitle();
                    intent.putExtra(INTENT_TITLE, title);
                    toActivity(intent);
                }else {
                    showShortToast("出错了");
                }
            }
        });
        updataList(new ArrayList<>(),true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showHistoryList();
            }
        },300);
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
        sm.put("searchkeyWord", searchkeyWord);
        LogicService.post(context, APIMethod.searchContentByTitle, sm, new ApiSubscriber<Response<List<Article>>>() {
            @Override
            public void onSuccess(Response<List<Article>> respon) {
                edit_query.clearFocus();
                SearchWorldHistory historyExit= DbUtils.getModelSingle(SearchWorldHistory.class, SearchWorldHistory_Table.name.eq(searchkeyWord));
                if (historyExit == null) {
                    SearchWorldHistory history = new SearchWorldHistory();
                    history.setName(searchkeyWord);
                    history.setTime(SystemClock.currentThreadTimeMillis());
                    DbUtils.saveModel(history);
                }
                hideLoading();
                updataList(respon.getResult(),false);
                if (historyPopupWindow!=null&&historyPopupWindow.isShowing()){
                    historyPopupWindow.dismiss();
                    historyPopupWindow=null;
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

    void updataList(List<Article> articles,boolean isFirst) {
        this.articles.clear();
        this.articles.addAll(articles);
        if (articles.size() != 0) {
            rvBaseRecycler.setVisibility(View.VISIBLE);
            ll_article_search_result_empty.setVisibility(View.GONE);
            articleAdapter.refresh(this.articles);
        } else {
            rvBaseRecycler.setVisibility(View.GONE);
            if (isFirst) {
                ll_article_search_result_empty.setVisibility(View.GONE);
            }else {
                ll_article_search_result_empty.setVisibility(View.VISIBLE);
            }
        }
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
        iv_delete_input_text.setOnClickListener(this);
        ivbtn_tcjy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete_all:
                DbUtils.deleteModel(SearchWorldHistory.class);
                initData();
                historyPopupWindow.dismiss();
                break;
            case R.id.iv_delete_input_text:
                edit_query.setText("");
                break;
            case R.id.ivbtn_tcjy:
                toActivity(new Intent(context,SuggestionActivity.class));
                break;
            case R.id.txt_cancle:
                finish();
                break;
            default:
                break;
        }
    }
}
