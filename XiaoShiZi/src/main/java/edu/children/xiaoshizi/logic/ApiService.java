package edu.children.xiaoshizi.logic;



import com.alibaba.fastjson.JSONObject;

import java.util.List;

import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.net.rxjava.Response;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("https://www.jianshu.com/p/0ad99e598dba")
    Observable<String> getJianShu();

    @GET("https://www.jianshu.com/p/0ad99e598dba")
    Flowable<String> getJianShu2();

    @POST("sendSms")
    Observable<Response> getVerifyCode(@Body RequestBody requestBody);
    @POST("login")
    Observable<Response<User>> login(@Body RequestBody requestBody);
    @POST("school/loadSchoolData")
    Observable<Response<List<School>>> getSchoolInfo(@Body RequestBody requestBody);
    @POST("student/studentBinding")
    Observable<Response<JSONObject>> studentBinding(@Body RequestBody requestBody);
}
