package edu.children.xiaoshizi.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.InOutSchoolRecodeAdapter;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.DateUtil;
import edu.children.xiaoshizi.utils.StringUtils;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.TimeUtil;

public class SafeToolFragment extends XszBaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM_CURRENT_STUDENT = "current_student";

    private int mParamCurrentStudentIndex=0;
    private String mParam2;

    @BindView(R.id.lvBaseList)
    ListView lvBaseList;
    @BindView(R.id.txt_date)
    TextView txt_date;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamCurrentStudentIndex = getArguments().getInt(ARG_PARAM_CURRENT_STUDENT);
        }
        Calendar calendar=Calendar.getInstance();
        selectedDate[0]=calendar.get(Calendar.YEAR);
        selectedDate[1]=calendar.get(Calendar.MONTH);
        selectedDate[2]=calendar.get(Calendar.DAY_OF_MONTH);
        student =DemoApplication.getInstance().getLoginRespon().getStudents().get(mParamCurrentStudentIndex);
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_save_tool;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void initView() {
        inOutSchoolRecodeAdapter= new InOutSchoolRecodeAdapter(context);
        lvBaseList.setAdapter(inOutSchoolRecodeAdapter);
    }

    @Override
    public void initData() {
        String birthday=selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2];
        TreeMap sm = new TreeMap<String,String>();
        sm.put("studentId",student.getStudentId());
        sm.put("snapMsgDate",birthday);
        ;
        LogicService.post(context, APIMethod.findStudentSnapMsg,sm,new ApiSubscriber<Response<List<InAndOutSchoolRecode>>>(){
            @Override
            public void onNext(Response<List<InAndOutSchoolRecode>> response) {
                inOutSchoolRecodeAdapter.refresh(response.getResult());
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
                showShortToast("记录获取失败");
            }
        });
    }

    @Override
    public void initEvent() {
        iv_change_date.setOnClickListener(this);
    }
    private static final int REQUEST_TO_DATE_PICKER = 1;
    private int[] selectedDate = new int[]{1971, 0, 1};
    @Override
    public void onClick(View v) {
        toActivity(DatePickerWindow.createIntent(context, new int[]{1971, 0, 1}
                , TimeUtil.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);
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
                        selectedDate = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            selectedDate[i] = list.get(i);
                        }

//                        showShortToast("选择的日期为" + selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
                        String birthday=selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2];
                        txt_date.setText(birthday);
                        initData();
                    }
                }
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
