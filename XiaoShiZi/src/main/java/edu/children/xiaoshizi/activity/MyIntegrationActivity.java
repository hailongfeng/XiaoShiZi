package edu.children.xiaoshizi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.makeramen.roundedimageview.RoundedImageView;
import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.IntegrationView;
import edu.children.xiaoshizi.adapter.view.QianDaoView;
import edu.children.xiaoshizi.bean.IntegrationRecode;
import edu.children.xiaoshizi.bean.MyIntegrationResponse;
import edu.children.xiaoshizi.bean.QianDao;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseAdapter;

//我的积分
public class MyIntegrationActivity extends XszBaseActivity {

    @BindView(R.id.iv_user_face)
    RoundedImageView iv_user_face;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.ll_qiandao)
    LinearLayout ll_qiandao;//签到
    @BindView(R.id.iv_jifen_guize)
    ImageView iv_jifen_guize;//签到
    @BindView(R.id.rvQianDaoRecycler)
    GridView rvQianDaoRecycler;
    @BindView(R.id.txt_qiandao_tianshu)
    TextView txt_qiandao_tianshu;
    @BindView(R.id.txt_jifen_all)
    TextView txt_jifen_all;

    private BaseAdapter qianDaoAdapter;
    @BindView(R.id.multiStatusLayout)
    MultiStatusLayout multiStatusLayout;//
    @BindView(R.id.rvQianDaoRecyclerJiLu)
    RecyclerView rvQianDaoRecyclerJiLu;//签到记录
    private BaseAdapter qianDaoAdapterJiLu;

    List<QianDao> qianDaoDatas =new ArrayList<>();
    List<IntegrationRecode> integrationRecodeData =new ArrayList<>();
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
//        qianDaoAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent=new Intent(context,ParentInfoActivity.class);
////                intent.putExtra("index",position);
////                toActivity(intent);
//            }
//        });
        rvQianDaoRecycler.setAdapter(qianDaoAdapter);
    }

    void initQianDaoJiLu(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvQianDaoRecyclerJiLu.setLayoutManager(linearLayoutManager);
        qianDaoAdapterJiLu = new BaseAdapter<IntegrationRecode, IntegrationView>(this) {
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
        loadIntegralDetailList();
    }

    //加载积分
    void signedDaily(){
        LogicService.post(context, APIMethod.signedDaily,null,new ApiSubscriber<Response<String>>() {
            @Override
            public void onSuccess(Response<String> respon) {
                showShortToast(respon.getMessage());
            }

            @Override
            protected void onFail(Throwable  error) {
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }
    //加载积分
    void loadIntegralDetailList(){
        LogicService.post(context, APIMethod.loadIntegralDetailList,null,new ApiSubscriber<Response<MyIntegrationResponse>>() {
            @Override
            public void onSuccess(Response<MyIntegrationResponse> respon) {
                if (respon.getResult()!=null&&respon.getResult().getIntegralDetailList()!=null) {
                    integrationRecodeData.clear();
                    integrationRecodeData.addAll(respon.getResult().getIntegralDetailList());
                    qianDaoAdapterJiLu.refresh(integrationRecodeData);
                }

                txt_user_name.setText(respon.getResult().getUserName());
                loadImage(respon.getResult().getHeadPortrait(),iv_user_face);
//                已连续签到\n2天
                String points=respon.getResult().getSignedDayNum()+"";
                SpannableString spannableString = new SpannableString(points+"天");
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#53ADFE")), 0,points.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txt_qiandao_tianshu.setText(spannableString);
                txt_jifen_all.setText(respon.getResult().getPoints());
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
        ll_qiandao.setOnClickListener(this);
        iv_jifen_guize.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.ll_qiandao){
            signedDaily();
        }else  if (v.getId()==R.id.iv_jifen_guize){

        }
    }
}
