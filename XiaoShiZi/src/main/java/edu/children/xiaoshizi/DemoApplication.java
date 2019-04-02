/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package edu.children.xiaoshizi;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.RemoteViews;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.UmengMessageHandle;
import edu.children.xiaoshizi.utils.ActivityLifecycle;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.util.Log;

/**Application
 * @author harlen
 */
public class DemoApplication extends BaseApplication {
	private static final String TAG = "DemoApplication";

	private static DemoApplication context;
	public static DemoApplication getInstance() {
		return context;
	}
	private static final String pushSecret="";
	private static final int deviceType=1;
	private User user;
	private LoginRespon loginRespon;
	private List<Activity> activities=new ArrayList<>();


	private String deviceToken=null;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		FlowManager.init(new FlowConfig.Builder(this).build());
		registerActivityLifecycleCallbacks(new ActivityLifecycle());
		new UmengMessageHandle(this).init();
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this) ;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoginRespon getLoginRespon() {
		return loginRespon;
	}

	public void setLoginRespon(LoginRespon loginRespon) {
		this.loginRespon = loginRespon;
	}

	public void addActivity(Activity activity){
		activities.add(activity);
	}
	public void removeActivity(Activity activity){
		activities.remove(activity);
	}
	public void exit(){
		for (Activity activity:activities){
			if (activity!=null){
				activity.finish();
			}
		}
		activities.clear();
		System.exit(0);
	}

}
