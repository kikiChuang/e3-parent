package com.jddfun.game.bean;

import java.util.List;

/**
 * Created by MACHINE on 2017/4/1.
 */

public class SignedListBean {

    /**
     * code : 200
     * data : {"daySigned":false,"moneyList":["688","488","566","588","666","688","50"],"signedDay":0}
     */

    int code;
    DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * daySigned : false
         * moneyList : ["688","488","566","588","666","688","50"]
         * signedDay : 0
         */

        boolean daySigned;
        int signedDay;
        List<String> moneyList;

        public boolean isDaySigned() {
            return daySigned;
        }

        public void setDaySigned(boolean daySigned) {
            this.daySigned = daySigned;
        }

        public int getSignedDay() {
            return signedDay;
        }

        public void setSignedDay(int signedDay) {
            this.signedDay = signedDay;
        }

        public List<String> getMoneyList() {
            return moneyList;
        }

        public void setMoneyList(List<String> moneyList) {
            this.moneyList = moneyList;
        }
    }
}
