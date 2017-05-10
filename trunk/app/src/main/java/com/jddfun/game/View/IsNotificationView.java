package com.jddfun.game.View;

/**
 * Created by MACHINE on 2017/4/10.
 */

public interface IsNotificationView extends BaseView {
    int[] getState();

    void setState(int[] states);

    void setClose(int index);

}



