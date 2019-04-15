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

package edu.children.xiaoshizi.adapter.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
import edu.children.xiaoshizi.bean.Article;
import zuo.biao.library.util.Log;

public class MyCacheArticleView extends XszBaseView<Article> implements OnClickListener {
	private static final String TAG = "MyCacheArticleView";



	public CheckBox cb_select;
	public ImageView iv_article_pic;
	public TextView txt_article_title;

	public MyCacheArticleView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_my_cache_article_view, parent);
	}

	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		cb_select = findView(R.id.cb_select);
		iv_article_pic = findView(R.id.iv_article_pic);
		txt_article_title = findView(R.id.txt_article_title);
		return super.createView();
	}

	@Override
	public void bindView(Article data_){
		super.bindView(data_ != null ? data_ : new Article());
		Log.d(TAG,this.data.toString());
		loadImage(this.data.getActivityVideoImageUrl(),iv_article_pic);
		txt_article_title.setText(this.data.getTitle());
		if (this.data.isShow()){
			cb_select.setVisibility(View.VISIBLE);
			cb_select.setChecked(this.data.isSelected());
		}else{
			cb_select.setVisibility(View.GONE);
		}
		cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				data.setSelected(isChecked);
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivUserViewHead:
//			toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
			break;
		default:
			break;
		}
	}
}