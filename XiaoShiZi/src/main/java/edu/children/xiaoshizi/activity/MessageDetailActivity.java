package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.bean.BuildBean;
import com.dou361.dialogui.listener.DialogUIListener;
import com.flyco.roundview.RoundTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Message;
import zuo.biao.library.ui.WebViewActivity;

public class MessageDetailActivity extends XszBaseActivity implements View.OnClickListener{
    @BindView(R.id.txt_student_name)
    TextView txt_student_name;

    @BindView(R.id.iv_student_face)
    RoundedImageView iv_student_face;
    @BindView(R.id.iv_student_recognite_face)
    RoundedImageView iv_student_recognite_face;
    @BindView(R.id.txt_pic_xsd)
    TextView txt_pic_xsd;
    @BindView(R.id.txt_in_out_time)
    TextView txt_in_out_time;
    @BindView(R.id.txt_in_out_reason)
    TextView txt_in_out_reason;
    @BindView(R.id.rtv_recognite_error)
    RoundTextView rtv_recognite_error;
    @BindView(R.id.rtv_recognite_right)
    RoundTextView rtv_recognite_right;



    private Message message;
    private int type;
    private static final String EXTRA_MESSAGE="message";
    private static final String EXTRA_TYPE="type";
    public static Intent createIntent(Context context, Message message,int type) {
        return new Intent(context, WebViewActivity.class).
                putExtra(MessageDetailActivity.EXTRA_MESSAGE, message).
                putExtra(MessageDetailActivity.EXTRA_TYPE, type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message= (Message) getIntent().getSerializableExtra(MessageDetailActivity.EXTRA_MESSAGE);
        type=  getIntent().getIntExtra(MessageDetailActivity.EXTRA_TYPE,0);
        setContentView(R.layout.activity_message_detail);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
       loadImage(message.imgPicUrl,iv_student_face);
       loadImage(message.snapPicUrl,iv_student_recognite_face);
       txt_pic_xsd.setText(message.similarity+"");
       txt_in_out_time.setText(message.triggerTime);
       txt_in_out_reason.setText(message.triggerTime);
    }

    @Override
    public void initEvent() {
        rtv_recognite_error.setOnClickListener(this);
        rtv_recognite_right.setOnClickListener(this);
    }
    private Dialog dialog=null;
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.rtv_recognite_error){
            dialog=DialogUIUtils.showMdAlert(context, "提示", "感谢您的反馈", new DialogUIListener() {
                @Override
                public void onPositive() {
                    DialogUIUtils.dismiss(dialog);
                }

                @Override
                public void onNegative() {
                    DialogUIUtils.dismiss(dialog);
                }
            }).show();
        }else if (v.getId()==R.id.rtv_recognite_right){
//            showShortToast("识别正确");
            dialog=DialogUIUtils.showMdAlert(context, "提示", "感谢您的反馈\n是否手动提高识别率", new DialogUIListener() {
                @Override
                public void onPositive() {
                    DialogUIUtils.dismiss(dialog);
                }

                @Override
                public void onNegative() {
                    DialogUIUtils.dismiss(dialog);
                }
            }).show();
        }
    }
}
