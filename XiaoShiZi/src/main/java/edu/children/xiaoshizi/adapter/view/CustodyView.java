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
import edu.children.xiaoshizi.bean.Custody;
import zuo.biao.library.base.BaseView;

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
public class CustodyView extends BaseView<Custody> implements OnClickListener {
	private static final String TAG = "UserView";

	public CustodyView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_custody_view, parent);
	}

	public ImageView iv_custody_face;
	public TextView txt_custody_name;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_custody_face = findView(R.id.iv_custody_face, this);
		txt_custody_name = findView(R.id.txt_custody_name, this);
		return super.createView();
	}

	@Override
	public void bindView(Custody data_){
		super.bindView(data_ != null ? data_ : new Custody());

//		Glide.with(context).asBitmap().load(data.getHead()).into(new SimpleTarget<Bitmap>() {
//
//			@Override
//			public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//				ivUserViewHead.setImageBitmap(CommonUtil.toRoundCorner(bitmap, bitmap.getWidth()/2));
//			}
//		});
//
//		ivUserViewStar.setImageResource(data.getStarred() ? R.drawable.icon_search : R.drawable.icon_search);
//
//		tvUserViewSex.setBackgroundResource(data.getSex() == User.SEX_FEMALE
//				? R.drawable.circle_pink : R.drawable.circle_blue);
//		tvUserViewSex.setText(data.getSex() == User.SEX_FEMALE ?  "女" : "男");
//		tvUserViewSex.setTextColor(getColor(data.getSex() == User.SEX_FEMALE ? R.color.pink : R.color.blue));
//
//		tvUserViewName.setText(StringUtil.getTrimedString(data.getName()));
//		tvUserViewId.setText("ID:" + data.getId());
//		tvUserViewNumber.setText("Phone:" + StringUtil.getNoBlankString(data.getPhone()));
	}

	@Override
	public void onClick(View v) {
//		if (BaseModel.isCorrect(data) == false) {
//			return;
//		}
//		switch (v.getId()) {
//		case R.id.ivUserViewHead:
//			toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
//			break;
//		default:
//			switch (v.getId()) {
//			case R.id.ivUserViewStar:
//				data.setStarred(! data.getStarred());
//				break;
//			case R.id.tvUserViewSex:
//				data.setSex(data.getSex() == User.SEX_FEMALE ? User.SEX_MAIL : User.SEX_FEMALE);
//				break;
//			}
//			bindView(data);
//			break;
//		}
	}
}