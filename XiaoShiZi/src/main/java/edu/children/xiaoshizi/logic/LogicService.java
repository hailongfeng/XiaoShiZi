package edu.children.xiaoshizi.logic;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.net.rxjava.RetrofitClient;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.Tools;
import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;
import zuo.biao.library.util.Log;

public class LogicService {
   private static final String TAG=LogicService.class.getSimpleName();
    public static void getVerifyCode(){

    }
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }
    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
    private static RequestBody convertToRequestBody(String param){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    public static void uploadPic(Context context, TreeMap<String,String> param, List<File> files,  Observer subscriber) {
        SortedMap<String,String> sm = param;
        appendCommonParam(sm,true);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (File file:files){
//            RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            RequestBody body=RequestBody.create(MediaType.parse("image/png"),file);
            builder.addFormDataPart("picfile",file.getName(),body);
        }
        Set<String>  keys=sm.keySet();
        Map<String, RequestBody> params=new HashMap<>();
        for (String key:keys){
            params.put(key,convertToRequestBody(sm.get(key)));
        }

        List<MultipartBody.Part> parts=builder.build().parts();
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.uploadFile(params,parts),subscriber);
    }

    public static void uploadVerifiedVideo(Context context, TreeMap<String,String> param, List<File> files,  Observer subscriber) {
        SortedMap<String,String> sm = param;
        appendCommonParam(sm,true);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (File file:files){
//            RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data"),file);
            RequestBody body=RequestBody.create(MediaType.parse("video/mp4"),file);
            builder.addFormDataPart("video_file",file.getName(),body);
        }
        Set<String>  keys=sm.keySet();
        Map<String, RequestBody> params=new HashMap<>();
        for (String key:keys){
            params.put(key,convertToRequestBody(sm.get(key)));
        }

        List<MultipartBody.Part> parts=builder.build().parts();
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.uploadVerifiedVideo(params,parts),subscriber);
    }
    public static <T> Response<T> post(Context context, final APIMethod method, TreeMap<String,String> param) {
        if (param==null){
            param=new TreeMap<String,String>();
        }
        if (method==APIMethod.getVerifyCode||method==APIMethod.login){
            //no case
            appendCommonParam(param,false);
        }else {
            appendCommonParam(param,true);
        }
        String root = JSONObject.toJSONString(param);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        Call observable=null;
        if (method==APIMethod.login){
            observable=apiService.login(requestBody);
        }else if (method==APIMethod.loadSysBannerList){
            observable=apiService.loadSysBannerList(requestBody);
        }else if (method==APIMethod.loadContentCategory){
            observable=apiService.loadContentCategory(requestBody);
        }else {
            return new Response<T>("500","不支持该方法");
        }
        try {
            retrofit2.Response<Response> r = observable.execute();
            Response<T> myResponse = r.body();
            String errMessage=r.message();
            int code=r.code();
            Log.d(TAG,"code="+code+"message="+errMessage);
            if (r.isSuccessful()&&myResponse!=null){
                return myResponse;
            }else {
                if (TextUtils.isEmpty(errMessage)){
                    errMessage="服务器返回异常";
                }
                return new Response<T>(r.code()+"",errMessage);
            }
        }catch (IOException e){
            return new Response<T>("500","网络异常");
        }catch (HttpException e){
            return new Response<T>("500","网络异常");
        }catch (Exception e){
            return new Response<T>("500","网络异常");
        }

    }

    public static void post(Context context,final APIMethod method,TreeMap<String,String> param,  Observer subscriber){
        if (param==null){
            param=new TreeMap<String,String>();
        }
        if (method==APIMethod.getVerifyCode||method==APIMethod.login){
            appendCommonParam(param,false);
        }else {
            appendCommonParam(param,true);
        }
        String root = JSONObject.toJSONString(param);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        Observable observable=null;
        if (method==APIMethod.getVerifyCode){
            observable=apiService.getVerifyCode(requestBody);
        }/*else if (method==APIMethod.login){
            observable=apiService.login(requestBody);
        }*/else if (method==APIMethod.loadSchoolData){
            observable=apiService.loadSchoolData(APIVersion.v2.name(),requestBody);
        }else if (method==APIMethod.findStudentSnapMsg){
            observable=apiService.findStudentSnapMsg(requestBody);
        }else if (method==APIMethod.findSnapMsgById){
            observable=apiService.findSnapMsgById(requestBody);
        }else if (method==APIMethod.doSnapMsgFeedBack){
            observable=apiService.doSnapMsgFeedBack(requestBody);
        }else if (method==APIMethod.getStudentsAndParents){
            observable=apiService.getStudentsAndParents(requestBody);
        }else if (method==APIMethod.verifiedSubmit){
            observable=apiService.verifiedSubmit(requestBody);
        }else if (method==APIMethod.studentBinding){
            observable=apiService.studentBinding(requestBody);
        }else if (method==APIMethod.studentUnBinding){
            observable=apiService.studentUnBinding(requestBody);
        }else if (method==APIMethod.getMyProfile){
            observable=apiService.getMyProfile(requestBody);
        }else if (method==APIMethod.saveMyProfile){
            observable=apiService.saveMyProfile(requestBody);
        }else if (method==APIMethod.loadContentByCategory){
            observable=apiService.loadContentByCategory(requestBody);
        }else if (method==APIMethod.loadContentById){
            observable=apiService.loadContentById(requestBody);
        }else if (method==APIMethod.searchContentByTitle){
            observable=apiService.searchContentByTitle(requestBody);
        }else if (method==APIMethod.loadSeLabContentCategory){
            observable=apiService.loadSeLabContentCategory(requestBody);
        }else if (method==APIMethod.loadSafeLabContentByCategory){
            observable=apiService.loadSafeLabContentByCategory(requestBody);
        }else if (method==APIMethod.loadSafeLabContentById){
            observable=apiService.loadSafeLabContentById(requestBody);
        }else if (method==APIMethod.loadSeClassRoomContentCategory){
            observable=apiService.loadSeClassRoomContentCategory(requestBody);
        }else if (method==APIMethod.loadSeClassRoomContentByCategory){
            observable=apiService.loadSeClassRoomContentByCategory(requestBody);
        }else if (method==APIMethod.loadSeClassRoomContentById){
            observable=apiService.loadSeClassRoomContentById(requestBody);
        }

        if (observable!=null) {
            RetrofitClient.execute(observable, subscriber);
        }else {
            Log.e(TAG,"参数错误");
        }
    }
    private static void appendCommonParam(SortedMap sm,boolean isNeedToken){
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        if (isNeedToken) {
            String token = DemoApplication.getInstance().getUser().getToken();
            sm.put("token", token);
        }
        sm.put("sign", Tools.createSign(sm));
    }
}
