package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.structure.BaseModel;

//积分
public class IntegrationRecode extends BaseModel {
//    id	string
//    非必须
//            积分记录id
//    tranTime	string
//    非必须
//            收支记录时间
//    tranPoints	number
//    非必须
//            积分数量
//    tranMsg	string
//    非必须
//            收支名称
    private String id;
    private String tranTime;
    private String tranMsg;
    private int tranPoints;

    public IntegrationRecode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranMsg() {
        return tranMsg;
    }

    public void setTranMsg(String tranMsg) {
        this.tranMsg = tranMsg;
    }

    public int getTranPoints() {
        return tranPoints;
    }

    public void setTranPoints(int tranPoints) {
        this.tranPoints = tranPoints;
    }
}
