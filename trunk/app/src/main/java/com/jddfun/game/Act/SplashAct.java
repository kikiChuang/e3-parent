package com.jddfun.game.Act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.TokenParams;
import com.jddfun.game.bean.TokenRes;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;

public class SplashAct extends JDDBaseActivity {



    private Handler handler =new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateToken();
            }
        },1000);
    }


    public void updateToken() {
        String token = UtilsTools.getInstance().getString("accessToken", "");
        String refresh = UtilsTools.getInstance().getString("refreshToken", "");
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(refresh)) {
            TokenParams params = new TokenParams();
            params.setToken(refresh);
            params.setType(2);
            ServiceFactory.getInstance().createService(JDDApiService.class, Constants.BASICURL_LOGIN).requestToken(params)
                    .compose(RxUtils.<HttpResult<TokenRes>>defaultSchedulers())
                    .compose(this.<HttpResult<TokenRes>>bindToLifecycle())
                    .subscribe(new HttpResultSubscriber<TokenRes>() {
                        @Override
                        public void onSuccess(TokenRes userInfo) {
                            if (userInfo != null && !TextUtils.isEmpty(userInfo.getAccessToken()) && !TextUtils.isEmpty(userInfo.getRefreshToken())) {
                                UtilsTools.getInstance().putString("accessToken", userInfo.getAccessToken());
                                UtilsTools.getInstance().putString("refreshToken", userInfo.getRefreshToken());
                                startHomeAct();
                            } else {
                                onError(null, -1);
                            }

                        }

                        @Override
                        public void onError(Throwable e, int code) {
                            startHomeAct();
                        }
                    });
        } else {
            startHomeAct();
        }
    }

    public void startHomeAct() {
        startActivity(new Intent(SplashAct.this, HomeAct.class));
        finish();
    }


}
