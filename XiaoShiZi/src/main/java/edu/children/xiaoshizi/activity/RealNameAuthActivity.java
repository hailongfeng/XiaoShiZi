package edu.children.xiaoshizi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.ItemDialog;

public class RealNameAuthActivity extends BaseActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_auth);
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
//        findView(R.id.ll_user_name,this);
        findView(R.id.ll_user_credentials_type,this);
//        findView(R.id.ll_user_credentials_num,this);
        findView(R.id.btn_user_add_credentials_zhengmian,this);
        findView(R.id.btn_user_add_credentials_fanmian,this);
        findView(R.id.btn_user_bind,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_credentials_type:
                break;
            case R.id.btn_user_add_credentials_zhengmian:
                break;
            case R.id.btn_user_add_credentials_fanmian:
                break;
            case R.id.btn_user_bind:
                break;
        }
    }
}
