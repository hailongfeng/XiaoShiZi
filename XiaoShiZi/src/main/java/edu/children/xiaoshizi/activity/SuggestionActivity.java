package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import edu.children.xiaoshizi.R;

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

    }
}
