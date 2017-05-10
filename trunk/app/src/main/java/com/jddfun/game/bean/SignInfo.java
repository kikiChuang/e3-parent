package com.jddfun.game.bean;

import java.util.ArrayList;

/**
 * Created by xuhongliang on 2017/4/8.
 */

public class SignInfo {

    boolean daySigned;

    ArrayList<String> moneyList;

    int signedDay;

    public boolean isDaySigned() {
        return daySigned;
    }

    public void setDaySigned(boolean daySigned) {
        this.daySigned = daySigned;
    }

    public ArrayList<String> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(ArrayList<String> moneyList) {
        this.moneyList = moneyList;
    }

    public int getSignedDay() {
        return signedDay;
    }

    public void setSignedDay(int signedDay) {
        this.signedDay = signedDay;
    }
}
