package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.ApiService;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.net.rxjava.RetrofitClient;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.FileUtil;
import edu.children.xiaoshizi.utils.IOUtil;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.Tools;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseActivity;
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
        BuildBean buildBean=DialogUIUtils.showLoading(context, "正在登录",true,false,false,true);
//        buildBean.dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        buildBean.show();
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
                DialogUIUtils.dismiss(buildBean);
                Log.d(TAG,"phoneNumber="+phoneNumber);
                respon.getResult().getLoginResp().setPhone(phoneNumber);
                DemoApplication.getInstance().setLoginRespon(respon.getResult());
                DemoApplication.getInstance().setUser(respon.getResult().getLoginResp());
//                DemoApplication.getInstance().getUser().setToken(Constant.TEST_TOKEN);
                toActivity(new Intent(context,MainActivity.class),false);
                finish();
            }

            @Override
            protected void onFail(NetErrorException error) {
                DialogUIUtils.dismiss(buildBean);
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });

    }

    void test1(){
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("studentId","");
        sm.put("studentId","2019-04-01");
        LogicService.post(context, APIMethod.findStudentSnapMsg,sm, new ApiSubscriber<Response<List<InAndOutSchoolRecode>>>() {
            @Override
            public void onSuccess(Response<List<InAndOutSchoolRecode>> respon) {
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
            }
        });
    }
    void test2(){
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("snapMsgId","");
        LogicService.post(context, APIMethod.findSnapMsgById,sm, new ApiSubscriber<Response<List<InAndOutSchoolRecode>>>() {
            @Override
            public void onSuccess(Response<List<InAndOutSchoolRecode>> respon) {
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
            }
        });
    }
    void test3(){
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("snapMsgId","");
        sm.put("feedbackResult","1");
        LogicService.post(context, APIMethod.findStudentSnapMsg,sm, new ApiSubscriber<Response<List<InAndOutSchoolRecode>>>() {
            @Override
            public void onSuccess(Response<List<InAndOutSchoolRecode>> respon) {
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
