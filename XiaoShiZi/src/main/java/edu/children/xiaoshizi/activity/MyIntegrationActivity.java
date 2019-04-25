package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.IntegrationView;
import edu.children.xiaoshizi.adapter.view.QianDaoView;
import edu.children.xiaoshizi.bean.Integration;
import edu.children.xiaoshizi.bean.QianDao;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.util.Log;

//我的积分
public class MyIntegrationActivity extends XszBaseActivity {

    @BindView(R.id.rvQianDaoRecycler)
    GridView rvQianDaoRecycler;//签到
    private BaseAdapter qianDaoAdapter;

    @BindView(R.id.multiStatusLayout)
    MultiStatusLayout multiStatusLayout;//
    @BindView(R.id.rvQianDaoRecyclerJiLu)
    RecyclerView rvQianDaoRecyclerJiLu;//签到记录
    private BaseAdapter qianDaoAdapterJiLu;

    List<QianDao> qianDaoDatas =new ArrayList<>();
    List<Integration> integrationDatas=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integration);
    }

    @Override
    public void initView() {
        initQianDao();
        initQianDaoJiLu();
        multiStatusLayout.showContent();
    }

    void initQianDao(){
        qianDaoAdapter = new BaseAdapter<QianDao, QianDaoView>(this) {
            @Override
            public QianDaoView createView(int viewType, ViewGroup parent) {
                return new QianDaoView(context,parent);
            }
        };
        qianDaoAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(context,ParentInfoActivity.class);
//                intent.putExtra("index",position);
//                toActivity(intent);
            }
        });
        rvQianDaoRecycler.setAdapter(qianDaoAdapter);
    }

    void initQianDaoJiLu(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvQianDaoRecyclerJiLu.setLayoutManager(linearLayoutManager);
        qianDaoAdapterJiLu = new BaseAdapter<Integration, IntegrationView>(this) {
            @Override
            public IntegrationView createView(int viewType, ViewGroup parent) {
                return new IntegrationView(context,parent);
            }
        };
        rvQianDaoRecyclerJiLu.setAdapter(qianDaoAdapterJiLu);
    }

    @Override
    public void initData() {
        int count=3;
        qianDaoDatas.add(new QianDao(1,1,count));
        qianDaoDatas.add(new QianDao(2,5,count));
        qianDaoDatas.add(new QianDao(3,10,count));
        qianDaoDatas.add(new QianDao(4,15,count));
        qianDaoDatas.add(new QianDao(5,20,count));
        qianDaoDatas.add(new QianDao(6,25,count));
        qianDaoDatas.add(new QianDao(7,30,count));
        qianDaoAdapter.refresh(qianDaoDatas);
        for (int i = 0; i < 15; i++) {
            integrationDatas.add(new Integration("签到"+i,"2019-04-18",5+i*2));
        }
        qianDaoAdapterJiLu.refresh(integrationDatas);
    }

    //加载积分
    void loadIntegralDetailList(){
        LogicService.post(context, APIMethod.loadIntegralDetailList,null,new ApiSubscriber<Response<JSONArray>>() {
            @Override
            public void onSuccess(Response<JSONArray> respon) {

            }

            @Override
            protected void onFail(Throwable  error) {
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    @Override
    public void initEvent() {

    }
}
