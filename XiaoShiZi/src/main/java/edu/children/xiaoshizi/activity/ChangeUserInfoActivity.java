package edu.children.xiaoshizi.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gyf.barlibrary.ImmersionBar;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;

public class ChangeUserInfoActivity extends BaseTakePhotoActivity  implements View.OnClickListener, ItemDialog.OnDialogItemClickListener {
    private static final int REQUEST_TO_DATE_PICKER = 1;
    @BindView(R.id.iv_user_face)
    ImageView iv_user_face;
    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.rg_user_sex)
    RadioGroup rg_user_sex;
    @BindView(R.id.edt_user_dizhi)
    EditText edt_user_dizhi;
    @BindView(R.id.edt_user_phone)
    EditText edt_user_phone;
    @BindView(R.id.edt_user_email)
    EditText edt_user_email;
    @BindView(R.id.edt_user_work_adddress)
    EditText edt_user_work_adddress;
    @BindView(R.id.edt_user_home_adddress)
    EditText edt_user_home_adddress;
    @BindView(R.id.edt_user_id_cardno)
    EditText edt_user_id_cardno;
    private String headPortrait="https://single-obs.obs.cn-east-2.myhuaweicloud.com:443/app_pic/head_portrait/5gcnrqBYVl1Qngok8uH/2019033111033725425.jpg";
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
    }

    @Override
    public void initData() {
        edt_user_name.setText(user.getUserName());
        edt_user_phone.setText(user.getPhone());
        loadImage(user.getHeadPortrait(),iv_user_face);
        edt_user_home_adddress.setText(user.getHomeAddress());
        edt_user_dizhi.setText(user.getHomeAddress());
        edt_user_work_adddress.setText(user.getWorkingAddress());
        edt_user_email.setText(user.getEmail());
        edt_user_id_cardno.setText(user.getCardNum());
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
        sm.put("userName",edt_user_name.getText().toString());
        sm.put("email",edt_user_email.getText().toString());
        sm.put("workingAddress",edt_user_work_adddress.getText().toString());
        sm.put("homeAddress",edt_user_home_adddress.getText().toString());
        sm.put("headPortrait",headPortrait);
        LogicService.post(context, APIMethod.saveMyProfile, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> respon) {
               User newUser=  respon.getResult();
               User user= DemoApplication.getInstance().getUser();
               user.setUserName(newUser.getUserName());
               user.setWorkingAddress(newUser.getWorkingAddress());
               user.setHomeAddress(newUser.getHomeAddress());
               user.setEmail(newUser.getEmail());
               user.setHeadPortrait(newUser.getHeadPortrait());
               showShortToast("修改成功");
               finish();
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if(test.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getOriginalPath());
            iv_user_face.setImageBitmap(bitmap);

            TreeMap sm = new TreeMap<String,String>();
            sm.put("width","80");
            sm.put("height","80");
            List<File> files =new ArrayList<>();
            files.add(test);
            LogicService.uploadPic(context,sm,files, new ApiSubscriber<Response<JSONArray>>() {
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
                protected void onFail(NetErrorException error) {
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
