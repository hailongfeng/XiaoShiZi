package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.bean.Student;
import zuo.biao.library.ui.WebViewActivity;

public class InOutSchoolRecodeDetailActivity extends XszBaseActivity {
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

    private InAndOutSchoolRecode recode;
    private static final String EXTRA_INANDOUTSCHOOLRECODE="InAndOutSchoolRecode";
    private static final String EXTRA_STUDENT="student";
    public static Intent createIntent(Context context, InAndOutSchoolRecode recode) {
        return new Intent(context, InOutSchoolRecodeDetailActivity.class).
                putExtra(InOutSchoolRecodeDetailActivity.EXTRA_INANDOUTSCHOOLRECODE, recode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recode= (InAndOutSchoolRecode) getIntent().getSerializableExtra(InOutSchoolRecodeDetailActivity.EXTRA_INANDOUTSCHOOLRECODE);
        setContentView(R.layout.activity_in_out_school_recode_detail);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        txt_student_name.setText(recode.student.getStudentName());
        loadImage(recode.imgPicUrl,iv_student_face);
        loadImage(recode.snapPicUrl,iv_student_recognite_face);
        txt_pic_xsd.setText(recode.similarity+"");
        txt_in_out_time.setText(recode.triggerTime);
        txt_in_out_reason.setText(recode.triggerTime);
    }

    @Override
    public void initEvent() {

    }

}
