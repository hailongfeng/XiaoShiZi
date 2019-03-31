package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseActivity;

/**
 * 第一联系人
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {

    }

    private void setEMCappDeviceOwer(){
       boolean isExit= checkAppInstalled(this,"com.ipebg.emc.fii");
       if (isExit){
           runtimeExec("dpm set-device-owner com.ipebg.emc.fii/com.ipebg.receiver.EMCDeviceAdminReceiver","dpc");
       }else {
           Log.w("fhl","com.ipebg.emc.fii not install!!!");
       }
    }
    private String runtimeExec(String type, String value) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "sh",
                    "-c",
                    type
            });
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String tv_result = null;
            while ((line = reader.readLine()) != null) {
                tv_result = "  =  " + line;
            }

            process.waitFor();
            inputStream.close();
            reader.close();
            process.destroy();
            return value + tv_result + "  ";
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            Log.w("fhl","dpm set-device-owner com.ipebg.emc.fii/com.ipebg.receiver.EMCDeviceAdminReceiver set error!!!");
        }
        return null;
    }
    private boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }

}
