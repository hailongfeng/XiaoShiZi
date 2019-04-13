package edu.children.xiaoshizi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dou361.dialogui.DialogUIUtils;
import com.flyco.roundview.RoundTextView;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.logic.LogicService;
import edu.children.xiaoshizi.net.rxjava.ApiSubscriber;
import edu.children.xiaoshizi.net.rxjava.Response;
import zuo.biao.library.util.Log;

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
    private String headPortrait="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                takePicture();
                break;
            case R.id.btn_rechange:
                takePicture();
                break;
            case R.id.btn_sure:
                DialogUIUtils.showToastCenter("更新成功");
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if(test.exists()){
            TreeMap sm = new TreeMap<String,String>();
            sm.put("width","250");
            sm.put("height","300");
            List<File> files =new ArrayList<>();
            files.add(test);
            LogicService.uploadPic(context,sm,files, new ApiSubscriber<Response<JSONArray>>() {
                @Override
                public void onSuccess(Response<JSONArray> respon) {
                    JSONArray jsonArray=respon.getResult();
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    headPortrait=jsonObject.getString("objectUrlWithStyle");
                    loadImage(headPortrait,iv_student_pic);
                }

                @Override
                protected void onFail(Throwable  error) {
                    showShortToast(error.getMessage());
                    error.printStackTrace();
                }
            });
        }else {
            showShortToast("没有选择照片");
        }
    }
}
