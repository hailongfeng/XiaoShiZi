package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

public class MyIntegrationResponse extends BaseModel {

    private List<IntegrationRecode> integralDetailList; //积分收支记录
    private String userName ;//用户姓名
    private String headPortrait ;//用户头像
    private String pointRuleMsg ;//积分规则
    private int signedDayNum ;//签到天数
    private String points ;//总积分

    public List<IntegrationRecode> getIntegralDetailList() {
        return integralDetailList;
    }

    public void setIntegralDetailList(List<IntegrationRecode> integralDetailList) {
        this.integralDetailList = integralDetailList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getPointRuleMsg() {
        return pointRuleMsg;
    }

    public void setPointRuleMsg(String pointRuleMsg) {
        this.pointRuleMsg = pointRuleMsg;
    }

    public int getSignedDayNum() {
        return signedDayNum;
    }

    public void setSignedDayNum(int signedDayNum) {
        this.signedDayNum = signedDayNum;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
