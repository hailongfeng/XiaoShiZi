package edu.children.xiaoshizi.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.view.MyCacheArticleView;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.Message;
import edu.children.xiaoshizi.db.DbUtils;
import edu.children.xiaoshizi.utils.XszCache;
import zuo.biao.library.base.BaseAdapter;

/**
 * 我的缓存
 */
public class MyCacheListActivity extends XszBaseActivity {


    @BindView(R.id.lvBaseList)
    ListView lvBaseList;
    @BindView(R.id.btn_edit)
    Button btn_edit;
    @BindView(R.id.rl_bottom_op)
    RelativeLayout rl_bottom_op;

    @BindView(R.id.cb_select_all)
    CheckBox cb_select_all;

    @BindView(R.id.btn_delete)
    Button btn_delete;
    BaseAdapter<Article, MyCacheArticleView> myCacheArticleAdapter;
    List<Article> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cache);
    }


    @Override
    public void initView() {
        myCacheArticleAdapter = new BaseAdapter<Article, MyCacheArticleView>(context){
            @Override
            public MyCacheArticleView createView(int viewType, ViewGroup parent) {
                return new MyCacheArticleView(context,parent);
            }
        };
        lvBaseList.setAdapter(myCacheArticleAdapter);
    }

    @Override
    public void initData() {
        data.clear();
        data.addAll( DbUtils.getModelList(Article.class));
        myCacheArticleAdapter.refresh(data);
    }

    @Override
    public void initEvent() {
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        cb_select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Article article:data){
                    article.setShow(true);
                    article.setSelected(isChecked);
                }
                myCacheArticleAdapter.refresh(data);
            }
        });
    }
    private int currentStatus=0;//1 编辑状态
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_edit:
                if (currentStatus==0){
                    currentStatus=1;
                    rl_bottom_op.setVisibility(View.VISIBLE);
                    for (Article article:data){
                        article.setShow(true);
                        article.setSelected(false);
                    }
                    myCacheArticleAdapter.notifyListDataSetChanged();
                    btn_edit.setText("取消");
                }else {
                    currentStatus=0;
                    rl_bottom_op.setVisibility(View.GONE);
                    for (Article article:data){
                        article.setShow(false);
                        article.setSelected(false);
                    }
                    myCacheArticleAdapter.refresh(data);
                    btn_edit.setText("编辑");
                }

                break;
            case R.id.btn_delete:
                String t="";
                Iterator<Article> it=data.iterator();
                while (it.hasNext()){
                    Article article=it.next();
                    if (article.isSelected()) {
                        article.delete();
                        File file = XszCache.getCachedVideoFile(article.getActivityVideoUrl());
                        if (file.exists()) {
                            file.delete();
                        }
                        it.remove();
                    }
                }
                currentStatus=0;
                rl_bottom_op.setVisibility(View.GONE);
                for (Article article:data){
                    article.setShow(false);
                    article.setSelected(false);
                }
                btn_edit.setText("编辑");
                myCacheArticleAdapter.refresh(data);
                showShortToast("删除完成");
                break;
        }
    }
}
