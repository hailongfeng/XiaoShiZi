package edu.children.xiaoshizi.bean;

import com.raizlabs.android.dbflow.structure.BaseModel;

//积分
public class Integration extends BaseModel {
    private String remark;
    private String time;
    private int jifen;

    public Integration() {
    }

    public Integration(String remark, String time, int jifen) {
        this.remark = remark;
        this.time = time;
        this.jifen = jifen;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }
}
