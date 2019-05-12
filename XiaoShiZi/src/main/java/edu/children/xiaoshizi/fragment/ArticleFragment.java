/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this imageFile except in compliance with the License.
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
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.walle.multistatuslayout.MultiStatusLayout;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ArticleDetailActivity;
import edu.children.xiaoshizi.activity.XszWebViewActivity;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.Banner;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.GlideImageLoader;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * 首页文章界面
 */
public class ArticleFragment extends XszBaseFragment implements View.OnClickListener,OnDialogButtonClickListener {


    @BindView(R.id.rvBaseRecycler)
    RecyclerView rvBaseRecycler;
    private ArticleAdapter articleAdapter;
	private ArticleType articleType;
    private List<Article> articles=new ArrayList<>();
	private ArticleType firstArticleType;
	@BindView(R.id.multiStatusLayout)
	MultiStatusLayout multiStatusLayout;
    @BindView(R.id.banner)
    com.youth.banner.Banner banner;
    private List<Banner> banners;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
	}

	@Override
	int getLayoutId() {
		return R.layout.fragment_article_layout;
	}


	@Override
	public void initView() {
		articleType=(ArticleType) getArguments().getSerializable("articleType");
		Log.d(TAG,"articleType"+articleType.getTitle()+","+articleType.getCategoryId());
//        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(context));
		rvBaseRecycler.setLayoutManager(new LinearLayoutManager(context){
			@Override
			public boolean canScrollVertically() {
				//解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
				//如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
				return false;
			}
		});
		//解决数据加载不完的问题
		rvBaseRecycler.setNestedScrollingEnabled(false);
		rvBaseRecycler.setHasFixedSize(true);
		//解决数据加载完成后, 没有停留在顶部的问题
		rvBaseRecycler.setFocusable(false);

        DividerItemDecoration divider = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context,R.drawable.list_view_divider));

        rvBaseRecycler.addItemDecoration(divider);
        articleAdapter = new ArticleAdapter(context,ArticleAdapter.Type_Shoye_Article);
		rvBaseRecycler.setAdapter(articleAdapter);
		articleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//点击查看某个文章

				if (!NetworkUtils.isConnected()){
					showShortToast(R.string.net_error);
					return;
				}
                Intent intent=new Intent(context,ArticleDetailActivity.class);
                Article article= articles.get(position);
                intent.putExtra("article",article);
                intent.putExtra("articleType",articleType);
				String title=articleType.getTitle()+"|"+article.getTitle();
                intent.putExtra(INTENT_TITLE,title);
                toActivity(intent);
			}
		});

		firstArticleType=DbUtils.getFirstArticleType(1);
		if (firstArticleType!=null) {
			print("首页文章：当前菜单为：" + this.articleType.getTitle() + "," + this.articleType.getCategoryId() + "；；；首页第一个菜单为：" + firstArticleType.getTitle() + "," + firstArticleType.getCategoryId());
			if (firstArticleType.getCategoryId()==this.articleType.getCategoryId()){
				String type1Articles=CacheUtils.get(context).getAsString("type1Articles");
				if (StringUtil.isNotEmpty(type1Articles,true)){
					List<Article> articles1=JSONArray.parseArray(type1Articles,Article.class);
					print("走缓存");
					updateList(articles1);
				}
			}
		}

		getArticleContentById(articleType.getCategoryId());

	}

	private void updateList(List<Article> articles1){
		if (articles1!=null){
			articles.clear();
			this.articles.addAll(articles1);
			articleAdapter.refresh(this.articles);
			multiStatusLayout.showContent();
		}else {
			multiStatusLayout.showEmpty();
		}

	}

	void getArticleContentById(int categoryId){
		TreeMap sm = new TreeMap<String,String>();
		sm.put("categoryId",categoryId);
		LogicService.post(context, APIMethod.loadContentByCategory,sm, new ApiSubscriber<Response<List<Article>>>() {
			@Override
			public void onSuccess(Response<List<Article>> response) {
				List<Article> articles1=response.getResult();
				updateList(articles1);
				if (firstArticleType.getCategoryId()==ArticleFragment.this.articleType.getCategoryId()){
					print("当前为首页文章，更新本地缓存。。。"+firstArticleType.getTitle());
					String type1Articles=JSONArray.toJSONString(articles);
					CacheUtils.get(context).remove("type1Articles");
					CacheUtils.get(context).put("type1Articles",type1Articles);
				}
			}

			@Override
			protected void onFail(Throwable  error) {
				error.printStackTrace();
			}
		});
	}



	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onReceiveEventBusMessage(EventBusMessage<Article> messageEvent) {
		Log.d(TAG,"EventBusMessage type= "+messageEvent.getType());
		if (messageEvent.getType()==EventBusMessage.Type_article_comment){
			Article newArticle=messageEvent.getData();
			boolean isExit=false;
			for (Article article:this.articles){
				if (article.getContentId().equalsIgnoreCase(newArticle.getContentId())){
					article.setLikedNumber(newArticle.getLikedNumber());
					article.setShareNumber(newArticle.getShareNumber());
					article.setIntroduce(newArticle.getIntroduce());
					article.setShareUrl(newArticle.getShareUrl());
					isExit=true;
					break;
				}
			}
			if (isExit){
				articleAdapter.refresh(this.articles);
			}
		}

	}

	@Override
	public void initData() {//必须调用
        banners=DbUtils.getModelList(Banner.class);
        if (banners!=null&&banners.size()>0){
            initBanber(banners);
        }
        if (DemoApplication.getInstance().getBanners()==null) {
            loadSysBannerList();
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

    private void initBanber(List<Banner> banners){
        banner.setImageLoader(new GlideImageLoader());
        List<String> images=new ArrayList<String>();
        for (Banner b:banners){
            images.add(b.getBannerImage());
        }
        banner.setImages(images);
//		banner.setBannerAnimation(Transformer.Tablet);
        banner.start();
    }


    @Override
	public void initEvent() {//必须调用
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position<banners.size()){
                    Banner banner=banners.get(position);
                    loadBannerContentById(banner.getId());
                }
            }
        });
	}
    void loadBannerContentById(String contentId){
        TreeMap<String, String> param = new TreeMap<String, String>();
        param.put("contentId",contentId);
        LogicService.post(context, APIMethod.loadBannerContentById,param,new ApiSubscriber<Response<Banner>>() {
            @Override
            public void onSuccess(Response<Banner> respon) {
                Banner banner=respon.getResult();
                toActivity(XszWebViewActivity.createIntent(context,banner.getTitle(),banner.getIntroduce()));
            }

            @Override
            protected void onFail(Throwable  error) {
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }
	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (! isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			break;
		default:
			break;
		}
	}



	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
//			case R.id.llSettingSetting:
////				toActivity(SettingActivity.createIntent(context));
//				break;
			default:
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
	}

}