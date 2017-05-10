package com.jddfun.game.bean;

import java.io.Serializable;

/**
 * Created by MACHINE on 2017/4/8.
 */

public class GetReceiverInfoBean implements Serializable {


    /**
     * receiverAddress : 苏州市
     * receiverFlag : true
     * receiverMobile : 15195656311
     * receiverName : 收货人名称
     */

    String receiverAddress;
    boolean receiverFlag;
    String receiverMobile;
    String receiverName;

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public void setReceiverFlag(boolean receiverFlag) {
        this.receiverFlag = receiverFlag;
    }

    public boolean isReceiverFlag() {
        return receiverFlag;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
