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
import edu.children.xiaoshizi.bean.QianDao;
import edu.children.xiaoshizi.bean.User;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.StringUtil;

/**用户View
 * @author Lemon
 * @use
 * <br> UserView userView = new UserView(context, resources);
 * <br> adapter中使用:[具体参考.BaseViewAdapter(getView使用自定义View的写法)]
 * <br> convertView = userView.createView(inflater);
 * <br> userView.bindView(position, data);
 * <br> 或  其它类中使用:
 * <br> containerView.addView(userView.createView(inflater));
 * <br> userView.bindView(data);
 * <br> 然后
 * <br> userView.setOnDataChangedListener(onDataChangedListener);data = userView.getData();//非必需
 * <br> userView.setOnClickListener(onClickListener);//非必需
 */
public class QianDaoView extends BaseView<QianDao> implements OnClickListener {
	private static final String TAG = "UserView";

	public QianDaoView(Activity context, ViewGroup parent) {
		super(context, R.layout.layout_qiandao, parent);
	}

	public TextView txt_day;
	public TextView txt_jifen;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		txt_day = findView(R.id.txt_day);
		txt_jifen = findView(R.id.txt_jifen);
		return super.createView();
	}

	@Override
	public void bindView(QianDao data_){
		super.bindView(data_ != null ? data_ : new QianDao());
		txt_day.setText(this.data.day+"天");
		txt_jifen.setText("+"+this.data.jiFen);

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