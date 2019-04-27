package edu.children.xiaoshizi.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.ArticleCache;
import edu.children.xiaoshizi.utils.XszCache;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayActivity extends XszBaseActivity {

        @BindView(R.id.player_list_video)
        JCVideoPlayerStandard player;
    ArticleCache cache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache= (ArticleCache) getIntent().getSerializableExtra("articleCache");
        setContentView(R.layout.activity_video_play);
    }

    @Override
    public void initView() {

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
    public void initData() {
        String videoUrl=cache.getActivityVideoUrl();
        File file= XszCache.getCachedVideoFile(videoUrl);
        if (file.exists()){
            Uri uri= Uri.fromFile(file);
            player.setUp(uri.getPath(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            //直接进入全屏
//            player.startFullscreen(context, JCVideoPlayerStandard.class, file.getAbsolutePath(), "");
            //模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
            player.startButton.performClick();
        }
    }

    @Override
    public void initEvent() {

    }
}
