package edu.children.xiaoshizi.net.rxjava;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.ResourceSubscriber;
import zuo.biao.library.util.Log;

public abstract class ApiSubscriber<T extends Response> extends DisposableObserver<T> {

    private static final String TAG="ApiSubscriber";
    @Override
    public void onComplete() {

    }

    @Override
    public final void onNext(T t) {
        Log.d(TAG,"code:"+t.getCode()+"，message ："+t.getMessage());
        if (t.getCode()==Response.SUCCESS){
            onSuccess(t);
        }else {
            onError(new RuntimeException(t.getMessage()));
        }
    }

    @Override
    public final void onError(Throwable e) {
        NetErrorException error = null;
        if (e != null) {
            // 对不是自定义抛出的错误进行解析
            if (!(e instanceof NetErrorException)) {
                if (e instanceof UnknownHostException) {
                    error = new NetErrorException(e, NetErrorException.NoConnectError);
                } else if (e instanceof JSONException || e instanceof JsonParseException) {
                    error = new NetErrorException(e, NetErrorException.PARSE_ERROR);
                } else if (e instanceof SocketTimeoutException) {
                    error = new NetErrorException(e, NetErrorException.SocketTimeoutError);
                } else if (e instanceof ConnectException) {
                    error = new NetErrorException(e, NetErrorException.ConnectExceptionError);
                } else if (e instanceof RuntimeException) {
                    error = new NetErrorException(e.getMessage(), NetErrorException.OTHER);
                } else {
                    error = new NetErrorException(e, NetErrorException.OTHER);
                }
            } else {
                error = new NetErrorException(e.getMessage(), NetErrorException.OTHER);
            }
        }
        // 回调抽象方法
        onFail(error);
    }

    /**
     * 回调接口
     */
    protected abstract void onSuccess(T Response);
    protected abstract void onFail(NetErrorException error);
}