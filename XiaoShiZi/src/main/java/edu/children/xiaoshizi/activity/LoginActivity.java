package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
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
import edu.children.xiaoshizi.bean.LoginRespon;
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
        SortedMap sm = new TreeMap<String,String>();
        String phone=edit_user_phone.getText().toString();
        if (StringUtils.isEmpty(phone))
        sm.put("phoneNumber","18697386272");
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        sm.put("sign", Tools.createSign(sm));
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.getVerifyCode(requestBody),new ApiSubscriber<Response>(){
            @Override
            public void onNext(Response response) {
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
        String verifyCode=edit_verifyCode.getText().toString();
        TreeMap sm = new TreeMap<String,String>();
        String phoneNumber=edit_user_phone.getText().toString();
        sm.put("phoneNumber",phoneNumber);
        sm.put("verifyCode",verifyCode);
        sm.put("deviceToken","");
        LogicService.login(context, sm, new ApiSubscriber<Response<LoginRespon>>() {
            @Override
            public void onNext(Response<LoginRespon> respon) {
                DemoApplication.getInstance().setLoginRespon(respon.getResult());
                DemoApplication.getInstance().setUser(respon.getResult().getLoginResp());
//                DemoApplication.getInstance().getUser().setToken(Constant.TEST_TOKEN);
                toActivity(new Intent(context,MainActivity.class));
                finish();
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
