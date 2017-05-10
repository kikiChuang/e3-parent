package com.jddfun.game.Act;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Presenter.IsNotificationPresenter;
import com.jddfun.game.R;
import com.jddfun.game.View.IsNotificationView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import ch.ielse.view.SwitchView;


/**
 * 通知中心
 * Created by MACHINE on 2017/4/10.
 */

public class IsNotification extends BaseActivity<IsNotificationView, IsNotificationPresenter> implements IsNotificationView {
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.notification_centre_support)
    SwitchView notification_centre_support;
    @BindView(R.id.notification_no_disturb_support)
    SwitchView notification_no_disturb_support;
    @BindView(R.id.notification_disturb_support)
    SwitchView notification_disturb_support;
    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;

    @Override
    protected void init() {
        tv_activity_title.setText("通知中心");
        notification_centre_support.setTag(1);
        notification_no_disturb_support.setTag(2);
        notification_disturb_support.setTag(3);

        notification_centre_support.setOnStateChangedListener(presenter);
        notification_no_disturb_support.setOnStateChangedListener(presenter);
        notification_disturb_support.setOnStateChangedListener(presenter);
        iv_back_rl.setOnClickListener(presenter);
        presenter.initStates();

    }

    @Override
    protected int setLayoutView() {
        return R.layout.activity_is_notification;
    }

    @Override
    public IsNotificationPresenter initPresenter() {
        return new IsNotificationPresenter();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void close() {
        //close();
        finish();
    }

    @Override
    public RxAppCompatActivity getRxAppAct() {
        return this;
    }

    @Override
    public int[] getState() {
        int[] state = new int[3];
        state[0] = notification_centre_support.isOpened() ? 1 : 0;
        state[1] = notification_no_disturb_support.isOpened() ? 1 : 0;
        state[2] = notification_disturb_support.isOpened() ? 1 : 0;
        return state;
    }

    @Override
    public void setState(int[] states) {
        notification_centre_support.setOpened(states[0] == 1 ? true : false);
        notification_no_disturb_support.setOpened(states[1] == 1 ? true : false);
        notification_disturb_support.setOpened(states[2] == 1 ? true : false);
    }

    @Override
    public void setClose(int index) {
        if (index == 2) {
            notification_no_disturb_support.setOpened(false);
        } else if (index == 3) {
            notification_disturb_support.setOpened(false);
        }
    }


}
