package edu.children.xiaoshizi.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dou361.dialogui.DialogUIUtils;

import butterknife.ButterKnife;
import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.util.Log;


public abstract class XszBaseFragment extends BaseFragment {

    protected String TAG=this.getClass().getSimpleName();

    protected  static RequestOptions glideOptions = new RequestOptions()
//            .placeholder(R.drawable.student_face_default)//图片加载出来前，显示的图片
            .fallback( R.drawable.user_default) //url为空的时候,显示的图片
            .error(R.drawable.user_default);//图片加载失败后，显示的图片

    protected void loadImage(String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imageView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this, view);
        initView();
        initData();
        initEvent();
        return view;
    }

    private Dialog loadingDialog=null;
    protected  void showLoading(String msg){
        loadingDialog=DialogUIUtils.showLoading(context, msg,true,false,false,true).show();
    }
    protected  void hideLoading(){
        DialogUIUtils.dismiss(loadingDialog);
    }

    abstract int getLayoutId();

    protected void print(String msg){
        Log.d(TAG,msg);
    }
}
