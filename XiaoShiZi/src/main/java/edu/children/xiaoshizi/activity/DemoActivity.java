package edu.children.xiaoshizi.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.fragment.DemoFragment;
import zuo.biao.library.util.Log;

public class DemoActivity extends XszBaseActivity {
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    @Override
    public void initView() {
        List<ArticleType> articleTypes=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ArticleType articleType=new ArticleType();
            articleType.setTitle("tt"+i);
        }
        FragmentPagerItems.Creator creator1=FragmentPagerItems.with(context);
        Log.d(TAG,"articleTypes size==="+articleTypes.size());
        for (ArticleType articleType:articleTypes){
            Bundle bundle=new Bundle();
            bundle.putSerializable("articleType",articleType);
            bundle.putSerializable(DemoFragment.ARG_TITLE,articleType.getTitle());
            creator1.add(articleType.getTitle(), DemoFragment.class,bundle);
        }

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                fragmentManager, creator1.create());
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
