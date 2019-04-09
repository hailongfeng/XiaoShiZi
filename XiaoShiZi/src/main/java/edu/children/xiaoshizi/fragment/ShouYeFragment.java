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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.SearchArticleActivity;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.utils.GlideImageLoader;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**设置fragment
 * @author Lemon
 * @use new WoDeFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class ShouYeFragment extends XszBaseFragment implements OnClickListener, OnDialogButtonClickListener {
	@BindView(R.id.banner)
	com.youth.banner.Banner banner;
	@BindView(R.id.viewpagertab)
	SmartTabLayout viewPagerTab;
	public static ShouYeFragment createInstance() {
		return new ShouYeFragment();
	}

	@Override
	int getLayoutId() {
		return R.layout.shouye_fragment;
	}


	@Override
	public void initView() {//必须调用
		List<ArticleType> articleTypes=DemoApplication.getInstance().getContentCategoryResponse().getCategoryResps();

		FragmentPagerItems.Creator creator=FragmentPagerItems.with(context);
		for (ArticleType articleType:articleTypes){
			Bundle bundle=new Bundle();
			bundle.putSerializable("articleType",articleType);
			creator.add(articleType.getTitle(), ArticleFragment.class,bundle);
		}
		FragmentManager fragmentManager=context.getSupportFragmentManager();
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
				fragmentManager, creator.create());
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);

		List<Banner> banners=DemoApplication.getInstance().getBanners();
		banner.setImageLoader(new GlideImageLoader());
		List<String> images=new ArrayList<String>();
		for (Banner b:banners){
			images.add(b.getBannerImage());
		}
		banner.setImages(images);
		banner.start();

	}

	@Override
	public void initData() {//必须调用

	}


	private void logout() {
		context.finish();
	}


	@Override
	public void initEvent() {//必须调用

//		ivSettingHead.setOnClickListener(this);
		findView(R.id.rll_search,this);
		findView(R.id.rtv_contribute,this);
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
			case R.id.rll_search:
				toActivity(new Intent(context,SearchArticleActivity.class));
				break;
//			case R.id.llSettingAbout:
////				toActivity(AboutActivity.createIntent(context));
//				break;
//			case R.id.llSettingLogout:
//				new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
//				break;
			default:
				break;
		}
	}

}