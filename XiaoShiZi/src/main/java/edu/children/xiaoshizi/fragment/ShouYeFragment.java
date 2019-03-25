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

package edu.children.xiaoshizi.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**设置fragment
 * @author Lemon
 * @use new WoDeFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class ShouYeFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener {
//	private static final String TAG = "WoDeFragment";

	//与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**创建一个Fragment实例
	 * @return
	 */
	public static ShouYeFragment createInstance() {
		return new ShouYeFragment();
	}

	//与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//类相关初始化，必须使用<<<<<<<<<<<<<<<<
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.shouye_fragment);
		//类相关初始化，必须使用>>>>>>>>>>>>>>>>

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

		return view;
	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

//	private ImageView ivSettingHead;
	@Override
	public void initView() {//必须调用

//		ivSettingHead = findView(R.id.ivSettingHead);
		FragmentManager fragmentManager=context.getSupportFragmentManager();
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
				fragmentManager, FragmentPagerItems.with(context)
				.add("黄金屋", HuangjinwuFragment.class)
				.add("放大镜", FangDaJingFragment.class)
				.create());

		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(adapter);

		SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
		viewPagerTab.setViewPager(viewPager);
	}
	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用

	}


	private void logout() {
		context.finish();
	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

//		ivSettingHead.setOnClickListener(this);

//		findView(R.id.llSettingSetting).setOnClickListener(this);
//		findView(R.id.llSettingAbout).setOnClickListener(this);
//		findView(R.id.llSettingLogout).setOnClickListener(this);
	}




	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (! isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			logout();
			break;
		default:
			break;
		}
	}



	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.llSettingSetting:
//				toActivity(SettingActivity.createIntent(context));
				break;
			case R.id.llSettingAbout:
//				toActivity(AboutActivity.createIntent(context));
				break;
			case R.id.llSettingLogout:
				new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
				break;
			default:
				break;
		}
	}




	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}