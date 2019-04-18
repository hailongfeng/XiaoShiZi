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

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.CacheUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ContributeArticleActivity;
import edu.children.xiaoshizi.activity.SearchArticleActivity;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.ArticleType_Table;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.GlideImageLoader;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.Log;

/**设置fragment
 * @author Lemon
 * @use new WoDeFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class ShouYeFragment extends XszBaseFragment implements OnClickListener, OnDialogButtonClickListener {
	@BindView(R.id.multiStatusLayout_shouye)
	MultiStatusLayout multiStatusLayout_shouye;
	@BindView(R.id.banner)
	com.youth.banner.Banner banner;
	@BindView(R.id.viewpagertab)
	SmartTabLayout viewPagerTab;
	@BindView(R.id.viewpager)
	ViewPager viewPager;
	private List<ArticleType> articleTypes=new ArrayList<>();
	private List<Banner> banners;

	public static ShouYeFragment createInstance() {
		return new ShouYeFragment();
	}

	@Override
	int getLayoutId() {
		return R.layout.shouye_fragment;
	}

	@Override
	public void initView() {//必须调用

	}

	@Override
	public void initData() {//必须调用
		List<ArticleType> articleTypes2=DbUtils.getArticleTypeList(1);
		print("数据库中的菜单为：");
		for (ArticleType type:articleTypes2) {
			print(type.getTitle()+","+type.getCategoryId());
		}
		initType(articleTypes2);
		loadContentCategory();

		banners=DbUtils.getModelList(Banner.class);
		if (banners!=null&&banners.size()>0){
			initBanber(banners);
		}
		loadSysBannerList();
	}

	private  void initType(List<ArticleType> types){
		if (types!=null){
			this.articleTypes.clear();
			this.articleTypes.addAll(types);
			initTabs(this.articleTypes);
			multiStatusLayout_shouye.showContent();
		}else {
			multiStatusLayout_shouye.showEmpty();
		}
	}


	private void loadSysBannerList() {
		TreeMap sm = new TreeMap<String,String>();
		LogicService.post(context, APIMethod.loadSysBannerList,sm, new ApiSubscriber<Response<List<Banner>>>() {
			@Override
			public void onSuccess(Response<List<Banner>> response) {
				DemoApplication.getInstance().setBanners(response.getResult());
				banners=response.getResult();
				DbUtils.deleteModel(Banner.class);
				DbUtils.saveModelList(banners);
				initBanber(banners);
			}

			@Override
			protected void onFail(Throwable  error) {
				error.printStackTrace();
			}
		});
	}
	private void loadContentCategory() {
		TreeMap sm = new TreeMap<String,String>();
		LogicService.post(context, APIMethod.loadContentCategory,sm, new ApiSubscriber<Response<LoadContentCategoryResponse>>() {
			@Override
			public void onSuccess(Response<LoadContentCategoryResponse> response) {
				DemoApplication.getInstance().setContentCategoryResponse(response.getResult());
				List<ArticleType> articleTypes2=response.getResult().getCategoryResps();
				print("网络获取的菜单为：");
				for (ArticleType type:articleTypes2){
					type.setBelongTo(1);
					print(type.getTitle()+","+type.getCategoryId());
				}
				List<Article> articles=response.getResult().getContentResps();
				DbUtils.deleteArticleType(1);
//				DbUtils.deleteModel(ArticleType.class,ArticleType_Table.belongTo.eq(1));
				DbUtils.saveModelList(articleTypes2);
                String type1Articles=JSONArray.toJSONString(articles);
                print("type1Articles="+type1Articles);
                CacheUtils.get(context).put("type1Articles",type1Articles);

                initType(articleTypes2);
			}

			@Override
			protected void onFail(Throwable  error) {
				error.printStackTrace();
			}
		});
	}

	public void initTabs(List<ArticleType> articleTypes) {//必须调用
		FragmentPagerItems.Creator creator=FragmentPagerItems.with(context);
		for (ArticleType articleType:articleTypes){
			Bundle bundle=new Bundle();
			bundle.putSerializable("articleType",articleType);
			creator.add(articleType.getTitle(), ArticleFragment.class,bundle);
		}
		FragmentManager fragmentManager=context.getSupportFragmentManager();
		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
				fragmentManager, creator.create());
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                Log.d(TAG,"当前点击的tab为："+position);
            }
        });
	}

	private void initBanber(List<Banner> banners){
		banner.setImageLoader(new GlideImageLoader());
		List<String> images=new ArrayList<String>();
		for (Banner b:banners){
			images.add(b.getBannerImage());
		}
		banner.setImages(images);
		banner.start();
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
			case R.id.rtv_contribute:
				if (!isLogin()){
					showShortToast("请先登录");
					return;
				}
				toActivity(new Intent(context, ContributeArticleActivity.class));
				break;
//			case R.id.llSettingLogout:
//				new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
//				break;
			default:
				break;
		}
	}

}