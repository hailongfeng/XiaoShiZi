package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.gyf.barlibrary.ImmersionBar;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class LoginActivity extends XszBaseActivity implements View.OnClickListener {

    @BindView(R.id.edit_user_phone)
    EditText edit_user_phone;
    @BindView(R.id.edit_verifyCode)
    EditText edit_verifyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.xsz_login_bg)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    public void initData() {
        EasyPermissions.requestPermissions(
                context,
                "申请权限",
                0,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);
    }
    Dialog dialog;
    @Override
    public void initEvent() {
        findView(R.id.btn_get_verify_code, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVeryCode();
            }
        });
        findView(R.id.btn_login, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog= DialogUIUtils.showAlert(context,"清除成功","清除30M","aaa","bbb","确定","取消",true,true,true,new DialogUIListener(){

                    @Override
                    public void onPositive() {
                        DialogUIUtils.dismiss(dialog);
                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        showShortToast(input1+","+input2);
                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();

                if (StringUtil.isEmpty(edit_user_phone,true)){
                    showShortToast("手机号不能为空");
                    return;
                }
                if (StringUtil.isEmpty(edit_verifyCode,true)){
                    showShortToast("验证码不能为空");
                    return;
                }
                login3();
            }
        });
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
                showShortToast("验证码发送成功");
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
                showShortToast("验证码发送失败，请重试");
            }
        });
    }

    private void login() {
        showLoading("正在登陆");
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
                DemoApplication.getInstance().setUser(respon.getResult().getLoginResp());
//                DemoApplication.getInstance().getUser().setToken(Constant.TEST_TOKEN);

                login3();
//                toActivity(new Intent(context,MainActivity.class),false);
//                finish();
            }

            @Override
            protected void onFail(NetErrorException error) {
                hideLoading();
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    private boolean login2() {
        String verifyCode=edit_verifyCode.getText().toString();
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("phoneNumber",phoneNumber);
        sm.put("verifyCode",verifyCode);
        sm.put("mobileType", "Android");
        sm.put("deviceToken", DemoApplication.getInstance().getDeviceToken());
        try {
            Response<LoginRespon> response =LogicService.post(context, APIMethod.login,sm);
            if (response.getCode().equals(Response.SUCCESS)) {
                response.getResult().getLoginResp().setPhone(phoneNumber);
                DemoApplication.getInstance().setLoginRespon(response.getResult());
                DemoApplication.getInstance().setUser(response.getResult().getLoginResp());
                return true;
            }else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean loadSysBannerList() {
        TreeMap sm = new TreeMap<String,String>();
        try {
            Response<List<Banner>> response=LogicService.post(context,APIMethod.loadSysBannerList,sm);
            if (response.getCode().equals(Response.SUCCESS)) {
                DemoApplication.getInstance().setBanners(response.getResult());
                return true;
            }else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean loadContentCategory() {
        TreeMap sm = new TreeMap<String,String>();
        try {
            Response<LoadContentCategoryResponse> response=LogicService.post(context,APIMethod.loadContentCategory,sm);
            if (response.getCode().equals(Response.SUCCESS)) {
                DemoApplication.getInstance().setContentCategoryResponse(response.getResult());
                return true;
            }else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    void login3(){
        Observable.create(
                new ObservableOnSubscribe<Integer>(){
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(0);
                        boolean loginResult=login2();
                        if (loginResult){
                            emitter.onNext(1);
                            boolean loadSysBannerListResult=loadSysBannerList();
                            boolean loadContentCategoryResult=loadContentCategory();
                            if(loginResult&&loadSysBannerListResult&&loadContentCategoryResult) {
                                emitter.onNext(2);
                            }else {
                                emitter.onError(new RuntimeException("登录失败，原因：同步数据失败"));
                            }
                        }else {
                            emitter.onError(new RuntimeException("登录失败"));
                        }
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer r) throws Exception {
                        if (r==0){
                            showLoading("正在登陆");
                        }else if (r==1){
                            hideLoading();
                            showLoading("正在同步数据");
                        }else if (r==2){
                            hideLoading();
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

    @Override
    public void onClick(View v) {

    }
}
