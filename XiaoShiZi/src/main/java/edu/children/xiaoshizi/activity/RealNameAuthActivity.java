package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.flyco.roundview.RoundTextView;
import com.zhaoshuang.weixinrecorded.RecordedActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.RealNameAuthInfo;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.NetErrorException;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;

public class RealNameAuthActivity extends XszBaseActivity  implements View.OnClickListener, ItemDialog.OnDialogItemClickListener {


    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.txt_user_credentials_type)
    TextView txt_user_credentials_type;
    @BindView(R.id.edt_user_credentials_num)
    EditText edt_user_credentials_num;


    @BindView(R.id.iv_credentials_video_pic)
    ImageView iv_credentials_video_pic;
    @BindView(R.id.rtv_credentials_zhengmian)
    RoundTextView rtv_credentials_zhengmian;



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
        findView(R.id.btn_user_bind,this);
    }
    private static String[] TOPBAR_SCHOOL_NAMES={"身份证","护照"} ;
    private static final int DIALOG_SET_CREDENTIAL = 1;
    private static int currentCredentialsType = 1;
    private static int currentPic = 1;

    private String video_url ="";
    private String video_pic_url ="";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_credentials_type:
                new ItemDialog(context, TOPBAR_SCHOOL_NAMES, "证件类型", DIALOG_SET_CREDENTIAL, this).show();
                break;
            case R.id.btn_user_add_credentials_zhengmian:
                //正面照
                currentPic=1;
                Intent intent = new Intent(this, RecordedActivity.class);
                startActivityForResult(intent, 0);
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
        sm.put("cardProsImagesUrl", "");
        sm.put("cardConsImagesUrl", "");
        sm.put("verifiedVideoUrl",video_url);
        sm.put("verifiedVideoPosterUrl",video_pic_url);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            String imagePath = data.getStringExtra("imagePath");
            String videoPath = data.getStringExtra("videoPath");
            if(!TextUtils.isEmpty(videoPath)){
                File file= new File(videoPath);
                Log.d(TAG,file.getAbsolutePath());
                TreeMap sm = new TreeMap<String,String>();
                sm.put("width","150");
                sm.put("height","100");
                sm.put("fileName",file.getName());
                List<File> files =new ArrayList<>();
                files.add(file);
                showLoading("正在上传视频");
                LogicService.uploadVerifiedVideo(context,sm,files, new ApiSubscriber<Response<JSONArray>>() {
                    @Override
                    public void onSuccess(Response<JSONArray> respon) {
                        hideLoading();
                        JSONArray jsonArray=respon.getResult();
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        Log.d(TAG,jsonObject.getString("objectUrl"));
                        Log.d(TAG,jsonObject.getString("verifiedVideoUrl"));
                        Log.d(TAG,jsonObject.getString("verifiedVideoPosterUrl"));
                        Log.d(TAG,jsonObject.getString("fileName"));
                        video_url =jsonObject.getString("verifiedVideoUrl");
                        video_pic_url =jsonObject.getString("verifiedVideoPosterUrl");
                        loadImage(video_url,iv_credentials_video_pic);
                    }

                    @Override
                    protected void onFail(NetErrorException error) {
                        hideLoading();
                        error.printStackTrace();
                        showShortToast(error.getMessage());
                    }
                });
            }else {
                showShortToast("录制失败，请重新录制");
            }
        }
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        if (position < 0) {
            return;
        }
        switch (requestCode) {
            case DIALOG_SET_CREDENTIAL:
                currentCredentialsType = position;
                String guangxi = TOPBAR_SCHOOL_NAMES[position];
                txt_user_credentials_type.setText(guangxi);
                break;
        }
    }
}
