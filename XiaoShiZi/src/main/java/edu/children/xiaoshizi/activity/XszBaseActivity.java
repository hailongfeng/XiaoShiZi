package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.utils.Constant;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.Log;

public abstract class XszBaseActivity extends BaseActivity implements View.OnClickListener {

    protected SPUtils spUtils;
    protected static RequestOptions glideOptions = new RequestOptions()
            .fallback(R.drawable.user_default) //url为空的时候,显示的图片
            .error(R.drawable.user_default);//图片加载失败后，显示的图片

    protected InputFilter filterLetterAndChines = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern p = Pattern.compile("[a-zA-Z|\u4e00-\u9fa5]+");
            Matcher m = p.matcher(source.toString());
            if (!m.matches()) return "";
            return null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(context).onAppStart();
        DialogUIUtils.init(context);
        spUtils = new SPUtils(Constant.SP_NAME);
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

    private Dialog loadingDialog = null;

    protected void showLoading(String msg) {
        loadingDialog = DialogUIUtils.showLoading(context, msg, true, false, false, true).show();
    }

    protected void showLoading(@StringRes int rid) {
        String msg = context.getResources().getString(rid);
        loadingDialog = DialogUIUtils.showLoading(context, msg, true, false, false, true).show();
    }

    protected void hideLoading() {
        DialogUIUtils.dismiss(loadingDialog);
    }

    protected void loadImage(String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imageView);
    }

    @Override
    public void onClick(View v) {

    }

    protected AgentWeb getAgentWebField(AgentWeb.PreAgentWeb preAgentWeb) {
        Field field = null;
        AgentWeb agentWeb = null;
        try {
            field = preAgentWeb.getClass().getDeclaredField("mAgentWeb");
            field.setAccessible(true);
            agentWeb = (AgentWeb) field.get(preAgentWeb);
            Log.d(TAG, (agentWeb == null) + ",,,agentWeb==null");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return agentWeb;
    }

    protected String getHtml(String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>").append(body).append("</body></html>");
        return sb.toString();
    }

    protected boolean isLogin() {
        return DemoApplication.getInstance().isLogin();
    }

    protected void print(String msg) {
        Log.d(TAG, msg);
    }
}
