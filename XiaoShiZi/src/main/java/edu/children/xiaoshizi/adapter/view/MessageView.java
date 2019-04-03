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
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.bean.Parent;
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
public class MessageView extends BaseView<Message> {
	private static final String TAG = "UserView";

	public MessageView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_message_view, parent);
	}

	public RoundedImageView iv_custody_face;
	public TextView txt_custody_name;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_custody_face = findView(R.id.iv_parent_face);
		txt_custody_name = findView(R.id.txt_parent_name);
		return super.createView();
	}

	@Override
	public void bindView(Message data_){
		super.bindView(data_ != null ? data_ : new Message());
//		String headPortrait=data.getHeadPortrait();
//		Glide.with(context).load(headPortrait ).into(iv_custody_face);
//		txt_custody_name.setText(data.getParentUserName());
	}
}