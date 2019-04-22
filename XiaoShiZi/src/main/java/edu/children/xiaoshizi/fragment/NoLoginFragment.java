package edu.children.xiaoshizi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.LoginActivity;
import zuo.biao.library.util.Log;

public class NoLoginFragment extends XszBaseFragment {
    @BindView(R.id.rtv_login)
    RoundTextView rtv_login;

    public static final String ARG_TITLE = "title";

    private String mTitle;

    public NoLoginFragment() {
        Log.d(TAG,"DemoFragment init = ");
    }
    public static NoLoginFragment newInstance() {
        NoLoginFragment fragment = new NoLoginFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_TITLE, title);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int getLayoutId() {
        return R.layout.fragment_nologin;
    }


    @Override
    public void initView() {
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
        Log.d(TAG,"mTitle = "+mTitle);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        rtv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(new Intent(context, LoginActivity.class));
            }
        });
    }
}
