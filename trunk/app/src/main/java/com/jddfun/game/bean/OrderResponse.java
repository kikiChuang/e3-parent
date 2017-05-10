package com.jddfun.game.bean;

import android.text.TextUtils;

/**
 * Created by xuhongliang on 2017/4/11.
 */

public class OrderResponse {

    //微信专用
    String mch_id;
    OrderPackage package_json;

    String prepay_id;
    String sign;
    String trade_type;


    //威富通支付宝用
    String token_id;
    String orderNum;
    String services;


    //小贝支付专用
    String payType;
    String orderId;
    String price;
    // String sign;
    String notifyUrl;
    String orderName;
    String publicKey;


    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public OrderPackage getPackage_json() {
        return package_json;
    }

    public void setPackage_json(OrderPackage package_json) {
        this.package_json = package_json;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getOrderNum() {
        if (TextUtils.isEmpty(orderNum)) {
            return orderId;
        }
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
