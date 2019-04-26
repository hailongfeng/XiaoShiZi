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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import zuo.biao.library.util.Log;


public class CourseListItemView extends XszBaseView<Article>{
	public CourseListItemView(Activity context, int layoutResId) {
		super(context, layoutResId);
	}

	public ImageView iv_course_pic;
	public TextView txt_course_name;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_course_pic = findView(R.id.iv_course_pic);
		txt_course_name = findView(R.id.txt_course_name);
		return super.createView();
	}

	public void bindView(Article data_) {
		this.data = data_;
		loadImage(this.data.getBannerImage(),iv_course_pic);
		txt_course_name.setText(this.data.getTitle());
		//不一定要用单选功能，实现也不一定要用这种方式，这里写会影响所有BaseView子类的性能，子类写更好 itemView.setSelected(selected);
	}
}