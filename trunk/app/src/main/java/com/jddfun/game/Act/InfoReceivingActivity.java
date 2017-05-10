package com.jddfun.game.Act;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.Presenter.InfoReceivingPresenter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.View.InfoReceivingView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收货信息
 * Created by MACHINE on 2017/4/6.
 */

public class InfoReceivingActivity extends BaseActivity<InfoReceivingView,InfoReceivingPresenter> implements InfoReceivingView {

    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.iv_iv_back)
    ImageView iv_iv_back;
    @BindView(R.id.receiving_name)
    EditText receiving_name;
    @BindView(R.id.receiving_phone)
    EditText receiving_phone;
    @BindView(R.id.receiving_door_number)
    EditText receiving_door_number;
    @BindView(R.id.receiving_ok)
    TextView receiving_ok;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_info_receiving;
    }


    @OnClick({R.id.iv_iv_back})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_iv_back:
                finish();
                break;
        }
    }


    @Override
    protected void init() {
        tv_activity_title.setText("收货信息");
        iv_iv_back.setImageResource(R.mipmap.left_back);
        receiving_ok.setOnClickListener(presenter);
        receiving_door_number.addTextChangedListener(presenter);
        receiving_phone.addTextChangedListener(presenter);
        receiving_name.addTextChangedListener(presenter);
        presenter.getData();
    }

    @Override
    public InfoReceivingPresenter initPresenter() {
        return new InfoReceivingPresenter();
    }

    @Override
    public void showMessage(String message) {
        UtilsTools.getInstance().show(message);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public RxAppCompatActivity getRxAppAct() {
        return this;
    }

    @Override
    public String getdoorNumber() {
        return receiving_door_number.getText().toString();
    }

    @Override
    public String getdoorName() {
        return receiving_name.getText().toString();
    }

    @Override
    public String getdoorPhone() {
        return receiving_phone.getText().toString();
    }

    @Override
    public void setOkBg(int color) {
        receiving_ok.setBackgroundResource(color);
    }

    @Override
    public void setOkOnclick(boolean isOnclick) {
        if(isOnclick){
            receiving_ok.setOnClickListener(presenter);
        }else{
            receiving_ok.setOnClickListener(null);
        }
    }

    @Override
    public void setdoorNumber(String number) {
        receiving_door_number.setText(number);
    }

    @Override
    public void setdoorName(String doorName) {
        receiving_name.setText(doorName);
    }

    @Override
    public void setdoorPhone(String doorPhone) {
        receiving_phone.setText(doorPhone);
    }
}
