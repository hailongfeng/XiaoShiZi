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

import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.CacheUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleType;
import edu.children.xiaoshizi.bean.ArticleType_Table;
import edu.children.xiaoshizi.bean.LoadContentCategoryResponse;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.util.Log;

public class SafeLabFragment extends XszBaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewPagerTab;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<ArticleType> articleTypes=new ArrayList<>();

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
        List<ArticleType> articleTypes2=DbUtils.getModelList(ArticleType.class,ArticleType_Table.belongTo.eq(3));

        initType(articleTypes2);
        loadSeLabContentCategory();

    }
    void initTabs(List<ArticleType> articleTypes){
        FragmentPagerItems.Creator creator=FragmentPagerItems.with(context);
        Log.d(TAG,"articleTypes size==="+articleTypes.size());
        for (ArticleType articleType:articleTypes){
            Bundle bundle=new Bundle();
            bundle.putSerializable("articleType",articleType);
            creator.add(articleType.getTitle(), SeLabArticleFragment.class,bundle);
        }
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                fragmentManager, creator.create());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }


    private  void initType(List<ArticleType> types){
        if (types!=null){
            this.articleTypes.clear();
            this.articleTypes.addAll(types);
            initTabs(this.articleTypes);
//            multiStatusLayout_shouye.showContent();
        }else {
//            multiStatusLayout_shouye.showEmpty();
        }
    }

    @Override
    public void initData() {

    }


    private void loadSeLabContentCategory() {
        TreeMap sm = new TreeMap<String,String>();
        LogicService.post(context,APIMethod.loadSeLabContentCategory,sm,new ApiSubscriber<Response<LoadContentCategoryResponse>>() {

            @Override
            protected void onSuccess(Response<LoadContentCategoryResponse> response) {
                DemoApplication.getInstance().setContentSeLabCategoryResponse(response.getResult());
                List<ArticleType> articleTypes2=response.getResult().getCategoryResps();
                for (ArticleType type:articleTypes2){
                    type.setBelongTo(3);
                }

                DbUtils.deleteArticleType(3);
//                DbUtils.deleteModel(ArticleType.class,ArticleType_Table.belongTo.eq(3));
                DbUtils.saveModelList(articleTypes2);

                List<Article> articles=response.getResult().getContentResps();
                String type3Articles= JSONArray.toJSONString(articles);
                print("type3Articles="+type3Articles);
                CacheUtils.get(context).put("type3Articles",type3Articles);
                initType(articleTypes2);

            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
            }
        });
    }
    @Override
    public void initEvent() {

    }
}
