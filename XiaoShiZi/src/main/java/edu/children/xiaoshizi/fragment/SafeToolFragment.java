package edu.children.xiaoshizi.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walle.multistatuslayout.MultiStatusLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.MessageListActivity;
import edu.children.xiaoshizi.adapter.InOutSchoolRecodeAdapter;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.DateUtil;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.TimeUtil;

public class SafeToolFragment extends XszBaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM_CURRENT_STUDENT = "current_student";

    private int mParamCurrentStudentIndex=0;
    private String mParam2;

    @BindView(R.id.rl_student_switch)
    RelativeLayout rl_student_switch;
    @BindView(R.id.iv_student_face)
    ImageView iv_student_face;
    @BindView(R.id.txt_student_name)
    TextView txt_student_name;
    @BindView(R.id.txt_student_birthday)
    TextView txt_student_birthday;
    @BindView(R.id.txt_student_school)
    TextView txt_student_school;
    @BindView(R.id.txt_student_gradle)
    TextView txt_student_gradle;
    @BindView(R.id.txt_student_Guardian)
    TextView txt_student_Guardian;

    @BindView(R.id.multiStatusLayout)
    MultiStatusLayout multiStatusLayout;

    @BindView(R.id.lvBaseList)
    ListView lvBaseList;
    @BindView(R.id.txt_date)
    TextView txt_date;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.iv_change_date)
    ImageView iv_change_date;
    InOutSchoolRecodeAdapter inOutSchoolRecodeAdapter;
    Student student;
//    private OnFragmentInteractionListener mListener;

    public static SafeToolFragment createInstance(int indexStudent) {
        SafeToolFragment fragment = new SafeToolFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_CURRENT_STUDENT, indexStudent);
        fragment.setArguments(args);
        return fragment;
    }

    public SafeToolFragment() {
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_save_tool;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamCurrentStudentIndex = getArguments().getInt(ARG_PARAM_CURRENT_STUDENT);
        }
        List<Student> students=DemoApplication.getInstance().getLoginRespon().getStudents();
        BINDING_STUDENT_NAMES =new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            BINDING_STUDENT_NAMES[i]=students.get(i).getStudentName();
        }
        Calendar calendar=Calendar.getInstance();
        currentDate=DateUtil.format(calendar.getTime(),DateUtil.P1);
    }

    private String currentDate="";

    @Override
    public void initView() {
        txt_date.setText(currentDate);
        inOutSchoolRecodeAdapter= new InOutSchoolRecodeAdapter(context,student);
        lvBaseList.setAdapter(inOutSchoolRecodeAdapter);
    }

    @Override
    public void initData() {
        changeCurrentStudent(0);
        getRecodeByDate(student.getStudentId(),currentDate);
    }

    private void getRecodeByDate(String studentId,String snapMsgDate) {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("studentId",studentId);
        sm.put("snapMsgDate",snapMsgDate);
        LogicService.post(context, APIMethod.findStudentSnapMsg,sm,new ApiSubscriber<Response<List<InAndOutSchoolRecode>>>(){
            @Override
            public void onSuccess(Response<List<InAndOutSchoolRecode>> response) {
                for (InAndOutSchoolRecode recode:response.getResult()){
                    recode.student=SafeToolFragment.this.student;
                }
                Log.d(TAG,"InAndOutSchoolRecode size="+response.getResult().size());
                if (response.getResult().size()==0){
                    multiStatusLayout.showEmpty();
                }else {
                    multiStatusLayout.showContent();
                    inOutSchoolRecodeAdapter.refresh(response.getResult());
                }
            }

            @Override
            protected void onFail(Throwable  error) {
                error.printStackTrace();
                showShortToast("记录获取失败");
            }
        });
    }

    @Override
    public void initEvent() {
        iv_change_date.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        rl_student_switch.setOnClickListener(this);
    }
    private static final int REQUEST_TO_DATE_PICKER = 1;
    private static final int REQUEST_TO_SELECT_STUDENT = 2;
//    private int[] selectedDate = new int[]{1971, 0, 1};

    private static String[] BINDING_STUDENT_NAMES;

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_message){
            toActivity(new Intent(context, MessageListActivity.class));
        }else if (v.getId()==R.id.iv_change_date){
            toActivity(DatePickerWindow.createIntent(context, new int[]{1971, 0, 1}
                , TimeUtil.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);
        }else if (v.getId()==R.id.rl_student_switch){
            toActivity(BottomMenuWindow.createIntent(context, BINDING_STUDENT_NAMES)
                    .putExtra(BottomMenuWindow.INTENT_TITLE, "选择学生"), REQUEST_TO_SELECT_STUDENT, false);
        }
    }

    private void changeCurrentStudent(int position){
        mParamCurrentStudentIndex=position;
        student =DemoApplication.getInstance().getLoginRespon().getStudents().get(mParamCurrentStudentIndex);
        loadImage(student.getHeadPortrait(),iv_student_face);
        txt_student_name.setText(student.getStudentName());
        txt_student_birthday.setText(student.getStudentName());
        txt_student_school.setText(student.getSchoolName());
        txt_student_gradle.setText(student.getSchoolGradeName()+","+student.getSchoolClassName());
        txt_student_Guardian.setText(student.getCustody());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_DATE_PICKER:
                if (data != null) {
                    ArrayList<Integer> list = data.getIntegerArrayListExtra(DatePickerWindow.RESULT_DATE_DETAIL_LIST);
                    if (list != null && list.size() >= 3) {
                        int selectedDate[] = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            selectedDate[i] = list.get(i);
                        }
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(Calendar.YEAR,selectedDate[0]);
                        calendar.set(Calendar.MONTH,(selectedDate[1]));
                        calendar.set(Calendar.DAY_OF_MONTH,(selectedDate[2]));
                        currentDate=DateUtil.format(calendar.getTime(),DateUtil.P1);
                        txt_date.setText(currentDate);
                        getRecodeByDate(student.getStudentId(),currentDate);
                    }
                }
                break;
            case REQUEST_TO_SELECT_STUDENT:
                if (data != null) {
                    int position = data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1);
                    if (position >= 0) {
                        mParamCurrentStudentIndex=position;
                        changeCurrentStudent(position);
                        getRecodeByDate(student.getStudentId(),currentDate);
                    }
                }
                break;
        }
    }
}
