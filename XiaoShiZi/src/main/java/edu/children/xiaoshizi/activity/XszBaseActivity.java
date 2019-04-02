package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseActivity;

public abstract class XszBaseActivity extends BaseActivity {

  protected  RequestOptions glideOptions = new RequestOptions()
            .placeholder(R.drawable.student_face_default)//图片加载出来前，显示的图片
            .fallback( R.drawable.student_face_default) //url为空的时候,显示的图片
            .error(R.drawable.student_face_default);//图片加载失败后，显示的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    // Activity页面onResume函数重载
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 不能遗漏
    }
    // Activity页面onResume函数重载
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 不能遗漏
    }

    protected void loadImage(String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imageView);
    }
}
