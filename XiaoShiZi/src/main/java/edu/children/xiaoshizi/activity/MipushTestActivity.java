package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

import edu.children.xiaoshizi.R;
import zuo.biao.library.util.Log;

public class MipushTestActivity extends UmengNotifyClickActivity {
    private static String TAG = MipushTestActivity.class.getName();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush_test);
    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        ToastUtils.showLong("华为消息："+body);
        Log.d(TAG,body);
    }
}
