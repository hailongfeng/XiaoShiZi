package edu.children.xiaoshizi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.CourseListActivity;
import edu.children.xiaoshizi.bean.ArticleType;

/**
 * Created by hackware on 2016/9/10.
 */

public class GradlePagerAdapter extends PagerAdapter {
    protected static RequestOptions glideOptions = new RequestOptions()
            .placeholder(R.drawable.user_default)//图片加载出来前，显示的图片
            .fallback( R.drawable.user_default) //url为空的时候,显示的图片
            .error(R.drawable.user_default);//图片加载失败后，显示的图片

    private List<ArticleType> mDataList;
    private Context context;
    public GradlePagerAdapter(Context context,List<ArticleType> dataList) {
        this.context=context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.fragment_gradle_item,null);
        //在这里可以做相应的操作
        TextView tv= (TextView) view.findViewById(R.id.txt_student_gradle_title);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_student_gradle_pic);
        //数据填充
        tv.setText(mDataList.get(position).getTitle());
        Glide.with(context)
                .load(mDataList.get(position).getBannerImage())
                .apply(glideOptions)
                .into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleType articleType=mDataList.get(position);
                context.startActivity(CourseListActivity.createIntent(context,articleType));
            }
        });
        view.setTag(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        LinearLayout ll = (LinearLayout) object;
        int position= (int) ll.getTag();
        int index = position;
        if (index >= 0) {
            return index;
        }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position).getTitle();
    }
}
