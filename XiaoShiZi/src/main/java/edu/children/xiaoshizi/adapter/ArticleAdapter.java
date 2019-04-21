/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this imageFile except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package edu.children.xiaoshizi.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.structure.BaseModel;

import edu.children.xiaoshizi.adapter.view.ArticleImageView;
import edu.children.xiaoshizi.adapter.view.ArticleVideoView;
import edu.children.xiaoshizi.adapter.view.ArticleView;
import edu.children.xiaoshizi.bean.Article;
import zuo.biao.library.base.BaseAdapter;

/**用户adapter
 * @author Lemon
 */
public class ArticleAdapter extends BaseAdapter<Article, ArticleView> {

	private int type;
	public static final int Type_Shoye_Article=1;
	public static final int Type_Class_Article=2;
	public static final int Type_Lab_Article=3;

	public ArticleAdapter(Activity context,int type) {
		super(context);
		this.type=type;
	}

	@Override
	public int getItemViewType(int position) {
//		内容类型。IT 图文，VT 视频
		String type=getItem(position).getContentType();
		if (type.equals("IT")){
			return 1;
		}else {
			return 2;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public ArticleView createView(int viewType, ViewGroup parent) {
		if (viewType==1){
			return new ArticleImageView(context, parent,this.type);
		}else {
			return new ArticleVideoView(context, parent,this.type);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}