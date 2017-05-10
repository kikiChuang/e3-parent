package com.jddfun.game.Act.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jddfun.game.R;

/**
 * 每日任务
 */

public class DailyTaskFragment extends BaseFragment {

    private LinearLayout emptyView;
    private TextView empty_des1;
    private TextView empty_des2;

    private Context mContext;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_daily_tasks);
        mContext = getActivity();
        initView();
    }


    private void initView() {
        emptyView = getViewById(R.id.empty_view);
        empty_des1 = getViewById(R.id.empty_des1);
        empty_des2 = getViewById(R.id.empty_des2);
        emptyView.setVisibility(View.VISIBLE);
        empty_des1.setText("居然没有任务");
        empty_des2.setText("还不偷着乐?");
    }


    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


}
