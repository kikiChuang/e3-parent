package com.jddfun.game.bean;

/**
 * Created by xuhongliang on 2017/4/10.
 */

public class YouMengToken {
    //client 1为android
    int clientType = 1;
    String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int client) {
        this.clientType = client;
    }
}
