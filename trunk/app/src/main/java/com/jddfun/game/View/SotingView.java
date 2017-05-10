package com.jddfun.game.View;

import android.support.v4.app.FragmentTransaction;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by MACHINE on 2017/4/26.
 */

public interface SotingView extends BaseView {
    @Override
    void showMessage(String message);

    @Override
    void close();

    @Override
    RxAppCompatActivity getRxAppAct();

    FragmentTransaction getSupportFragmentMan();

    void changePage(int i);

}
