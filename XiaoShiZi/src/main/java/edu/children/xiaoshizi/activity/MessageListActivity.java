package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.MessageAdapter;
import edu.children.xiaoshizi.adapter.UserAdapter;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.utils.TestUtil;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.base.BaseListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.JSON;

public class MessageListActivity extends XszBaseActivity {


    @BindView(R.id.lvBaseList)
    ListView lvBaseList;
    MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        adapter= new MessageAdapter(context);
        lvBaseList.setAdapter(adapter);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        List<Message> data=new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i <10 ; i++) {
                    data.add(new Message(i+"","消息："+i));
                }
                adapter.refresh(data);
            }
        }, 1000);
    }

    @Override
    public void initEvent() {

    }
}
