package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Parent;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;

public class ParentInfoActivity extends XszBaseActivity  {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.iv_parent_face)
    RoundedImageView iv_parent_face;
    @BindView(R.id.txt_parent_name)
    TextView txt_parent_name;
    @BindView(R.id.txt_parent_phone)
    TextView txt_parent_phone;
    @BindView(R.id.txt_parent_area)
    TextView txt_parent_area;
    @BindView(R.id.txt_work_address)
    TextView txt_work_address;
    @BindView(R.id.txt_parent_home_address)
    TextView txt_parent_home_address;
    @BindView(R.id.txt_parent_email)
    TextView txt_parent_email;
    @BindView(R.id.txt_parent_id_cardno)
    TextView txt_parent_id_cardno;
    @BindView(R.id.txt_parent_guanxi)
    TextView txt_parent_guanxi;
    Parent parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_info);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        int index=getIntent().getIntExtra("index",0);
        parent=DemoApplication.getInstance().getLoginRespon().getParents().get(index);
        updateParent(parent);
        getPersionInfoById(parent.getParentId());
    }

    private void updateParent(Parent parent) {
        loadImage(parent.getHeadPortrait(),iv_parent_face);
        txt_parent_name.setText(parent.getParentUserName());
        txt_parent_phone.setText(parent.getMobile());
        txt_parent_area.setText(parent.getArea());
        txt_parent_guanxi.setText(parent.getCustody());
        txt_parent_home_address.setText(parent.getHomeAddress());
        txt_work_address.setText(parent.getWorkingAddress());
        txt_parent_email.setText(parent.getEmail());
        txt_parent_id_cardno.setText(parent.getCardNum());
    }

    private void getPersionInfoById(String id) {
        showLoading(R.string.msg_handing);
        TreeMap sm = new TreeMap<String,String>();
        sm.put("userId",id);
        LogicService.post(context, APIMethod.getMyProfile, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> listResponse) {
                hideLoading();
                User newUser= listResponse.getResult();
                parent.setLoginName(newUser.getLoginName());
                parent.setUserName(newUser.getUserName());
                parent.setHeadPortrait(newUser.getHeadPortrait());
                parent.setEmail(newUser.getEmail());
                parent.setWorkingAddress(newUser.getWorkingAddress());
                parent.setHomeAddress(newUser.getHomeAddress());
                parent.setCardNum(newUser.getCardNum());
                updateParent(parent);
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
            }

        });
    }
    @Override
    public void initEvent() {

    }


}
