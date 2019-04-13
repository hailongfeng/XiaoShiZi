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

package edu.children.xiaoshizi.bean;

import zuo.biao.library.base.BaseModel;

/**用户类
 * @author Lemon
 */
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;

	public static final int SEX_MAIL = 0;
	public static final int SEX_FEMALE = 1;
	public static final int SEX_UNKNOWN = 2;

	private String sex; //性别
	private String head; //头像
	private String name; //名字
	private String phone; //电话号码
	private String tag; //标签
	private boolean starred; //星标

	private String userId;
	private String loginName;
	private String userName;
	private String token;
	private String headPortrait;
	private String workingAddress;
	private String homeAddress;
	private String email;
	private String cardNum;
	private String verifiedStatus; //是否实名认证。1 是 0不是
	private String firstGuardianStatus; //是否第一联系人。1 是，0不是



	/**默认构造方法，JSON等解析时必须要有
	 */
	public User() {
		//default1
	}
	public User(long id) {
		this();
		this.id = id;
	}
	public User(long id, String name) {
		this(id);
		this.name = name;
	}


	public String getSex() {
		return sex;
	}
	public String getSexName() {
		String tsex="未知";
		if (sex!=null){
			if (sex.equals("M")){
				tsex="男";
			}else if (sex.equals("F")){
				tsex="女";
			}
		}
		return tsex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public boolean getStarred() {
		return starred;
	}
	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	protected boolean isCorrect() {//根据自己的需求决定，也可以直接 return true
		return id > 0;// && StringUtil.isNotEmpty(phone, true);
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getWorkingAddress() {
		return workingAddress;
	}

	public void setWorkingAddress(String workingAddress) {
		this.workingAddress = workingAddress;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getVerifiedStatus() {
		return verifiedStatus;
	}

	public void setVerifiedStatus(String verifiedStatus) {
		this.verifiedStatus = verifiedStatus;
	}

	public String getFirstGuardianStatus() {
		return firstGuardianStatus;
	}

	public void setFirstGuardianStatus(String firstGuardianStatus) {
		this.firstGuardianStatus = firstGuardianStatus;
	}
}
