package edu.children.xiaoshizi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.adapter.SearchWordHistoryAdapter;
import edu.children.xiaoshizi.adapter.view.ArticleCommentView;
import edu.children.xiaoshizi.adapter.view.ArticleImageView;
import edu.children.xiaoshizi.adapter.view.ArticleView;
import edu.children.xiaoshizi.adapter.view.ArticleWebView;
import edu.children.xiaoshizi.adapter.view.XszBaseView;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleCache;
import edu.children.xiaoshizi.bean.ArticleComment;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.XszCache;
import edu.children.xiaoshizi.view.MyLayoutManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.MD5Util;
import zuo.biao.library.util.StringUtil;

import static zuo.biao.library.interfaces.Presenter.INTENT_TITLE;

public class ArticleDetailActivity extends XszBaseActivity implements View.OnClickListener{

    private ArticleType articleType;
    private Article article;
    private AgentWeb agentWeb;
    private AgentWeb.PreAgentWeb preAgentWeb;
    @BindView(R.id.ib_share)
    ImageButton ib_share;
    @BindView(R.id.player_list_video)
    JCVideoPlayerStandard player;
//    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    @BindView(R.id.tvBaseTitle)
    TextView tvBaseTitle;
    @BindView(R.id.btn_down_cache)
    RoundTextView btn_down_cache;


//    @BindView(R.id.lvBaseList)
    ListView lvComments;
    List<ArticleComment> comments=new ArrayList<>();
    BaseAdapter commentAdapter;




    @BindView(R.id.rvBaseRecycler)
    RecyclerView rvBaseRecycler;




    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title= getIntent().getStringExtra(INTENT_TITLE);
        mShareListener = new ArticleDetailActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb("http://www.baidu.com");
                        web.setTitle(title);
                        web.setDescription("来自小狮子的分享");
                        web.setThumb(new UMImage(context, R.mipmap.logo));
                        new ShareAction(context).withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
        setContentView(R.layout.activity_article_detail);
    }

    public void initView() {
//        linWeb= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_head_webview,null);
//        rvComments.addHeaderView(linWeb);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        articleType=(ArticleType) getIntent().getSerializableExtra("articleType");
        article=(Article) getIntent().getSerializableExtra("article");
        print("articleType==="+articleType.getType()+","+articleType.getBelongTo());
//        preAgentWeb= AgentWeb.with(this)
//                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
//                .useDefaultIndicator()
//                .createAgentWeb()
//                .ready();
        if (articleType.getType().equalsIgnoreCase("VT")){
            player.setVisibility(View.VISIBLE);
            btn_down_cache.setVisibility(View.VISIBLE);
        }else {
            player.setVisibility(View.GONE);
            btn_down_cache.setVisibility(View.GONE);
        }
//        getAgentWebField();
//        initComments();
        test2();
    }


    void test2(){
        rvBaseRecycler.setLayoutManager(new MyLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.list_view_divider));
        commentAdapter= new BaseAdapter<ArticleComment, ArticleWebView>(context){
            @Override
            public ArticleWebView createView(int viewType, ViewGroup parent) {
               return new ArticleWebView(context,parent);
            }
        };
        rvBaseRecycler.addItemDecoration(divider);
        rvBaseRecycler.setAdapter(commentAdapter);
    }

   private void updateComments(List<ArticleComment> comments) {
//       if (!articleType.getType().equalsIgnoreCase("VT")&&articleType.getBelongTo()==1) {
           this.comments.clear();
           if (comments != null) {
               print("comments.size()=="+comments.size());
               this.comments.addAll(comments);
           }
           commentAdapter.refresh(this.comments);
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
//                adapter.refresh(data);
//                adapter.notifyDataSetChanged();
                }
            });
            lvComments.setAdapter(commentAdapter);
    }

    private void getAgentWebField(){
        Field  field = null;
        try {
            field = preAgentWeb.getClass().getDeclaredField("mAgentWeb");
            field.setAccessible(true);
            agentWeb= (AgentWeb) field.get(preAgentWeb);
            Log.d(TAG,(agentWeb==null)+",,,agentWeb==null");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void initData() {
        getArticleById(article.getContentId());
//        String title=articleType.getTitle()+"|"+article.getTitle();
        tvBaseTitle.setText(title);
    }

    private String getHtml(String body){

        StringBuilder sb=new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>").append(body).append("</body></html>");
        return sb.toString();
    }

    private void downLoadVideToCache(String url,String path){
        FileDownloader.getImpl().create(url)
                .setPath(path)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int percent=(int) ((double) soFarBytes / (double) totalBytes * 100);
                        Log.d(TAG,"当前下载进度："+percent);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        print("缓存地址："+task.getTargetFilePath());
                        showShortToast("缓存成功");

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        FileUtils.deleteFile(task.getTargetFilePath());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }

    private void getArticleById(String id) {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("contentId",id);
        LogicService.post(context, APIMethod.loadContentById,sm, new ApiSubscriber<Response<Article>>() {
            @Override
            public void onSuccess(Response<Article> respon) {
                if (respon.getResult()!=null&&StringUtil.isNotEmpty(respon.getResult().getIntroduce(),true)){
                    article=respon.getResult();
                    if (articleType.getType().equalsIgnoreCase("VT")){
                        String url=article.getActivityVideoUrl();
                        File file=XszCache.getCachedVideoFile(url);
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
                    List<ArticleComment> comments2= article.getCommentResps();
                    ArticleComment a=new ArticleComment();
                    a.setCommentContent(introduce);
                    comments2.add(0,a);
                    updateComments(article.getCommentResps());
//                    String introduce=respon.getResult().getIntroduce();
//                    agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(introduce), "text/html", "UTF-8", null);
                }else {
//                    agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml("内容为空"), "text/html", "UTF-8", null);
                }
     }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
            }
        });
    }

    public void initEvent() {
        ib_share.setOnClickListener(this);
        btn_down_cache.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down_cache:
                ArticleCache articleCache=new ArticleCache(article);
                if (!articleCache.exists()){
                    articleCache.save();
                }
                String url=article.getActivityVideoUrl();
                File file=XszCache.getCachedVideoFile(url);
                if(file.exists()){
                    showShortToast("已添加至我的缓存");
                }else {
                    showShortToast("已加入缓存下载任务");
                    downLoadVideToCache(url,file.getAbsolutePath());
                }
                break;
            case R.id.ib_share:
//                mShareAction.open();
                break;
        }
    }
    public void onReturnClick(View v) {
            onBackPressed();//会从最外层子类调finish();BaseBottomWindow就是示例
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<ShareBoardActivity> mActivity;

        private CustomShareListener(ArticleDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

}
