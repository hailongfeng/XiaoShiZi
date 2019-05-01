package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.MyIntegrationResponse;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.WebViewActivity;

public class XszWebViewActivity extends XszBaseActivity {
    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    private AgentWeb agentWeb;
    private AgentWeb.PreAgentWeb preAgentWeb;

    public static final String INTENT_RETURN = "INTENT_RETURN";
    public static final String INTENT_CONTENT = "INTENT_CONTENT";
    private String content;
    public static Intent createIntent(Context context, String title, String content) {
        return new Intent(context, XszWebViewActivity.class).
                putExtra(XszWebViewActivity.INTENT_TITLE, title).
                putExtra(XszWebViewActivity.INTENT_CONTENT, content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content=getIntent().getStringExtra(INTENT_CONTENT);
        setContentView(R.layout.activity_xsz_web_view);
    }

    @Override
    public void initView() {
        autoSetTitle();
        preAgentWeb= AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready();
        agentWeb= getAgentWebField(preAgentWeb);

    }

    @Override
    public void initData() {
        agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(content), "text/html", "UTF-8", null);
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agentWeb.destroy();
    }
}
