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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundTextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.walle.multistatuslayout.MultiStatusLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.BindingStudentActivity;
import edu.children.xiaoshizi.activity.MyCacheListActivity;
import edu.children.xiaoshizi.activity.ParentInfoActivity;
import edu.children.xiaoshizi.activity.RealNameAuthActivity;
import edu.children.xiaoshizi.activity.SettingActivity;
import edu.children.xiaoshizi.activity.SuggestionActivity;
import edu.children.xiaoshizi.activity.UserInfoActivity;
import edu.children.xiaoshizi.adapter.ParentAdapter;
import edu.children.xiaoshizi.adapter.StudentAdapter;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.Parent;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.StringUtils;
import edu.children.xiaoshizi.utils.XszCache;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;

/**设置fragment
 * @author Lemon
 * @use new WoDeFragment(),详细使用见.DemoFragmentActivity(initData方法内)
 */
public class WoDeFragment extends XszBaseFragment implements OnClickListener{

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
	private StudentAdapter studentAdapter;

	@BindView(R.id.cv_no_students)
	CardView cv_no_students;
	@BindView(R.id.btn_no_student_bind)
	RoundTextView btn_no_student_bind;

	@BindView(R.id.multiStatusLayout)
	MultiStatusLayout multiStatusLayout;

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

	@Override
	int getLayoutId() {
		return R.layout.wode_fragment;
	}

	@Override
	public void initView() {//必须调用

		rvStudentsRecycler.setLayoutManager(new LinearLayoutManager(context));
		studentAdapter = new StudentAdapter(context);
		rvStudentsRecycler.setAdapter(studentAdapter);
		studentAdapter.setOnViewClickListener(new BaseView.OnViewClickListener() {
			@Override
			public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
				Student student=(Student)bv.data;
				studentUnBinding(student.getStudentId());
			}
		});
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

	private void studentUnBinding(String studentId){
		showLoading("正在解绑");
		TreeMap sm = new TreeMap<String,String>();
		sm.put("studentId",studentId);
		LogicService.post(context, APIMethod.studentUnBinding,sm, new ApiSubscriber<Response<List<Student>>>() {
			@Override
			public void onSuccess(Response<List<Student>> respon) {
				hideLoading();
				List<Student> list= respon.getResult();
				DemoApplication.getInstance().getLoginRespon().setStudents(list);
				if (list.size()==0){
					btn_add_student.setVisibility(View.GONE);
					rvStudentsRecycler.setVisibility(View.GONE);
					cv_no_students.setVisibility(View.VISIBLE);
				}else {
					cv_no_students.setVisibility(View.GONE);
					studentAdapter.refresh(list);
				}
			}

			@Override
			protected void onFail(Throwable  error) {
				hideLoading();
				showShortToast(error.getMessage());
				error.printStackTrace();
			}
		});
	}

	@Override
	public void initData() {//必须调用
		updateUserInfo();

		updateStrudent();

		updateParent();
		long size= XszCache.getCacheSize();
		Log.d(TAG,"cache size="+size);
	}

	private void updateParent() {
		List<Parent> list1= DemoApplication.getInstance().getLoginRespon().getParents();
		if (list1!=null&&list1.size() != 0) {
			multiStatusLayout.showContent();
			parentAdapter.refresh(list1);
		} else {
			multiStatusLayout.showEmpty();
		}
	}

	private void updateStrudent() {
		List<Student> list=DemoApplication.getInstance().getLoginRespon().getStudents();
		if (list!=null){
			if (list.size()==0){
				btn_add_student.setVisibility(View.GONE);
				rvStudentsRecycler.setVisibility(View.GONE);
				cv_no_students.setVisibility(View.VISIBLE);
			}else {
				cv_no_students.setVisibility(View.GONE);
				studentAdapter.refresh(list);
			}
		}

	}

	private void updateUserInfo() {
		User user= DemoApplication.getInstance().getUser();
		String userName=user.getUserName();
		if (!StringUtils.isEmpty(userName)){
			txt_user_name.setText(userName);
		}else {
			txt_user_name.setText("");
		}
		loadImage(user.getHeadPortrait(),iv_user_face);
		txt_user_telphone.setText(DemoApplication.getInstance().getUser().getLoginName());
	}


	private void logout() {
		context.finish();
	}

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
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUserInfoChange(EventBusMessage<String> messageEvent) {
		if (messageEvent.getType()==EventBusMessage.Type_User_info_change){
			updateUserInfo();
		}else if (messageEvent.getType()==EventBusMessage.Type_binding_user){
			updateStrudent();
		}
	}

	/*粘性事件*/
//	EventBus.getDefault().postSticky(messageEvent);
//	@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//	public void XXX(MessageEvent messageEvent) {
//    ...
//	}
	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.iv_user_setting:
				toActivity(new Intent(context, UserInfoActivity.class));
				break;
			case R.id.ll_my_huancun:
				toActivity(new Intent(context, MyCacheListActivity.class));
				break;
			case R.id.ll_my_jifen:
//				integration;
//				toActivity(new Intent(context, SettingActivity.class));
				break;
			case R.id.ll_my_shezhi:
				toActivity(new Intent(context, SettingActivity.class));
				break;
			case R.id.ll_my_jiazhangjianyi:
				toActivity(new Intent(context, SuggestionActivity.class));
				break;
			case R.id.btn_no_student_bind:
			case R.id.btn_add_student:
				User user=DemoApplication.getInstance().getUser();
//				toActivity(new Intent(context, RealNameAuthActivity.class));
				if (user.getVerifiedStatus().equals("0")){
					toActivity(new Intent(context, RealNameAuthActivity.class));
				}else {
					toActivity(new Intent(context, BindingStudentActivity.class));
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
	}
}