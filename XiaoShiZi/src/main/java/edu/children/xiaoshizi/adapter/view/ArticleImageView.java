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
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.adapter.ArticleAdapter;
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
public class ArticleImageView extends ArticleView implements OnClickListener {
	private static final String TAG = "UserView";



	public ImageView iv_article_pic;
	public TextView txt_article_title;
	public LinearLayout ll_article_pl_and_fx;
	public TextView txt_article_pinglun;
	public TextView txt_article_fenxiang;

	public ArticleImageView(Activity context, ViewGroup parent,int type) {
		super(context, R.layout.list_item_artivle_image_view, parent,type);
	}

	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_article_pic = findView(R.id.iv_article_pic);
		ll_article_pl_and_fx = findView(R.id.ll_article_pl_and_fx);
		txt_article_title = findView(R.id.txt_article_title);
		txt_article_pinglun = findView(R.id.txt_article_pinglun);
		txt_article_fenxiang = findView(R.id.txt_article_fenxiang);
		return super.createView();
	}

	@Override
	public void bindView(Article data_){
		super.bindView(data_ != null ? data_ : new Article());
		loadImage(this.data.getBannerImage(),iv_article_pic);
		txt_article_title.setText(this.data.getTitle());
		if (this.type== ArticleAdapter.Type_Lab_Article){
			ll_article_pl_and_fx.setVisibility(View.GONE);
		}else {
			ll_article_pl_and_fx.setVisibility(View.VISIBLE);
		}

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivUserViewHead:
//			toActivity(WebViewActivity.createIntent(context, data.getName(), data.getHead()));
			break;
		default:
			break;
		}
	}
}