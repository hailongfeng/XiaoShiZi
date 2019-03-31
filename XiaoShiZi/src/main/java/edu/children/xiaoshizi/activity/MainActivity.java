package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gyf.barlibrary.ImmersionBar;
import com.umeng.message.PushAgent;

import java.util.List;
import java.util.TreeMap;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.fragment.ShouYeFragment;
import edu.children.xiaoshizi.fragment.WoDeFragment;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseBottomTabActivity;
import zuo.biao.library.util.Log;

public class MainActivity extends BaseBottomTabActivity {
    private static final String TAG = "MainActivity";

    private static final String[] TAB_NAMES = {"主页", "消息", "发现", "设置"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }


    void getSchools() {
        LogicService.getSchoolInfo(context, 1, new ApiSubscriber<Response<List<School>>>() {
            @Override
            public void onNext(Response<List<School>> listResponse) {
                Log.d(TAG, "1" + listResponse.getResult().size());
                DbUtils.saveSchool(listResponse.getResult());
            }

            @Override
            protected void onFail(NetErrorException error) {
                Log.d(TAG, error.getMessage());
            }
        });
        LogicService.getSchoolInfo(context, 2, new ApiSubscriber<Response<List<School>>>() {
            @Override
            public void onNext(Response<List<School>> listResponse) {
                Log.d(TAG, "2" + listResponse.getResult().size());
                DbUtils.saveSchool(listResponse.getResult());
            }

            @Override
            protected void onFail(NetErrorException error) {
                Log.d(TAG, error.getMessage());
            }
        });
        LogicService.getSchoolInfo(context, 3, new ApiSubscriber<Response<List<School>>>() {
            @Override
            public void onNext(Response<List<School>> listResponse) {
                Log.d(TAG, "3" + listResponse.getResult().size());
                DbUtils.saveSchool(listResponse.getResult());
            }

            @Override
            protected void onFail(NetErrorException error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        PushAgent.getInstance(this).onAppStart();
        TreeMap sm = new TreeMap<String, String>();
        sm.put("userId", DemoApplication.getInstance().getUser().getUserId());

        /*
        LogicService.getStudentsAndParents(context,sm,new ApiSubscriber<Response<LoginRespon>>() {
            @Override
            public void onNext(Response<LoginRespon> respon) {
                DemoApplication.getInstance().setLoginRespon(respon.getResult());
                DemoApplication.getInstance().setUser(respon.getResult().getLoginResp());
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
            }
        });
        */
    }

    @Override
    public void initView() {
        super.initView();
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    protected int[] getTabClickIds() {
        return new int[]{R.id.llBottomTabTab0, R.id.llBottomTabTab1, R.id.llBottomTabTab2, R.id.llBottomTabTab3};
    }

    @Override
    protected int[][] getTabSelectIds() {
        return new int[][]{
                new int[]{R.id.ivBottomTabTab0, R.id.ivBottomTabTab1, R.id.ivBottomTabTab2, R.id.ivBottomTabTab3},//顶部图标
                new int[]{R.id.tvBottomTabTab0, R.id.tvBottomTabTab1, R.id.tvBottomTabTab2, R.id.tvBottomTabTab3}//底部文字
        };
    }

    @Override
    public int getFragmentContainerResId() {
        return R.id.flMainTabFragmentContainer;
    }


    @Override
    protected void selectTab(int position) {
        //导致切换时闪屏，建议去掉BottomTabActivity中的topbar，在fragment中显示topbar
        //		rlBottomTabTopbar.setVisibility(position == 2 ? View.GONE : View.VISIBLE);

//        tvBaseTitle.setText(TAB_NAMES[position]);

        //点击底部tab切换顶部tab，非必要
//        if (position == 2 && position == currentPosition && demoTabFragment != null) {
//            demoTabFragment.selectNext();
//        }
    }

    @Override
    protected Fragment getFragment(int position) {
        switch (position) {
            case 1:
                return ShouYeFragment.createInstance();
            case 2:
                return WoDeFragment.createInstance();
            case 3:
                return WoDeFragment.createInstance();
            default:
                return ShouYeFragment.createInstance();
        }
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
