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

        if (!isTaskRoot()) {
            // Android launched another instance of the root activity into an existing task
            //  so just quietly finish and go away, dropping the user back into the activity
            //  at the top of the stack (ie: the last state of this task)
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.xsz_login_bg)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    public void initData() {

        String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        EasyPermissions.requestPermissions(
                context,
                "申请权限",
                0,mPermissionList
                );
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
//                DialogUIUtils.showToastCenter("发送成功");
//                dialog= DialogUIUtils.showAlert(context,"清除成功","清除30M","aaa","bbb","确定","取消",true,true,true,new DialogUIListener(){
//
//                    @Override
//                    public void onPositive() {
//                        DialogUIUtils.dismiss(dialog);
//                    }
//
//                    @Override
//                    public void onGetInput(CharSequence input1, CharSequence input2) {
//                        super.onGetInput(input1, input2);
//                        showShortToast(input1+","+input2);
//                    }
//
//                    @Override
//                    public void onNegative() {
//
//                    }
//                }).show();

                if (StringUtil.isEmpty(edit_user_phone,true)){
                    showShortToast("手机号不能为空");
                    return;
                }
                if (StringUtil.isEmpty(edit_verifyCode,true)){
                    showShortToast("验证码不能为空");
                    return;
                }
//                DialogUIUtils.showToastCenter("更新成功");
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
                showShortToast(response.getMessage());
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast(error.getMessage());
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
                            showLoading("正在登陆");
                        }else if (r.getCode().equals("2")){
                            hideLoading();
                            showShortToast(r.getMessage());
                        }else if (r.getCode().equals("3")){
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

    @Override
    public void onClick(View v) {

    }
}
