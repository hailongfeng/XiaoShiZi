package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.MessageView;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;

public class MessageListActivity extends XszBaseActivity {


    @BindView(R.id.lvBaseList)
    ListView lvBaseList;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        EventBus.getDefault().register(this);

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
                    toActivity(MessageErrorDetailActivity.createIntent(context, message.getId(),message.getSnapStatus()));
                }else {
                    toActivity(MessageDetailActivity.createIntent(context, message.getId(),message.getSnapStatus()));
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEventBusMessage(EventBusMessage<String> messageEvent) {
        Log.d(TAG,"EventBusMessage type= "+messageEvent.getType());
        if (messageEvent.getType()==EventBusMessage.Type_message_FeedBack){
            initData();
        }

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
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
