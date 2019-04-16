package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.Log;

public abstract class XszBaseActivity extends BaseActivity implements View.OnClickListener {

  protected static   RequestOptions glideOptions = new RequestOptions()
            .fallback( R.drawable.user_default) //url为空的时候,显示的图片
            .error(R.drawable.user_default);//图片加载失败后，显示的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(context).onAppStart();
        DialogUIUtils.init(context);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
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

    private Dialog loadingDialog=null;
    protected  void showLoading(String msg){
        loadingDialog=DialogUIUtils.showLoading(context, msg,true,false,false,true).show();
    }
    protected  void showLoading(@StringRes int rid){
        String msg= context.getResources().getString(rid);
        loadingDialog=DialogUIUtils.showLoading(context, msg,true,false,false,true).show();
    }
    protected  void hideLoading(){
        DialogUIUtils.dismiss(loadingDialog);
    }

    protected void loadImage(String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imageView);
    }

    @Override
    public void onClick(View v) {

    }

    protected void print(String msg){
        Log.d(TAG,msg);
    }
}
