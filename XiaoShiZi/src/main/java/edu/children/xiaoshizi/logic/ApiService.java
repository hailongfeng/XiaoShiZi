package edu.children.xiaoshizi.logic;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.RealNameAuthInfo;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.net.rxjava.Response;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiService {


    @Multipart
    @POST("v1/upload/uploadPic")
    Observable<Response<JSONArray>> uploadFile(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> parts);

    @POST("v1/sendSms")
    Observable<Response> getVerifyCode(@Body RequestBody requestBody);

    @POST("v1/login")
    Observable<Response<LoginRespon>> login(@Body RequestBody requestBody);

    @POST("v1/sys/loadSysBannerList")
    Call<retrofit2.Response<List<Banner>>> loadSysBannerList(@Body RequestBody requestBody);
    @POST("v1/index/loadContentCategory")
    Call<retrofit2.Response<LoadContentCategoryResponse>> loadContentCategory(@Body RequestBody requestBody);


    @POST("{apiVerison}/school/loadSchoolData")
    Observable<Response<List<School>>> loadSchoolData(@Path("apiVerison") String apiVerison, @Body RequestBody requestBody);

    @POST("v1/student/studentBinding")
    Observable<Response<LoginRespon>> studentBinding(@Body RequestBody requestBody);

    @POST("v1/sysUser/getStudentsAndParents")
    Observable<Response<LoginRespon>> getStudentsAndParents(@Body RequestBody requestBody);
    @POST("v1/sysUser/verifiedSubmit")
    Observable<Response<RealNameAuthInfo>> verifiedSubmit(@Body RequestBody requestBody);

    @POST("v1/sysUser/getMyProfile")
    Observable<Response<User>> getMyProfile(@Body RequestBody requestBody);

    @POST("v1/sysUser/saveMyProfile")
    Observable<Response<User>> saveMyProfile(@Body RequestBody requestBody);

    @POST("v1/student/findStudentSnapMsg")
    Observable<Response<List<InAndOutSchoolRecode>>> findStudentSnapMsg(@Body RequestBody requestBody);
    @POST("v1/student/findSnapMsgById")
    Observable<Response<InAndOutSchoolRecode>> findSnapMsgById(@Body RequestBody requestBody);
    @POST("v1/student/doSnapMsgFeedBack")
    Observable<Response<User>> doSnapMsgFeedBack(@Body RequestBody requestBody);


}
