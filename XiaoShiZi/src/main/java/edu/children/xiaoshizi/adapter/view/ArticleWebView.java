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

import com.just.agentweb.AgentWeb;

import java.lang.reflect.Field;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.bean.Article;
import edu.children.xiaoshizi.bean.ArticleComment;
import zuo.biao.library.util.Log;


public class ArticleWebView extends XszBaseView<ArticleComment> implements OnClickListener {
	private static final String TAG = "UserView";
	private AgentWeb.PreAgentWeb preAgentWeb;
	private AgentWeb agentWeb;

	public ArticleWebView(Activity context, ViewGroup parent) {
		super(context, R.layout.list_head_webview, parent);
	}

	public LinearLayout linWeb;
	public ImageView iv_delete;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		linWeb = findView(R.id.lin_web, this);
		preAgentWeb= AgentWeb.with(context)
				.setAgentWebParent((LinearLayout) linWeb, new LinearLayout.LayoutParams(-1, -1))
				.useDefaultIndicator()
				.createAgentWeb()
				.ready();
		getAgentWebField();
		return super.createView();
	}

	private void getAgentWebField(){
		Field field = null;
		try {
			field = preAgentWeb.getClass().getDeclaredField("mAgentWeb");
			field.setAccessible(true);
			agentWeb= (AgentWeb) field.get(preAgentWeb);
			Log.d(TAG,(agentWeb==null)+",,,agentWeb==null");
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	private String getHtml(String body){

		StringBuilder sb=new StringBuilder();
		sb.append("<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>").append(body).append("</body></html>");
		return sb.toString();
	}
	@Override
	public void bindView(ArticleComment data_){
		super.bindView(data_ != null ? data_ : new ArticleComment());
		print(this.data.getContentId());
		agentWeb.getUrlLoader().loadDataWithBaseURL(null, getHtml(this.data.getCommentContent()), "text/html", "UTF-8", null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_delete:
			this.onViewClickListener.onViewClick(this,iv_delete);
			break;
		default:
			break;
		}
	}
}