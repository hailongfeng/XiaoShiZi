package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.StringUtils;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;

public class UserInfoActivity extends XszBaseActivity  implements View.OnClickListener {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.iv_user_face)
    ImageView iv_user_face;
    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.rg_user_sex)
    RadioGroup rg_user_sex;
    @BindView(R.id.edt_user_dizhi)
    EditText edt_user_dizhi;
    @BindView(R.id.edt_user_phone)
    EditText edt_user_phone;
    @BindView(R.id.edt_user_email)
    EditText edt_user_email;
    @BindView(R.id.edt_user_work_adddress)
    EditText edt_user_work_adddress;
    @BindView(R.id.edt_user_home_adddress)
    EditText edt_user_home_adddress;
    @BindView(R.id.edt_user_id_cardno)
    EditText edt_user_id_cardno;
    private String headPortrait="https://single-obs.obs.cn-east-2.myhuaweicloud.com:443/app_pic/head_portrait/5gcnrqBYVl1Qngok8uH/2019033111033725425.jpg";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=DemoApplication.getInstance().getUser();
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    public void initData() {
        edt_user_name.setText(user.getUserName());
        edt_user_phone.setText(user.getPhone());
        loadImage(user.getHeadPortrait(),iv_user_face);
        edt_user_home_adddress.setText(user.getHomeAddress());
        edt_user_dizhi.setText(user.getHomeAddress());
        edt_user_work_adddress.setText(user.getWorkingAddress());
        edt_user_email.setText(user.getEmail());
        edt_user_id_cardno.setText(user.getCardNum());
    }

    @Override
    public void initEvent() {
        findView(R.id.btn_change_user).setOnClickListener(this);
    }

    class SendMsgDialogView{
        @BindView(R.id.txt_user_phone)
        TextView txt_dialog_user_phone;
        @BindView(R.id.edt_user_input_code)
        EditText edt_dialog_user_input_code;
        @BindView(R.id.btn_resend)
        RoundTextView btn_dialog_resend;
        @BindView(R.id.btn_sure)
        RoundTextView btn_dialog_sure;
    }
    SendMsgDialogView sendMsgDialogView;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_user:
                getVeryCode();
                View rootView = View.inflate(context, R.layout.dialog_send_msg_view, null);
                sendMsgDialogView=new SendMsgDialogView();
                ButterKnife.bind(sendMsgDialogView, rootView);
                sendMsgDialogView.txt_dialog_user_phone.setText(user.getLoginName());
                sendMsgDialogView.btn_dialog_resend.setOnClickListener(this);
                sendMsgDialogView.btn_dialog_sure.setOnClickListener(this);
                DialogUIUtils.showCustomAlert(this, rootView).show();
                break;
            case R.id.btn_resend:
                getVeryCode();
                break;
            case R.id.btn_sure:
                toActivity(new Intent(context, ChangeUserInfoActivity.class));
                finish();
                break;
        }
    }
    private void getVeryCode() {
        TreeMap sm = new TreeMap<String,String>();
        String phone=user.getPhone();
        if (StringUtils.isEmpty(phone))
            sm.put("phoneNumber","18697386272");
        LogicService.post(context, APIMethod.getVerifyCode,sm,new ApiSubscriber<Response>(){
            @Override
            public void onSuccess(Response response) {
                Log.d(TAG,"response code:"+response.getCode());
                Log.d(TAG,"onNext  , "+Thread.currentThread().getName());
                showShortToast("验证码发送成功");
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
                showShortToast("验证码发送失败，请重试");
            }
        });
    }
}
