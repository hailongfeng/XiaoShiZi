package edu.children.xiaoshizi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.ArticleType;
import zuo.biao.library.util.Log;

public class SafeLabFragment extends XszBaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    @BindView(R.id.viewpager)
    ViewPager viewPager;


    public static SafeLabFragment newInstance(String param1, String param2) {
        SafeLabFragment fragment = new SafeLabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int getLayoutId() {
        return R.layout.fragment_safe_lab;
    }

    @Override
    public void initView() {
        List<ArticleType> articleTypes= DemoApplication.getInstance().getContentSeLabCategoryResponse().getCategoryResps();
        FragmentPagerItems.Creator creator=FragmentPagerItems.with(context);
        Log.d(TAG,"articleTypes size==="+articleTypes.size());
        for (ArticleType articleType:articleTypes){
            Bundle bundle=new Bundle();
            bundle.putSerializable("articleType",articleType);
//            bundle.putString(DemoFragment.ARG_TITLE,articleType.getTitle());
            creator.add(articleType.getTitle(), SeLabArticleFragment.class,bundle);
        }

        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                fragmentManager, creator.create());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
