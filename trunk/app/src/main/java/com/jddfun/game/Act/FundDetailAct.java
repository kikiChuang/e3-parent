package com.jddfun.game.Act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Act.fragment.LeafRecordFragment;
import com.jddfun.game.Act.fragment.RechargeRecordFragment;
import com.jddfun.game.R;
import com.jddfun.game.View.MyTabView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 资金明细
 * Created by MACHINE on 2017/4/5.
 */

public class FundDetailAct extends JDDBaseActivity implements View.OnClickListener {
    @BindView(R.id.act_colorful_tb)
    MyTabView act_colorful_tb;
    @BindView(R.id.frag_srore_vp)
    ViewPager frag_srore_vp;
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;
    @BindView(R.id.act_head_head)
    LinearLayout act_head_head;
    @BindView(R.id.iv_iv_back)
    ImageView iv_iv_back;
    @BindView(R.id.fl_right)
    RelativeLayout fl_right;


    private ArrayList<Fragment> list_fragment;
    private RechargeRecordFragment mRechargeRecordFragment;
    private LeafRecordFragment mLeafRecordFragment;
    private String[] list_title = {"充值记录", "金叶子记录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        tv_activity_title.setText("资金明细");
    }

    private void initView() {

        tv_activity_title.setTextColor(Color.parseColor("#ffffff"));
        iv_back_rl.setOnClickListener(this);
        fl_right.setVisibility(View.GONE);
        mRechargeRecordFragment = new RechargeRecordFragment();
        mLeafRecordFragment = new LeafRecordFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(mRechargeRecordFragment);
        list_fragment.add(mLeafRecordFragment);

        act_colorful_tb.setViewPager(frag_srore_vp, list_title, this, list_fragment);
        frag_srore_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //资金明细-充值记录标签页点击
                    MobclickAgent.onEvent(FundDetailAct.this, "financial_0001");
                } else {
                    //资金明细-金叶子记录标签页点击
                    MobclickAgent.onEvent(FundDetailAct.this, "financial_0002");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_rl:
                finish();
                break;
        }
    }
}
