package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.MessageAdapter;
import edu.children.xiaoshizi.adapter.UserAdapter;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.utils.TestUtil;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.JSON;

public class MessageActivity extends BaseHttpListActivity<Message, ListView, MessageAdapter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        initView();
        initData();
        initEvent();
        srlBaseHttpList.autoRefresh();
    }

    @Override
    public void setList(List<Message> list) {
        setList(new AdapterCallBack<MessageAdapter>() {
            @Override
            public MessageAdapter createAdapter() {
                return new MessageAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void getListAsync(int page) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                List<Message> data=new ArrayList<>();
                for (int i = 0; i <10 ; i++) {
                    data.add(new Message(i+"","消息："+i));
                }
                onHttpResponse(-page, page >= 5 ? null : JSON.toJSONString(data), null);
            }
        }, 1000);

    }

    @Override
    public List<Message> parseArray(String json) {
        return JSON.parseArray(json, Message.class);
    }
}
