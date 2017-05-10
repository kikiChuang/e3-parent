package com.jddfun.game.Act;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.TokenParams;
import com.jddfun.game.bean.TokenRes;
import com.jddfun.game.bean.User;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.jddfun.game.push.NotifyMsgHelper;
import com.umeng.message.PushAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginAct extends JDDBaseActivity implements View.OnClickListener {

    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.forgerPassword)
    TextView mForgerPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.act_login_point_out)
    TextView act_login_point_out;
    @BindView(R.id.act_login_psw)
    TextView act_login_psw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        et_pwd.addTextChangedListener(new LoginTextChanged(et_pwd));
        et_phone_number.addTextChangedListener(new LoginTextChanged(et_phone_number));
    }

    @OnClick({R.id.forgerPassword, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgerPassword:
                startActivity(new Intent(this, ForgetPasswordAct.class));
                break;
            case R.id.login:
                phoneLogin(et_phone_number.getText().toString().trim(), et_pwd.getText().toString().trim());
                break;
        }
    }

    /**
     * 手机号密码登录
     *
     * @param phone 手机号
     * @param pwd   密码
     */
    public void phoneLogin(String phone, String pwd) {
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)) {
            User user = new User();
            user.setUsername(phone);
            user.setPassword(pwd);

            ServiceFactory.getInstance().createService(JDDApiService.class, Constants.BASICURL_LOGIN).login(user)
                    .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                    .compose(this.<HttpResult<String>>bindToLifecycle())
                    .subscribe(new HttpResultSubscriber<String>() {
                        @Override
                        public void onSuccess(String token) {
                            loginAccess(token);
                        }

                        @Override
                        public void onError(Throwable e, int code) {
                        }
                    });
        }
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
                        NotifyMsgHelper.updateToken(PushAgent.getInstance(JDDApplication.getAppContext()).getRegistrationId(), LoginAct.this);
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED));
                        //登陆成功关闭登录方式界面
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_AFTER_LOGIN));
                        LoginAct.this.finish();

                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED_NO));
        return super.onKeyDown(keyCode, event);
    }

    private class LoginTextChanged implements TextWatcher {
        EditText text;

        public LoginTextChanged(EditText text) {
            this.text = text;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (et_phone_number.getText().length() != 0 && et_pwd.getText().length() != 0) {
                mLogin.setBackgroundResource(R.mipmap.btn_click);
                mLogin.setOnClickListener(LoginAct.this);
            } else {
                mLogin.setBackgroundResource(R.mipmap.btn_bg);
                mLogin.setOnClickListener(null);
            }

            if (text.getId() == R.id.et_phone_number) {
                if (s.length() == 0) {
                    act_login_point_out.setVisibility(View.INVISIBLE);
                } else {
                    act_login_point_out.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_pwd) {
                if (s.length() == 0) {
                    act_login_psw.setVisibility(View.INVISIBLE);
                } else {
                    act_login_psw.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
