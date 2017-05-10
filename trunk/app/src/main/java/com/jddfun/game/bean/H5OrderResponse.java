package com.jddfun.game.bean;

/**
 * Created by xuhongliang on 2017/4/12.
 */

public class H5OrderResponse {

    String orderNum;
    String prepay_url;
    String trade_type;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPrepay_url() {
        return prepay_url;
    }

    public void setPrepay_url(String prepay_url) {
        this.prepay_url = prepay_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
