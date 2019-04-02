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
        LogicService.post(context, APIMethod.loadSchoolData,null, new ApiSubscriber<Response<List<School>>>() {
            @Override
            public void onNext(Response<List<School>> listResponse) {
                if (listResponse.getResult().size()>0){
                    Log.d(TAG, "School size =" + listResponse.getResult().size());
                    DbUtils.deleteModel(School.class);
                    DbUtils.saveModelList(listResponse.getResult());
                }else {
                    Log.w(TAG,"无数据");
                }
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
    }

    private Fragment fragments[]={
            ShouYeFragment.createInstance(),
            ShouYeFragment.createInstance(),
            SafeToolFragment.createInstance(0),
            WoDeFragment.createInstance()
    };

    private int currentPosition=0;
    @Override
    public void initEvent() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {            @Override
        public void onTabSelected(@IdRes int tabId) {
            int position =0;
            if (tabId == R.id.tab_favorites) {
                position=0;
            // change your content accordingly.
            }else if (tabId == R.id.tab_nearby) {
                position=1;// The tab with id R.id.tab_favorites was selected,
                // change your content accordingly.
            }else if (tabId == R.id.tab_friends) {                    // The tab with id R.id.tab_favorites was selected,
                position=2;
            }else if (tabId == R.id.tab_wo) {                    // The tab with id R.id.tab_favorites was selected,
                position=3;
            }
            if (currentPosition==position){
                return;
            }
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.hide(fragments[currentPosition]);
            if (fragments[position].isAdded() == false) {
                ft.add(R.id.flMainTabFragmentContainer, fragments[position]);
            }
            ft.show(fragments[position]).commit();

        }});
    }


    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
    }



//    @Override
//    protected Fragment getFragment(int position) {
//        switch (position) {
//            case 1:
//                return ShouYeFragment.createInstance();
//            case 2:
//                return SafeToolFragment.createInstance(0);
//            case 3:
//                return WoDeFragment.createInstance();
//            default:
//                return ShouYeFragment.createInstance();
//        }
//    }

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
