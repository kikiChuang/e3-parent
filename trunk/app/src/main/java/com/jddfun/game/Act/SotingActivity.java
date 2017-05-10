package com.jddfun.game.Act;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Presenter.SotingPresenter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.View.SotingView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;

/**
 * 排行榜
 * Created by MACHINE on 2017/4/26.
 */

public class SotingActivity extends BaseActivity<SotingView,SotingPresenter> implements SotingView{

    @BindView(R.id.act_head_bg)
    LinearLayout act_head_bg;
    @BindView(R.id.ll_title_tip)
    LinearLayout ll_title_tip;
    @BindView(R.id.act_head_ll)
    LinearLayout act_head_ll;
    @BindView(R.id.act_head_profit)
    TextView act_head_profit;
    @BindView(R.id.act_head_magnate)
    TextView act_head_magnate;
    @BindView(R.id.iv_head_right)
    ImageView iv_head_right;
    @BindView(R.id.fragment_ptrsenter)
    FrameLayout fragment_ptrsenter;
    @BindView(R.id.fragment_magnate)
    FrameLayout fragment_magnate;
    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;
    @BindView(R.id.fl_right)
    RelativeLayout fl_right;

    @Override
    protected int setLayoutView() {
        return R.layout.activity_sotin;
    }

    @Override
    protected void init() {
        act_head_bg.setBackground(null);
        ll_title_tip.setVisibility(View.GONE);
        act_head_ll.setVisibility(View.VISIBLE);
        iv_head_right.setVisibility(View.VISIBLE);

        act_head_profit.setOnClickListener(presenter);
        act_head_magnate.setOnClickListener(presenter);
        iv_back_rl.setOnClickListener(presenter);
        fl_right.setOnClickListener(presenter);

        presenter.initData();

    }


    @Override
    public SotingPresenter initPresenter() {
        return new SotingPresenter();
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
    public FragmentTransaction getSupportFragmentMan() {
        return getSupportFragmentManager().beginTransaction();
    }

    @Override
    public void changePage(int index) {
        initButtom();
        if (index == 0) {
            fragment_ptrsenter.setVisibility(View.VISIBLE);
            act_head_profit.setBackgroundResource(R.mipmap.soting_pitch_on);
            act_head_profit.setTextColor(Color.parseColor("#4d9ce8"));
        }else if(index == 1){
            fragment_magnate.setVisibility(View.VISIBLE);
            act_head_magnate.setBackgroundResource(R.mipmap.soting_pitch_on_re);
            act_head_magnate.setTextColor(Color.parseColor("#4d9ce8"));
        }
    }
    private void initButtom() {
        fragment_ptrsenter.setVisibility(View.GONE);
        fragment_magnate.setVisibility(View.GONE);
        act_head_profit.setTextColor(Color.parseColor("#ffffff"));
        act_head_magnate.setTextColor(Color.parseColor("#ffffff"));
        act_head_profit.setBackgroundResource(R.mipmap.soting_unchecked);
        act_head_magnate.setBackgroundResource(R.mipmap.soting_unchecked_re);

    }
}
