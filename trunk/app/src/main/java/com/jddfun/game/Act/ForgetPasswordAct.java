package com.jddfun.game.Act;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.ForgetPwdReq;
import com.jddfun.game.bean.SendVerCode;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 重置密码
 */
public class ForgetPasswordAct extends JDDBaseActivity implements View.OnClickListener {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_verificationCode)
    EditText et_verificationCode;
    @BindView(R.id.tv_send_code)
    TextView tv_send_code;
    @BindView(R.id.et_new_pwd)
    EditText et_new_pwd;
    @BindView(R.id.et_confirm_pwd)
    EditText et_confirm_pwd;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.forget_phone_point_out)
    TextView forget_phone_point_out;
    @BindView(R.id.forget__verificationCode_tv)
    TextView forget__verificationCode_tv;
    @BindView(R.id.forget__setNewPwd_tv)
    TextView forget__setNewPwd_tv;
    @BindView(R.id.forget__confirmNewPwd_tv)
    TextView forget__confirmNewPwd_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        mContext = ForgetPasswordAct.this;


        et_phone.addTextChangedListener(new ForgetTextWatcher(et_phone));
        et_verificationCode.addTextChangedListener(new ForgetTextWatcher(et_verificationCode));
        et_new_pwd.addTextChangedListener(new ForgetTextWatcher(et_new_pwd));
        et_confirm_pwd.addTextChangedListener(new ForgetTextWatcher(et_confirm_pwd));

    }

    @OnClick({R.id.tv_send_code, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                String phone = et_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone) && UtilsTools.getInstance().isPhoneRight(phone)) {
                    new TimeCount(60000, 1000).start();
                    sendCode(phone);
                } else {
                    UtilsTools.showToast("请输入正确的手机号码", mContext);
                }
                break;
            case R.id.btn_confirm:
                String pwd = et_new_pwd.getText().toString().trim();
                String pwd_confirm = et_confirm_pwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwd_confirm) && TextUtils.equals(pwd, pwd_confirm)) {
                    //修改密码点击
                    MobclickAgent.onEvent(this, "setting_0002");
                    String phone1 = et_phone.getText().toString().trim();
                    String code = et_verificationCode.getText().toString().trim();
                    modifyPwd(phone1, code, pwd);
                } else {
                    UtilsTools.showToast("密码不一致,请重新设置", mContext);
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
        ServiceFactory.getInstance().createService(JDDApiService.class).sendForgetVerificationCode(sendVerCode)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String s) {
                        UtilsTools.showToast(s, mContext);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }


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
            tv_send_code.setText(millisUntilFinished / 1000 + "S后重发");
        }
    }

    /***
     * 重置密码
     * @param phone
     * @param code
     * @param pwd
     */
    public void modifyPwd(String phone, String code, String pwd) {
        ForgetPwdReq forgetPwdReq = new ForgetPwdReq();
        forgetPwdReq.setNewPassword(pwd);
        forgetPwdReq.setSmsCode(code);
        forgetPwdReq.setUsername(phone);

        ServiceFactory.getInstance().createService(JDDApiService.class).newForgetpassword(forgetPwdReq)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        UtilsTools.showToast("重置密码成功", mContext);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    private class ForgetTextWatcher implements TextWatcher {
        EditText text;

        public ForgetTextWatcher(EditText text) {
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
            if (et_phone.getText().length() != 0 && et_verificationCode.getText().length() != 0 && et_new_pwd.getText().length() != 0 && et_confirm_pwd.getText().length() != 0) {
                btn_confirm.setBackgroundResource(R.mipmap.btn_click);
                btn_confirm.setOnClickListener(ForgetPasswordAct.this);
            } else {
                btn_confirm.setBackgroundResource(R.mipmap.btn_bg);
                btn_confirm.setOnClickListener(null);
            }


            if (text.getId() == R.id.et_phone) {
                if (s.length() == 0) {
                    forget_phone_point_out.setVisibility(View.INVISIBLE);
                } else {
                    forget_phone_point_out.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_verificationCode) {
                if (s.length() == 0) {
                    forget__verificationCode_tv.setVisibility(View.INVISIBLE);
                } else {
                    forget__verificationCode_tv.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_new_pwd) {
                if (s.length() == 0) {
                    forget__setNewPwd_tv.setVisibility(View.INVISIBLE);
                } else {
                    forget__setNewPwd_tv.setVisibility(View.VISIBLE);
                }
            } else if (text.getId() == R.id.et_confirm_pwd) {
                if (s.length() == 0) {
                    forget__confirmNewPwd_tv.setVisibility(View.INVISIBLE);
                } else {
                    forget__confirmNewPwd_tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}

