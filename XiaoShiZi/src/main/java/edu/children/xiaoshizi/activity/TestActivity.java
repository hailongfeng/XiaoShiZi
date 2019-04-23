package edu.children.xiaoshizi.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;

import org.devio.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.ArticleCommentView;
import edu.children.xiaoshizi.bean.ArticleComment;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;

public class TestActivity extends BaseTakePhotoActivity {

    @BindView(R.id.btn_test)
    Button btn_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvent() {
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        print("pic size:"+result.getImages().size());
    }
}
