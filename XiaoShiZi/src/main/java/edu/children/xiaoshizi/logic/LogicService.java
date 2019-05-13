package edu.children.xiaoshizi.logic;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.bean.User;
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
import zuo.biao.library.util.Log;

public class LogicService {
    private static final String TAG = LogicService.class.getSimpleName();

    private static RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    static void printParam(APIMethod method, TreeMap<String, String> param) {
        Log.d(TAG, "method = " + method.name() + ",param = " + JSONObject.toJSONString(param));
    }

    public static void post(Context context, final APIMethod method, TreeMap<String, String> param, Map<String, File> imagefiles, Map<String, File> videoFiles, Observer subscriber) {
        if (param == null) {
            param = new TreeMap<String, String>();
        }
        if (imagefiles == null) {
            imagefiles = new HashMap<String, File>();
        }
        if (videoFiles == null) {
            videoFiles = new HashMap<String, File>();
        }
        appendCommonParam(param, true);
        printParam(method, param);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        //普通文本参数
        Set<String> keys = param.keySet();
        Map<String, RequestBody> params = new HashMap<>();
        for (String key : keys) {
            params.put(key, convertToRequestBody(param.get(key)));
        }

        //图片参数
        Set<Map.Entry<String, File>> setImage = imagefiles.entrySet();
        for (Map.Entry<String, File> entry : setImage) {
            String key = entry.getKey();
            File file = entry.getValue();
            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
            builder.addFormDataPart(key, file.getName(), body);
        }
        //视频参数
        Set<Map.Entry<String, File>> setVideo = videoFiles.entrySet();
        for (Map.Entry<String, File> entry : setVideo) {
            String key = entry.getKey();
            File file = entry.getValue();
            RequestBody body1 = RequestBody.create(MediaType.parse("video/mp4"), file);
            builder.addFormDataPart(key, file.getName(), body1);

        }

        if (imagefiles.size()==0&&videoFiles.size()==0){
            builder.addFormDataPart("", "");
        }
        ApiService apiService = RetrofitClient.getInstance(context).provideApiService();
        List<MultipartBody.Part> parts = builder.build().parts();
        Observable observable = null;
        if (method == APIMethod.uploadStudentHeadPortrait) {
            observable = apiService.uploadStudentHeadPortrait(params, parts);
        } else if (method == APIMethod.submitDraftContent) {
            observable = apiService.submitDraftContent(params, parts);
        } else if (method == APIMethod.uploadFile) {
            observable = apiService.uploadFile(params, parts);
        } else if (method == APIMethod.uploadVerifiedVideo) {
            observable = apiService.uploadVerifiedVideo(params, parts);
        }

        if (observable != null) {
            RetrofitClient.execute(observable, subscriber);
        } else {
            Log.e(TAG, "参数错误");
//            subscriber.onError(new );
        }
    }


    public static <T> Response<T> post(Context context, final APIMethod method, TreeMap<String, String> param) {
        if (param == null) {
            param = new TreeMap<String, String>();
        }
        if (method == APIMethod.getVerifyCode || method == APIMethod.login) {
            appendCommonParam(param, false);
        } else {
            appendCommonParam(param, true);
        }
        printParam(method, param);
        String root = JSONObject.toJSONString(param);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), root);
        ApiService apiService = RetrofitClient.getInstance(context).provideApiService();
        Call observable = null;
        if (method == APIMethod.login) {
            observable = apiService.login(requestBody);
        } else if (method == APIMethod.loadSysBannerList) {
            observable = apiService.loadSysBannerList(requestBody);
        } else if (method == APIMethod.loadContentCategory) {
            observable = apiService.loadContentCategory(requestBody);
        } else {
            return new Response<T>("500", "不支持该方法");
        }
        try {
            retrofit2.Response<Response> r = observable.execute();
            Response<T> myResponse = r.body();
            String errMessage = r.message();
            int code = r.code();
            Log.d(TAG, "code=" + code + "message=" + errMessage);
            if (r.isSuccessful() && myResponse != null) {
                return myResponse;
            } else {
                if (TextUtils.isEmpty(errMessage)) {
                    errMessage = "服务器返回异常";
                }
                return new Response<T>(r.code() + "", errMessage);
            }
        } catch (IOException e) {
            return new Response<T>("500", "网络异常");
        } catch (HttpException e) {
            return new Response<T>("500", "网络异常");
        } catch (Exception e) {
            return new Response<T>("500", "网络异常");
        }

    }

    public static void post(Context context, final APIMethod method, TreeMap<String, String> param, Observer subscriber) {
        if (param == null) {
            param = new TreeMap<String, String>();
        }
        if (method == APIMethod.getVerifyCode || method == APIMethod.login) {
            appendCommonParam(param, false);
        } else {
            appendCommonParam(param, true);
        }
        printParam(method, param);
        String root = JSONObject.toJSONString(param);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), root);
        ApiService apiService = RetrofitClient.getInstance(context).provideApiService();
//        ReflectionUtils.getMethod(ApiService.class,"d");
//        ReflectionUtils.invokeMethod()
        Observable observable = null;
        if (method == APIMethod.getVerifyCode) {
            observable = apiService.getVerifyCode(requestBody);
        } else if (method == APIMethod.validSmsVCode) {
            observable = apiService.validSmsVCode(requestBody);
        } else if (method == APIMethod.login) {
            observable = apiService.loginAsyc(requestBody);
        } else if (method == APIMethod.loginOut) {
            observable = apiService.loginOut(requestBody);
        } else if (method == APIMethod.loadSysBannerList) {
            observable = apiService.loadSysBannerListAsys(requestBody);
        } else if (method == APIMethod.loadContentCategory) {
            observable = apiService.loadContentCategoryAsyc(requestBody);
        } else if (method == APIMethod.loadSchoolData) {
            observable = apiService.loadSchoolData(APIVersion.v2.name(), requestBody);
        } else if (method == APIMethod.findStudentSnapMsg) {
            observable = apiService.findStudentSnapMsg(requestBody);
        } else if (method == APIMethod.findSnapMsgById) {
            observable = apiService.findSnapMsgById(requestBody);
        } else if (method == APIMethod.doSnapMsgFeedBack) {
            observable = apiService.doSnapMsgFeedBack(requestBody);
        } else if (method == APIMethod.getStudentsAndParents) {
            observable = apiService.getStudentsAndParents(requestBody);
        } else if (method == APIMethod.verifiedSubmit) {
            observable = apiService.verifiedSubmit(requestBody);
        } else if (method == APIMethod.studentBinding) {
            observable = apiService.studentBinding(requestBody);
        } else if (method == APIMethod.studentUnBinding) {
            observable = apiService.studentUnBinding(requestBody);
        } else if (method == APIMethod.getMyProfile) {
            observable = apiService.getMyProfile(requestBody);
        } else if (method == APIMethod.saveMyProfile) {
            observable = apiService.saveMyProfile(requestBody);
        } else if (method == APIMethod.loadContentByCategory) {
            observable = apiService.loadContentByCategory(requestBody);
        } else if (method == APIMethod.loadContentById) {
            observable = apiService.loadContentById(requestBody);
        } else if (method == APIMethod.searchContentByTitle) {
            observable = apiService.searchContentByTitle(requestBody);
        } else if (method == APIMethod.loadSeLabContentCategory) {
            observable = apiService.loadSeLabContentCategory(requestBody);
        } else if (method == APIMethod.loadSafeLabContentByCategory) {
            observable = apiService.loadSafeLabContentByCategory(requestBody);
        } else if (method == APIMethod.loadSafeLabContentById) {
            observable = apiService.loadSafeLabContentById(requestBody);
        } else if (method == APIMethod.loadSeClassRoomContentCategory) {
            observable = apiService.loadSeClassRoomContentCategory(requestBody);
        } else if (method == APIMethod.loadSeClassRoomContentByCategory) {
            observable = apiService.loadSeClassRoomContentByCategory(requestBody);
        } else if (method == APIMethod.loadSeClassRoomContentById) {
            observable = apiService.loadSeClassRoomContentById(requestBody);
        } else if (method == APIMethod.submitFeedBack) {
            observable = apiService.submitFeedBack(requestBody);
        } else if (method == APIMethod.submitComment) {
            observable = apiService.submitComment(requestBody);
        } else if (method == APIMethod.findPushAppSnapMsgList) {
            observable = apiService.findPushAppSnapMsgList(requestBody);
        }else if (method == APIMethod.loadIntegralDetailList) {
            observable = apiService.loadIntegralDetailList(requestBody);
        }else if (method == APIMethod.signedDaily) {
            observable = apiService.signedDaily(requestBody);
        }else if (method == APIMethod.aboutContent) {
            observable = apiService.aboutContent(requestBody);
        }else if (method == APIMethod.loadBannerContentById) {
            observable = apiService.loadBannerContentById(requestBody);
        }

        if (observable != null) {
            RetrofitClient.execute(observable, subscriber);
        } else {
            Log.e(TAG, "参数错误");
        }
    }

    private static void appendCommonParam(SortedMap sm, boolean isNeedToken) {
        sm.put("timestamp", System.currentTimeMillis() + "");
        sm.put("noncestr", StringUtils.randomString(10));
        if (isNeedToken) {
            if (DemoApplication.getInstance().isLogin()) {
                User user = DemoApplication.getInstance().getUser();
                if (user != null) {
                    String token = user.getToken();
                    sm.put("token", token);
                }
            }
        }
        sm.put("sign", Tools.createSign(sm));
    }
}
