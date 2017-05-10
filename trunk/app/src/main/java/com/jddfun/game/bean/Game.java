package com.jddfun.game.bean;

import java.io.Serializable;

/**
 * Created by xuhongliang on 2017/4/12.
 */

public class Game implements Serializable {
    String corner;
    int extraFlag;
    String icon;
    int id;
    String name;
    boolean redFlag;
    String remark;
    int sort;
    int type;
    String url;

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public int getExtraFlag() {
        return extraFlag;
    }

    public void setExtraFlag(int extraFlag) {
        this.extraFlag = extraFlag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRedFlag() {
        return redFlag;
    }

    public void setRedFlag(boolean redFlag) {
        this.redFlag = redFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
