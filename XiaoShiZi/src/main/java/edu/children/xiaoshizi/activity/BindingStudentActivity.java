package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dou361.dialogui.DialogUIUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.lzj.pass.dialog.PayPassDialog;
import com.lzj.pass.dialog.PayPassView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.LoginRespon;
import edu.children.xiaoshizi.bean.School;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.ui.EditTextInfoActivity;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.TimeUtil;

public class BindingStudentActivity extends XszBaseActivity  implements ItemDialog.OnDialogItemClickListener {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.edt_student_name)
    EditText edt_student_name;
    @BindView(R.id.rg_student_sex)
    RadioGroup rg_student_sex;
    @BindView(R.id.txt_student_birthday)
    TextView txt_student_birthday;
    @BindView(R.id.txt_student_school)
    TextView txt_student_school;
    @BindView(R.id.txt_student_gradle)
    TextView txt_student_gradle;
    @BindView(R.id.txt_student_banji)
    TextView txt_student_banji;
    @BindView(R.id.txt_student_guanxi)
    TextView txt_student_guanxi;
    @BindView(R.id.edt_bindingPassword)
    EditText edt_bindingPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_student);
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
      schools=  DbUtils.getSchoolByType(1,null);
    }

    @Override
    public void initEvent() {
        findView(R.id.ll_student_name).setOnClickListener(this);
        findView(R.id.ll_student_sex).setOnClickListener(this);
        findView(R.id.ll_student_birthday).setOnClickListener(this);
        findView(R.id.ll_student_school).setOnClickListener(this);
        findView(R.id.ll_student_gradle).setOnClickListener(this);
        findView(R.id.ll_student_banji).setOnClickListener(this);
        findView(R.id.ll_student_guanxi).setOnClickListener(this);
        findView(R.id.ll_student_bangdingmima).setOnClickListener(this);
        findView(R.id.btn_bind_student).setOnClickListener(this);

    }


    private List<School> schools;
    private List<School> gradles;
    private List<School> banjis;
    private static final int DIALOG_SET_GUANXI = 1;
    private static final int DIALOG_SET_SCHOOL = 2;
    private static final int DIALOG_SET_GRADLE = 3;
    private static final int DIALOG_SET_BANJI = 4;
    private static final String[] STUDENT_GUARDIAN_GUANXI = {"爸爸", "妈妈", "爷爷", "奶奶"};
    private static String[] TOPBAR_SCHOOL_NAMES ;
    private static String[] TOPBAR_SCHOOL_GRADLE ;
    private static String[] TOPBAR_SCHOOL_BANJI;

    private int currentSchoolIndex=-1;
    private int currentGradlelIndex=-1;
    private int currentBanJiIndex=-1;
    private int currentGuanXi=-1;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_student_school:
                TOPBAR_SCHOOL_NAMES=new String[schools.size()];
                for (int i = 0; i <schools.size() ; i++) {
                    TOPBAR_SCHOOL_NAMES[i]=schools.get(i).schoolName;
                }
                new ItemDialog(context, TOPBAR_SCHOOL_NAMES, "选择学校", DIALOG_SET_SCHOOL, this).show();
                break;
            case R.id.ll_student_gradle:
                if (currentSchoolIndex<0){
                    showShortToast("先选择学校");
                    return;
                }
                gradles=  DbUtils.getSchoolByType(2,schools.get(currentSchoolIndex).id);
                TOPBAR_SCHOOL_GRADLE=new String[gradles.size()];
                for (int i = 0; i <gradles.size() ; i++) {
                    TOPBAR_SCHOOL_GRADLE[i]=gradles.get(i).schoolName;
                }
                new ItemDialog(context, TOPBAR_SCHOOL_GRADLE, "选择年级", DIALOG_SET_GRADLE, this).show();
                break;
            case R.id.ll_student_banji:
                if (currentSchoolIndex<0){
                    showShortToast("先选择年级");
                    return;
                }
                banjis=  DbUtils.getSchoolByType(3,gradles.get(currentGradlelIndex).id);
                showShortToast(gradles.get(currentGradlelIndex).schoolName);
                TOPBAR_SCHOOL_BANJI =new String[banjis.size()];
                for (int i = 0; i <banjis.size() ; i++) {
                    TOPBAR_SCHOOL_BANJI[i]=banjis.get(i).schoolName;
                }
                new ItemDialog(context, TOPBAR_SCHOOL_BANJI, "选择班级", DIALOG_SET_BANJI, this).show();

                break;
            case R.id.ll_student_guanxi:
                new ItemDialog(context, STUDENT_GUARDIAN_GUANXI, "选择监护人", DIALOG_SET_GUANXI, this).show();
                break;
            case R.id.ll_student_birthday:
                toActivity(DatePickerWindow.createIntent(context, new int[]{1971, 0, 1}
                        , TimeUtil.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);

                break;
            case R.id.btn_bind_student:
//                payDia();
                bindingStudent();
                break;
        }
    }

    private void bindingStudent(){
        TreeMap sm = new TreeMap<String,String>();
        sm.put("parentId", DemoApplication.getInstance().getUser().getUserId());
        sm.put("userName", edt_student_name.getText().toString());
        boolean isMan=((RadioButton)rg_student_sex.getChildAt(0)).isChecked();
        sm.put("sex", isMan?"M":"F");
        sm.put("schoolId", schools.get(currentSchoolIndex).id);
        sm.put("schoolGradeId", gradles.get(currentGradlelIndex).id);
        sm.put("schoolClassId", banjis.get(currentBanJiIndex).id);
        sm.put("birthday", txt_student_birthday.getText().toString());
        sm.put("parentCustody",STUDENT_GUARDIAN_GUANXI[currentGuanXi] );
        sm.put("bindingPassword", edt_bindingPassword.getText().toString());
        LogicService.post(context, APIMethod.studentBinding, sm, new ApiSubscriber<Response<LoginRespon>>() {
            @Override
            public void onSuccess(Response<LoginRespon> response) {
                if (response.getCode()==Response.SUCCESS){
                    showShortToast("绑定成功");
                    LoginRespon loginRespon=response.getResult();
                    DemoApplication.getInstance().getLoginRespon().setParents(loginRespon.getParents());
                    DemoApplication.getInstance().getLoginRespon().setStudents(loginRespon.getStudents());
                    context.finish();
                }else {
                    showShortToast(response.getMessage());
                }
            }

            @Override
            protected void onFail(Throwable  error) {
                showShortToast("绑定失败");
            }
        });
    }

    private void verifyPassword(){
        //TODO:验证绑定密码
        bindingStudent();
    }

    private void payDia() {
        final PayPassDialog dialog=new PayPassDialog(this,R.style.dialog_pay_theme);
        //弹框自定义配置
        dialog.setAlertDialog(false)
                .setWindowSize(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.4f)
                .setOutColse(false)
                .setGravity(R.style.dialogOpenAnimation, Gravity.BOTTOM);
        //组合控件自定义配置
        PayPassView payView=dialog.getPayViewPass();
        payView.setForgetText("输入绑定密码");
        payView.setForgetColor(getResources().getColor(R.color.colorPrimary));
        payView.setForgetSize(16);
        payView.setPayClickListener(new PayPassView.OnPayClickListener() {
            @Override
            public void onPassFinish(String passContent) {
                //6位输入完成回调
                showShortToast(passContent);
            }
            @Override
            public void onPayClose() {
                dialog.dismiss();
                //关闭回调
            }
            @Override
            public void onPayForget() {
                dialog.dismiss();
                //忘记密码回调
            }
        });
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        if (position < 0) {
            return;
        }
        switch (requestCode) {
            case DIALOG_SET_GUANXI:
                //setTintColor(position);
                currentGuanXi=position;
               String guangxi= STUDENT_GUARDIAN_GUANXI[position];
                txt_student_guanxi.setText(guangxi);
                break;
            case DIALOG_SET_SCHOOL:
                if (currentSchoolIndex!=position){
                    currentGradlelIndex=-1;
                    currentBanJiIndex=-1;
                }
                currentSchoolIndex=position;
                txt_student_school.setText(schools.get(position).schoolName);
                break;
            case DIALOG_SET_GRADLE:
                if (currentGradlelIndex!=position){
                    currentBanJiIndex=-1;
                }
                currentGradlelIndex=position;
                txt_student_gradle.setText(gradles.get(position).schoolName);
                break;
            case DIALOG_SET_BANJI:
                currentBanJiIndex=position;
                txt_student_banji.setText(banjis.get(position).schoolName);
                break;
            default:
                break;
        }
    }

    private int[] selectedDate = new int[]{1971, 0, 1};
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

                        selectedDate = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            selectedDate[i] = list.get(i);
                        }

//                        showShortToast("选择的日期为" + selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
                        String birthday=selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2];
                        txt_student_birthday.setText(birthday);
                    }
                }
                break;
        }
    }
}
