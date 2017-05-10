package com.jddfun.game.Act;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.FastClickFilter;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.PhoneBindCodeBean;
import com.jddfun.game.bean.UserPhoneBindInfo;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 手机绑定
 * Created by MACHINE on 2017/4/6.
 */
public class PhonebindingActivity extends JDDBaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_iv_back)
    ImageView iv_iv_back;
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;
    @BindView(R.id.binding_phone)
    EditText binding_phone;
    @BindView(R.id.seng_phone)
    TextView seng_phone;
    @BindView(R.id.seng_identifying)
    EditText seng_identifying;
    @BindView(R.id.confirm)
    TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_binding);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        iv_back_rl.setOnClickListener(this);
        iv_iv_back.setImageResource(R.mipmap.left_back);

        binding_phone.addTextChangedListener(new EditChangedListener(binding_phone));
        seng_identifying.addTextChangedListener(new EditChangedListener(seng_identifying));
        tv_activity_title.setText("手机绑定");
        String pop = getIntent().getStringExtra("pop");
        if (!"".equals(pop)) {
            binding_phone.setText(pop);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_rl:
                finish();
                break;
            case R.id.confirm: //绑定
                sendPop();
                break;
            case R.id.seng_phone: //发送验证码
                if (FastClickFilter.isFastClick()) {
                    return;
                }
                senPop();
                break;
        }
    }

    public void senPop() {
        PhoneBindCodeBean mPhoneBindCodeBean = new PhoneBindCodeBean();
        mPhoneBindCodeBean.setPhone(binding_phone.getText().toString());
        ServiceFactory.getInstance().createService(JDDApiService.class).userPhoneBindCode(mPhoneBindCodeBean)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        new TimeCount(60000, 1000).start();
                        UtilsTools.getInstance().show("发送成功");
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
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
            seng_phone.setText("重新验证");
            seng_phone.setClickable(true);
            seng_phone.setBackgroundResource(R.drawable.act_identifying_pitch);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            seng_phone.setClickable(false);
            seng_phone.setTextColor(getResources().getColor(R.color.white));
            seng_phone.setText(millisUntilFinished / 1000 + "s");
            seng_phone.setBackgroundResource(R.drawable.act_identifying_code);
        }
    }


    //绑定手机号
    public void sendPop() {
        UserPhoneBindInfo mUserPhoneBindInfo = new UserPhoneBindInfo();
        mUserPhoneBindInfo.setPhone(binding_phone.getText().toString());
        mUserPhoneBindInfo.setSmsCodel(seng_identifying.getText().toString());
        ServiceFactory.getInstance().createService(JDDApiService.class).userPhoneBind(mUserPhoneBindInfo)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        UtilsTools.getInstance().show("绑定成功");

                        Intent intent = new Intent();
                        intent.putExtra("phone", binding_phone.getText().toString());
                        PhonebindingActivity.this.setResult(1, intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        if (code == 101) {
                            UtilsTools.getInstance().show("手机已被绑定");
                        }
                    }
                });
    }


    public class EditChangedListener implements TextWatcher {

        private final EditText mEditText;

        public EditChangedListener(EditText text) {
            mEditText = text;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (binding_phone.getText().toString().length() > 0 && seng_identifying.getText().toString().length() > 0) {
                confirm.setBackgroundResource(R.mipmap.btn_click);
                confirm.setOnClickListener(PhonebindingActivity.this);
            } else {
                confirm.setBackgroundResource(R.mipmap.btn_bg);
                confirm.setOnClickListener(null);
            }

            if (mEditText.getId() == R.id.binding_phone) {
                if (binding_phone.getText().length() >= 11) {
                    seng_phone.setBackgroundResource(R.drawable.act_identifying_pitch);
                    seng_phone.setOnClickListener(PhonebindingActivity.this);
                } else {
                    seng_phone.setBackgroundResource(R.drawable.act_identifying_code);
                    seng_phone.setOnClickListener(null);
                }
            }
        }
    }

}
