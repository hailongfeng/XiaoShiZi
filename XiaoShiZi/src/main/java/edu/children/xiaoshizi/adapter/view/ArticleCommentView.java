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
import android.widget.ImageView;
import android.widget.TextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.ArticleComment;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import zuo.biao.library.base.BaseView;


public class ArticleCommentView extends XszBaseView<ArticleComment> implements OnClickListener {
	private static final String TAG = "UserView";

	public ArticleCommentView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_search_history_view, parent);
	}

	public TextView txt_name;
	public ImageView iv_delete;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		txt_name = findView(R.id.txt_name, this);
		iv_delete = findView(R.id.iv_delete, this);
		return super.createView();
	}

	@Override
	public void bindView(ArticleComment data_){
		super.bindView(data_ != null ? data_ : new ArticleComment());
		print(this.data.getContentId());
		txt_name.setText(this.data.getContentId());
		iv_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_delete:
			this.onViewClickListener.onViewClick(this,iv_delete);
			break;
		default:
			break;
		}
	}
}