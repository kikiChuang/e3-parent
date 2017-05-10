package com.jddfun.game.View;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by jdd on 2017/3/27.
 */

public interface BaseView {
    //提示语
    void showMessage(String message);
    //关闭界面
    void close();

    RxAppCompatActivity getRxAppAct();

}
