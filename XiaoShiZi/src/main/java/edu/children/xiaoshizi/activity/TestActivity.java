package edu.children.xiaoshizi.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.ArticleCommentView;
import edu.children.xiaoshizi.bean.ArticleComment;
import zuo.biao.library.base.BaseAdapter;
import zuo.biao.library.base.BaseView;

public class TestActivity extends XszBaseActivity {
    LinearLayout linWeb;
    @BindView(R.id.lvBaseList)
    ListView lvComments;
    List<ArticleComment> comments=new ArrayList<>();
    BaseAdapter commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void initView() {
        linWeb= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_head_webview,null);
        lvComments.addHeaderView(linWeb);
        initComments();
    }

    @Override
    public void initData() {
        List<ArticleComment> comments1=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            ArticleComment articleComment= new ArticleComment();
            articleComment.setContentId(i+"---");
            comments1.add(articleComment);
        }
        updateComments(comments1);
    }

    @Override
    public void initEvent() {

    }

    private void updateComments(List<ArticleComment> comments1) {
//       if (!articleType.getType().equalsIgnoreCase("VT")&&articleType.getBelongTo()==1) {
        this.comments.clear();
        if (comments1 != null) {
            print("comments.size()="+comments1.size());
            this.comments.addAll(comments1);
        }
        commentAdapter.refresh(this.comments);
//       }
    }
    private void initComments(){
        commentAdapter= new BaseAdapter<ArticleComment, ArticleCommentView>(context){
            @Override
            public ArticleCommentView createView(int viewType, ViewGroup parent) {
                return new ArticleCommentView(context,parent);
            }
        };
        commentAdapter.setOnViewClickListener(new BaseView.OnViewClickListener() {
            @Override
            public void onViewClick(@NonNull BaseView bv, @NonNull View v) {
//                data.remove(bv.data);
//                adapter.refresh(data);
//                adapter.notifyDataSetChanged();
            }
        });
        lvComments.setAdapter(commentAdapter);
    }

}
