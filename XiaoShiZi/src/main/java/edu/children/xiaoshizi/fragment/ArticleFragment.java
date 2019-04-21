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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.CacheUtils;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ArticleDetailActivity;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
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
	@BindView(R.id.multiStatusLayout)
	MultiStatusLayout multiStatusLayout;
	@Override
	int getLayoutId() {
		return R.layout.fragment_article_layout;
	}


	@Override
	public void initView() {
		articleType=(ArticleType) getArguments().getSerializable("articleType");
		Log.d(TAG,"articleType"+articleType.getTitle()+","+articleType.getCategoryId());
        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context,R.drawable.list_view_divider));
        rvBaseRecycler.addItemDecoration(divider);
        articleAdapter = new ArticleAdapter(context,ArticleAdapter.Type_Shoye_Article);
		rvBaseRecycler.setAdapter(articleAdapter);
		articleAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//点击查看某个文章
                Intent intent=new Intent(context,ArticleDetailActivity.class);
                Article article= articles.get(position);
                intent.putExtra("article",article);
                intent.putExtra("articleType",articleType);
				String title=articleType.getTitle()+"|"+article.getTitle();
                intent.putExtra(INTENT_TITLE,title);
                toActivity(intent);
			}
		});

		ArticleType firstArticleType=DbUtils.getFirstArticleType(1);
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
			}

			@Override
			protected void onFail(Throwable  error) {
				error.printStackTrace();
			}
		});
	}

	@Override
	public void initData() {//必须调用

	}

	@Override
	public void initEvent() {//必须调用

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

}