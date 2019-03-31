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
import android.content.Context;
import android.support.multidex.MultiDex;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.User;
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



	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		FlowManager.init(new FlowConfig.Builder(this).build());
		registerActivityLifecycleCallbacks(new ActivityLifecycle());
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this) ;
	}

	void  initUment(){
//		UMConfigure.init(this, "5b90e5e9f29d982634000285", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "15d9d427ff1ea729a7a9dc8abaca9060");
//		UMConfigure.init(Context context, int deviceType, String pushSecret);
		UMConfigure.init(this, deviceType, pushSecret);
		PushAgent mPushAgent = PushAgent.getInstance(this);
		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				Log.i("token", "22222");
				//注册成功会返回device token
				Log.i("token", deviceToken+"");
			}
			@Override
			public void onFailure(String s, String s1) {
				Log.i("tokens", s+","+s1+"");
			}
		});
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
