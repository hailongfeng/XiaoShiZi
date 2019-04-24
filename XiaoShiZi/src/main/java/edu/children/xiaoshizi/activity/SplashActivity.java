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

package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**闪屏activity，保证点击桌面应用图标后无延时响应
 * @author Lemon
 */
public class SplashActivity extends XszBaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testRxPermission2();
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				toActivity(new Intent(context,MainActivity.class));
//				finish();
//			}
//		}, 500);
	}
	private void testRxPermission2() {
		String[] mPermissionList = new String[]{
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.INTERNET,
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.CAMERA
		};
		RxPermissions rxPermission = new RxPermissions(context);
		//请求权限全部结果
		rxPermission.request(mPermissionList)
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean granted) throws Exception {
						if (!granted) {
							showShortToast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.");
						}
						//不管是否获取全部权限，进入主页面
//						initCountDown();
						toActivity(new Intent(context,MainActivity.class));
						finish();
					}
				});
	}

	private void testRxPermission(){
		String[] mPermissionList = new String[]{
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.CALL_PHONE,
				Manifest.permission.READ_LOGS,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.SET_DEBUG_APP,
				Manifest.permission.SYSTEM_ALERT_WINDOW,
				Manifest.permission.GET_ACCOUNTS,
				Manifest.permission.WRITE_APN_SETTINGS,
				Manifest.permission.INTERNET,
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.CAMERA
		};
		RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
		rxPermissions
				.requestEach(mPermissionList)
				.subscribe(new Consumer<Permission>() {
					@Override
					public void accept(Permission permission) throws Exception {
						if (permission.granted) {
							print("testRxPermission CallBack onPermissionsGranted() : "+permission.name+
									" request granted , to do something...");
						}
						else if (permission.shouldShowRequestPermissionRationale) {
							print("testRxPermission CallBack onPermissionsDenied() : " + permission.name + "request denied");
//							showShortToast("拒绝权限，等待下次询问哦");
						}
						else {
							print("testRxPermission CallBack onPermissionsDenied() : this " + permission.name + " is denied " +
									"and never ask again");
//							showShortToast("拒绝权限，不再弹出询问框，请前往APP应用设置中打开此权限");
						}
					}
				});
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
}