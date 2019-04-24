package edu.children.xiaoshizi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import zuo.biao.library.util.Log;

public class DemoFragment extends XszBaseFragment {
    @BindView(R.id.txt_title)
    TextView txt_title;
    public static final String ARG_TITLE = "pushAppTitle";

    private String mTitle;

    public DemoFragment() {
        Log.d(TAG,"DemoFragment init = ");
    }
    public static DemoFragment newInstance(String title, String param2) {
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int getLayoutId() {
        return R.layout.fragment_demo;
    }


    @Override
    public void initView() {
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
        Log.d(TAG,"mTitle = "+mTitle);
        txt_title.setText(mTitle);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
