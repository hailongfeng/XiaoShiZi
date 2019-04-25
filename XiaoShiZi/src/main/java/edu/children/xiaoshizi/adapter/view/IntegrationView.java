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

package edu.children.xiaoshizi.adapter.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.IntegrationRecode;
import zuo.biao.library.base.BaseView;

public class IntegrationView extends BaseView<IntegrationRecode> implements OnClickListener {
	private static final String TAG = "UserView";

	public IntegrationView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_integration_view, parent);
	}

	public TextView txt_remark;
	public TextView txt_time;
	public TextView txt_jifen;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		txt_remark = findView(R.id.txt_remark);
		txt_time = findView(R.id.txt_time);
		txt_jifen = findView(R.id.txt_jifen);
		return super.createView();
	}

	@Override
	public void bindView(IntegrationRecode data_){
		super.bindView(data_ != null ? data_ : new IntegrationRecode());
		txt_remark.setText(this.data.getTranMsg());
		txt_time.setText(this.data.getTranTime());
		txt_jifen.setText("+"+this.data.getTranPoints());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.ivUserViewHead:
//			toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
//			break;
//		default:
//			switch (v.getId()) {
//			case R.id.ivUserViewStar:
//				data.setStarred(! data.getStarred());
//				break;
//			case R.id.tvUserViewSex:
////				data.setSex(data.getSex() == User.SEX_FEMALE ? User.SEX_MAIL : User.SEX_FEMALE);
//				break;
//			}
////			bindView(data);
//			break;
		}
	}
}