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

import edu.children.xiaoshizi.adapter.view.InAndOutSchoolRecodeView;
import edu.children.xiaoshizi.adapter.view.UserView;
import edu.children.xiaoshizi.bean.InAndOutSchoolRecode;
import edu.children.xiaoshizi.bean.Student;
import edu.children.xiaoshizi.bean.User;
import zuo.biao.library.base.BaseAdapter;

/**用户adapter
 * @author Lemon
 */
public class InOutSchoolRecodeAdapter extends BaseAdapter<InAndOutSchoolRecode, InAndOutSchoolRecodeView> {
	//	private static final String TAG = "UserAdapter";
	private Student student;
	public InOutSchoolRecodeAdapter(Activity context, Student student) {
		super(context);
		this.student=student;
	}

	@Override
	public InAndOutSchoolRecodeView createView(int position, ViewGroup parent) {
		return new InAndOutSchoolRecodeView(context, parent );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}