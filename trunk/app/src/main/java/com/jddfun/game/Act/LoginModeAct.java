package com.jddfun.game.Act;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.ToastUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.QQLoginReq;
import com.jddfun.game.bean.TokenParams;
import com.jddfun.game.bean.TokenRes;
import com.jddfun.game.bean.WxLogin;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.jddfun.game.push.NotifyMsgHelper;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.message.PushAgent;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 登录方式
 */
public class LoginModeAct extends JDDBaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_wx_login)
    Button btn_wx_login;
    @BindView(R.id.btn_qq_login)
    Button btn_qq_login;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_register)
    TextView tv_register;

    private IWXAPI api;
    private Tencent mTencent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mode);
        ButterKnife.bind(this);
        mContext = LoginModeAct.this;

        tv_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //注册APP到微信
        api = WXAPIFactory.createWXAPI(mContext, Constants.WEIXIN_APPID, true);
        api.registerApp(Constants.WEIXIN_APPID);

        mTencent = Tencent.createInstance(Constants.QQ_APPID, this.getApplicationContext());

        RxBus.getInstance().toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(final JDDEvent jddEvent) {
                        if (jddEvent.getType() == JDDEvent.TYPE_WX_LOGIN) {
                            //微信登录
                            wxLogin(jddEvent.getCode());
                        } else if (jddEvent.getType() == JDDEvent.TYPE_AFTER_LOGIN) {
                            //关闭登录方式界面
                            LoginModeAct.this.finish();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, new QQLoginListener());
    }


    /**
     * 获取token
     *
     * @param code 微信登录授权code
     */
    private void wxLogin(String code) {
        WxLogin wxLogin = new WxLogin();
        wxLogin.setCode(code);
        ServiceFactory.getInstance().createService(JDDApiService.class).wxlogin(wxLogin)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String token) {
                        Constants.TOKEN = token;
                        loginAccess(token);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * QQ登录
     *
     * @param headImg  头像地址
     * @param nickname 昵称
     * @param openid   openid
     */
    private void qqLogin(String headImg, String nickname, String openid) {
        QQLoginReq qqLoginReq = new QQLoginReq();
        qqLoginReq.setHeadImg(headImg);
        qqLoginReq.setNickname(nickname);
        qqLoginReq.setOpenid(openid);
        ServiceFactory.getInstance().createService(JDDApiService.class).qqLogin(qqLoginReq)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String token) {
                        Constants.TOKEN = token;
                        loginAccess(token);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                    }
                });
    }

    /***
     * 获取accessToken
     * @param token 获取到的token
     */
    public void loginAccess(String token) {
        TokenParams tokenParams = new TokenParams();
        tokenParams.setType(1);
        tokenParams.setToken(token);
        ServiceFactory.getInstance().createService(JDDApiService.class, Constants.BASICURL_LOGIN).requestToken(tokenParams)
                .compose(RxUtils.<HttpResult<TokenRes>>defaultSchedulers())
                .compose(this.<HttpResult<TokenRes>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<TokenRes>() {
                    @Override
                    public void onSuccess(TokenRes token) {
                        UtilsTools.getInstance().putString("accessToken", token.getAccessToken());
                        UtilsTools.getInstance().putString("refreshToken", token.getRefreshToken());
                        UtilsTools.getInstance().show("登录成功");
                        NotifyMsgHelper.updateToken(PushAgent.getInstance(JDDApplication.getAppContext()).getRegistrationId(), LoginModeAct.this);
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED));
                        LoginModeAct.this.finish();
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }


    @OnClick({R.id.btn_wx_login, R.id.btn_qq_login, R.id.tv_login, R.id.tv_register})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wx_login:
                //微信登录
                if (!api.isWXAppInstalled()) {
                    ToastUtils.show(this, "未安装微信");
                    return;
                }
                showLoading(true, "微信登录中");
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "1234";
                api.sendReq(req);
                break;
            case R.id.btn_qq_login:
                //qq登录
                if (!mTencent.isSessionValid()) {
                    showLoading(true, "QQ登录中");
                    mTencent.login(this, "all", new QQLoginListener());
                }
                break;
            case R.id.tv_login:
                startActivity(new Intent(mContext, LoginAct.class));
                break;
            case R.id.tv_register:
                startActivity(new Intent(mContext, RegisterCountAct.class));
                break;
        }
    }


    /**
     * QQ登录回调
     */
    class QQLoginListener implements IUiListener {

        @Override
        public void onComplete(Object value) {
            if (value == null) {
                return;
            }
            try {
                JSONObject jo = (JSONObject) value;
                int ret = jo.getInt("ret");
                if (ret == 0) {
                    String openID = jo.getString("openid");
                    String accessToken = jo.getString("access_token");
                    String expires = jo.getString("expires_in");
                    mTencent.setOpenId(openID);
                    mTencent.setAccessToken(accessToken, expires);

                    UserInfo userInfo = new UserInfo(mContext, mTencent.getQQToken());
                    userInfo.getUserInfo(new UserInfoListener());
                }

            } catch (Exception e) {
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    /**
     * 获取QQ用户信息回调
     */
    class UserInfoListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            if (o == null) {
                return;
            }
            try {
                JSONObject jo = (JSONObject) o;
                String nickName = jo.getString("nickname");
                String figureurl_qq_2 = jo.getString("figureurl_qq_2");
                if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(figureurl_qq_2) && !TextUtils.isEmpty(mTencent.getOpenId()))
                    qqLogin(figureurl_qq_2, nickName, mTencent.getOpenId());

            } catch (Exception e) {
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
}
