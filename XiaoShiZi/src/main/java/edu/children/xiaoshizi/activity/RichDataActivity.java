package edu.children.xiaoshizi.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.coolindicator.sdk.CoolIndicator;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.ArticleCommentView;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleComment;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.XszCache;
import edu.children.xiaoshizi.view.richweb.HeaderScrollHelper;
import edu.children.xiaoshizi.view.richweb.HeaderViewPager;
import edu.children.xiaoshizi.view.richweb.JavaScriptLog;
import edu.children.xiaoshizi.view.richweb.RichWebView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * Created by yongzheng on 2018/8/24.
 * 显示html标签内容
 */
public class RichDataActivity extends XszBaseActivity {

    @BindView(R.id.web_view)
    RichWebView webView;
    @BindView(R.id.recycler_view)
    RecyclerView rvComments;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;//滚动控件父容器
    @BindView(R.id.indicator)
    CoolIndicator indicator;//进度条

    private ArticleType articleType;
    private Article article;
    @BindView(R.id.ib_share)
    ImageButton ib_share;
    @BindView(R.id.player_list_video)
    JCVideoPlayerStandard player;
    @BindView(R.id.tvBaseTitle)
    TextView tvBaseTitle;
    @BindView(R.id.btn_down_cache)
    RoundTextView btn_down_cache;

    List<ArticleComment> comments=new ArrayList<>();
    private BaseAdapter commentAdapter;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title= getIntent().getStringExtra(INTENT_TITLE);
        setContentView(R.layout.activity_rich_data);
    }

    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        //设置点击图片
        webView.addJavascriptInterface(new JavaScriptLog(this, new JavaScriptLog.ClickImageCallBack() {
            @Override
            public void clickImage(String src) {
                Toast.makeText(context,"点击:"+src,Toast.LENGTH_SHORT).show();
            }
        }), "control");
        //设置html内容
        webView.setShow(getHtml(""));
        //设置图片加载失败回调
        webView.setLoadImgError();
        //添加点击图片脚本事件
        webView.setImageClickListener();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                indicator.start();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                indicator.complete();
            }
        });
        //滚动绑定
        scrollableLayout.setCurrentScrollableContainer(new HeaderScrollHelper.ScrollableContainer(){
            @Override
            public View getScrollableView() {
                return rvComments;
            }
        });
        initComments();
    }

    @Override
    public void initData() {
        articleType=(ArticleType) getIntent().getSerializableExtra("articleType");
        article=(Article) getIntent().getSerializableExtra("article");
        tvBaseTitle.setText(title);
        print("articleType==="+articleType.getType()+","+articleType.getBelongTo());
        if (articleType.getType().equalsIgnoreCase("VT")){
            player.setVisibility(View.VISIBLE);
            btn_down_cache.setVisibility(View.VISIBLE);
        }else {
            player.setVisibility(View.GONE);
            btn_down_cache.setVisibility(View.GONE);
        }
        getArticleById(article.getContentId());
    }

    @Override
    public void initEvent() {

    }
    private void getArticleById(String id) {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("contentId",id);
        LogicService.post(context, APIMethod.loadContentById,sm, new ApiSubscriber<Response<Article>>() {
            @Override
            public void onSuccess(Response<Article> respon) {
                if (respon.getResult()!=null&& StringUtil.isNotEmpty(respon.getResult().getIntroduce(),true)){
                    article=respon.getResult();
                    if (articleType.getType().equalsIgnoreCase("VT")){
                        String url=article.getActivityVideoUrl();
                        File file= XszCache.getCachedVideoFile(url);
                        if (file.exists()){
                            Log.d(TAG,"已经存在，直接播放");
                            Uri uri= Uri.fromFile(file);
                            player.setUp(uri.getPath(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                        }else {
                            Log.d(TAG,"网络下载播放");
                            player.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                        }
                        loadImage(article.getActivityVideoImageUrl(),player.thumbImageView);
                    }

                    String introduce=respon.getResult().getIntroduce();
                    webView.setShow(getHtml(introduce));
                    updateComments(article.getCommentResps());
                }else {
                    webView.setShow(getHtml("内容为空"));
                }
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
            }
        });
    }

    private void updateComments(List<ArticleComment> comments) {
//       if (!articleType.getType().equalsIgnoreCase("VT")&&articleType.getBelongTo()==1) {
        this.comments.clear();
        if (comments != null) {
            print("comments.size()=="+comments.size());
            this.comments.addAll(comments);
        }
        commentAdapter.refresh(comments);
//       }
    }
    private void initComments(){
        commentAdapter= new BaseAdapter<ArticleComment, ArticleCommentView>(context){
            @Override
            public ArticleCommentView createView(int viewType, ViewGroup parent) {
                return new ArticleCommentView(context,parent);
            }
        };
        commentAdapter.setOnViewClickListener(new BaseView.OnViewClickListener() {
            @Override
            public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
//                data.remove(bv.data);
//                commentAdapter.refresh(data);
//                commentAdapter.notifyDataSetChanged();
            }
        });
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(commentAdapter);
    }
    private String getHtml(String body){
        StringBuilder sb=new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>").append(body).append("</body></html>");
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
