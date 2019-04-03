package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.gyf.barlibrary.ImmersionBar;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.fragment.SafeToolFragment;
import edu.children.xiaoshizi.fragment.ShouYeFragment;
import edu.children.xiaoshizi.fragment.WoDeFragment;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.util.Log;

public class MainActivity extends XszBaseActivity {
    private static final String TAG = "MainActivity";

    private static final String[] TAB_NAMES = {"主页", "消息", "发现", "设置"};

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void getSchools() {
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
            protected void onFail(NetErrorException error) {
                Log.d(TAG, error.getMessage());
            }

        });
    }

    void getMyprofile() {
        LogicService.post(context, APIMethod.getMyProfile, null, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> listResponse) {
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
            protected void onFail(NetErrorException error) {
                Log.d(TAG, error.getMessage());
            }

        });
    }
    @Override
    public void initData() {
        getSchools();
        getMyprofile();
    }

    private Fragment fragments[] = {
            ShouYeFragment.createInstance(),
            ShouYeFragment.createInstance(),
            SafeToolFragment.createInstance(0),
            WoDeFragment.createInstance()
    };

    private int currentPosition = 0;

    @Override
    public void initEvent() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                int position = 0;
                if (tabId == R.id.tab_shouye) {
                    position = 0;
                } else if (tabId == R.id.tab_aqkt) {
                    position = 1;
                } else if (tabId == R.id.tab_aqgj) {
                    position = 2;
                } else if (tabId == R.id.tab_wo) {
                    position = 3;
                }
                if (currentPosition == position) {
                    return;
                }
                Log.d(TAG,"position : "+position);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.hide(fragments[currentPosition]);
                if (fragments[position].isAdded() == false) {
                    ft.add(R.id.flMainTabFragmentContainer, fragments[position]);
                }
                ft.show(fragments[position]).commit();
                currentPosition=position;
            }
        });
    }


    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.flMainTabFragmentContainer, fragments[currentPosition]);
        ft.show(fragments[currentPosition]).commit();
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
