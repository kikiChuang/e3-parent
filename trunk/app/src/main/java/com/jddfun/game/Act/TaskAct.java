package com.jddfun.game.Act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Act.fragment.DailyTaskFragment;
import com.jddfun.game.Act.fragment.GrowTaskFragment;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.View.MyTabView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskAct extends JDDBaseActivity {


    @BindView(R.id.iv_iv_back)
    ImageView ivIvBack;
    @BindView(R.id.iv_head_right)
    ImageView ivHeadRight;
    @BindView(R.id.tv_activity_title)
    TextView tvActivityTitle;
    @BindView(R.id.tab_view)
    MyTabView tab_view;
    @BindView(R.id.view_pager)
    ViewPager view_pager;

    private String[] list_title = {"每日任务", "成长任务"};
    private ArrayList<Fragment> list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mContext = this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivIvBack.setImageResource(R.mipmap.left_back);
        tvActivityTitle.setText("任务");
        ivHeadRight.setImageResource(R.mipmap.question_icon);
        ivHeadRight.setVisibility(View.VISIBLE);

        DailyTaskFragment dailyTaskFragment = new DailyTaskFragment();
        GrowTaskFragment growTaskFragment = new GrowTaskFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(dailyTaskFragment);
        list_fragment.add(growTaskFragment);

        tab_view.setViewPager(view_pager, list_title, TaskAct.this, list_fragment);
    }


    @OnClick({R.id.iv_iv_back, R.id.iv_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_iv_back:
                finish();
                break;
            case R.id.iv_head_right:
                JDDUtils.jumpToH5Activity(this, Constants.HELP_CENTER + "6", "帮助中心", false);
                break;
        }
    }
}
