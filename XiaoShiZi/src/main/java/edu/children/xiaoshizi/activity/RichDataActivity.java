package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coolindicator.sdk.CoolIndicator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.ArticleCommentView;
import edu.children.xiaoshizi.adapter.view.ArticleWebView;
import edu.children.xiaoshizi.bean.ArticleComment;
import edu.children.xiaoshizi.view.richweb.HeaderScrollHelper;
import edu.children.xiaoshizi.view.richweb.HeaderViewPager;
import edu.children.xiaoshizi.view.richweb.JavaScriptLog;
import edu.children.xiaoshizi.view.richweb.RichWebView;
import zuo.biao.library.base.BaseAdapter;

/**
 * Created by yongzheng on 2018/8/24.
 * 显示html标签内容
 */
public class RichDataActivity extends AppCompatActivity {

    private RichWebView webView;
    private RecyclerView recyclerView;
    private HeaderViewPager scrollableLayout;//滚动控件父容器
    private CoolIndicator indicator;//进度条

    private List<String> datas = new ArrayList<>();
    private BaseAdapter adapter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_data);
        this.context = this;
        initView();
    }

    private void initView() {
        webView = findViewById(R.id.web_view);
        recyclerView = findViewById(R.id.recycler_view);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        indicator = findViewById(R.id.indicator);
        for (int i=0;i<30;i++){
            datas.add("");
        }
        //设置点击图片
        webView.addJavascriptInterface(new JavaScriptLog(this, new JavaScriptLog.ClickImageCallBack() {
            @Override
            public void clickImage(String src) {
                Toast.makeText(context,"点击:"+src,Toast.LENGTH_SHORT).show();
            }
        }), "control");
        //设置html内容
        webView.setShow(getHtmlData());
        //设置图片加载失败回调
        webView.setLoadImgError();
        //添加点击图片脚本事件
        webView.setImageClickListener();
        adapter= new BaseAdapter<ArticleComment, ArticleCommentView>(this){
            @Override
            public ArticleCommentView createView(int viewType, ViewGroup parent) {
                return new ArticleCommentView(context,parent);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        List<ArticleComment> comments1=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            ArticleComment articleComment= new ArticleComment();
            articleComment.setContentId(i+"---");
            comments1.add(articleComment);
        }
        adapter.refresh(comments1);

        //滚动绑定
        scrollableLayout.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer(){
            @Override
            public View getScrollableView() {
                return recyclerView;
            }
        });
    }




    private String getHtmlData() {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = getAssets().open("data.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

}
