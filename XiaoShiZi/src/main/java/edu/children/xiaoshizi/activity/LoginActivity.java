package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.StringUtils;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.ArticleCache;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.logic.UmengThirdLoginHandle;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.Constant;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class LoginActivity extends XszBaseActivity implements View.OnClickListener {

    @BindView(R.id.edit_user_phone)
    EditText edit_user_phone;
    @BindView(R.id.edit_verifyCode)
    EditText edit_verifyCode;
    @BindView(R.id.ib_login_by_wexin)
    ImageButton ib_login_by_wexin;
    @BindView(R.id.ib_login_by_qq)
    ImageButton ib_login_by_qq;
    @BindView(R.id.btn_get_verify_code)
    RoundTextView btn_get_verify_code;

    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        time = new TimeCount(60*1000, 1000);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    public void initData() {


    }
    Dialog dialog;
    @Override
    public void initEvent() {
        findView(R.id.btn_get_verify_code, this);
        findView(R.id.btn_login, this);
        findView(R.id.ib_login_by_wexin, this);
        findView(R.id.ib_login_by_qq, this);
    }

    @Override
    public void onReturnClick(View v) {
        super.onReturnClick(v);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_verify_code:
                getVeryCode();
                break;
            case R.id.ib_login_by_wexin:
                loginByThird(1);
                break;
            case R.id.ib_login_by_qq:
                loginByThird(2);
                break;
            case R.id.btn_login:
                if (StringUtil.isEmpty(edit_user_phone,true)){
                    showShortToast("手机号不能为空");
                    return;
                }
                if (StringUtil.isEmpty(edit_verifyCode,true)){
                    showShortToast("验证码不能为空");
                    return;
                }
//                DialogUIUtils.showToastCenter("更新成功");
//                login3();
                login();
                break;
        }
    }


    private void getVeryCode() {
        TreeMap sm = new TreeMap<String,String>();
        String phone=edit_user_phone.getText().toString();

        if (StringUtils.isEmpty(phone)) {
            showShortToast("手机号不能为空");
            return;
        }
        sm.put("phoneNumber",phone);
        LogicService.post(context, APIMethod.getVerifyCode,sm,new ApiSubscriber<Response>(){
            @Override
            public void onSuccess(Response response) {
                Log.d(TAG,"response code:"+response.getCode());
                Log.d(TAG,"onNext  , "+Thread.currentThread().getName());
                showShortToast(response.getMessage());
                time.start();
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast(error.getMessage());
            }
        });
    }

    private void login() {
        if (StringUtils.isEmpty(DemoApplication.getInstance().getDeviceToken())){
            showShortToast("请退出应用，重新登录");
            return;
        }
        showLoading("正在登录");
        String verifyCode=edit_verifyCode.getText().toString();
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("phoneNumber",phoneNumber);
        sm.put("verifyCode",verifyCode);
        sm.put("mobileType", "Android");
        sm.put("deviceToken", DemoApplication.getInstance().getDeviceToken());
        LogicService.post(context, APIMethod.login,sm, new ApiSubscriber<Response<LoginRespon>>() {
            @Override
            public void onSuccess(Response<LoginRespon> respon) {
                hideLoading();
                Log.d(TAG,"phoneNumber="+phoneNumber);
                respon.getResult().getLoginResp().setPhone(phoneNumber);
                DemoApplication.getInstance().setLoginRespon(respon.getResult());
                User user=respon.getResult().getLoginResp();
                DemoApplication.getInstance().setUser(user);
                if (!StringUtils.equals(spUtils.getString("phoneNumber",""),phoneNumber)){
                    DbUtils.deleteModel(ArticleCache.class);
                    DbUtils.deleteModel(SearchWorldHistory.class);
                    spUtils.put("lastUser",phoneNumber);
                }
                EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_user_login,"登陆成功",""));
                CacheUtils.get(context).remove(Constant.cache_user);
                CacheUtils.get(context).put(Constant.cache_user,user);
                toActivity(new Intent(context,MainActivity.class),false);
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

    private Response<LoginRespon> login2() throws IOException {
        String verifyCode=edit_verifyCode.getText().toString();
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("phoneNumber",phoneNumber);
        sm.put("verifyCode",verifyCode);
        sm.put("mobileType", "Android");
        sm.put("deviceToken", DemoApplication.getInstance().getDeviceToken());
        Response<LoginRespon> response =LogicService.post(context, APIMethod.login,sm);
        return response;
    }

    private Response<List<Banner>> loadSysBannerList() throws IOException {
        TreeMap sm = new TreeMap<String,String>();
            Response<List<Banner>> response=LogicService.post(context,APIMethod.loadSysBannerList,sm);
            return response;
    }
    private Response<LoadContentCategoryResponse> loadContentCategory() throws IOException {
        TreeMap sm = new TreeMap<String,String>();
        Response<LoadContentCategoryResponse> response=LogicService.post(context,APIMethod.loadContentCategory,sm);
        return  response;
    }



    void login3(){
        Observable.create(
                new ObservableOnSubscribe<Response>(){
                    @Override
                    public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
                        emitter.onNext(new Response("1",""));
                        Response<LoginRespon> loginRespon=login2();
                        if (loginRespon.getCode().equals(Response.SUCCESS)) {
                            String phoneNumber=edit_user_phone.getText().toString();
                            loginRespon.getResult().getLoginResp().setPhone(phoneNumber);
                            DemoApplication.getInstance().setLoginRespon(loginRespon.getResult());
                            DemoApplication.getInstance().setUser(loginRespon.getResult().getLoginResp());
                            Response<List<Banner>> loadSysBannerListResult=loadSysBannerList();
                            if (loadSysBannerListResult.getCode().equals(Response.SUCCESS)) {
                                DemoApplication.getInstance().setBanners(loadSysBannerListResult.getResult());
                                Response<LoadContentCategoryResponse> loadContentCategoryResult=loadContentCategory();
                                if (loadContentCategoryResult.getCode().equals(Response.SUCCESS)){
                                    DemoApplication.getInstance().setContentCategoryResponse(loadContentCategoryResult.getResult());
                                    emitter.onNext(new Response("3",loginRespon.getMessage()));
                                }else {
                                    emitter.onNext(new Response("2",loadContentCategoryResult.getMessage()));
                                }
                            }else {
                                emitter.onNext(new Response("2",loadSysBannerListResult.getMessage()));
                            }
                        }else {
                            emitter.onNext(new Response("2",loginRespon.getMessage()));
                        }
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response r) throws Exception {
                        if (r.getCode().equals("1")){
                            showLoading("正在登录");
                        }else if (r.getCode().equals("2")){
                            hideLoading();
                            showShortToast(r.getMessage());
                        }else if (r.getCode().equals("3")){
                            EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_user_login,"登陆成功",""));
                            hideLoading();
                            showShortToast(r.getMessage());
                            toActivity(new Intent(context,MainActivity.class),false);
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showShortToast(throwable.getMessage());
                        hideLoading();
                    }
                });
    }




    private void loginByThird(int type){
        UmengThirdLoginHandle umengThirdLoginHandle= new UmengThirdLoginHandle(context,authListener);
        if (type==1) {
            umengThirdLoginHandle.auther(SHARE_MEDIA.WEIXIN);
        }else {
            umengThirdLoginHandle.auther(SHARE_MEDIA.QQ);
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(context, "成功了", Toast.LENGTH_LONG).show();
           Log.d(TAG,"result==="+JSONObject.toJSONString(data));
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context, "取消了", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        time.onFinish();
        super.onDestroy();
        UMShareAPI.get(this).release();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

//            btn_get_verify_code.setBackgroundColor(Color.parseColor("#B6B6D8"));
            btn_get_verify_code.setClickable(false);
            btn_get_verify_code.setText(millisUntilFinished / 1000 +"秒");
        }

        @Override
        public void onFinish() {
            btn_get_verify_code.setText("获取验证码");
            btn_get_verify_code.setClickable(true);
//            btn_get_verify_code.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }
}
