package com.jddfun.game.Utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jddfun.game.Act.JddH5Act;
import com.jddfun.game.Act.LoginModeAct;
import com.jddfun.game.bean.GetWealParams;
import com.jddfun.game.bean.PrizeGet;
import com.jddfun.game.bean.SignInfo;
import com.jddfun.game.bean.Weal;
import com.jddfun.game.dialog.CommonDialog;
import com.jddfun.game.dialog.DialogUtils;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.manager.AccountManager;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.qiyukf.unicorn.api.Unicorn;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by xuhongliang on 2017/4/7.
 */

public class JDDUtils {


    public interface GetPrizeCallBack {
        void getSuccess();

        void getFailed();
    }

    public interface JumpListener {
        void jumpToTarget();
    }

    public static void getPrize(Weal weal, RxFragment rxFragment, final GetPrizeCallBack getPrizeCallBack) {
        GetWealParams params = new GetWealParams();
        params.setActivityId(weal.getActivityId());
        params.setId(weal.getId() + "");
        ServiceFactory.getInstance().createService(JDDApiService.class).getPrize(params)
                .compose(RxUtils.<HttpResult<PrizeGet>>defaultSchedulers())
                .compose(rxFragment.<HttpResult<PrizeGet>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<PrizeGet>() {
                    @Override
                    public void onSuccess(PrizeGet object) {
                        if (object.isSuccess()) {
                            getPrizeCallBack.getSuccess();
                        } else {
                            getPrizeCallBack.getFailed();
                        }
                    }


                    @Override
                    public void onError(Throwable e, int code) {
                        getPrizeCallBack.getFailed();
                    }

                });
    }


    public static boolean isLogin() {
        String token = UtilsTools.getInstance().getString("accessToken", "");
        boolean isLogin = !TextUtils.isEmpty(token);
        return isLogin;
    }


    public static void ifJumpToLoginAct(Context context, JumpListener jumpListener) {
        if (isLogin()) {
            if (jumpListener != null) {
                jumpListener.jumpToTarget();
            }
        } else {
            Intent intent = new Intent(context, LoginModeAct.class);
            context.startActivity(intent);
        }
    }


    public static void ifShowSignDialog(final RxAppCompatActivity context) {
        ServiceFactory.getInstance().createService(JDDApiService.class).getSignIn()
                .compose(RxUtils.<HttpResult<SignInfo>>defaultSchedulers())
                .compose(context.<HttpResult<SignInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<SignInfo>() {
                    @Override
                    public void onSuccess(SignInfo signInfo) {
                        if (signInfo != null && !signInfo.isDaySigned()) {
                            if (isLogin()) {
                                DialogUtils.showSignInDialog(new CommonDialog(context), context, signInfo);
                            }
                        } else {
                            onError(null, -1);
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {

                    }

                });

    }

    /**
     * 退出登录
     */
    public static void WithdrawFrom() {
        UtilsTools.getInstance().putString("accessToken", "");
        UtilsTools.getInstance().putString("refreshToken", "");
        Unicorn.logout();
        AccountManager.getInstance().setCurrentUser(null);
        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED));

    }


    /**
     * 跳转到H5页面
     */

    public static void jumpToH5Activity(Context context, String web_url, String title, boolean isGame) {
        Intent intent = new Intent(context, JddH5Act.class);
        intent.putExtra(JddH5Act.WEB_URL, web_url);
        // intent.putExtra(JddH5Act.TITLE_NAME, title);
        intent.putExtra(JddH5Act.IS_GAME, isGame);
        context.startActivity(intent);

    }


}
