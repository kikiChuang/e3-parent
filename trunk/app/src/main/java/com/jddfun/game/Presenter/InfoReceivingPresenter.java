package com.jddfun.game.Presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.R;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.View.InfoReceivingView;
import com.jddfun.game.bean.GetReceiverInfoBean;
import com.jddfun.game.bean.SaveReceiverInfo;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;


/**
 * Created by MACHINE on 2017/4/6.
 */

public class InfoReceivingPresenter extends BasePresenter<InfoReceivingView> implements View.OnClickListener, TextWatcher {


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receiving_ok:
                if (UtilsTools.getInstance().isPhoneRight(mView.getdoorPhone())) {
                    getWorkData();
                } else {
                    mView.showMessage("请输入正确的手机号码");
                }
                break;
        }
    }


    public void getWorkData() {
        SaveReceiverInfo mSaveReceiverInfo = new SaveReceiverInfo();
        mSaveReceiverInfo.setRecName(mView.getdoorName());
        mSaveReceiverInfo.setRecMobile(mView.getdoorPhone());
        mSaveReceiverInfo.setRecAddress(mView.getdoorNumber());

        ServiceFactory.getInstance().createService(JDDApiService.class).saveReceiverInfo(mSaveReceiverInfo)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(mView.getRxAppAct().<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        mView.showMessage("保存成功");
                        mView.close();
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_MODIFY_ADDRESS_SUCCESS));
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        mView.showMessage("保存失败");
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mView.getdoorName().length() != 0 && mView.getdoorNumber().length() != 0 && mView.getdoorPhone().length() != 0) {
            mView.setOkBg(R.mipmap.btn_click);
            mView.setOkOnclick(true);
        } else {
            mView.setOkBg(R.mipmap.btn_bg);
            mView.setOkOnclick(false);
        }
    }

    public void getData() {
        ServiceFactory.getInstance().createService(JDDApiService.class).getReceiverInfo()
                .compose(RxUtils.<HttpResult<GetReceiverInfoBean>>defaultSchedulers())
                .compose(mView.getRxAppAct().<HttpResult<GetReceiverInfoBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<GetReceiverInfoBean>() {
                    @Override
                    public void onSuccess(GetReceiverInfoBean list) {
                        if (list.isReceiverFlag()) {
                            mView.setdoorName(list.getReceiverName());
                            mView.setdoorNumber(list.getReceiverAddress());
                            mView.setdoorPhone(list.getReceiverMobile());
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    @Override
    public void InfoData() {

    }
}
