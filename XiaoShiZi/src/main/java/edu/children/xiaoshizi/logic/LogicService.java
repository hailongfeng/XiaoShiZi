package edu.children.xiaoshizi.logic;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.net.rxjava.RetrofitClient;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.Tools;
import io.reactivex.Observer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Header;
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


    public static void login(Context context,TreeMap<String,String> param,  Observer subscriber) {
        SortedMap sm = param;
        sm.put("mobileType", "Android");
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        sm.put("sign", Tools.createSign(sm));
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.login(requestBody),subscriber);
    }

    public static void uploadPic(Context context, TreeMap<String,String> param, List<File> files,  Observer subscriber) {
        SortedMap<String,String> sm = param;
        appendCommonParam(sm);
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


    public static void getSchoolInfo(Context context, int type, Observer subscriber) {
        SortedMap sm = new TreeMap<String,String>();
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        sm.put("sign", Tools.createSign(sm));
        sm.put("token", Constant.TEST_TOKEN);
        if (type==2){
            sm.put("parentId","suA38j1AxGBjWcUJP4h");
        }else if (type==3){
            sm.put("parentId","o33hGG7RPcmkHHvZ4EW");
        }else {
            sm.put("parentId", "1");
        }
        sm.put("type",type);
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.getSchoolInfo(requestBody),subscriber);
    }

    public static void getStudentsAndParents(Context context,TreeMap<String,String> param,  Observer subscriber) {
        SortedMap sm = param;
        appendCommonParam(sm);
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.getStudentsAndParents(requestBody),subscriber);
    }


    public static void studentBinding(Context context,TreeMap<String,String> param,  Observer subscriber) {
        SortedMap sm = param;
        appendCommonParam(sm);
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.studentBinding(requestBody),subscriber);
    }


    public static void getMyProfile(Context context,TreeMap<String,String> param,  Observer subscriber) {
        SortedMap sm = param;
        appendCommonParam(sm);
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.getMyProfile(requestBody),subscriber);
    }

    public static void saveMyProfile(Context context,TreeMap<String,String> param,  Observer subscriber) {
        SortedMap sm = param;
        appendCommonParam(sm);
        String root = JSONObject.toJSONString(sm);
        Log.d(TAG,"root ="+root);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),root);
        ApiService apiService= RetrofitClient.getInstance(context).provideApiService();
        RetrofitClient.execute(apiService.saveMyProfile(requestBody),subscriber);
    }




    private static void appendCommonParam(SortedMap sm){
        sm.put("timestamp", System.currentTimeMillis()+"");
        sm.put("noncestr", StringUtils.randomString(10));
        String token=DemoApplication.getInstance().getUser().getToken();
        sm.put("token", token);
        sm.put("sign", Tools.createSign(sm));
    }

}
