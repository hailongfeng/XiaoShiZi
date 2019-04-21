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

package edu.children.xiaoshizi.adapter.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import edu.children.xiaoshizi.R;
import edu.children.xiaoshizi.activity.ninepic.DefaultItemTouchHelper;

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
public class NinePicAddView extends XszBaseView<String> implements View.OnClickListener {
	public NinePicAddView(Activity context, ViewGroup parent) {
		super(context, R.layout.view_review_add_img, parent);
	}

	ImageView ivAdd;
	ImageView ivImg,ivDel;
	FrameLayout frameLayoutImgs;
	int mPosition;

	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		ivAdd = (ImageView)itemView.findViewById(R.id.ivAdd);
		ivDel = (ImageView)itemView.findViewById(R.id.ivDel);
		ivImg = (ImageView)itemView.findViewById(R.id.ivImg);
		frameLayoutImgs = (FrameLayout)itemView.findViewById(R.id.frameLayoutImgs);
		return super.createView();
	}

	@Override
	public void bindView(String data_){
		super.bindView(data_ != null ? data_ : new String());
		isShowAdd(data_);
		mPosition = position;
		ivDel.setOnClickListener(this);
		ivAdd.setOnClickListener(this);
		refreTxt(data_);
	}

	void refreTxt(String url){
		ivImg.setImageResource(android.R.color.transparent);
		Glide.with(context).load(url).into(ivImg);
	}

	void isShowAdd(String txt){
		if(context.getResources().getString(R.string.cp_open_review_add).equals(txt)){
			ivImg.setVisibility(View.GONE);
			ivAdd.setVisibility(View.VISIBLE);
			frameLayoutImgs.setVisibility(View.GONE);
		}else{
			ivImg.setVisibility(View.VISIBLE);
			ivAdd.setVisibility(View.GONE);
			frameLayoutImgs.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ivAdd:
				super.onViewClickListener.onViewClick(this,v);
				break;
			case R.id.ivDel:
				super.onViewClickListener.onViewClick(this,v);
				break;
		}
	}
}