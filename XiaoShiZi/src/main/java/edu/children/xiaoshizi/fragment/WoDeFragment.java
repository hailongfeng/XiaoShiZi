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

package edu.children.xiaoshizi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.BindingStudentActivity;
import edu.children.xiaoshizi.activity.ChangeUserInfoActivity;
import edu.children.xiaoshizi.activity.ParentInfoActivity;
import edu.children.xiaoshizi.adapter.ParentAdapter;
import edu.children.xiaoshizi.adapter.StudentAdapter;
import edu.children.xiaoshizi.bean.Parent;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;

/**设置fragment
 * @author Lemon
 * @use new WoDeFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class WoDeFragment extends XszBaseFragment implements OnClickListener, OnDialogButtonClickListener {

	@BindView(R.id.iv_user_face)
	RoundedImageView iv_user_face;;
	@BindView(R.id.txt_user_name)
	TextView txt_user_name;;

	@BindView(R.id.txt_user_telphone)
	TextView txt_user_telphone;;
	@BindView(R.id.btn_add_student)
	RoundTextView btn_add_student;;
	@BindView(R.id.iv_user_setting)
	ImageView iv_user_setting;;

	@BindView(R.id.rvBaseRecycler)
	RecyclerView rvStudentsRecycler;

	@BindView(R.id.cv_no_students)
	CardView cv_no_students;
	@BindView(R.id.btn_no_student_bind)
	RoundTextView btn_no_student_bind;

	private StudentAdapter studentAdapter;
	@BindView(R.id.rvCustodyRecycler)
	RecyclerView rvParentRecycler;;
	private ParentAdapter parentAdapter;



	//与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**创建一个Fragment实例
	 * @return
	 */
	public static WoDeFragment createInstance() {
		return new WoDeFragment();
	}

	//与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//类相关初始化，必须使用<<<<<<<<<<<<<<<<
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.wode_fragment, container, false);
		ButterKnife.bind(this, view);
		setContentView(view);
		//类相关初始化，必须使用>>>>>>>>>>>>>>>>

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		String headPortrait= DemoApplication.getInstance().getUser().getHeadPortrait();
		Glide.with(context).load(headPortrait ).into(iv_user_face);
	}

	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initView() {//必须调用

		rvStudentsRecycler.setLayoutManager(new LinearLayoutManager(context));
		studentAdapter = new StudentAdapter(context);
		rvStudentsRecycler.setAdapter(studentAdapter);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvParentRecycler.setLayoutManager(linearLayoutManager);
        parentAdapter = new ParentAdapter(context);
		parentAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(context,ParentInfoActivity.class);
				intent.putExtra("index",position);
				toActivity(intent);
			}
		});
        rvParentRecycler.setAdapter(parentAdapter);

	}
	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须调用
		User user=DemoApplication.getInstance().getUser();
		String userName=user.getUserName();
		if (userName!=null){
			txt_user_name.setText(userName);
		}else {
			txt_user_name.setText("");
		}
		loadImage(user.getHeadPortrait(),iv_user_face);
		txt_user_telphone.setText(DemoApplication.getInstance().getUser().getLoginName());

		List<Student> list= DemoApplication.getInstance().getLoginRespon().getStudents();
//		list.clear();
		if (list.size()==0){
			btn_add_student.setVisibility(View.GONE);
			rvStudentsRecycler.setVisibility(View.GONE);
			cv_no_students.setVisibility(View.VISIBLE);
		}else {
			cv_no_students.setVisibility(View.GONE);
			studentAdapter.refresh(list);
		}

		List<Parent> list1= DemoApplication.getInstance().getLoginRespon().getParents();
		parentAdapter.refresh(list1);


	}


	private void logout() {
		context.finish();
	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用

		findView(R.id.iv_user_setting, this);
		findView(R.id.btn_add_student, this);
		findView(R.id.ll_my_jifen, this);
		findView(R.id.ll_my_huancun, this);
		findView(R.id.ll_my_jiazhangjianyi, this);
		findView(R.id.ll_my_shezhi, this);
		findView(R.id.ll_my_fenxiang, this);
		findView(R.id.btn_no_student_bind, this);
	}




	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (! isPositive) {
			return;
		}

		switch (requestCode) {
		case 0:
			logout();
			break;
		default:
			break;
		}
	}



	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.iv_user_setting:
				showShortToast("设置");
				toActivity(new Intent(context, ChangeUserInfoActivity.class));
				break;
			case R.id.btn_no_student_bind:
			case R.id.btn_add_student:
				toActivity(new Intent(context, BindingStudentActivity.class));
//				toActivity(new Intent(context, RealNameAuthActivity.class));
				break;
			default:
				break;
		}
	}




	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}