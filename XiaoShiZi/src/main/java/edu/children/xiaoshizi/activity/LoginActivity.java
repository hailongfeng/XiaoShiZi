package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
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
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);
    }

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
                login();
            }
        });
    }


    private void getVeryCode() {
        TreeMap sm = new TreeMap<String,String>();
        String phone=edit_user_phone.getText().toString();
        if (StringUtils.isEmpty(phone))
            sm.put("phoneNumber","18697386272");
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
        sm.put("deviceToken","");
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

                t1();
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

    void t1(){
        Observable.create(
                new ObservableOnSubscribe<String>(){
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        TreeMap sm = new TreeMap<String,String>();
                        try {
                            retrofit2.Response<List<Banner>> response=LogicService.post(context,APIMethod.loadSysBannerList,sm);
                            emitter.onNext("111");
                            retrofit2.Response<List<LoadContentCategoryResponse>> response1=LogicService.post(context,APIMethod.loadContentCategory,sm);
                            emitter.onNext("222");
                            if (response.isSuccessful()&&response1.isSuccessful()){
                                List<Banner> banners=response.body();
                                Log.d(TAG,"banner size="+banners.size());
                            }else {
                                emitter.onError(new RuntimeException("服务器错误"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            emitter.onError(e);
                        }
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept = " + s);
                        showShortToast(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showShortToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}
