package com.jddfun.game.bean;

/**
 * Created by xuhongliang on 2017/4/11.
 */

public class OrderPackage {
    String appId;
    String nonceStr;
    String package_app;
    String partnerId;
    String prepayId;
    String sign;
    String timeStamp;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackage_app() {
        return package_app;
    }

    public void setPackage_app(String package_app) {
        this.package_app = package_app;
    }
}
