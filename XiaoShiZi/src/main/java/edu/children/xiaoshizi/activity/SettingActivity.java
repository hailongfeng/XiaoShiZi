package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.FileUtils;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.utils.XszCache;

public class SettingActivity extends XszBaseActivity{

    @BindView(R.id.ll_setting_clear_cache)
    LinearLayout ll_setting_clear_cache;
    @BindView(R.id.ll_setting_about_us)
    LinearLayout ll_setting_about_us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ll_setting_clear_cache.setOnClickListener(this);
        ll_setting_about_us.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting_clear_cache:
                XszCache.getCacheSize();
                XszCache.clearCacheSize();
                showShortToast("清除完成");
                break;
            case R.id.ll_setting_about_us:
                break;
        }
    }
}
