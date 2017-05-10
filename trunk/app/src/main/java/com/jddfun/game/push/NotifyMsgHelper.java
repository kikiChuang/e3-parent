package com.jddfun.game.push;

import android.content.Context;

import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.LogUtils;
import com.jddfun.game.Utils.ToastUtils;
import com.jddfun.game.bean.YouMengToken;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by xuhongliang on 2017/4/8.
 */

public class NotifyMsgHelper {

    public static void updateToken(String deviceToken, RxAppCompatActivity rxAppCompatActivity) {
        if (JDDUtils.isLogin()) {
            YouMengToken token = new YouMengToken();
            token.setDeviceToken(deviceToken);
            ServiceFactory.getInstance().createService(JDDApiService.class).postToken(token)
                    .compose(RxUtils.<HttpResult<Object>>defaultSchedulers())
                    .compose(rxAppCompatActivity.<HttpResult<Object>>bindToLifecycle())
                    .subscribe(new HttpResultSubscriber<Object>() {
                        @Override
                        public void onSuccess(Object list) {
                            LogUtils.i("youmeng", "提交友盟token成功");
                        }

                        @Override
                        public void onError(Throwable e, int code) {
                            LogUtils.i("youmeng", "提交友盟token失败,错误码:" + code);
                        }
                    });
        }


    }

    public static void dealYouMengMsg(UmengMessageHandler umengMessageHandler, Context context, UMessage uMessage) {
        ToastUtils.show(JDDApplication.mApp, uMessage.text.toString());
    }

    public static void dealYouMengMsgAction(Context context, UMessage msg) {
    }
}
