package edu.children.xiaoshizi.logic;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import org.android.agoo.huawei.HuaWeiRegister;
import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Map;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.MessageDetailActivity;
import edu.children.xiaoshizi.activity.MessageErrorDetailActivity;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class UmengMessageHandle {
    private static final String TAG = "UmengMessageHandle";
    private static final String appKey="5ca1c9ee20365714de0016c7";
    private static final String messageKey="9f02750370dfce2541fcfbe2f9e0803d";

    private static final String wxKey="wx2425baf047ee9bdb";
    private static final String wxEncryKey="1acb0f130bbb75e2c23249daa0d45cd0";
    private static final String qqKey="1108806144";
    private static final String qqEncryKey="nBUEjTV9vNV0yaQt";


    private PushAgent mPushAgent;
    private Context context;
    public UmengMessageHandle(Context context) {
        this.context=context;
    }

    public void init(){
        UMConfigure.init(context, appKey, "豌豆荚", UMConfigure.DEVICE_TYPE_PHONE, messageKey);
        mPushAgent = PushAgent.getInstance(context);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i(TAG, "deviceToken="+deviceToken);
                DemoApplication.getInstance().setDeviceToken(deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.i(TAG, s+","+s1+"");
            }
        });
        mPushAgent.setMessageHandler(messageHandler);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        Log.d(TAG,"weixin = "+"wxa41c3e0c840a841a"+" , "+"fa491522a0411a83437a90d57b252e0b");
//        PlatformConfig.setWeixin("wxa41c3e0c840a841a", "fa491522a0411a83437a90d57b252e0b");
        PlatformConfig.setWeixin(wxKey, wxEncryKey);
//        PlatformConfig.setQQZone("101571206", "369a35990daa3f5185eb2dfc02ef5ac7");
        PlatformConfig.setQQZone(qqKey, qqEncryKey);
        registerHuaWei();
    }

    void registerHuaWei(){
        HuaWeiRegister.register(DemoApplication.getInstance());
    }

    private UmengMessageHandler messageHandler = new UmengMessageHandler() {

        @Override
        public void handleMessage(Context context, UMessage uMessage) {
            EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_message_new,"新消息",""));
            super.handleMessage(context, uMessage);
        }

        /**
         * 自定义通知栏样式的回调方法
         */
        @Override
        public Notification getNotification(Context context, UMessage msg) {
            for (Map.Entry entry : msg.extra.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                Log.d(TAG,key.toString()+"="+value);
            }
            Log.e(TAG,"msg.builder_id ="+msg.builder_id+"getNotification after_open=" +msg.after_open);
//            switch (msg.builder_id) {
//                case 1:
//                    Log.d(TAG,"创建通知...");
//                    Notification.Builder builder = new Notification.Builder(context);
//                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
//                            R.layout.notification_view);
//                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                    myNotificationView.setImageViewResource(R.id.notification_small_icon,
//                            getSmallIconId(context, msg));
//                    builder.setContent(myNotificationView)
//                            .setSmallIcon(getSmallIconId(context, msg))
//                            .setTicker(msg.ticker)
//                            .setAutoCancel(true);
//                    Notification notification1 = builder.build();
//                    return notification1;
//                default:
//                    //默认为0，若填写的builder_id并不存在，也使用默认。
//                    return super.getNotification(context, msg);
//            }
            return super.getNotification(context, msg);
        }
    };

    private UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){

        @Override
        public void openActivity(Context context, UMessage uMessage) {
            super.openActivity(context, uMessage);
        }

        @Override
        public void dealWithCustomAction(Context context, UMessage msg){
            String snapMsgId=msg.extra.get("snapMsgId");
            String snapStatus=msg.extra.get("snapStatus");
            Log.d(TAG,"click,snapMsgId="+snapMsgId+",snapStatus="+snapStatus);
            if (!StringUtil.isEmpty(snapMsgId,true)&&!StringUtil.isEmpty(snapStatus,true)){
                if (snapStatus.equalsIgnoreCase("errorGoschool")
                        ||snapStatus.equalsIgnoreCase("errorLeaveschool")
                ){
                    Intent intent=MessageErrorDetailActivity.createIntent(context, snapMsgId,snapStatus);
                    append(intent,msg);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DemoApplication.getInstance().startActivity(intent);
                }else {
                    Intent intent=MessageDetailActivity.createIntent(context, snapMsgId,snapStatus);
                    append(intent,msg);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DemoApplication.getInstance().startActivity(intent);
                }
            }else {
                Log.e(TAG,"该消息[ "+msg.msg_id +" ]snapMsgId为空");
            }
        }
    };

    private Intent append(Intent var1, UMessage var2) {
        if (var1 != null && var2 != null && var2.extra != null) {
            Iterator var3 = var2.extra.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry var4 = (Map.Entry)var3.next();
                String var5 = (String)var4.getKey();
                String var6 = (String)var4.getValue();
                if (var5 != null) {
                    var1.putExtra(var5, var6);
                }
            }

            return var1;
        } else {
            return var1;
        }
    }
}
