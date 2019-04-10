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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyco.roundview.RoundTextView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**
 * 安全课堂首页
 */
public class SafeClassRoomFragment extends XszBaseFragment implements OnClickListener {

	private static final String[] CHANNELS = new String[]{"安全课堂", "安全实验室"};
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
	@BindView(R.id.magic_indicator)
	MagicIndicator magicIndicator;

	@Override
	int getLayoutId() {
		return R.layout.fragment_save_class_room;
	}

	@Override
	public void initView() {
		initFragments();
		initMagicIndicator1();
		mFragmentContainerHelper.handlePageSelected(0, false);
		switchPages(0);
	}

	@Override
	public void initData() {

	}

	@Override
	public void initEvent() {

	}


	private void switchPages(int index) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragment;
		for (int i = 0, j = mFragments.size(); i < j; i++) {
			if (i == index) {
				continue;
			}
			fragment = mFragments.get(i);
			if (fragment.isAdded()) {
				fragmentTransaction.hide(fragment);
			}
		}
		fragment = mFragments.get(index);
		if (fragment.isAdded()) {
			fragmentTransaction.show(fragment);
		} else {
			fragmentTransaction.add(R.id.fragment_container, fragment);
		}
		fragmentTransaction.commitAllowingStateLoss();
	}

	private void initFragments() {
		mFragments.add(SafeClassFragment.newInstance("",""));
		mFragments.add(SafeLabFragment.newInstance("",""));
//		mFragments.add(DemoFragment.newInstance("安全课堂","安全课堂"));
//		mFragments.add(DemoFragment.newInstance("安全实验室","安全实验室"));
//		mFragments.add(new ShouYeFragment());
	}

	private void initMagicIndicator1() {
		magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg);
		CommonNavigator commonNavigator = new CommonNavigator(context);
		commonNavigator.setAdapter(new CommonNavigatorAdapter() {
			@Override
			public int getCount() {
				return CHANNELS.length;
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
				clipPagerTitleView.setText(CHANNELS[index]);
				clipPagerTitleView.setTextColor(Color.parseColor("#FFFFFF"));
				clipPagerTitleView.setClipColor(getResources().getColor(R.color.colorPrimary));
				clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mFragmentContainerHelper.handlePageSelected(index);
						switchPages(index);
					}
				});
				return clipPagerTitleView;
			}

			@Override
			public IPagerIndicator getIndicator(Context context) {
				LinePagerIndicator indicator = new LinePagerIndicator(context);
				float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height1);
				float borderWidth = UIUtil.dip2px(context, 1);
				float lineHeight = navigatorHeight - 2 * borderWidth;
				indicator.setLineHeight(lineHeight);
				indicator.setRoundRadius(lineHeight / 2);
				indicator.setYOffset(borderWidth);
				indicator.setColors(Color.parseColor("#FFFFFF"));
				return indicator;
			}
		});
		magicIndicator.setNavigator(commonNavigator);
		mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
	}

	@Override
	public void onClick(View v) {

	}


}