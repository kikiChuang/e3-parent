package com.jddfun.game.bean;

/**
 * Created by MACHINE on 2017/4/21.
 */

public class UnreadCountBean {

    /**
     * unRead : 0
     * unReadBySystem : 0
     * unReadByUser : 0
     */

    int unRead;
    int unReadBySystem;
    int unReadByUser;

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public int getUnReadBySystem() {
        return unReadBySystem;
    }

    public void setUnReadBySystem(int unReadBySystem) {
        this.unReadBySystem = unReadBySystem;
    }

    public int getUnReadByUser() {
        return unReadByUser;
    }

    public void setUnReadByUser(int unReadByUser) {
        this.unReadByUser = unReadByUser;
    }
}
