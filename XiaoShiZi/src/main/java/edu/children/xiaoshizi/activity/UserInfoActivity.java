package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.util.StringUtil;

public class UserInfoActivity extends XszBaseActivity implements View.OnClickListener {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.iv_user_face)
    ImageView iv_user_face;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.txt_user_area)
    TextView txt_user_area;
    @BindView(R.id.txt_user_phone)
    TextView txt_user_phone;
    @BindView(R.id.txt_user_email)
    TextView txt_user_email;
    @BindView(R.id.txt_user_work_adddress)
    TextView txt_user_work_adddress;
    @BindView(R.id.txt_user_home_adddress)
    TextView txt_user_home_adddress;
    @BindView(R.id.txt_user_id_cardno)
    TextView txt_user_id_cardno;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = DemoApplication.getInstance().getUser();
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
        txt_user_name.setText(user.getUserName());
        txt_user_phone.setText(user.getMobile());
        loadImage(user.getHeadPortrait(), iv_user_face);
        txt_user_home_adddress.setText(user.getHomeAddress());
        txt_user_area.setText(user.getArea());
        txt_user_work_adddress.setText(user.getWorkingAddress());
        txt_user_email.setText(user.getEmail());
        txt_user_id_cardno.setText(user.getCardNum());
    }

    @Override
    public void initEvent() {
        findView(R.id.btn_change_user).setOnClickListener(this);
    }

    class SendMsgDialogView {
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
                sendMsgDialogView = new SendMsgDialogView();
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
                if (StringUtil.isEmpty(sendMsgDialogView.edt_dialog_user_input_code,true)){
                    showShortToast("请填写验证码");
                    return;
                }
                String verifyCode=sendMsgDialogView.edt_dialog_user_input_code.getText().toString();
                validSmsVCode(verifyCode);
                break;
        }
    }

    private void validSmsVCode(String verifyCode) {
        TreeMap sm = new TreeMap<String, String>();
        sm.put("verifyCode", verifyCode);
        LogicService.post(context, APIMethod.validSmsVCode, sm, new ApiSubscriber<Response>() {
            @Override
            public void onSuccess(Response response) {
                toActivity(new Intent(context, ChangeUserInfoActivity.class));
                finish();
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast(error.getMessage());
            }
        });
    }


    private void getVeryCode() {
        TreeMap sm = new TreeMap<String, String>();
        String phone = user.getPhone();
        sm.put("phoneNumber", phone);
        LogicService.post(context, APIMethod.getVerifyCode, sm, new ApiSubscriber<Response>() {
            @Override
            public void onSuccess(Response response) {
                showShortToast(response.getMessage());
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast(error.getMessage());
            }
        });
    }
}
