package com.jddfun.game.Act.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jddfun.game.Act.base.JDDBaseFragment;


public abstract class BaseFragment extends JDDBaseFragment {
    protected Activity mApp;
    protected View mContentView;
    protected boolean mIsPrepared = false;
    protected boolean mIsFirstResume = true;
    protected boolean mIsFirstVisible = true;
    protected boolean mIsFirstInvisible = true;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mApp = getActivity();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstResume) {
            mIsFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (mIsFirstInvisible) {
                mIsFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }

    }

    public synchronized void initPrepare() {
        if (mIsPrepared) {
            onFirstUserVisible();
        } else {
            mIsPrepared = true;
        }

    }

    /**
     * 第一次对用户可见时会调用该方法
     */
    protected abstract void onFirstUserVisible();

    /**
     * 对用户可见时会调用该方法，除了第一次
     */
    public void onUserVisible() {
    }

    /**
     * 第一次对用户不可见时会调用该方法
     */
    public void onFirstUserInvisible() {
    }

    /**
     * 对用户不可见时会调用该方法，除了第一次
     */
    public void onUserInvisible() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            initView(savedInstanceState);
            processLogic(savedInstanceState);
            setListener();

        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }

        return mContentView;
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(mApp).inflate(layoutResID, null);
    }

    protected void setContentView(View view) {
        mContentView = view;
    }

    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);


    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

}