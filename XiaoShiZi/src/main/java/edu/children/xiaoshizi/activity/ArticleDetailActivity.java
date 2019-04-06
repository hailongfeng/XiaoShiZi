package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;

public class ArticleDetailActivity extends XszBaseActivity {
    private ArticleType articleType;
    private Article article;

    private AgentWeb agentWeb;
    private AgentWeb.PreAgentWeb preAgentWeb;
    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
    }

    @Override
    public void initView() {
        article=(Article) getIntent().getSerializableExtra("article");
        articleType=(ArticleType) getIntent().getSerializableExtra("articleType");
        preAgentWeb= AgentWeb.with(context)
                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready();
        agentWeb= preAgentWeb.go("");
    }

    @Override
    public void initData() {
        getArticleById(article.getContentId());
//        String title=articleType.getTitle()+"|"+article.getTitle();
        autoSetTitle();
    }
    private void getArticleById(String id) {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("contentId",id);
        LogicService.post(context, APIMethod.loadContentById,sm, new ApiSubscriber<Response<Article>>() {
            @Override
            public void onSuccess(Response<Article> respon) {
                String introduce=respon.getResult().getIntroduce();
                agentWeb.getUrlLoader().loadData(introduce,"text/html","UTF-8");
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void initEvent() {

    }
}
