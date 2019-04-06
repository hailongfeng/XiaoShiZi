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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyco.roundview.RoundTextView;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 安全课堂首页
 */
public class SafeClassRoomFragment extends XszBaseFragment implements OnClickListener {

	@BindView(R.id.fl_safe_content)
	FrameLayout fl_safe_content;;
	@BindView(R.id.txt_seclass)
	RoundTextView txt_seclass;;
	@BindView(R.id.txt_selab)
	RoundTextView txt_selab;;

	private int currentTab=1;

	public static SafeClassRoomFragment createInstance() {
		return new SafeClassRoomFragment();
	}

	@Override
	int getLayoutId() {
		return R.layout.fragment_save_class_room;
	}


	@Override
	public void initView() {//必须调用
		setIndexSelected(0);
	}

	@Override
	public void initData() {//必须调用

	}


	private void logout() {
		context.finish();
	}

	@Override
	public void initEvent() {//必须调用
		txt_seclass.setOnClickListener(this);
		txt_selab.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.txt_seclass:
				changeTab(0);
				setIndexSelected(0);
				break;
			case R.id.txt_selab:
				changeTab(1);
				setIndexSelected(1);
				break;
			default:
				break;
		}
	}
	//添加到数组
	Fragment mFragments[] = new Fragment[]{new ArticleFragment(),new ArticleFragment()};
	void changeTab(int position){
		if (currentTab==position){
			return;
		}
		if (currentTab==0){
			txt_seclass.setTextColor(getResources().getColor(R.color.colorPrimary));
			txt_seclass.getDelegate().setBackgroundColor(getResources().getColor(R.color.white));
			txt_seclass.getDelegate().setCornerRadius(20);

			txt_selab.setTextColor(getResources().getColor(R.color.white));
			txt_selab.getDelegate().setBackgroundColor(getResources().getColor(R.color.transparent));
			txt_selab.getDelegate().setCornerRadius(0);
		}else {
			txt_selab.setTextColor(getResources().getColor(R.color.colorPrimary));
			txt_selab.getDelegate().setBackgroundColor(getResources().getColor(R.color.white));
			txt_selab.getDelegate().setCornerRadius(20);

			txt_seclass.setTextColor(getResources().getColor(R.color.white));
			txt_seclass.getDelegate().setBackgroundColor(getResources().getColor(R.color.transparent));
			txt_seclass.getDelegate().setCornerRadius(0);
		}
	}

	private void setIndexSelected(int index) {
		if(currentTab==index){
			return;
		}
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		//隐藏
		ft.hide(mFragments[currentTab]);
		//判断是否添加
		if(!mFragments[index].isAdded()){
			ft.add(R.id.content,mFragments[index]).show(mFragments[index]);
		}else {
			ft.show(mFragments[index]);
		}
		ft.commit();
		currentTab=index;
	}
}