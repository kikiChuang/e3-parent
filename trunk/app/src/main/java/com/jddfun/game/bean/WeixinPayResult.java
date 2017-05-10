package com.jddfun.game.bean;

import java.io.Serializable;

public class WeixinPayResult implements Serializable {

    /**
     * code : 200
     * data : {"prepay_url":"https://api.zwxpay.com/pay/jspay?ret=1&prepay_id=407afe2f9c84688df4ba17f5819a8f19","trade_type":"trade.weixin.h5pay","orderNum":"ORDER_2017032900000226"}
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
         * prepay_url : https://api.zwxpay.com/pay/jspay?ret=1&prepay_id=407afe2f9c84688df4ba17f5819a8f19
         * trade_type : trade.weixin.h5pay
         * orderNum : ORDER_2017032900000226
         */

        String prepay_url;
        String trade_type;
        String orderNum;

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

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }
    }
}
