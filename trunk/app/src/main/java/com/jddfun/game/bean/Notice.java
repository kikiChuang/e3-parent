package com.jddfun.game.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by xuhongliang on 2017/4/8.
 */

public class Notice implements Serializable {
    int id;
    String nickName;
    String remark;
    String time;
    int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemark() {
        if (!TextUtils.isEmpty(remark)) {
            String temp = remark;
            if (temp.contains("<color")) {
                temp = temp.replace("<color", "<font color");
            }
            if (temp.contains("</color>")) {
                temp = temp.replace("</color>", "</font>");
            }
            return temp;
        }
        return remark;

    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
