package com.jddfun.game.bean;

import java.io.Serializable;

/**
 * Created by MACHINE on 2017/4/8.
 */

public class UserPhoneBindInfoBean implements Serializable {


    /**
     * existsFlag : false
     * phone : 1
     */

    boolean existsFlag;
    String phone;

    public boolean isExistsFlag() {
        return existsFlag;
    }

    public void setExistsFlag(boolean existsFlag) {
        this.existsFlag = existsFlag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
