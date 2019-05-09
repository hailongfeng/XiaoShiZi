package edu.children.xiaoshizi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.flyco.roundview.RoundTextView;

import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.StringUtils;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

//识别反馈
public class RecogniteFeedbackActivity extends BaseTakePhotoActivity  {

    @BindView(R.id.iv_student_pic)
    ImageView iv_student_pic;
    @BindView(R.id.rtv_student_add)
    RoundTextView rtv_student_add;
    @BindView(R.id.btn_rechange)
    RoundTextView btn_rechange;
    @BindView(R.id.btn_sure)
    RoundTextView btn_sure;

    private String studentId;
    private String snapMsgId;

    private static final String EXTRA_MESSAGE="studentId";
    private static final String EXTRA_SNAPMSGID="snapMsgId";
    public static Intent createIntent(Context context, String studentId, String snapMsgId) {
        return new Intent(context, RecogniteFeedbackActivity.class).
                putExtra(RecogniteFeedbackActivity.EXTRA_MESSAGE, studentId).
                putExtra(RecogniteFeedbackActivity.EXTRA_SNAPMSGID, snapMsgId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!=null&&!StringUtils.isEmail(getIntent().getStringExtra(EXTRA_MESSAGE))){
            studentId=getIntent().getStringExtra(EXTRA_MESSAGE);
            snapMsgId=getIntent().getStringExtra(EXTRA_SNAPMSGID);
        }
        setContentView(R.layout.activity_recognite_feedback);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        rtv_student_add.setOnClickListener(this);
        btn_rechange.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rtv_student_add:
            case R.id.btn_rechange:
                takePicture();
                break;
            case R.id.btn_sure:
                submit();
                break;
        }
    }

    private void submit(){
        TreeMap sm = new TreeMap<String,String>();
        if (StringUtil.isEmpty(originalFilePath,true)) {
            showShortToast("还未选择头像");
            return;
        }
        sm.put("studentId",studentId);
        sm.put("snapMsgId",snapMsgId);
        Map<String,File> imagefiles=new HashMap<>();
        imagefiles.put("studentHeadPortrait",new File(originalFilePath));
        Map<String,File> videofiles=new HashMap<>();

        showLoading(R.string.msg_handing);
        LogicService.post(context, APIMethod.uploadStudentHeadPortrait, sm, imagefiles,videofiles,new ApiSubscriber<Response<List<Student>>>() {
            @Override
            public void onSuccess(Response<List<Student>> respon) {
                if (respon.getResult()!=null&&respon.getResult().size()>0){
                    Student student=respon.getResult().get(0);
                    List<Student> students= DemoApplication.getInstance().getLoginRespon().getStudents();
                    Iterator<Student> it= students.iterator();
                    int i=0;
                    while (it.hasNext()){
                        Student student1=it.next();
                        if (student1.getStudentId().equalsIgnoreCase(student.getStudentId())){
                            it.remove();
                            students.add(i,student);
                            print("找到学生，更新学生信息，"+student.getStudentName());
                            break;
                        }
                        i++;
                    }
                }

                hideLoading();
                showShortToast(respon.getMessage());
                finish();
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if(!StringUtils.isEmpty(originalFilePath)){
            iv_student_pic.setVisibility(View.VISIBLE);
            rtv_student_add.setVisibility(View.GONE);
            loadImage(originalFilePath,iv_student_pic);
        }
    }
}
