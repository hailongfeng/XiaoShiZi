package edu.children.xiaoshizi.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gyf.barlibrary.ImmersionBar;
import com.makeramen.roundedimageview.RoundedImageView;

import org.devio.takephoto.model.TResult;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.SizeFilterWithTextAndLetter;
import edu.children.xiaoshizi.utils.StringUtils;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class ChangeUserInfoActivity extends BaseTakePhotoActivity  implements ItemDialog.OnDialogItemClickListener {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.iv_user_face)
    RoundedImageView iv_user_face;
    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.rg_user_sex)
    RadioGroup rg_user_sex;
    @BindView(R.id.edt_user_dizhi)
    EditText edt_user_dizhi;
    @BindView(R.id.edt_user_email)
    EditText edt_user_email;
    @BindView(R.id.edt_user_mobile)
    EditText edt_user_mobile;
    @BindView(R.id.edt_user_area)
    EditText edt_user_area;
    @BindView(R.id.edt_user_work_adddress)
    EditText edt_user_work_adddress;
    @BindView(R.id.edt_user_home_adddress)
    EditText edt_user_home_adddress;
    private String headPortrait="";
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=DemoApplication.getInstance().getUser();
        setContentView(R.layout.activity_change_user_info);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
                .init();
        edt_user_name.setFilters(new InputFilter[]{new SizeFilterWithTextAndLetter(8,4)});
    }

    @Override
    public void initData() {
        headPortrait=user.getHeadPortrait();
        edt_user_name.setText(user.getUserName());
        loadImage(user.getHeadPortrait(),iv_user_face);
        edt_user_home_adddress.setText(user.getHomeAddress());
        edt_user_dizhi.setText(user.getHomeAddress());
        edt_user_work_adddress.setText(user.getWorkingAddress());
        edt_user_email.setText(user.getEmail());
//        性别.U未知 F女 M男
        String sex=user.getSex()==null?"U":user.getSex();
        if (sex.equalsIgnoreCase("U")){
            ((RadioButton)rg_user_sex.getChildAt(0)).setChecked(false);
            ((RadioButton)rg_user_sex.getChildAt(1)).setChecked(false);
        }else if (sex.equalsIgnoreCase("M")){
            ((RadioButton)rg_user_sex.getChildAt(0)).setChecked(true);
            ((RadioButton)rg_user_sex.getChildAt(1)).setChecked(false);
        }else if (sex.equalsIgnoreCase("F")){
            ((RadioButton)rg_user_sex.getChildAt(0)).setChecked(false);
            ((RadioButton)rg_user_sex.getChildAt(1)).setChecked(true);
        }

    }


    @Override
    public void initEvent() {
        findView(R.id.ll_user_face).setOnClickListener(this);
        findView(R.id.btn_change_user).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_face:
                takePicture();
                break;
            case R.id.btn_change_user:

                saveMyProfile();
                break;
        }
    }

    private void saveMyProfile() {
        TreeMap sm = new TreeMap<String,String>();
        if (StringUtil.isEmpty(edt_user_name,true)) {
            showShortToast("姓名不能为空");
            return;
        }
        sm.put("userName",edt_user_name.getText().toString());
        sm.put("email",edt_user_email.getText().toString());
        boolean isMan=((RadioButton)rg_user_sex.getChildAt(0)).isChecked();
        boolean isWoMan=((RadioButton)rg_user_sex.getChildAt(1)).isChecked();
        if (isMan){
            sm.put("sex", "M");
        }else  if (isWoMan){
            sm.put("sex", "F");
        }else {
            sm.put("sex", "U");
        }

        sm.put("mobile",edt_user_mobile.getText().toString());
        sm.put("area",edt_user_area.getText().toString());
        sm.put("workingAddress",edt_user_work_adddress.getText().toString());
        sm.put("homeAddress",edt_user_home_adddress.getText().toString());
        sm.put("headPortrait",headPortrait);
        showLoading(R.string.msg_handing);
        LogicService.post(context, APIMethod.saveMyProfile, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> respon) {
                hideLoading();
               User newUser=  respon.getResult();
               User user= DemoApplication.getInstance().getUser();
               user.setUserName(newUser.getUserName());
               user.setWorkingAddress(newUser.getWorkingAddress());
               user.setHomeAddress(newUser.getHomeAddress());
               user.setEmail(newUser.getEmail());
               user.setHeadPortrait(newUser.getHeadPortrait());
                user.setSex(newUser.getSex());
               DemoApplication.getInstance().getLoginRespon().setLoginResp(user);
                EventBus.getDefault().post(new EventBusMessage<String>(EventBusMessage.Type_User_info_change,"用户信息更改",""));
               showShortToast(respon.getMessage());
               finish();
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
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if(!StringUtils.isEmpty(originalFilePath)){
            TreeMap sm = new TreeMap<String,String>();
            sm.put("width","80");
            sm.put("height","80");
            Map<String,File> files =new HashMap<>();
            files.put("picfile",new File(originalFilePath));
            loadImage(originalFilePath,iv_user_face);
            LogicService.post(context,APIMethod.uploadFile,sm,files,null, new ApiSubscriber<Response<JSONArray>>() {
                @Override
                public void onSuccess(Response<JSONArray> respon) {
                    JSONArray jsonArray=respon.getResult();
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    Log.d(TAG,jsonObject.getString("objectUrl"));
                    Log.d(TAG,jsonObject.getString("objectUrlWithStyle"));
                    Log.d(TAG,jsonObject.getString("fileName"));
                    headPortrait=jsonObject.getString("objectUrlWithStyle");
                }

                @Override
                protected void onFail(Throwable  error) {
                    showShortToast(error.getMessage());
                    error.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        if (position < 0) {
            return;
        }
        switch (requestCode) {
            case 1:
                break;
            default:
                break;
        }
    }
}
