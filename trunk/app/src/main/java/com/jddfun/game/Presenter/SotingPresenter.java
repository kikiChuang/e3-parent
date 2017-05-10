package com.jddfun.game.Presenter;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jddfun.game.Act.fragment.MagnateFragment;
import com.jddfun.game.Act.fragment.ProfitFragment;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.View.SotingView;

/**
 * Created by MACHINE on 2017/4/26.
 */

public class SotingPresenter extends BasePresenter<SotingView> implements View.OnClickListener,Animator.AnimatorListener  {


    private ProfitFragment mProfitFragment; //盈利
    private MagnateFragment mMagnateFragment; //富豪
    private android.support.v4.app.FragmentTransaction transaction;

    public void initData(){
        initMainPager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_head_profit: //盈利榜
                mView.changePage(0);
                break;
            case R.id.act_head_magnate: //富豪榜
                mView.changePage(1);
                break;
            case R.id.iv_back_rl:
                mView.close();
                break;
            case R.id.fl_right:
                JDDUtils.jumpToH5Activity((Context) mView, Constants.HELP_CENTER + "1", "帮助中心", false);
                break;
        }
    }

    public void initMainPager() {
        mProfitFragment = new ProfitFragment();
        mMagnateFragment = new MagnateFragment();
        addPage(0, mProfitFragment);
        addPage(1, mMagnateFragment);
        mView.changePage(0);
    }

    private void addPage(int index, Fragment fragment) {
        transaction = mView.getSupportFragmentMan();
        if (index == 0) {
            transaction.add(R.id.fragment_ptrsenter, fragment);
        } else if (index == 1) {
            transaction.add(R.id.fragment_magnate, fragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }
    @Override
    public void onAnimationEnd(Animator animation) {
    }
    @Override
    public void onAnimationCancel(Animator animation) {
    }
    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    @Override
    public void InfoData() {

    }
}
