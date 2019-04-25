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
import com.flyco.roundview.RoundTextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.InOutSchoolRecodeDetailActivity;
import edu.children.xiaoshizi.activity.MessageDetailActivity;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.StringUtil;


public class InAndOutSchoolRecodeView extends XszBaseView<InAndOutSchoolRecode> implements OnClickListener {
	public InAndOutSchoolRecodeView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_in_out_school_recode_view, parent);
	}

	public ImageView txt_student_in_out_school_recode_icon;
	public TextView txt_student_in_out_school_recode_time;
	public TextView txt_student_in_out_school_recode_miaoshu;
	public RoundTextView rtv_view_detail;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		txt_student_in_out_school_recode_time = findView(R.id.txt_student_in_out_school_recode_time);
		txt_student_in_out_school_recode_icon = findView(R.id.txt_student_in_out_school_recode_icon);
		txt_student_in_out_school_recode_miaoshu = findView(R.id.txt_student_in_out_school_recode_miaoshu);
		rtv_view_detail = findView(R.id.rtv_view_detail, this);
		return super.createView();
	}

	@Override
	public void bindView(InAndOutSchoolRecode data_){
		super.bindView(data_ != null ? data_ : new InAndOutSchoolRecode());

		if (this.data.snapStatus.equalsIgnoreCase("normal")){
			txt_student_in_out_school_recode_icon.setImageResource(R.drawable.icon_inout_school_right_blue);
		}else {
			txt_student_in_out_school_recode_icon.setImageResource(R.drawable.icon_inout_school_error);
		}

		txt_student_in_out_school_recode_time.setText(data.triggerTime);
		txt_student_in_out_school_recode_miaoshu.setText(data.snapRemark);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rtv_view_detail:
//			toActivity(InOutSchoolRecodeDetailActivity.createIntent(context, data));
			toActivity(MessageDetailActivity.createIntent(context, data.getId()));
			break;
		default:
			break;
		}
	}
}