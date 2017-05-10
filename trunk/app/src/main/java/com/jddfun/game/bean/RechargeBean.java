package com.jddfun.game.bean;

import java.io.Serializable;

/**
 * Created by MACHINE on 2017/4/8.
 */

public class RechargeBean implements Serializable {


    /**
     * amount : 318888
     * business : 金叶子发放
     * status : 已到账
     * time : 2017.03.21 13:09
     */

    float thirdAmount;
    String status;
    String createTime;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getThirdAmount() {
        return thirdAmount;
    }

    public void setThirdAmount(float thirdAmount) {
        this.thirdAmount = thirdAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
