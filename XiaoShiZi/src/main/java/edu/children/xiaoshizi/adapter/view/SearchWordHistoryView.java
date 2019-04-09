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
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.SearchWorldHistory;
import edu.children.xiaoshizi.bean.User;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.StringUtil;


public class SearchWordHistoryView extends BaseView<SearchWorldHistory> implements OnClickListener {
	private static final String TAG = "UserView";

	public SearchWordHistoryView(Activity context, ViewGroup parent) {
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
	public void bindView(SearchWorldHistory data_){
		super.bindView(data_ != null ? data_ : new SearchWorldHistory());
		txt_name.setText(this.data.getName());
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