package edu.children.xiaoshizi.logic;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.net.rxjava.Response;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {


    @Multipart
    @POST("upload/uploadPic")
    Observable<Response<JSONArray>> uploadFile(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> parts);

    @POST("sendSms")
    Observable<Response> getVerifyCode(@Body RequestBody requestBody);

    @POST("login")
    Observable<Response<LoginRespon>> login(@Body RequestBody requestBody);

    @POST("school/loadSchoolData")
    Observable<Response<List<School>>> getSchoolInfo(@Body RequestBody requestBody);

    @POST("student/studentBinding")
    Observable<Response<LoginRespon>> studentBinding(@Body RequestBody requestBody);

    @POST("sysUser/getStudentsAndParents")
    Observable<Response<LoginRespon>> getStudentsAndParents(@Body RequestBody requestBody);

    @POST("sysUser/getMyProfile")
    Observable<Response<User>> getMyProfile(@Body RequestBody requestBody);

    @POST("sysUser/saveMyProfile")
    Observable<Response<User>> saveMyProfile(@Body RequestBody requestBody);


}
