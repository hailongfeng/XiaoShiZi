package edu.children.xiaoshizi.activity;

import android.support.v7.app.AppCompatActivity;
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

public class IntegrationRuleActivity extends XszBaseActivity {
    @BindView(R.id.lin_web)
    LinearLayout linWeb;
    private AgentWeb agentWeb;
    private AgentWeb.PreAgentWeb preAgentWeb;
    String pointRuleMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointRuleMsg=getIntent().getStringExtra("pointRuleMsg");
        setContentView(R.layout.activity_integration_rule);
    }

    @Override
    public void initView() {
        preAgentWeb= AgentWeb.with(this)
                .setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready();
        agentWeb= getAgentWebField(preAgentWeb);

    }

    @Override
    public void initData() {
        agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(pointRuleMsg), "text/html", "UTF-8", null);
    }

    @Override
    public void initEvent() {

    }

    //加载积分
    void signedDaily(){
        showLoading(R.string.msg_handing);
        LogicService.post(context, APIMethod.signedDaily,null,new ApiSubscriber<Response<MyIntegrationResponse>>() {
            @Override
            public void onSuccess(Response<MyIntegrationResponse> respon) {
                hideLoading();
                String introduce="";
                agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(introduce), "text/html", "UTF-8", null);
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }
}
