package edu.children.xiaoshizi.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import edu.children.xiaoshizi.R;
import zuo.biao.library.base.BaseFragment;


public abstract class XszBaseFragment extends BaseFragment {
    protected RequestOptions glideOptions = new RequestOptions()
            .placeholder(R.drawable.student_face_default)//图片加载出来前，显示的图片
            .fallback( R.drawable.student_face_default) //url为空的时候,显示的图片
            .error(R.drawable.student_face_default);//图片加载失败后，显示的图片

    protected void loadImage(String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .apply(glideOptions)
                .into(imageView);
    }
}
