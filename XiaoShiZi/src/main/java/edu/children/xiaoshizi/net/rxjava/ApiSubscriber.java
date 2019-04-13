package edu.children.xiaoshizi.net.rxjava;

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
import zuo.biao.library.util.Log;

public abstract class ApiSubscriber<T extends Response> extends DisposableObserver<T> {

    private static final String TAG = "ApiSubscriber";

    @Override
    public void onComplete() {

    }

    @Override
    public final void onNext(T t) {
        Log.d(TAG, "code:" + t.getCode() + "，message ：" + t.getMessage());
        if (t.getCode().equals(Response.SUCCESS)) {
            onSuccess(t);
        } else {
            onError(new Exception(t.getMessage()));
        }
    }

    @Override
    public final void onError(Throwable e) {
        Exception error = null;
        if (e != null) {
            if (e instanceof UnknownHostException) {
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
//        MobclickAgent.reportError(DemoApplication.getInstance(),error);
    }

    /**
     * 回调接口
     */
    protected abstract void onSuccess(T response);

    protected abstract void onFail(Throwable error);
}