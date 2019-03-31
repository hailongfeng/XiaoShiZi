package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Parent;

public class ParentInfoActivity extends XszBaseActivity  {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.iv_parent_face)
    RoundedImageView iv_parent_face;
    @BindView(R.id.txt_parent_name)
    TextView txt_parent_name;
    @BindView(R.id.txt_parent_address)
    TextView txt_parent_address;
    @BindView(R.id.txt_work_address)
    TextView txt_work_address;
    @BindView(R.id.txt_parent_home_address)
    TextView txt_parent_home_address;
    @BindView(R.id.txt_parent_email)
    TextView txt_parent_email;
    @BindView(R.id.txt_parent_id_num)
    TextView txt_parent_id_num;
    @BindView(R.id.txt_parent_guanxi)
    TextView txt_parent_guanxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_info);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
    }

    @Override
    public void initData() {
        int index=getIntent().getIntExtra("index",0);
        Parent parent= DemoApplication.getInstance().getLoginRespon().getParents().get(index);
        loadImage(parent.getHeadPortrait(),iv_parent_face);
        txt_parent_name.setText(parent.getParentUserName());
        txt_parent_address.setText(parent.getParentUserName());
        txt_parent_guanxi.setText(parent.getCustody());
    }

    @Override
    public void initEvent() {

    }


}
