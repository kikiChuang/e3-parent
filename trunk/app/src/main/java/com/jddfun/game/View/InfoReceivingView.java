package com.jddfun.game.View;

/**
 * Created by MACHINE on 2017/4/6.
 */

public interface InfoReceivingView extends BaseView{
    String getdoorNumber();
    String getdoorName();
    String getdoorPhone();
    void setOkBg(int color);
    void setOkOnclick(boolean isOnclick);

    void setdoorNumber(String number);
    void setdoorName(String doorName);
    void setdoorPhone(String doorPhone);

}
