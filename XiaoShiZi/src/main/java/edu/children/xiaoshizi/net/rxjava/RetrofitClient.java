package edu.children.xiaoshizi.net.rxjava;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import edu.children.xiaoshizi.logic.ApiService;
import edu.children.xiaoshizi.utils.Constant;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import zuo.biao.library.util.Log;

public class RetrofitClient {


    private static RetrofitClient instance;

    private OkHttpClient mOkHttpClient;
    private Context mContext;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    private RetrofitClient(Context context) {
        this.mContext = context;
    }

    public static RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }


    private OkHttpClient provideOkHttpClient() {
        if (mOkHttpClient == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            Interceptor addQueryParameterInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request oldRequest = chain.request();
                    String method = oldRequest.method();
                    HttpUrl.Builder newQueryParameter = oldRequest.url().newBuilder();
                    Map<String, String> commonQueryParameter = new HashMap<>();
                    for (Map.Entry<String, String> entry : commonQueryParameter.entrySet()) {
                        newQueryParameter.addQueryParameter(entry.getKey(), entry.getValue());
                    }
                    String contentType = oldRequest.header("Content-Type");
                    Request.Builder newBuilder = oldRequest.newBuilder();
                    if (Build.VERSION.SDK_INT > 13) {
                        newBuilder.addHeader("Connection", "close");
                    }
                    if (contentType == null) {
                        newBuilder.addHeader("Content-Type", "application/json;charset=UTF-8");
                    }
                    newBuilder.url(newQueryParameter.build());
                    Request newRequest=newBuilder.build();
                    printHead(newRequest.headers());
                    return chain.proceed(newRequest);
                }
            };


            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor)  //日志
                    .addInterceptor(addQueryParameterInterceptor)  //公共参数的封装
                    .cookieJar(new CookieJarImpl(mContext)) //cookie 保存方案
                    .build();
        }

        return mOkHttpClient;
    }

    private void printHead(Headers headers){
        Set<String> names=headers.names();
        for (String n:names){
          Log.d("head",n+"="+Arrays.toString(headers.values(n).toArray()));
        }
    }

    private Retrofit provideRetrofit() {

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASEURL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   //RxJava2 的CallAdapter
                    .addConverterFactory(FastJsonConverterFactory.create())       // FastJsonConvertFactory
                    .client(provideOkHttpClient()).build();
        }

        return mRetrofit;
    }

    public ApiService provideApiService() {
        if (mApiService == null) {
            mApiService = provideRetrofit().create(ApiService.class);
        }
        return mApiService;
    }


    //拦截固定格式的公共数据类型Response<T>,判断里面的状态码
    public static class ServerResponseFunc<T> implements Function<Response<T>, T> {
        @Override
        public T apply(Response<T> reponse) throws Exception {
            //对返回码进行判断，如果不是0，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常

            if (reponse.getCode().equals(Response.SUCCESS)){
                //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
                throw new NetErrorException(reponse.message,NetErrorException.OTHER);
            }
            //服务器请求数据成功，返回里面的数据实体
            return reponse.result;
        }
    }

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .map((Function<? super T, ?>) new ServerResponseFunc<T>())
                .subscribe(subscriber);
        return null;
    }

    public static <T> T execute(Flowable<T> observable, Consumer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }

}
