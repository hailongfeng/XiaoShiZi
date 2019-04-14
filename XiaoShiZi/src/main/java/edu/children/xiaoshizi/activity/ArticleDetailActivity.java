package edu.children.xiaoshizi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;
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
    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    @BindView(R.id.tvBaseTitle)
    TextView tvBaseTitle;

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
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        articleType=(ArticleType) getIntent().getSerializableExtra("articleType");
        preAgentWeb= AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready();
            article=(Article) getIntent().getSerializableExtra("article");
//        agentWeb= preAgentWeb.go("");
        if (articleType.getType().equalsIgnoreCase("VT")){
            player.setVisibility(View.VISIBLE);
        }else {
            player.setVisibility(View.GONE);
        }
        getAgentWebField();
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
    private void getArticleById(String id) {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("contentId",id);
        LogicService.post(context, APIMethod.loadContentById,sm, new ApiSubscriber<Response<Article>>() {
            @Override
            public void onSuccess(Response<Article> respon) {
                if (respon.getResult()!=null&&StringUtil.isNotEmpty(respon.getResult().getIntroduce(),true)){
                    if (articleType.getType().equalsIgnoreCase("VT")){
                        Article article=respon.getResult();
                        player.setUp(article.getActivityVideoUrl(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                        loadImage(article.getActivityVideoImageUrl(),player.thumbImageView);
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
            }
        });
    }

    public void initEvent() {
        ib_share.setOnClickListener(this);
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
