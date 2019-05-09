package edu.children.xiaoshizi.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheUtils;

import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.utils.Constant;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.StringUtil;

public class LoginOutActivity extends XszBaseActivity {
    private TextView tvTitle;
    private TextView tvMessage;
    private Button btnPositive;
    private Button btnNegative;


    public static Intent createIntent(Context context, String title) {
        return new Intent(context, LoginOutActivity.class).
                putExtra(INTENT_TITLE, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_out);
    }

    @Override
    public void initView() {
        tvTitle = findViewById(zuo.biao.library.R.id.tvAlertDialogTitle);
        tvMessage = findViewById(zuo.biao.library.R.id.tvAlertDialogMessage);
        btnPositive = findViewById(zuo.biao.library.R.id.btnAlertDialogPositive);
        btnNegative = findViewById(zuo.biao.library.R.id.btnAlertDialogNegative);

//        tvTitle.setVisibility(StringUtil.isNotEmpty(title, true) ? View.VISIBLE : View.GONE);
        tvTitle.setText("提示");

//        btnPositive.setText(StringUtil.getCurrentString());
        btnPositive.setOnClickListener(this);

        if (false) {
            if (StringUtil.isNotEmpty("", true)) {
                btnNegative.setText(StringUtil.getCurrentString());
            }
            btnNegative.setOnClickListener(this);
        } else {
            btnNegative.setVisibility(View.GONE);
        }

        String msg=getIntent().getStringExtra(INTENT_TITLE);
        tvMessage.setText(StringUtil.getTrimedString(msg));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        CacheUtils.get(context).remove(Constant.cache_user);
        CacheUtils.get(context).clear();
        DemoApplication.getInstance().exit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //    Dialog dialog;
//    void loginout(String msg){
//        dialog=new AlertDialog(context, "提示", msg, false, 0, new AlertDialog.OnDialogButtonClickListener() {
//            @Override
//            public void onDialogButtonClick(int requestCode, boolean isPositive) {
//                if (isPositive){
//                    CacheUtils.get(context).remove(Constant.cache_user);
//                    CacheUtils.get(context).clear();
//                    dialog.dismiss();
//                    DemoApplication.getInstance().exit();
//                }
//            }
//        });
//        dialog.show();
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//    }
}
