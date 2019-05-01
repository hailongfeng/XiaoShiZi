package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
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
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleCache;
import edu.children.xiaoshizi.bean.ArticleComment;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.XszCache;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class ArticleDetailActivity extends XszBaseActivity implements View.OnClickListener{

    private ArticleType articleType;
    private Article article;
    private AgentWeb agentWeb;
    private AgentWeb.PreAgentWeb preAgentWeb;
    @BindView(R.id.ib_share)
    ImageButton ib_share;
    @BindView(R.id.player_list_video)
    JCVideoPlayerStandard player;
    @BindView(R.id.cv_video_wrap)
    CardView cv_video_wrap;
    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    @BindView(R.id.tvBaseTitle)
    TextView tvBaseTitle;
    @BindView(R.id.btn_down_cache)
    ImageView btn_down_cache;

    @BindView(R.id.ll_xiepinglun)
    LinearLayout ll_xiepinglun;
    @BindView(R.id.edit_xiepinglun)
    EditText edit_xiepinglun;
    @BindView(R.id.rtv_dianzan)
    RoundTextView rtv_dianzan;
    @BindView(R.id.rtv_fenxiang)
    RoundTextView rtv_fenxiang;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title= getIntent().getStringExtra(INTENT_TITLE);
        setContentView(R.layout.activity_article_detail);
    }


    void initShare(){
        mShareListener = new ArticleDetailActivity.CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(context).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        print("ShareUrl==="+article.getShareUrl());
                        UMWeb web = new UMWeb(article.getShareUrl());
                        web.setTitle(title);
                        web.setDescription("来自小狮子的分享");
                        web.setThumb(new UMImage(context, R.mipmap.logo));
                        new ShareAction(context).withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
    }

    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        articleType=(ArticleType) getIntent().getSerializableExtra("articleType");
        article=(Article) getIntent().getSerializableExtra("article");
        print("typeTitle="+articleType.getTitle()+",belongTo"+articleType.getBelongTo());
        print(article.toString());
        if (isVideoArticle()){
            cv_video_wrap.setVisibility(View.VISIBLE);
            btn_down_cache.setVisibility(View.VISIBLE);
            ll_xiepinglun.setVisibility(View.GONE);
            ib_share.setVisibility(View.VISIBLE);
        }else {
            if (articleType.getBelongTo()==1&&isLogin()){
                ll_xiepinglun.setVisibility(View.VISIBLE);
                edit_xiepinglun.setOnEditorActionListener(new EditorActionListener());
            }else {
                ll_xiepinglun.setVisibility(View.GONE);
            }
            cv_video_wrap.setVisibility(View.GONE);
            btn_down_cache.setVisibility(View.GONE);
            ib_share.setVisibility(View.GONE);
        }
        preAgentWeb= AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready();
        agentWeb= getAgentWebField(preAgentWeb);
        initShare();
    }


    private boolean isImageArticle(){
       return articleType.getType().equalsIgnoreCase("IT");
    }
    private boolean isVideoArticle(){
        return articleType.getType().equalsIgnoreCase("VT");
    }



    public void initData() {
        getArticleById(article.getContentId());
//        String pushAppTitle=articleType.getPushAppTitle()+"|"+article.getPushAppTitle();
        if (isVideoArticle()){
            tvBaseTitle.setText("视频详情");
        }else {
            tvBaseTitle.setText(title);
        }
    }


    private class EditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (v.getId()) {
                case R.id.edit_xiepinglun://写评论
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        String content = edit_xiepinglun.getText().toString();
                        submitComment(article.getContentId(),"0",content, ArticleComment.comment_type_Comment);
                    }
                    break;
            }
            return false;
        }
    }

    void updateCommont(Article article){
        rtv_dianzan.setText(article.getLikedNumber()+"");
        rtv_fenxiang.setText(article.getShareNumber()+"");
    }

    void submitComment(String contentId,String commentParentId,String commentContent,String articleCommentType){
        showLoading(R.string.msg_handing);
        TreeMap sm = new TreeMap<String, String>();
        sm.put("contentId", contentId);
        sm.put("commentParentId", commentParentId);
        sm.put("commentContent", commentContent);
        sm.put("type", articleCommentType);
        LogicService.post(context, APIMethod.submitComment, sm, new ApiSubscriber<Response<Article>>() {
            @Override
            public void onSuccess(Response<Article> respon) {
                Article newArticle=respon.getResult();
                edit_xiepinglun.setText("");
                ArticleDetailActivity.this.article.setLikedNumber(newArticle.getLikedNumber());
                ArticleDetailActivity.this.article.setShareNumber(newArticle.getShareNumber());
                ArticleDetailActivity.this.article.setIntroduce(newArticle.getIntroduce());
                ArticleDetailActivity.this.article.setShareUrl(newArticle.getShareUrl());
                hideLoading();
                showShortToast(respon.getMessage());
                if (articleCommentType.equalsIgnoreCase(ArticleComment.comment_type_Comment)){
                    String introduce=respon.getResult().getIntroduce();
                    agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(introduce), "text/html", "UTF-8", null);
                }else {
                    updateCommont(newArticle);
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

        APIMethod method=APIMethod.loadContentById;
        if (articleType.getBelongTo()==2){
            method=APIMethod.loadSeClassRoomContentById;
        }else if (articleType.getBelongTo()==3){
            method=APIMethod.loadSafeLabContentById;
        }
        LogicService.post(context, method,sm, new ApiSubscriber<Response<Article>>() {
            @Override
            public void onSuccess(Response<Article> respon) {
                if (respon.getResult()!=null&&StringUtil.isNotEmpty(respon.getResult().getIntroduce(),true)){
                    article=respon.getResult();
                    if (isVideoArticle()){
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
                    }else {
                        updateCommont(article);
                    }
                    String introduce=respon.getResult().getIntroduce();
                    agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(introduce), "text/html", "UTF-8", null);
                }else {
                    agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml("内容为空"), "text/html", "UTF-8", null);
                }
     }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast(error.getMessage());
                player.setUp("", JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            }
        });
    }

    public void initEvent() {
        ib_share.setOnClickListener(this);
        rtv_dianzan.setOnClickListener(this);
        rtv_fenxiang.setOnClickListener(this);
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
               if (!NetworkUtils.isConnected()){
                   showShortToast("无法连接到网络");
                   return;
               }
                ArticleCache articleCache=new ArticleCache(article);
                if (!articleCache.exists()){
                    articleCache.save();
                }
                String url=article.getActivityVideoUrl();
                File file=XszCache.getCachedVideoFile(url);
                if(file.exists()){
                    showShortToast("缓存成功");
                }else {
                    showShortToast("已加入缓存下载任务");
                    downLoadVideToCache(url,file.getAbsolutePath());
                }
                break;
            case R.id.rtv_dianzan:
                if (!isLogin()){
                    showShortToast("请先登录");
                    return;
                }
//                mShareAction.open();
                submitComment(article.getContentId(),"0","", ArticleComment.comment_type_Liked);
                break;
            case R.id.rtv_fenxiang:
            case R.id.ib_share:
                mShareAction.open();
                break;
        }
    }
    public void onReturnClick(View v) {
            onBackPressed();//会从最外层子类调finish();BaseBottomWindow就是示例

    }

    private  class CustomShareListener implements UMShareListener {
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
                submitComment(article.getContentId(),"0","", ArticleComment.comment_type_Share);
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
                    submitComment(article.getContentId(),"0","", ArticleComment.comment_type_Share);
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }else {
                    showShortToast("分享失败啦");
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
