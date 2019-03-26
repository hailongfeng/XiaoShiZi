package edu.children.xiaoshizi.logic;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.SortedMap;
import java.util.TreeMap;

import edu.children.xiaoshizi.net.rxjava.RetrofitClient;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.Tools;
import io.reactivex.Observer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import zuo.biao.library.util.Log;

public class LogicService {
   private static final String TAG=LogicService.class.getSimpleName();
    public static void getVerifyCode(){

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
}
