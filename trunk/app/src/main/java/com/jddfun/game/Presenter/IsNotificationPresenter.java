package com.jddfun.game.Presenter;

import android.view.View;

import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.R;
import com.jddfun.game.View.IsNotificationView;
import com.jddfun.game.bean.NotifyState;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;

import ch.ielse.view.SwitchView;

/**
 * Created by MACHINE on 2017/4/10.
 */

public class IsNotificationPresenter extends BasePresenter<IsNotificationView> implements View.OnClickListener, SwitchView.OnStateChangedListener {


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back_rl:
                mView.close();
                break;
        }
    }


    private void setStates(int num, boolean isOpen) {
        final int[] states = mView.getState();
        states[num] = isOpen ? 1 : 0;
        NotifyState notifystat = new NotifyState();
        notifystat.setFeverStatus(states[0]);
        notifystat.setNotDisturbStatus(states[1]);
        notifystat.setPrizeStatus(states[2]);
        ServiceFactory.getInstance().createService(JDDApiService.class).setNotifyStates(notifystat)
                .compose(RxUtils.<HttpResult<Object>>defaultSchedulers())
                .compose(mView.getRxAppAct().<HttpResult<Object>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object object) {
                        mView.setState(states);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        mView.close();
                    }

                });
    }


    public void initStates() {
        ServiceFactory.getInstance().createService(JDDApiService.class).getNotifyStates()
                .compose(RxUtils.<HttpResult<NotifyState>>defaultSchedulers())
                .compose(mView.getRxAppAct().<HttpResult<NotifyState>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<NotifyState>() {
                    @Override
                    public void onSuccess(NotifyState states) {
                        if (states != null) {
                            mView.setState(new int[]{states.getFeverStatus(), states.getNotDisturbStatus(), states.getPrizeStatus()});
                        } else {
                            onError(null, -1);
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        mView.close();
                    }

                });
    }

    @Override
    public void toggleToOn(SwitchView view) {
        switch ((Integer) view.getTag()) {
            case 1:
                setStates(0, true);
                break;
            case 2:
                mView.setClose(3);
                setStates(1, true);
                break;
            case 3:
                mView.setClose(2);
                setStates(2, true);
                break;
        }
    }

    @Override
    public void toggleToOff(SwitchView view) {
        switch ((Integer) view.getTag()) {
            case 1:
                setStates(0, false);
                break;
            case 2:
                setStates(1, false);
                break;
            case 3:
                setStates(2, false);
                break;
        }
    }

    @Override
    public void InfoData() {

    }
}
