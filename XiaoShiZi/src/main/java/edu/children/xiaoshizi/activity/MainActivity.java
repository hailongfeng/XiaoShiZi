package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.fragment.ShouYeFragment;
import edu.children.xiaoshizi.fragment.WoDeFragment;
import zuo.biao.library.base.BaseBottomTabActivity;

public class MainActivity extends BaseBottomTabActivity {
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

    @Override
    public void initView() {
        super.initView();
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    protected int[] getTabClickIds() {
        return new int[]{R.id.llBottomTabTab0,R.id.llBottomTabTab1,R.id.llBottomTabTab2,R.id.llBottomTabTab3};
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
    };

}
