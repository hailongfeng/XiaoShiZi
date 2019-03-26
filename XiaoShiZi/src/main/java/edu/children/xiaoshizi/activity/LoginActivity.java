package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.bean.request.RequestBindStudentParam;
import edu.children.xiaoshizi.logic.ApiService;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.net.rxjava.RetrofitClient;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.Tools;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.Log;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnGetVerifyCode)
    Button btnGetVerifyCode;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.edit_verifyCode)
    EditText edit_verifyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
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
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void initEvent() {
        findView(R.id.btnGetVerifyCode, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVeryCode();
            }
        });
        findView(R.id.btnLogin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findView(R.id.btnGetSchoolInfo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSchoolInfo(1);
//                getSchoolInfo(2);
//                getSchoolInfo(3);
                studentBinding();
            }
        });
    }




    private void studentBinding() {
        RequestBindStudentParam param=new RequestBindStudentParam();
        param.setTimestamp(System.currentTimeMillis()+"");
        param.setNoncestr(StringUtils.randomString(10));
        param.setSign("ECA78F26B61B69E70522D3C329B64A67");
        param.setToken(Constant.TEST_TOKEN);
        param.setParentId(Constant.TEST_USER_ID);
        param.setUserName("张三");
        param.setSex("M");
        param.setSchoolId("suA38j1AxGBjWcUJP4h");
        param.setSchoolGradeId("5eaZmroMMgYZu0kX1nB");
        param.setSchoolClassId("uQ8jhJZCPEP1vqA9lQZ");
        param.setBirthday("2019-03-20");
        param.setParentCustody("爸爸");
        param.setBindingPassword("1");
        String root = JSONObject.toJSONString(param);

        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.studentBinding(requestBody),new DisposableObserver<Response<JSONObject>>(){

            @Override
            public void onNext(Response<JSONObject> response) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"onComplete , "+Thread.currentThread().getName());
            }
        });
    }


    private void getVeryCode() {
        SortedMap sm = new TreeMap<String,String>();
        sm.put("phoneNumber","18697386272");
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        sm.put("sign", Tools.createSign(sm));
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.getVerifyCode(requestBody),new DisposableObserver<Response>(){

            @Override
            public void onNext(Response response) {
                Log.d(TAG,"response code:"+response.getCode());
                Log.d(TAG,"onNext  , "+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"onComplete , "+Thread.currentThread().getName());
            }
        });
    }

    private void login() {
        String verifyCode=edit_verifyCode.getText().toString();
        SortedMap sm = new TreeMap<String,String>();
        sm.put("phoneNumber","18697386272");
        sm.put("verifyCode",verifyCode);
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        sm.put("sign", Tools.createSign(sm));
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.login(requestBody),new DisposableObserver<Response<User>>(){

            @Override
            public void onNext(Response<User> response) {
                Log.d(TAG,"response code:"+response.getCode());
                Log.d(TAG,"onNext  , "+Thread.currentThread().getName());
                User user=response.getResult();
                user.setPhone("18697386272");
                DemoApplication.getInstance().setUser(user);
                Log.d(TAG,"loginName="+DemoApplication.getInstance().getUser().getLoginName());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"onComplete , "+Thread.currentThread().getName());
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
