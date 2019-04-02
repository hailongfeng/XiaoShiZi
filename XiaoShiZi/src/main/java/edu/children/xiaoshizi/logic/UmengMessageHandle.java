package edu.children.xiaoshizi.logic;

import android.app.Notification;
import android.content.Context;
import android.widget.RemoteViews;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import zuo.biao.library.util.Log;

public class UmengMessageHandle {
    private static final String TAG = "UmengMessageHandle";
    private static final String appKey="5ca1c9ee20365714de0016c7";
    private static final String messageKey="9f02750370dfce2541fcfbe2f9e0803d";
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
    }

    private UmengMessageHandler messageHandler = new UmengMessageHandler() {
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

            switch (msg.builder_id) {
                case 1:
                    Notification.Builder builder = new Notification.Builder(context);
                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                            R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon,
                            getSmallIconId(context, msg));
                    builder.setContent(myNotificationView)
                            .setSmallIcon(getSmallIconId(context, msg))
                            .setTicker(msg.ticker)
                            .setAutoCancel(true);

                    return builder.getNotification();
                default:
                    //默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, msg);
            }
        }
    };

    private UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){

        @Override
        public void dealWithCustomAction(Context context, UMessage msg){
            Log.e(TAG,"click");

        }

    };
}
