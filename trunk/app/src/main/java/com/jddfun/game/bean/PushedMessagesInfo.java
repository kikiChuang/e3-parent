package com.jddfun.game.bean;

/**
 * Created by MACHINE on 2017/4/10.
 */

public class PushedMessagesInfo {
    int page;
    int pageSize;
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
