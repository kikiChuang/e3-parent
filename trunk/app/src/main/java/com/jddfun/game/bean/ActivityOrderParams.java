package com.jddfun.game.bean;

/**
 * Created by xuhongliang on 2017/4/11.
 */

public class ActivityOrderParams {

    int bizTarget;
    int bizType;
    int payType;
    int source;

    public int getBizTarget() {
        return bizTarget;
    }

    public void setBizTarget(int bizTarget) {
        this.bizTarget = bizTarget;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
