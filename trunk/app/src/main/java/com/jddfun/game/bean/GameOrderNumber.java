package com.jddfun.game.bean;

/**
 * Created by xuhongliang on 2017/4/10.
 */

public class GameOrderNumber {
    //钱
    int money;
    //业务对象
    int bizTarget;
    //业务类型
    int bizType;

    String str1;
    String str2;
    String num2;
    int nextTime;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

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

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public int getNextTime() {
        return nextTime;
    }

    public void setNextTime(int nextTime) {
        this.nextTime = nextTime;
    }
}
