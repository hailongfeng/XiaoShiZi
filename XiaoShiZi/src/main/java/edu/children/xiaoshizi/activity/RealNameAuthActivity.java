package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.RealNameAuthInfo;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;

public class RealNameAuthActivity extends BaseTakePhotoActivity  implements View.OnClickListener, ItemDialog.OnDialogItemClickListener {


    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.txt_user_credentials_type)
    TextView txt_user_credentials_type;
    @BindView(R.id.edt_user_credentials_num)
    EditText edt_user_credentials_num;


    @BindView(R.id.iv_credentials_zhengmian)
    ImageView iv_credentials_zhengmian;
    @BindView(R.id.iv_credentials_fanmian)
    ImageView iv_credentials_fanmian;
    @BindView(R.id.rtv_credentials_zhengmian)
    RoundTextView rtv_credentials_zhengmian;
    @BindView(R.id.rtv_credentials_fanmian)
    RoundTextView rtv_credentials_fanmian;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_auth);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
//        findView(R.id.ll_user_name,this);
        findView(R.id.ll_user_credentials_type,this);
//        findView(R.id.ll_user_credentials_num,this);
        findView(R.id.btn_user_add_credentials_zhengmian,this);
        findView(R.id.btn_user_add_credentials_fanmian,this);
        findView(R.id.btn_user_bind,this);
    }
    private static String[] TOPBAR_SCHOOL_NAMES={"身份证","护照"} ;
    private static final int DIALOG_SET_CREDENTIAL = 1;
    private static int currentCredentialsType = 1;
    private static int currentPic = 1;

    private String pic_zhengmian_url="";
    private String pic_fanmian_url="";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_credentials_type:
                new ItemDialog(context, TOPBAR_SCHOOL_NAMES, "证件类型", DIALOG_SET_CREDENTIAL, this).show();
                break;
            case R.id.btn_user_add_credentials_zhengmian:
                //正面照
                currentPic=1;
                takePicture();
                break;
            case R.id.btn_user_add_credentials_fanmian:
                //反面照
                currentPic=2;
                takePicture();
                break;
            case R.id.btn_user_bind:
                authInfo();
                break;
        }
    }
    private void authInfo() {
        TreeMap sm = new TreeMap<String,String>();
        sm.put("userName",edt_user_name.getText().toString());
        sm.put("cardType",currentCredentialsType);
        sm.put("cardNum",edt_user_credentials_num.getText().toString());
        sm.put("cardProsImagesUrl",pic_zhengmian_url);
        sm.put("cardConsImagesUrl",pic_fanmian_url);
        sm.put("verifiedVideoUrl","");
        sm.put("verifiedVideoPosterUrl","");
        LogicService.post(context, APIMethod.verifiedSubmit, sm, new ApiSubscriber<Response<RealNameAuthInfo>>() {
            @Override
            protected void onSuccess(Response<RealNameAuthInfo> Response) {
                showShortToast("认证成功成功");
                toActivity(new Intent(context,BindingStudentActivity.class));
                finish();
            }

            @Override
            protected void onFail(NetErrorException error) {
                error.printStackTrace();
                showShortToast("认证失败："+error.getMessage());
            }
        });
    }
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if(test.exists()){
            TreeMap sm = new TreeMap<String,String>();
            sm.put("width","150");
            sm.put("height","100");
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
                    Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getOriginalPath());
                    if (currentPic==1){
                        iv_credentials_zhengmian.setImageBitmap(bitmap);
                        rtv_credentials_zhengmian.setVisibility(View.GONE);
                        pic_zhengmian_url=jsonObject.getString("objectUrlWithStyle");
                    }else {
                        iv_credentials_fanmian.setImageBitmap(bitmap);
                        rtv_credentials_fanmian.setVisibility(View.GONE);
                        pic_fanmian_url=jsonObject.getString("objectUrlWithStyle");
                    }
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
            case DIALOG_SET_CREDENTIAL:
                //setTintColor(position);
                currentCredentialsType = position;
                String guangxi = TOPBAR_SCHOOL_NAMES[position];
                txt_user_credentials_type.setText(guangxi);
                break;
        }
    }
}
