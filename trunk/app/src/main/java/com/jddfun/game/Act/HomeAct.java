package com.jddfun.game.Act;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.Presenter.HomePersenter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.AndroidUtil;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.View.HomeActView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;


public class HomeAct extends BaseActivity<HomeActView,HomePersenter> implements HomeActView{
    @BindView(R.id.tv_tab_main)
    TextView tvTabMain;
    @BindView(R.id.tv_tab_share)
    TextView tvTabShare;
    @BindView(R.id.tv_tab_mine)
    TextView tvTabMine;
    @BindView(R.id.iv_edit)
    ImageView ivEidt;
    @BindView(R.id.pop_view)
    View popView;
    @BindView(R.id.close)
    View close;
    @BindView(R.id.iv_launch)
    ImageView iv_launch;

    public static int device_with;

    @Override
    protected int setLayoutView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_home;
    }

    @Override
    protected void init() {
        device_with = AndroidUtil.getDeviceWidth(this);
        presenter.InfoData();
        tvTabMain.setOnClickListener(presenter);
        tvTabShare.setOnClickListener(presenter);
        tvTabMine.setOnClickListener(presenter);
        ivEidt.setOnClickListener(presenter);
        close.setOnClickListener(presenter);
    }

    @Override
    public HomePersenter initPresenter() {
        return new HomePersenter();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(presenter.onKeyDownPer0(keyCode)){
           return true;
       }else{
           return super.onKeyDown(keyCode, event);
       }
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
    public void setPopViewVisib(int type) {
        popView.setVisibility(type);
    }

    @Override
    public void setivEidtVisib(int type) {
        ivEidt.setVisibility(type);
    }

    @Override
    public Bundle getBundle() {
        return mSavedInstanceState;
    }

    @Override
    public TextView getTabMain() {
        return tvTabMain;
    }

    @Override
    public TextView getTabShare() {
        return tvTabShare;
    }

    @Override
    public TextView getTabMine() {
        return tvTabMine;
    }

    @Override
    public ImageView getIvLaunch() {
        return iv_launch;
    }
}
