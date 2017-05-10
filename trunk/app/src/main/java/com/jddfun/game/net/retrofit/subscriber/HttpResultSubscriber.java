package com.jddfun.game.net.retrofit.subscriber;


import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.LogUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.net.retrofit.HttpResult;

import rx.Subscriber;

/**
 * Created by _SOLID
 * Date:2016/7/27
 * Time:21:27
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                onError(new Throwable(e.toString()), -1);
            } else {
                onError(new Throwable(e.getMessage()), -1);
            }
        } else {
            onError(new Exception("null message"), -1);
        }
    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (!t.error && t.code == 200) {
                onSuccess(t.data);
        } else {
            LogUtils.e("responseError", "errorCode:" + t.code + "   error msg:" + t.message);
            if (t.message.equals("用户没有登录") || t.code == 401) {
                JDDUtils.WithdrawFrom();
            }
            if (!t.message.equals("用户没有登录")) {
                UtilsTools.getInstance().show(t.message);
            }
            onError(new Throwable("error=true"), t.code);
        }
    }

    public abstract void onSuccess(T t);

    public void _onError(Throwable e) {
    }

    public abstract void onError(Throwable e, int code);
}
