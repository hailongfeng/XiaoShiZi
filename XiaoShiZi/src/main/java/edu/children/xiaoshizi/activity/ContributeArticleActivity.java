package edu.children.xiaoshizi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.DemoApplication;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.EventBusMessage;
import edu.children.xiaoshizi.bean.User;
import edu.children.xiaoshizi.logic.APIMethod;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

/**
 * 投稿界面
 */
public class ContributeArticleActivity extends BaseTakePhotoActivity {
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.edt_suggestion_title)
    EditText edt_suggestion_title;
    @BindView(R.id.edt_suggestion_content)
    EditText edt_suggestion_content;
    @BindView(R.id.rg_home_or_school)
    RadioGroup rg_home_or_school;
    @BindView(R.id.iv_credentials_video_pic)
    ImageView iv_credentials_video_pic;
    private String headPortrait="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute_article);
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvent() {
        findView(R.id.btn_user_add_credentials_zhengmian,this);
        btn_sure.setOnClickListener(this);
        iv_credentials_video_pic.setOnClickListener(this);
    }

    private void submit(){
        TreeMap sm = new TreeMap<String,String>();
        if (StringUtil.isEmpty(edt_suggestion_title,true)) {
            showShortToast("投稿标题不能为空");
            return;
        }
        sm.put("title",edt_suggestion_title.getText().toString());
//        投稿类型。P家长投稿，S学校投稿
        boolean isJiaZhang=((RadioButton)rg_home_or_school.getChildAt(0)).isChecked();
        sm.put("type", isJiaZhang?"P":"S");
        sm.put("introduce",edt_suggestion_content.getText().toString());
        showLoading(R.string.msg_handing);
        LogicService.post(context, APIMethod.submitDraftContent, sm, new ApiSubscriber<Response<User>>() {
            @Override
            public void onSuccess(Response<User> respon) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_credentials_video_pic:
            case R.id.btn_user_add_credentials_zhengmian:
                takePicture();
                break;
            case R.id.btn_sure:
                submit();
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if(test.exists()){
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
                    loadImage(headPortrait,iv_credentials_video_pic);
                }

                @Override
                protected void onFail(Throwable  error) {
                    showShortToast(error.getMessage());
                    error.printStackTrace();
                }
            });
        }
    }
}
