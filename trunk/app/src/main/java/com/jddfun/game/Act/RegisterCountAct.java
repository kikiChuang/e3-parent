package com.jddfun.game.Act;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.FastClickFilter;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.RegisterBean;
import com.jddfun.game.bean.SendVerCode;
import com.jddfun.game.bean.TokenParams;
import com.jddfun.game.bean.TokenRes;
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
 * 注册
 */
public class RegisterCountAct extends JDDBaseActivity implements View.OnClickListener {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_send_code)
    TextView tv_send_code;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_confirm_pwd)
    EditText et_confirm_pwd;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.iv_register_agree)
    ImageView iv_register_agree;
    @BindView(R.id.count_phone_point_out)
    TextView count_phone_point_out;
    @BindView(R.id.count_verificationCode_point_out)
    TextView count_verificationCode_point_out;
    @BindView(R.id.forget_confirmNewPwd_two)
    TextView forget_confirmNewPwd_two;
    @BindView(R.id.forget_again_confirmNewPwd_two)
    TextView forget_again_confirmNewPwd_two;

    private int agree = 1; //1选中
    private boolean is_text_ok = false; //1选中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_count);
        ButterKnife.bind(this);
        mContext = RegisterCountAct.this;

        et_phone.addTextChangedListener(new EditTextWatcher(et_phone));
        et_code.addTextChangedListener(new EditTextWatcher(et_code));
        et_pwd.addTextChangedListener(new EditTextWatcher(et_pwd));
        et_confirm_pwd.addTextChangedListener(new EditTextWatcher(et_confirm_pwd));
        btn_register.setOnClickListener(null);

    }


    @OnClick({R.id.tv_send_code, R.id.register_user_agreement, R.id.iv_register_agree, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                //发送验证码
                String phone1 = et_phone.getText().toString().trim();
                if (FastClickFilter.isFastClick()) {
                    return;
                }
                if (UtilsTools.getInstance().isPhoneRight(phone1)) {
                    new TimeCount(60000, 1000).start();
                    sendCode(phone1);
                } else {
                    UtilsTools.showToast("请输入正确的手机号码", mContext);
                }
                break;
            case R.id.btn_register:
                //注册
                if (agree == 0) {
                    UtilsTools.getInstance().show("请勾选用户协议");
                } else {
                    if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                        UtilsTools.getInstance().show("请输入验证码");
                    } else {
                        if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                            UtilsTools.getInstance().show("请输入手机号");
                        } else {
                            String pwd = et_pwd.getText().toString().trim();
                            String pwd_confirm = et_confirm_pwd.getText().toString().trim();
                            String phone = et_phone.getText().toString().trim();
                            String code = et_code.getText().toString().trim();
                            if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_confirm)) {
                                UtilsTools.getInstance().show("请输入密码");
                            } else {
                                if (UtilsTools.isPhoneRight(et_phone.getText().toString().trim())) {
                                    if (TextUtils.equals(pwd, pwd_confirm)) {
                                        register(phone, code, pwd);
                                    } else {
                                        UtilsTools.getInstance().show("两次密码输入不一致");
                                    }
                                } else {
                                    UtilsTools.getInstance().show("请输入正确的手机号码");
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.register_user_agreement: //用户协议
                JDDUtils.jumpToH5Activity(this, Constants.USER_AGREEMENT, "用户协议", false);
                break;

            case R.id.count_consent_registration:
                if (agree == 0) {
                    agree = 1;
                    iv_register_agree.setImageResource(R.mipmap.agree);
                } else {
                    agree = 0;
                    iv_register_agree.setImageResource(R.mipmap.no_agree);
                }
                isOk();

                if (is_text_ok && agree == 1) {
                    btn_register.setBackgroundResource(R.mipmap.btn_click);
                    btn_register.setOnClickListener(RegisterCountAct.this);
                } else {
                    btn_register.setBackgroundResource(R.mipmap.btn_bg);
                    btn_register.setOnClickListener(null);
                }
                break;
        }
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    private void sendCode(String phone) {
        SendVerCode sendVerCode = new SendVerCode();
        sendVerCode.setUsername(phone);
        ServiceFactory.getInstance().createService(JDDApiService.class).sendVer(sendVerCode)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        UtilsTools.showToast("验证码已发送", mContext);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }


    private void register(String phone, String code, String pwd) {
        RegisterBean registerBean = new RegisterBean();
        registerBean.setUsername(phone);
        registerBean.setSmsCode(code);
        registerBean.setPassword(pwd);

        ServiceFactory.getInstance().createService(JDDApiService.class).register(registerBean)
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
                        NotifyMsgHelper.updateToken(PushAgent.getInstance(JDDApplication.getAppContext()).getRegistrationId(), RegisterCountAct.this);
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED));
                        //登陆成功关闭登录方式界面
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_AFTER_LOGIN));
                        RegisterCountAct.this.finish();
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    private void isOk() {
        if (et_phone.getText().length() != 0 && et_code.getText().length() != 0 && et_pwd.getText().length() != 0 && et_confirm_pwd.getText().length() != 0 && agree == 1) {
            btn_register.setBackgroundResource(R.mipmap.btn_click);
            btn_register.setOnClickListener(RegisterCountAct.this);
            is_text_ok = true;
        } else {
            btn_register.setBackgroundResource(R.mipmap.btn_bg);
            btn_register.setOnClickListener(null);
            is_text_ok = false;
        }
    }

    /**
     * 倒计时
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {
            tv_send_code.setText("重新验证");
            tv_send_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_send_code.setClickable(false);
            tv_send_code.setTextColor(getResources().getColor(R.color.red));
            tv_send_code.setText(millisUntilFinished / 1000 + "s后重新获取");
        }
    }

    private class EditTextWatcher implements TextWatcher {
        EditText text;

        public EditTextWatcher(EditText text) {
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
            isOk();

            if (text.getId() == R.id.et_phone) {
                if (s.length() == 0) {
                    count_phone_point_out.setVisibility(View.INVISIBLE);
                } else {
                    count_phone_point_out.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_code) {
                if (s.length() == 0) {
                    count_verificationCode_point_out.setVisibility(View.INVISIBLE);
                } else {
                    count_verificationCode_point_out.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_pwd) {
                if (s.length() == 0) {
                    forget_confirmNewPwd_two.setVisibility(View.INVISIBLE);
                } else {
                    forget_confirmNewPwd_two.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_confirm_pwd) {
                if (s.length() == 0) {
                    forget_again_confirmNewPwd_two.setVisibility(View.INVISIBLE);
                } else {
                    forget_again_confirmNewPwd_two.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}