package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.children.xiaoshizi.R;
import zuo.biao.library.ui.WebViewActivity;

public class InOutSchoolRecodeDetailActivity extends XszBaseActivity {

    public static Intent createIntent(Context context, String title, String url) {
        return new Intent(context, WebViewActivity.class).
                putExtra(WebViewActivity.INTENT_TITLE, title).
                putExtra(WebViewActivity.INTENT_URL, url);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_school_recode_detail);
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
