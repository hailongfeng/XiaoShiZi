package edu.children.xiaoshizi.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.heima.tabview.library.TabView;
import com.heima.tabview.library.TabViewChild;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.fragment.DemoFragment;
import edu.children.xiaoshizi.fragment.SafeClassRoomFragment;
import edu.children.xiaoshizi.fragment.SafeToolFragment;
import edu.children.xiaoshizi.fragment.ShouYeFragment;
import edu.children.xiaoshizi.fragment.WoDeFragment;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class MainActivity extends XszBaseActivity {
    private static final String TAG = "MainActivity";

//    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @BindView(R.id.tabView)
    TabView tabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        setContentView(R.layout.activity_main);
    }

   private void getSchools() {
        LogicService.post(context, APIMethod.loadSchoolData, null, new ApiSubscriber<Response<List<School>>>() {
            @Override
            public void onSuccess(Response<List<School>> listResponse) {
                if (listResponse.getResult().size() > 0) {
                    Log.d(TAG, "School size =" + listResponse.getResult().size());
                    DbUtils.deleteModel(School.class);
                    DbUtils.saveModelList(listResponse.getResult());
                } else {
                    Log.w(TAG, "无数据");
                }
            }
            @Override
            protected void onFail(Throwable  error) {
                Log.d(TAG, error.getMessage());
            }

        });
    }

    private void getMyprofile(String id)  {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("userId",id);
        LogicService.post(context, APIMethod.getMyProfile, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> listResponse) {
                hideLoading();
                User newUser= listResponse.getResult();
                User oldUser=DemoApplication.getInstance().getUser();
                oldUser.setLoginName(newUser.getLoginName());
                oldUser.setUserName(newUser.getUserName());
                oldUser.setHeadPortrait(newUser.getHeadPortrait());
                oldUser.setEmail(newUser.getEmail());
                oldUser.setWorkingAddress(newUser.getWorkingAddress());
                oldUser.setHomeAddress(newUser.getHomeAddress());
                oldUser.setCardNum(newUser.getCardNum());
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                Log.d(TAG, error.getMessage());
            }

        });
    }
    @Override
    public void initData() {
//        getSchools();
//        String userId=DemoApplication.getInstance().getUser().getUserId();
//        getMyprofile(userId);
//        loadSeClassRoomContentCategory();
//        loadSeLabContentCategory();
    }

    private Fragment fragments[] = {
            ShouYeFragment.createInstance(),
            new SafeClassRoomFragment(),
//            DemoFragment.newInstance("开发中...",""),
            SafeToolFragment.createInstance(0),
            WoDeFragment.createInstance()
    };


    private void initPermission(){
        String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS,
                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        EasyPermissions.requestPermissions(
                context,
                "申请权限",
                0,mPermissionList
        );
    }

    @Override
    public void initEvent() {
        tabView.setOnTabChildClickListener(new TabView.OnTabChildClickListener() {
            @Override
            public void onTabChildClick(int  position, ImageView currentImageIcon, TextView currentTextView) {
            }
        });
    }

    @Override
    public void initView() {

        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        List<TabViewChild> tabViewChildList=new ArrayList<>();
        TabViewChild tabViewChild01=new TabViewChild(R.drawable.main_tab_shouye_select,R.drawable.main_tab_shouye_normal,"首页",  fragments[0]);
        TabViewChild tabViewChild02=new TabViewChild(R.drawable.main_tab_aqkt_select,R.drawable.main_tab_aqkt_normal,"安全课堂",  fragments[1]);
        TabViewChild tabViewChild03=new TabViewChild(R.drawable.main_tab_aqgj_select,R.drawable.main_tab_aqgj_normal,"安全工具",  fragments[2]);
        TabViewChild tabViewChild04=new TabViewChild(R.drawable.main_tab_wo_select,R.drawable.main_tab_wo_normal,"我的",fragments[3]);
        tabViewChildList.add(tabViewChild01);
        tabViewChildList.add(tabViewChild02);
        tabViewChildList.add(tabViewChild03);
        tabViewChildList.add(tabViewChild04);
        tabView.setTabViewChild(tabViewChildList,fragmentManager);
    }
    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
            showShortToast("再按一次退出程序");
            mPressedTime = mNowTime;
        } else {//退出程序
            this.finish();
            DemoApplication.getInstance().exit();
        }
    }
}
