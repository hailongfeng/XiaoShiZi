package edu.children.xiaoshizi.adapter.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseView;

public abstract class XszBaseView<T> extends BaseView<T> {
    protected static RequestOptions glideOptions = new RequestOptions()
            .placeholder(R.drawable.student_face_default)//图片加载出来前，显示的图片
            .fallback( R.drawable.student_face_default) //url为空的时候,显示的图片
            .error(R.drawable.student_face_default);//图片加载失败后，显示的图片

    public XszBaseView(Activity context, int layoutResId) {
        super(context, layoutResId);
    }

    public XszBaseView(Activity context, int layoutResId, ViewGroup parent) {
        super(context, layoutResId, parent);
    }

    public XszBaseView(Activity context, View itemView) {
        super(context, itemView);
    }
    protected void loadImage(String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imageView);
    }
}
