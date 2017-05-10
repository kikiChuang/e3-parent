package com.jddfun.game.Act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationCodeLogin extends JDDBaseActivity {

    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.verificationCode)
    EditText mVerificationCode;
    @BindView(R.id.sendVerificationCode)
    TextView mSendVerificationCode;
    @BindView(R.id.confirm)
    Button mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.close, R.id.phone, R.id.verificationCode, R.id.sendVerificationCode, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone:
                break;
            case R.id.verificationCode:
                break;
            case R.id.sendVerificationCode:
                break;
            case R.id.confirm:
                break;
            case R.id.close:
                finish();
                break;
        }
    }
}
