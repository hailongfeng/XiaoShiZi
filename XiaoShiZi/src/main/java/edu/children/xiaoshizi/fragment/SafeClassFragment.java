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

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.walle.multistatuslayout.MultiStatusLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.GradlePagerAdapter;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;

/**
 * 安全课堂，年级列表
 */
public class SafeClassFragment extends XszBaseFragment implements OnClickListener {

	private List<ArticleType> articleTypes =new ArrayList<>();
	private GradlePagerAdapter mExamplePagerAdapter ;

	@BindView(R.id.multiStatusLayout_aqkt)
	MultiStatusLayout multiStatusLayout_aqkt;
	@BindView(R.id.magic_indicator)
	MagicIndicator magicIndicator;
	@BindView(R.id.view_pager)
	ViewPager mViewPager;
	LoadContentCategoryResponse response;
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
		mExamplePagerAdapter= new GradlePagerAdapter(context,articleTypes);
		mViewPager.setAdapter(mExamplePagerAdapter);
	}

	@Override
	public void initData() {
		List<ArticleType>  articleTypes2=DbUtils.getArticleTypeList(2);
		initData1(articleTypes2);
		loadSeClassRoomContentCategory();
	}


	private void initData1(List<ArticleType> articleTypes){
		if (articleTypes!=null){
			this.articleTypes.clear();
			this.articleTypes.addAll(articleTypes);
			mExamplePagerAdapter.notifyDataSetChanged();
			initMagicIndicator1();
			multiStatusLayout_aqkt.showContent();
		}else {
			multiStatusLayout_aqkt.showLoading();
		}
	}

	private void loadSeClassRoomContentCategory() {
		TreeMap sm = new TreeMap<String,String>();
		LogicService.post(context,APIMethod.loadSeClassRoomContentCategory,sm,new ApiSubscriber<Response<LoadContentCategoryResponse>>() {

			@Override
			protected void onSuccess(Response<LoadContentCategoryResponse> response) {
				List<ArticleType>  articleTypes2=response.getResult().getCategoryResps();
				for (ArticleType type:articleTypes2){
					type.setBelongTo(2);
				}
				DbUtils.deleteArticleType(2);
//				DbUtils.deleteModel(ArticleType.class,ArticleType_Table.belongTo.eq(2));
				DbUtils.saveModelList(articleTypes2);
				initData1(articleTypes2);
			}

			@Override
			protected void onFail(Throwable  error) {
				showShortToast(error.getMessage());
			}
		});
	}



	@Override
	public void initEvent() {

	}
	private void initMagicIndicator1() {
		CircleNavigator circleNavigator = new CircleNavigator(context);
		circleNavigator.setCircleCount(articleTypes.size());
		circleNavigator.setCircleColor(getResources().getColor(R.color.colorPrimary));
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