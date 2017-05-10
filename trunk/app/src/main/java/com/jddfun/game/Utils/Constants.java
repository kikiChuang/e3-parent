package com.jddfun.game.Utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.jddfun.game.JDDApplication;

/**
 * Created by MACHINE on 2017/3/24.
 */

public class Constants {


    public final static int DEV_MODE = 1;
    public final static int TEST_MODE = 2;
    public final static int LINE_MODE = 3;

    public final static int mode = DEV_MODE;

    public static String URL = getUrl();

    public static String getUrl() {
        switch (mode) {
            //开发
            case DEV_MODE:
                return "http://192.168.101.241";
            //测试
            case TEST_MODE:
                return "http://192.168.101.242";
            //线上
            case LINE_MODE:
                return "https://www.jddfun.com";
            default:
                return "http://192.168.101.241";
        }
    }

    //渠道
    public static String Constants_APP_CHANNEL = getChannelName();

    //包名
    public static String PACKAGENAME = "com.jddfun.game";

    //是否开启debug
//    public static boolean isDebug = mode != LINE_MODE;
    public static boolean isDebug = true;
    //    public static boolean isDebug = true;
    //TOKEN保存
    public static String TOKEN = "";
    //接口URL
    public static String BASICURL = URL + "/api_app/";
    //登录专用接口URL
    public static String BASICURL_LOGIN = URL + "/api_platform/";
    //商城url
    public static String SHOP_URL_UNLINE = URL + "/wap/common/#/shopping";
    public static String SHOP_URL = (mode == LINE_MODE ? URL + "/app/#/shopping" : SHOP_URL_UNLINE);
    //抽奖url
    public static String PRIZE_URL = URL + "/ring";
    //帮助中心url

    public static String HELP_CENTER_UNLINE = URL + "/wap/common/#/help/";
    public static String HELP_CENTER = (mode == LINE_MODE ? URL + "/app/#/help/" : HELP_CENTER_UNLINE);

    //用户协议
    public static String USER_AGREEMENT_UNLINE = URL + "/wap/common/#/agreement";
    public static String USER_AGREEMENT = (mode == LINE_MODE ? URL + "/app/#/agreement" : USER_AGREEMENT_UNLINE);
    //分享全详情页


    public static String SHARE_DETAIL_UNLINE = URL + "/wap/common/#/article/";
    public static String SHARE_DETAIL = (mode == LINE_MODE ? URL + "/app/#/article/" : SHARE_DETAIL_UNLINE);

    //碎片说明
    public static String DEBRIS_DES_UNLINE = URL + "/wap/common/#/fragment/description";
    public static String DEBRIS_DES = (mode == LINE_MODE ? URL + "/app/#/fragment/description" : DEBRIS_DES_UNLINE);

    //排行规则
    public static String RANK_DES_UNLINE = URL + "/wap/common/#/ranking";
    public static String RANK_DES = (mode == LINE_MODE ? URL + "/app/#/ranking" : RANK_DES_UNLINE);

    //微信开放平台
    public static String WEIXIN_APPID = "wx454776f1da5f42b6";

    //QQ开放平台
    public static String QQ_APPID = "101395087";

    public static int pageSize = 10;

    public static String FIRST = "FIRST";
    public static String MESSAGE_ALL_NUMBER = "MESSAGE_ALL_NUMBER";
    public static String NOTICECENTRALITY = "NOTICECENTRALITY";


    public static String getChannelName() {
        ApplicationInfo appInfo = null;
        Integer channelName = 0;
        try {
            appInfo = JDDApplication.getAppContext().getPackageManager().getApplicationInfo(JDDApplication.mApp.getPackageName(), PackageManager.GET_META_DATA);
            channelName = appInfo.metaData.getInt("APP_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(channelName);
    }
}
