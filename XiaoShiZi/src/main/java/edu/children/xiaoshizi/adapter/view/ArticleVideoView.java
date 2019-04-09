/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package edu.children.xiaoshizi.adapter.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import zuo.biao.library.base.BaseModel;

/**用户View
 * @author Lemon
 * @use
 * <br> UserView userView = new UserView(context, resources);
 * <br> adapter中使用:[具体参考.BaseViewAdapter(getView使用自定义View的写法)]
 * <br> convertView = userView.createView(inflater);
 * <br> userView.bindView(position, data);
 * <br> 或  其它类中使用:
 * <br> containerView.addView(userView.createView(inflater));
 * <br> userView.bindView(data);
 * <br> 然后
 * <br> userView.setOnDataChangedListener(onDataChangedListener);data = userView.getData();//非必需
 * <br> userView.setOnClickListener(onClickListener);//非必需
 */
public class ArticleVideoView extends ArticleView implements OnClickListener {
	private static final String TAG = "UserView";


	public ImageView iv_video_pic;
	public TextView txt_video_title;
	public TextView txt_video_play_count;
	public TextView txt_video_time;


	public ArticleVideoView(Activity context, ViewGroup parent,int type) {
		super(context, R.layout.list_item_artivle_video_view, parent,type);
	}


	@SuppressLint("InflateParams")
	@Override
	public View createView() {

		iv_video_pic = findView(R.id.iv_video_pic);
		txt_video_title = findView(R.id.txt_video_title);
		txt_video_play_count = findView(R.id.txt_video_play_count);
		txt_video_time = findView(R.id.txt_video_time);
		return super.createView();
	}

	@Override
	public void bindView(Article data_){
		super.bindView(data_ != null ? data_ : new Article());

		loadImage(this.data.getActivityVideoImageUrl(),iv_video_pic);
		txt_video_title.setText(this.data.getTitle());
	}


	@Override
	public void onClick(View v) {
		if (BaseModel.isCorrect(data) == false) {
			return;
		}
		switch (v.getId()) {
		case R.id.ivUserViewHead:
//			toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
			break;
		default:
			break;
		}
	}
}