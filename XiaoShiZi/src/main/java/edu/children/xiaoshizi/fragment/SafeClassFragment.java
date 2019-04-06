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
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ArticleDetailActivity;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.adapter.ExamplePagerAdapter;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.Log;

/**
 * 首页文章界面
 */
public class SafeClassFragment extends XszBaseFragment implements OnClickListener {

	private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
	private List<String> mDataList = Arrays.asList(CHANNELS);
	private ExamplePagerAdapter mExamplePagerAdapter = new ExamplePagerAdapter(mDataList);

	@BindView(R.id.magic_indicator)
	MagicIndicator magicIndicator;
	@BindView(R.id.view_pager)
	ViewPager mViewPager;

	public static SafeClassFragment newInstance(String param1, String param2) {
		SafeClassFragment fragment = new SafeClassFragment();
		return fragment;
	}

	@Override
	int getLayoutId() {
		return R.layout.fragment_safe_class;
	}


	@Override
	public void onClick(View v) {

	}

	@Override
	public void initView() {
		mViewPager.setAdapter(mExamplePagerAdapter);
		initMagicIndicator1();
	}

	@Override
	public void initData() {

	}

	@Override
	public void initEvent() {

	}
	private void initMagicIndicator1() {
		CircleNavigator circleNavigator = new CircleNavigator(context);
		circleNavigator.setCircleCount(CHANNELS.length);
		circleNavigator.setCircleColor(Color.RED);
		circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
			@Override
			public void onClick(int index) {
				mViewPager.setCurrentItem(index);
			}
		});
		magicIndicator.setNavigator(circleNavigator);
		ViewPagerHelper.bind(magicIndicator, mViewPager);
	}


}