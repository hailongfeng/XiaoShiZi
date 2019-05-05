package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.flyco.roundview.RoundTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Random;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;

public class MessageErrorDetailActivity extends XszBaseActivity implements View.OnClickListener{
    @BindView(R.id.txt_student_name)
    TextView txt_student_name;

    @BindView(R.id.iv_student_face)
    RoundedImageView iv_student_face;
    @BindView(R.id.iv_student_recognite_face)
    RoundedImageView iv_student_recognite_face;
    @BindView(R.id.txt_pic_xsd)
    TextView txt_pic_xsd;//相似度
    @BindView(R.id.rtv_recognite_already_out)
    RoundTextView rtv_recognite_already_out;
    @BindView(R.id.rtv_recognite_not_out)
    RoundTextView rtv_recognite_not_out;


    private InAndOutSchoolRecode inAndOutSchoolRecode;
    private String snapMsgId="";
    private static final String EXTRA_MESSAGE="snapMsgId";
    private static final String EXTRA_TYPE="type";
    public static Intent createIntent(Context context, String messageId) {
        return new Intent(context, MessageErrorDetailActivity.class).
                putExtra(MessageErrorDetailActivity.EXTRA_MESSAGE, messageId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            snapMsgId = bun.getString("snapMsgId");
        }
        setContentView(R.layout.activity_message_error_detail);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            snapMsgId = bun.getString("snapMsgId");
        }
    }
    @Override
    public void initView() {

    }

    private void getMessageById(String messageId){
        TreeMap sm = new TreeMap<String,String>();
        sm.put("snapMsgId", snapMsgId);
        LogicService.post(context, APIMethod.findSnapMsgById, sm, new ApiSubscriber<Response<InAndOutSchoolRecode>>() {
            @Override
            public void onSuccess(Response<InAndOutSchoolRecode> response) {
                if (response.getResult()!=null) {
                    inAndOutSchoolRecode = response.getResult();
                    initMessage();
                }else {
                    showShortToast("消息读取失败");
                }
            }
            @Override
            protected void onFail(Throwable  error) {
                showShortToast(error.getMessage());
            }
        });
    }

    @Override
    public void initData() {
        getMessageById(snapMsgId);
    }


    private void initMessage(){
        loadImage(inAndOutSchoolRecode.imgPicUrl,iv_student_face);
        loadImage(inAndOutSchoolRecode.snapPicUrl,iv_student_recognite_face);
        txt_student_name.setText(inAndOutSchoolRecode.getStudentName());
        txt_pic_xsd.setText(inAndOutSchoolRecode.similarity+"");

    }

    @Override
    public void initEvent() {
        rtv_recognite_already_out.setOnClickListener(this);
        rtv_recognite_not_out.setOnClickListener(this);
    }

    private void recogniteBack(String snapMsgId, String feedbackResult){
        showLoading(R.string.msg_handing);
        TreeMap sm = new TreeMap<String,String>();
        sm.put("snapMsgId", snapMsgId);
        sm.put("feedbackResult", feedbackResult);
        LogicService.post(context, APIMethod.doSnapMsgFeedBack, sm, new ApiSubscriber<Response<Message>>() {
            @Override
            public void onSuccess(Response<Message> response) {
                hideLoading();

                dialog=DialogUIUtils.showAlert(context, "提示", "感谢您的反馈", "","","确定","取消",false,false,false,new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        DialogUIUtils.dismiss(dialog);
                        finish();
                    }

                    @Override
                    public void onNegative() {
                        DialogUIUtils.dismiss(dialog);
                        finish();
                    }
                }).show();
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                error.printStackTrace();
                showShortToast(error.getMessage());
            }
        });
    }

    private Dialog dialog=null;
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.rtv_recognite_already_out){
            recogniteBack(this.snapMsgId,"已出校门");
        }else if (v.getId()==R.id.rtv_recognite_not_out){
            recogniteBack(this.snapMsgId,"未出校门");
        }
    }
}
