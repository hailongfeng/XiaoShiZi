package edu.children.xiaoshizi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.FileUtils;
import com.flyco.roundview.RoundTextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.RealNameAuthInfo;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import edu.children.xiaoshizi.utils.Constant;
import edu.children.xiaoshizi.utils.DateUtil;
import edu.children.xiaoshizi.utils.FileProvider7;
import edu.children.xiaoshizi.utils.XszCache;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class RealNameAuthActivity extends XszBaseActivity  implements ItemDialog.OnDialogItemClickListener {


    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.txt_user_credentials_type)
    TextView txt_user_credentials_type;
    @BindView(R.id.edt_user_credentials_num)
    EditText edt_user_credentials_num;


    @BindView(R.id.iv_credentials_video_pic)
    ImageView iv_credentials_video_pic;
    @BindView(R.id.rtv_credentials_video_add)
    RoundTextView rtv_credentials_video_add;



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
        findView(R.id.ll_user_credentials_type,this);
        findView(R.id.btn_user_add_credentials_zhengmian,this);
        findView(R.id.btn_user_bind,this);
    }
    private static String[] TOPBAR_SCHOOL_NAMES={"身份证","护照"} ;
    private static final int DIALOG_SET_CREDENTIAL = 1;
    private static int currentCredentialsType = -1;

    private String video_url ="";
    private String video_pic_url ="";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_credentials_type:
                new ItemDialog(context, TOPBAR_SCHOOL_NAMES, "证件类型", DIALOG_SET_CREDENTIAL, this).show();
                break;
            case R.id.iv_credentials_video_pic:
            case R.id.btn_user_add_credentials_zhengmian:
                //视频拍摄
                takeVideo2();
                break;
            case R.id.btn_user_bind:
                authInfo();
                break;
        }
    }
    private void authInfo() {
        if (StringUtil.isEmpty(edt_user_name,true)){
            showShortToast("姓名不能为空");
            return;
        }
        if (currentCredentialsType==-1){
            showShortToast("请选择证件类型");
            return;
        }
        if (StringUtil.isEmpty(edt_user_credentials_num,true)){
            showShortToast("证件号码不能为空");
            return;
        }
        if (StringUtil.isEmpty(video_url)||StringUtil.isEmpty(video_pic_url)){
            showShortToast("请上传本人正面视频(2秒以上)");
            return;
        }

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
                showShortToast(Response.getMessage());
                DemoApplication.getInstance().getUser().setVerifiedStatus(Response.getResult().getVerifiedStatus());
                toActivity(new Intent(context,BindingStudentActivity.class));
                finish();
            }

            @Override
            protected void onFail(Throwable  error) {
                showShortToast(error.getMessage());
            }
        });
    }

    private File videoFile;

    void takeVideo2(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoFile = createMediaFile(); // create a file to save the video
        Uri fileUri = FileProvider7.getUriForFile(this, videoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high
        startActivityForResult(intent, 1);
    }

    private File createMediaFile(){
        String timeStamp = DateUtil.format(new Date(),DateUtil.P10);
        String imageFileName = "VID_" + timeStamp;
        String suffix = ".mp4";
        File mediaStorageDir=XszCache.getCacheDir(Constant.CACHE_DIR_FILE);
        File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode==1){
                if (!videoFile.exists()){
                    showShortToast("视频拍摄失败，请重试");
                    return;
                }
                uploadVideo();
            }
        }
    }

    private void uploadVideo() {
        TreeMap sm = new TreeMap<String, String>();
        List<File> files = new ArrayList<>();
        files.add(videoFile);
        showLoading("正在上传视频");
        LogicService.uploadVerifiedVideo(context, sm, files, new ApiSubscriber<Response<JSONArray>>() {
            @Override
            public void onSuccess(Response<JSONArray> respon) {
                hideLoading();
                JSONArray jsonArray = respon.getResult();
                Log.d(TAG, jsonArray.toJSONString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                video_url = jsonObject.getString("objectUrl");
                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                video_pic_url = jsonObject1.getString("objectUrl");
                iv_credentials_video_pic.setVisibility(View.VISIBLE);
                rtv_credentials_video_add.setVisibility(View.GONE);
                loadImage(video_pic_url, iv_credentials_video_pic);
                showShortToast(respon.getMessage());
                FileUtils.deleteFile(videoFile);
            }

            @Override
            protected void onFail(Throwable  error) {
                hideLoading();
                showShortToast(error.getMessage());
            }
        });
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
