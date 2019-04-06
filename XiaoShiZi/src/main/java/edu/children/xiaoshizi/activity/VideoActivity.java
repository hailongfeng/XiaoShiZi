package edu.children.xiaoshizi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yixia.camera.MediaRecorderBase;
import com.zhaoshuang.weixinrecorded.MyVideoView;
import com.zhaoshuang.weixinrecorded.RecordedActivity;

import edu.children.xiaoshizi.R;

public class VideoActivity extends Activity {

    private MyVideoView vv_play;
    private ImageView iv_photo;
    private RelativeLayout rl_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);

        vv_play = findViewById(R.id.vv_play);
        iv_photo = findViewById(R.id.iv_photo);
        rl_show = findViewById(R.id.rl_show);
    }

    public void recordVideo(View view){

        Intent intent = new Intent(this, RecordedActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {

            String imagePath = data.getStringExtra("imagePath");
            String videoPath = data.getStringExtra("videoPath");
            if(!TextUtils.isEmpty(imagePath)){
                iv_photo.setVisibility(View.VISIBLE);
                vv_play.setVisibility(View.GONE);

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                iv_photo.setImageBitmap(bitmap);
            }else if(!TextUtils.isEmpty(videoPath)){
                iv_photo.setVisibility(View.GONE);
                vv_play.setVisibility(View.VISIBLE);

                vv_play.setVideoPath(videoPath);
                vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        vv_play.setLooping(true);
                        vv_play.start();

                        float widthF = vv_play.getVideoWidth() * 1f / MediaRecorderBase.VIDEO_HEIGHT;
                        float heightF = vv_play.getVideoHeight() * 1f / MediaRecorderBase.VIDEO_WIDTH;
                        ViewGroup.LayoutParams layoutParams = vv_play.getLayoutParams();
                        layoutParams.width = (int) (rl_show.getWidth() * widthF);
                        layoutParams.height = (int) (rl_show.getHeight() * heightF);
                        vv_play.setLayoutParams(layoutParams);
                    }
                });
            }

        }
    }
}
