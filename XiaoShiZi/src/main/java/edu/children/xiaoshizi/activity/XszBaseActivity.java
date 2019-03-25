package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseActivity;

public class XszBaseActivity extends BaseActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
