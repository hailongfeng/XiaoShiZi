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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.Log;
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
public class StudentView extends BaseView<Student> implements OnClickListener {
	private static final String TAG = StudentView.class.getSimpleName();

	public StudentView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_item_student_view,parent);
	}

	public ImageView iv_child_face;
	public TextView txt_child_name;
	public TextView txt_child_birthday;
	public TextView tvUserViewName;
	public TextView txt_child_school;
	public TextView txt_child_gradle;
	public TextView txt_child_Guardian;
	public Button btn_student_jiebang;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_child_face = findView(R.id.iv_child_face, this);
		txt_child_name = findView(R.id.txt_child_name, this);
		txt_child_birthday = findView(R.id.txt_child_birthday, this);
		tvUserViewName = findView(R.id.txt_child_school);
		txt_child_school = findView(R.id.txt_child_gradle);
		txt_child_gradle = findView(R.id.txt_child_Guardian);
		btn_student_jiebang = findView(R.id.btn_student_jiebang, this);
		txt_child_Guardian = findView(R.id.txt_child_Guardian, this);

		return super.createView();
	}

	@Override
	public void bindView(Student data_){
		this.data = data_;
		Log.d(TAG,"data=null : "+(data==null));
		txt_child_birthday.setText(data.getBirthday());
		txt_child_name.setText(data.getStudentName());
		txt_child_school.setText(data.getSchoolName());
		txt_child_gradle.setText(data.getSchoolGradeName());
		txt_child_Guardian.setText(data.getCustody());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_student_jiebang:
			if (this.data!=null){
				showShortToast("解绑 111:"+data.getStudentName());
			}else {
				showShortToast("解绑222");
			}
			break;
		default:
			break;
		}
	}
}