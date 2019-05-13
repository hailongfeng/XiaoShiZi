package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;

import org.greenrobot.eventbus.EventBus;

import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.StringUtil;

/**
 * 家长建议
 */
public class SuggestionActivity extends XszBaseActivity {


    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.edt_suggestion_title)
    EditText edt_suggestion_title;
    @BindView(R.id.edt_suggestion_content)
    EditText edt_suggestion_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void initEvent() {
        btn_sure.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (StringUtil.isEmpty(edt_suggestion_title,true)){
            showShortToast("标题不能为空");
            return;
        }
        String title =edt_suggestion_title.getText().toString();
        String content=edt_suggestion_content.getText().toString();
        TreeMap sm = new TreeMap<String,String>();
        sm.put("title",title);
        sm.put("content",content);
        showLoading(R.string.msg_handing);
        LogicService.post(context, APIMethod.submitFeedBack, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> respon) {
                hideLoading();
//                new AlertDialog(context, "提示", "提交成功!", false, 0, new AlertDialog.OnDialogButtonClickListener() {
//                    @Override
//                    public void onDialogButtonClick(int requestCode, boolean isPositive) {
//                        if (isPositive){
//                            finish();
//                        }
//                    }
//                }).show();

                DialogUIUtils.showAlert(context, "提示", "提交成功!", "", "", "", "确定", false, false, false, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        finish();
                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onReturnClick(View v) {
//        new AlertDialog(context, "提示", "放弃提交当前反馈？", true, 0, new AlertDialog.OnDialogButtonClickListener() {
//            @Override
//            public void onDialogButtonClick(int requestCode, boolean isPositive) {
//                if (isPositive){
//                    SuggestionActivity.super.onReturnClick(v);
//                }else {
//
//                }
//            }
//        }).show();

        DialogUIUtils.showAlert(context, "提示", "放弃提交当前反馈？", "", "", "取消", "确定", false, false, false, new DialogUIListener() {
            @Override
            public void onPositive() {
            }

            @Override
            public void onNegative() {
                SuggestionActivity.super.onReturnClick(v);
            }
        }).show();
    }
}
