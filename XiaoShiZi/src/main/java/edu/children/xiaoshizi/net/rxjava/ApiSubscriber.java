package edu.children.xiaoshizi.net.rxjava;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.blankj.utilcode.util.CacheUtils;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.google.gson.JsonParseException;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.activity.LoginOutActivity;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.utils.Constant;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public abstract class ApiSubscriber<T extends Response> extends DisposableObserver<T> {

    private static final String TAG = "ApiSubscriber";

    @Override
    public void onComplete() {

    }

    @Override
    public final void onNext(T t) {
        Log.d(TAG, "code:" + t.getCode() + "，message ：" + t.getMessage());
        if (t!=null&&t.getCode().equals(Response.SUCCESS)) {
            onSuccess(t);
        } else if (t.getCode().equals("10012")){
//            t1(t.getMessage());
//            EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_login_ineffective,t.getMessage()));
            String msg=(t!=null&&StringUtil.isNotEmpty(t.getMessage(),true)?t.getMessage():"服务器异常");
            onFail(new Exception(msg));
            DemoApplication.getInstance().startActivity(LoginOutActivity.createIntent(DemoApplication.getInstance(), t.getMessage()));
        }else{
            String msg=(t!=null&&StringUtil.isNotEmpty(t.getMessage(),true)?t.getMessage():"服务器异常");
            onFail(new Exception(msg));
        }
    }

    void t1(){
        
    }



    @Override
    public final void onError(Throwable e) {
        e.printStackTrace();
        Exception error =new Exception("服务器异常");;
        if (e != null) {
            if (e instanceof HttpException) {
                ResponseBody responseBody = ((HttpException) e).response().errorBody();
                if (responseBody!=null){
                    try {
                        String bodyString=responseBody.string();
                        if (TextUtils.isEmpty(bodyString)) {
                            error = new Exception(bodyString);
                        }else {
                            error = new Exception("服务器异常");
                        }
                    } catch (IOException e1) {
                        error = new Exception("服务器异常");
                        e1.printStackTrace();
                    }
                }else {
                    error = new Exception("服务器异常");
                }
            }else  if (e instanceof UnknownHostException) {
                error = new Exception("网络异常");
            } else if (e instanceof JSONException || e instanceof JsonParseException) {
                error = new Exception("数据解析失败");
            } else if (e instanceof SocketTimeoutException) {
                error = new Exception("网络异常");
            } else if (e instanceof UnknownServiceException) {
                error = new Exception("网络异常");
            } else if (e instanceof ConnectException) {
                error = new Exception("网络异常");
            } else if (e instanceof IOException) {
                error = new Exception("网络异常");
            }else{
                error = new Exception(e.getMessage());
            }
        }
        onFail(error);
    }

    /**
     * 回调接口
     */
    protected abstract void onSuccess(T response);

    protected abstract void onFail(Throwable error);
}