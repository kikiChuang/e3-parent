package com.jddfun.game.Presenter;

/**
 * Presenter的基类
 * Created by jdd on 2017/3/24.
 */
public abstract class BasePresenter<T> {
    public  T mView;

    /**
     * view跟Presenter绑定
     *
     * @param mView
     */
    public void attach(T mView) {
        this.mView = mView;
    }

    /**
     * 解绑
     */
    public void dettach() {
        mView = null;
    }

    //初始化
    public abstract void InfoData();
}
