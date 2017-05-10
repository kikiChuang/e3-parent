package com.jddfun.game.bean;

import com.jddfun.game.Utils.UtilsTools;

import java.io.Serializable;

/**
 * Created by MACHINE on 2017/4/7.
 */

public class UserPersonalBean implements Serializable {
    String headImg;
    String loginname;
    String nickname;
    boolean nicknameFlag;
    String phone;
    long useAmount;
    int userId;
    int userType;
    String userToken;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isNicknameFlag() {
        return nicknameFlag;
    }

    public void setNicknameFlag(boolean nicknameFlag) {
        this.nicknameFlag = nicknameFlag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(long useAmount) {
        this.useAmount = useAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserToken() {
        return UtilsTools.getInstance().getString("accessToken", "");
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }


}
