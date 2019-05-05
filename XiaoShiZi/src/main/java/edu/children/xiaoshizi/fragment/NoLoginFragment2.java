package edu.children.xiaoshizi.fragment;

import android.content.Intent;
import android.view.View;

import com.flyco.roundview.RoundTextView;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.LoginActivity;
import zuo.biao.library.util.Log;

public class NoLoginFragment2 extends XszBaseFragment {
    @BindView(R.id.rtv_login)
    RoundTextView rtv_login;

    public static final String ARG_TITLE = "pushAppTitle";

    private String mTitle;

    public NoLoginFragment2() {
        Log.d(TAG,"DemoFragment init = ");
    }
    public static NoLoginFragment2 newInstance() {
        NoLoginFragment2 fragment = new NoLoginFragment2();
//        Bundle args = new Bundle();
//        args.putString(ARG_TITLE, pushAppTitle);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    int getLayoutId() {
        return R.layout.fragment_nologin2;
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
