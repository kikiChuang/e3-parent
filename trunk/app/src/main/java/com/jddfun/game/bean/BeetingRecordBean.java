package com.jddfun.game.bean;

import java.io.Serializable;

/**
 * Created by MACHINE on 2017/4/8.
 */

public class BeetingRecordBean implements Serializable {

    /**
     * changeMoney : 7521
     * createTime : 2017年03月21日 00:01
     * name : 平台
     */

    int changeMoney;
    String createTime;
    String name;

    public int getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(int changeMoney) {
        this.changeMoney = changeMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
