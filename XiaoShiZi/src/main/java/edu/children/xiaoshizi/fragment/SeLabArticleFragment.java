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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ArticleDetailActivity;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
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
 * 安全实验室文章列表界面
 */
public class SeLabArticleFragment extends XszBaseFragment implements OnClickListener, OnDialogButtonClickListener {


    @BindView(R.id.rvBaseRecycler)
    RecyclerView rvBaseRecycler;
    private ArticleAdapter articleAdapter;
	private ArticleType articleType;
    private List<Article> articles;
	@Override
	int getLayoutId() {
		return R.layout.huangjinwu_fragment;
	}

	@Override
	public void initView() {
		articleType=(ArticleType) getArguments().getSerializable("articleType");
		Log.d(TAG,"articleType==null ,,,"+(articleType.getTitle()));
        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context,R.drawable.list_view_divider));
        rvBaseRecycler.addItemDecoration(divider);
        articleAdapter = new ArticleAdapter(context);
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

		ArticleType firstArticleType=DemoApplication.getInstance().getContentSeLabCategoryResponse().getCategoryResps().get(0);
		if (articleType.equals(firstArticleType)){
            articles =DemoApplication.getInstance().getContentCategoryResponse().getContentResps();
			articleAdapter.refresh(articles);
			Log.d(TAG,"111articles size = "+articles.size());
		}else {
			getArticleContentById(articleType.getCategoryId()+"");
		}

	}

	void getArticleContentById(String categoryId){
		TreeMap sm = new TreeMap<String,String>();
		sm.put("categoryId",categoryId);
		LogicService.post(context, APIMethod.loadSafeLabContentByCategory,sm, new ApiSubscriber<Response<List<Article>>>() {
			@Override
			public void onSuccess(Response<List<Article>> response) {
                articles=response.getResult();
                Log.d(TAG,"222articles size = "+articles.size());
				articleAdapter.refresh(articles);
			}

			@Override
			protected void onFail(NetErrorException error) {
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