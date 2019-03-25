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

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import edu.children.xiaoshizi.bean.User;
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

	//5gcnrqBYVI1Qngok8uH 18697386272
	private User user;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		FlowManager.init(new FlowConfig.Builder(this).build());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
