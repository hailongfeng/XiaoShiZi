package edu.children.xiaoshizi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.MessageAdapter;
import edu.children.xiaoshizi.adapter.UserAdapter;
import edu.children.xiaoshizi.adapter.view.MessageView;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.fragment.SafeToolFragment;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.TestUtil;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.base.BaseListActivity;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;

public class MessageListActivity extends XszBaseActivity {


    @BindView(R.id.lvBaseList)
    ListView lvBaseList;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

    }
    @Override
    public void initView() {
        adapter= new BaseAdapter<Message, MessageView>(context) {
            @Override
            public MessageView createView(int position, ViewGroup parent) {
                return new MessageView(context, parent);
            }

        };
        lvBaseList.setAdapter(adapter);
    }

    @Override
    public void initData() {
        getMessage();
    }

    @Override
    public void initEvent() {
        adapter.setOnViewClickListener(new BaseView.OnViewClickListener() {
            @Override
            public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
                Message message=((Message)bv.data);
                if (message.getSnapStatus().equalsIgnoreCase("errorGoschool")
                ||message.getSnapStatus().equalsIgnoreCase("errorLeaveschool")
                ){
                    toActivity(MessageErrorDetailActivity.createIntent(context, ((Message) bv.data).getId()));
                }else {
                    toActivity(MessageDetailActivity.createIntent(context, ((Message) bv.data).getId()));
                }
            }
        });
    }

    private void getMessage() {
        TreeMap sm = new TreeMap<String,String>();
        LogicService.post(context, APIMethod.findPushAppSnapMsgList,sm,new ApiSubscriber<Response<List<Message>>>(){
            @Override
            public void onSuccess(Response<List<Message>> response) {
                List<Message> data=response.getResult();
                if (data!=null){
                    adapter.refresh(data);
                }
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast(error.getMessage());
            }
        });
    }
}
