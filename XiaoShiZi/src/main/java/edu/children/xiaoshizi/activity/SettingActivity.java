package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.FileUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.flyco.roundview.RoundTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.XszCache;
import zuo.biao.library.util.StringUtil;

public class SettingActivity extends XszBaseActivity{

    @BindView(R.id.ll_setting_clear_cache)
    LinearLayout ll_setting_clear_cache;
    @BindView(R.id.ll_setting_about_us)
    LinearLayout ll_setting_about_us;
    @BindView(R.id.btn_logout)
    RoundTextView btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ll_setting_clear_cache.setOnClickListener(this);
        ll_setting_about_us.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }
    Dialog dialog;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting_clear_cache:
                XszCache.getCacheSize();
                XszCache.clearCacheSize();
                dialog=DialogUIUtils.showAlert(context,"清除成功","清除30M","","","确定","",false,true,true,new DialogUIListener(){

                    @Override
                    public void onPositive() {
                        DialogUIUtils.dismiss(dialog);
                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();

                showShortToast("清除完成");
                break;
            case R.id.ll_setting_about_us:
                break;
            case R.id.btn_logout:
                loginOut();
                break;
        }
    }

    void loginOut(){
        TreeMap sm = new TreeMap<String,String>();
        showLoading(R.string.msg_handing);
        LogicService.post(context, APIMethod.loginOut, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> respon) {
                hideLoading();
                CacheUtils.get(context).remove(Constant.cache_user);
                DemoApplication.getInstance().setUser(null);
                DemoApplication.getInstance().setLoginRespon(null);
                EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_user_logout,"用户退出"));
                showShortToast(respon.getMessage());
//                toActivity(new Intent(context,MainActivity.class),false);
                finish();
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }
}
