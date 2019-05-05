package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.widget.PayPsdInputView;

public class PasswordReInputActivity extends XszBaseActivity {

    @BindView(R.id.password)
    PayPsdInputView passwordInputView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_re_input);
    }

    @Override
    public void initView() {

        passwordInputView.setComparePassword( new PayPsdInputView.onPasswordListener() {

            @Override
            public void onDifference(String oldPsd, String newPsd) {
                // TODO: 2018/1/22  和上次输入的密码不一致  做相应的业务逻辑处理
//                Toast.makeText(PasswordReInputActivity.this, "两次密码输入不同" + oldPsd + "!=" + newPsd, Toast.LENGTH_SHORT).show();
//                passwordInputView.cleanPsd();
            }

            @Override
            public void onEqual(String psd) {
                // TODO: 2017/5/7 两次输入密码相同，那就去进行支付楼
//                Toast.makeText(PasswordReInputActivity.this, "密码相同" + psd, Toast.LENGTH_SHORT).show();
//                passwordInputView.setComparePassword("");
//                passwordInputView.cleanPsd();
            }

            @Override
            public void inputFinished(String inputPsd) {
                // TODO: 2018/1/3 输完逻辑
//                Toast.makeText(PasswordReInputActivity.this, "输入完毕：" + inputPsd, Toast.LENGTH_SHORT).show();
//                passwordInputView.setComparePassword(inputPsd);
                Intent intent=new Intent();
                intent.putExtra("psd",inputPsd);
                setResult(RESULT_OK,intent);
                onBackPressed();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onReturnClick(View v) {
//        super.onReturnClick(v);
        setResult(RESULT_CANCELED);
        super.onReturnClick(v);
    }
}
