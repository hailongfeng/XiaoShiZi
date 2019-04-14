package edu.children.xiaoshizi.net.rxjava;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import edu.children.xiaoshizi.DemoApplication;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
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
        } else {
            String msg=(t!=null&&StringUtil.isNotEmpty(t.getMessage(),true)?t.getMessage():"服务器异常");
            onFail(new Exception(msg));
        }
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