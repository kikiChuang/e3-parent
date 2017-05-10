package com.jddfun.game.event;

/**
 * Created by xuhongliang on 2017/4/7.
 */

public class JDDEvent {
    //领取福利成功
    public final static int TYPE_WEAL_SUCCESS = 1;
    //签到成功
    public final static int TYPE_SIGN_SUCCESS = 2;
    //修改地址成功
    public final static int TYPE_MODIFY_ADDRESS_SUCCESS = 3;
    //登录状态改变
    public final static int TYPE_NOTFIY_LOGIN_STATE_CHANGED = 4;

    //支付成功
    public final static int TYPE_NOTFIY_PAY_SUCCESS = 5;
    //支付失败
    public final static int TYPE_NOTFIY_PAY_FAIL = 6;

    //游戏返回
    public final static int TYPE_GAME_BACK = 7;

    //微信登录
    public final static int TYPE_WX_LOGIN = 8;

    //注册后登录类型
    public final static int TYPE_REGISIT_LOGIN = 9;

    //注册后登录关闭注册界面
//    public final static int TYPE_REGISIT_LOGIN_CLOSE = 10;

    //消息红点
    public final static int TYPE_MESSAGE_COUNT = 11;

    //绑定手机成功
    public final static int TYPE_BIND_PHONE_SUCCESS = 12;

    //擦除成功
    public final static int TYPE_CLEAR_SUCCESS = 13;

    //监听登录界面未登录
    public final static int TYPE_NOTFIY_LOGIN_STATE_CHANGED_NO = 14;
    //发布分享成功
    public final static int TYPE_NOTFIY_PUBLIC_SHARE_SUCCESS = 15;
    public final static int TYPE_REFRESH_ARTICLE = 16;

    //碎片合成成功
    public final static int TYPE_NOTIFY_COMBINE_SUCCESS = 17;

    //登录成功后关闭登录方式界面
    public final static int TYPE_AFTER_LOGIN = 18;

    //领取成功
    int type = -1;

    //订单号
    private String orderNum;

    //微信登录专用
    public String code;


    //注册返回专用
    private String phone;
    private String pwd;


    //合成碎片专用
    private int position;


    public JDDEvent(int type) {
        this.type = type;
    }

    public JDDEvent() {
    }

    public int getType() {
        return type;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
