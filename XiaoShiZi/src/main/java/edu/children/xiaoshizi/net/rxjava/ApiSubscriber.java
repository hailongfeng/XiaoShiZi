package edu.children.xiaoshizi.net.rxjava;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subscribers.ResourceSubscriber;

public abstract class ApiSubscriber<T> extends DisposableObserver<T> {

    @Override
    public void onComplete() {

    }
    @Override
    public void onError(Throwable e) {
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
     * 回调错误
     */
    protected abstract void onFail(NetErrorException error);
}