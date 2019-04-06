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

import com.blankj.utilcode.util.Utils;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.List;

import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.UmengMessageHandle;
import edu.children.xiaoshizi.utils.ActivityLifecycle;
import zuo.biao.library.base.BaseApplication;

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
	private List<Banner> banners;
	private LoadContentCategoryResponse contentCategoryResponse;
	private LoadContentCategoryResponse contentSeClassCategoryResponse;
	private LoadContentCategoryResponse contentSeLabCategoryResponse;


	private String deviceToken=null;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		FlowManager.init(new FlowConfig.Builder(this).build());
		Utils.init(context);
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

	public List<Banner> getBanners() {
		return banners;
	}

	public void setBanners(List<Banner> banners) {
		this.banners = banners;
	}

	public LoadContentCategoryResponse getContentCategoryResponse() {
		return contentCategoryResponse;
	}

	public void setContentCategoryResponse(LoadContentCategoryResponse contentCategoryResponse) {
		this.contentCategoryResponse = contentCategoryResponse;
	}

	public LoadContentCategoryResponse getContentSeClassCategoryResponse() {
		return contentSeClassCategoryResponse;
	}

	public void setContentSeClassCategoryResponse(LoadContentCategoryResponse contentSeClassCategoryResponse) {
		this.contentSeClassCategoryResponse = contentSeClassCategoryResponse;
	}

	public LoadContentCategoryResponse getContentSeLabCategoryResponse() {
		return contentSeLabCategoryResponse;
	}

	public void setContentSeLabCategoryResponse(LoadContentCategoryResponse contentSeLabCategoryResponse) {
		this.contentSeLabCategoryResponse = contentSeLabCategoryResponse;
	}
}
