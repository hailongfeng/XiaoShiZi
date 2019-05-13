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

package edu.children.xiaoshizi.fragment;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.roundview.RoundTextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.BindingStudentActivity;
import edu.children.xiaoshizi.activity.LoginActivity;
import edu.children.xiaoshizi.activity.MyCacheListActivity;
import edu.children.xiaoshizi.activity.MyIntegrationActivity;
import edu.children.xiaoshizi.activity.ParentInfoActivity;
import edu.children.xiaoshizi.activity.RealNameAuthActivity;
import edu.children.xiaoshizi.activity.SettingActivity;
import edu.children.xiaoshizi.activity.ShareBoardActivity;
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
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.StringUtils;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.AlertDialog;
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

	@BindView(R.id.cv_no_parent)
	CardView cv_no_parent;
	@BindView(R.id.cv_no_students)
	CardView cv_no_students;
	@BindView(R.id.btn_no_student_bind)
	RoundTextView btn_no_student_bind;

	@BindView(R.id.ll_have_no_parent)
	LinearLayout ll_have_no_parent;
	@BindView(R.id.rvCustodyRecycler)
	RecyclerView rvParentRecycler;;
	private ParentAdapter parentAdapter;

	public static WoDeFragment createInstance() {
		return new WoDeFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
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
				new AlertDialog(context, "提示", "确定要解绑当前学生？", true, 0, new AlertDialog.OnDialogButtonClickListener() {
					@Override
					public void onDialogButtonClick(int requestCode, boolean isPositive) {
						if (isPositive){
							Student student=(Student)bv.data;
							studentUnBinding(student.getStudentId());
						}
					}
				}).show();

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
		initShare();
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
				EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_unbinding_student,"解绑学生",""));
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

		if (!NetworkUtils.isConnected()){
			showShortToast(R.string.net_error);
		}
		updateUserInfo();

		updateStudent();

		updateParent();
	}

	private void updateParent() {
		if (isLogin()) {
			List<Parent> list1 = DemoApplication.getInstance().getLoginRespon().getParents();
			if (list1 != null && list1.size() != 0) {
				ll_have_no_parent.setVisibility(View.GONE);
				rvParentRecycler.setVisibility(View.VISIBLE);
				parentAdapter.refresh(list1);
			} else {
				ll_have_no_parent.setVisibility(View.VISIBLE);
				rvParentRecycler.setVisibility(View.GONE);
			}
		}else {
			ll_have_no_parent.setVisibility(View.VISIBLE);
			rvParentRecycler.setVisibility(View.GONE);
		}
	}

	private void updateStudent() {
		if (isLogin()) {
			cv_no_parent.setVisibility(View.VISIBLE);
			List<Student> list = DemoApplication.getInstance().getLoginRespon().getStudents();
			if (list != null) {
				Log.d(TAG,"有学生"+list.size());
				if (list.size() == 0) {
					btn_add_student.setVisibility(View.GONE);
					rvStudentsRecycler.setVisibility(View.GONE);
					cv_no_students.setVisibility(View.VISIBLE);
				} else {
					btn_add_student.setVisibility(View.VISIBLE);
					rvStudentsRecycler.setVisibility(View.VISIBLE);
					cv_no_students.setVisibility(View.GONE);
					studentAdapter.refresh(list);
				}
			}
		}else {
			btn_add_student.setVisibility(View.GONE);
			rvStudentsRecycler.setVisibility(View.GONE);
			cv_no_parent.setVisibility(View.GONE);
			cv_no_students.setVisibility(View.VISIBLE);
		}

	}

	private void updateUserInfo() {
		if (isLogin()){
			User user= DemoApplication.getInstance().getUser();
			String userName=user.getUserName();
			if (!StringUtils.isEmpty(userName)){
				txt_user_name.setText(userName);
			}else {
				txt_user_name.setText("");
			}
			loadImage(user.getHeadPortrait(),iv_user_face);
			txt_user_telphone.setText(DemoApplication.getInstance().getUser().getLoginName());
		}else {
			txt_user_name.setText("注册/登陆");
		}
	}


	private void logout() {
		context.finish();
	}

	@Override
	public void initEvent() {//必须调用
		findView(R.id.iv_user_face, this);
		findView(R.id.iv_user_setting, this);
		findView(R.id.btn_add_student, this);
		findView(R.id.ll_my_jifen, this);
		findView(R.id.ll_my_huancun, this);
		findView(R.id.ll_my_jiazhangjianyi, this);
		findView(R.id.ll_my_shezhi, this);
		findView(R.id.ll_my_fenxiang, this);
		findView(R.id.btn_no_student_bind, this);
		if (!isLogin()){
			txt_user_name.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isLogin()) {
						toActivity(new Intent(context, LoginActivity.class));
					}
				}
			});
		}
	}
	private UMShareListener mShareListener;
	private ShareAction mShareAction;
	void initShare(){
		mShareListener = new WoDeFragment.CustomShareListener(context);
		/*增加自定义按钮的分享面板*/
		mShareAction = new ShareAction(context).setDisplayList(
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
				.setShareboardclickCallback(new ShareBoardlistener() {
					@Override
					public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
						String url=DemoApplication.getInstance().getUser().getAppShareUrl();
						print("ShareUrl==="+url);
						UMWeb web = new UMWeb(url);
						web.setTitle("推荐小狮子");
						web.setDescription("来自小狮子的分享");
						web.setThumb(new UMImage(context, R.mipmap.logo));
						new ShareAction(context).withMedia(web)
								.setPlatform(share_media)
								.setCallback(mShareListener)
								.share();
					}
				});
	}


	private  class CustomShareListener implements UMShareListener {
		private WeakReference<ShareBoardActivity> mActivity;
		private CustomShareListener(Context activity) {
			mActivity = new WeakReference(activity);
		}

		@Override
		public void onStart(SHARE_MEDIA platform) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {

			if (platform.name().equals("WEIXIN_FAVORITE")) {
				Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
//				submitComment(article.getContentId(),"0","", ArticleComment.comment_type_Share);
			} else {
				if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
						&& platform != SHARE_MEDIA.EMAIL
						&& platform != SHARE_MEDIA.FLICKR
						&& platform != SHARE_MEDIA.FOURSQUARE
						&& platform != SHARE_MEDIA.TUMBLR
						&& platform != SHARE_MEDIA.POCKET
						&& platform != SHARE_MEDIA.PINTEREST

						&& platform != SHARE_MEDIA.INSTAGRAM
						&& platform != SHARE_MEDIA.GOOGLEPLUS
						&& platform != SHARE_MEDIA.YNOTE
						&& platform != SHARE_MEDIA.EVERNOTE) {
//					submitComment(article.getContentId(),"0","", ArticleComment.comment_type_Share);
					Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
				}else {
					showShortToast("分享失败啦");
				}
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
					&& platform != SHARE_MEDIA.EMAIL
					&& platform != SHARE_MEDIA.FLICKR
					&& platform != SHARE_MEDIA.FOURSQUARE
					&& platform != SHARE_MEDIA.TUMBLR
					&& platform != SHARE_MEDIA.POCKET
					&& platform != SHARE_MEDIA.PINTEREST

					&& platform != SHARE_MEDIA.INSTAGRAM
					&& platform != SHARE_MEDIA.GOOGLEPLUS
					&& platform != SHARE_MEDIA.YNOTE
					&& platform != SHARE_MEDIA.EVERNOTE) {
				Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	}


//	@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUserInfoChange(EventBusMessage<String> messageEvent) {
		if (messageEvent.getType()==EventBusMessage.Type_User_info_change){
			updateUserInfo();
		}else if (messageEvent.getType()==EventBusMessage.Type_binding_student){
			updateStudent();
		}else if (messageEvent.getType()==EventBusMessage.Type_user_login){
			Log.d(TAG,"Type_user_login====");
			initData();
		}else if (messageEvent.getType()==EventBusMessage.Type_user_logout){
			iv_user_face.setImageResource(R.drawable.user_default);
			txt_user_telphone.setText("");
			initData();
			initEvent();
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
		if (!isLogin()){
//			showShortToast("您还未登录，请先登录");
			toActivity(new Intent(context,LoginActivity.class));
			return;
		}
		switch (v.getId()) {
			case R.id.iv_user_face:
			case R.id.iv_user_setting:
				toActivity(new Intent(context, UserInfoActivity.class));
				break;
			case R.id.ll_my_huancun:
				toActivity(new Intent(context, MyCacheListActivity.class));
				break;
			case R.id.ll_my_jifen:
				toActivity(new Intent(context, MyIntegrationActivity.class));
				break;
			case R.id.ll_my_shezhi:
				toActivity(new Intent(context, SettingActivity.class));
				break;
			case R.id.ll_my_jiazhangjianyi:
				toActivity(new Intent(context, SuggestionActivity.class));
				break;
			case R.id.ll_my_fenxiang:
				//toActivity(new Intent(context, SuggestionActivity.class));
				mShareAction.open();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
		UMShareAPI.get(context).release();
	}
}