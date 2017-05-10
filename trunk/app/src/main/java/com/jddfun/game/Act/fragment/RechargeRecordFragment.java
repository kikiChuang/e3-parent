package com.jddfun.game.Act.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jddfun.game.bean.TransDetailsInfo;
import com.jddfun.game.Adapter.RechargeAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.TimeUtils;
import com.jddfun.game.bean.RechargeBean;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.jddfun.game.R.id.empty_des1;
import static com.jddfun.game.R.id.empty_des2;

/**
 * 充值记录
 * Created by MACHINE on 2017/4/5.
 */

public class RechargeRecordFragment extends BaseFragment implements View.OnClickListener {


    private ImageView mFrag_record_calendar;
    private LinearLayout layout;
    private PopupWindow popupWindow;
    private RecyclerView mFrag_recharge_rl;
    private RechargeAdapter mMRechargeAdapter;
    List<RechargeBean> list;
    private TextView mRecord_tiem_type;
    private TwinklingRefreshLayout refresh_layout;
    private String time = TimeUtils.getTile9(0);
    private LinearLayout mEmpty_view;
    private TextView mEmpty_des1;
    private TextView mEmpty_des2;
    private GradientDrawable mMyGrad;
    private GradientDrawable mMyGrad2;
    private GradientDrawable mMMyGrad3;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_recharge_record);
        refresh_layout = getViewById(R.id.refresh_layout);
        mFrag_record_calendar = getViewById(R.id.frag_record_calendar);
        mFrag_recharge_rl = getViewById(R.id.frag_recharge_rl);
        mRecord_tiem_type = getViewById(R.id.record_tiem_type);
        mEmpty_view = getViewById(R.id.empty_view);
        mEmpty_des1 = getViewById(empty_des1);
        mEmpty_des2 = getViewById(empty_des2);


        mFrag_record_calendar.setOnClickListener(this);
        initAdapter();
        initRefresh();
    }

    private void initAdapter() {

        mEmpty_des1.setText("还没有充值记录");
        mEmpty_des2.setVisibility(View.GONE);

        mMRechargeAdapter = new RechargeAdapter(getActivity(), new ArrayList<RechargeBean>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFrag_recharge_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_recharge_rl.setAdapter(mMRechargeAdapter);
        mFrag_recharge_rl.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_calendar: //日历
                showPopupWindow(mFrag_record_calendar);
                break;
            case R.id.fund_detail_moon:
                mRecord_tiem_type.setText("本月");
                time = TimeUtils.getTile9(0);
                getWorkData(time, true);
                popupWindow.dismiss();
                break;
            case R.id.fund_detail_moon_2:
                mRecord_tiem_type.setText(TimeUtils.getTile10(30).substring(1, 2) + "月");
                time = TimeUtils.getTile9(30);
                getWorkData(time, true);
                popupWindow.dismiss();
                break;
            case R.id.fund_detail_moon_3:
                mRecord_tiem_type.setText(TimeUtils.getTile10(60).substring(1, 2) + "月");
                time = TimeUtils.getTile9(60);
                getWorkData(time, true);
                popupWindow.dismiss();
                break;
        }
    }

    public void showPopupWindow(View parent) {
        layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fund_detail, null);
        popupWindow = new PopupWindow(layout, 300, 300);
        popupWindow.setFocusable(true);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(parent, xpos - 650, 10);
        final TextView fund_detail_moon =  (TextView) layout.findViewById(R.id.fund_detail_moon);
        final TextView mFund_detail_moon_2 = (TextView) layout.findViewById(R.id.fund_detail_moon_2);
        final TextView fund_detail_moon_3 = (TextView) layout.findViewById(R.id.fund_detail_moon_3);
        mMyGrad = (GradientDrawable)fund_detail_moon.getBackground();
        mMyGrad2 = (GradientDrawable)fund_detail_moon_3.getBackground();
        mMMyGrad3 = (GradientDrawable) mFund_detail_moon_2.getBackground();

        mMMyGrad3.setColor(Color.parseColor("#FFFFFF"));
        mMyGrad2.setColor(Color.parseColor("#FFFFFF"));
        mMyGrad.setColor(Color.parseColor("#FFFFFF"));

       if(mRecord_tiem_type.getText().equals("本月")){
           mMyGrad.setColor(Color.parseColor("#ff524c"));
           fund_detail_moon.setTextColor(Color.parseColor("#FFFFFF"));
       }else if(mRecord_tiem_type.getText().equals(TimeUtils.getTile10(30).substring(1, 2) + "月")){
           mMMyGrad3.setColor(Color.parseColor("#ff524c"));
           mFund_detail_moon_2.setTextColor(Color.parseColor("#FFFFFF"));
       }else{
           mMyGrad2.setColor(Color.parseColor("#ff524c"));
           fund_detail_moon_3.setTextColor(Color.parseColor("#FFFFFF"));
       }

        fund_detail_moon.setText(TimeUtils.getTime7());
        mFund_detail_moon_2.setText(TimeUtils.getTile8(30));
        fund_detail_moon_3.setText(TimeUtils.getTile8(60));
        fund_detail_moon.setOnClickListener(this);
        mFund_detail_moon_2.setOnClickListener(this);
        fund_detail_moon_3.setOnClickListener(this);
    }


    private int page = 1;
    private int pageSize = 10;


    public void getWorkData(String time, final boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }

        TransDetailsInfo mTransDetailsInfo = new TransDetailsInfo();
        mTransDetailsInfo.setPage(page);
        mTransDetailsInfo.setPageSize(pageSize);
        mTransDetailsInfo.setParams(time);
        ServiceFactory.getInstance().createService(JDDApiService.class).getTransDetails(mTransDetailsInfo)
                .compose(RxUtils.<HttpResult<List<RechargeBean>>>defaultSchedulers())
                .compose(this.<HttpResult<List<RechargeBean>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<RechargeBean>>() {

                    @Override
                    public void onSuccess(List<RechargeBean> list) {
                        if (list != null) {
                            if (isRefresh) {
                                mMRechargeAdapter.setData(list);

                                refresh_layout.setEnableLoadmore(true);
                                if (list.size() == 0) {
                                    refresh_layout.setVisibility(View.GONE);
                                    mEmpty_view.setVisibility(View.VISIBLE);
                                } else {
                                    refresh_layout.setVisibility(View.VISIBLE);
                                    mEmpty_view.setVisibility(View.GONE);
                                }
                            } else {
                                mMRechargeAdapter.appendDatas(list);
                                refresh_layout.finishLoadmore();
                            }
                        } else {
                            refresh_layout.setVisibility(View.GONE);
                            mEmpty_view.setVisibility(View.VISIBLE);
                            onError(null, -1);
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        //TODO 展示空白页
                    }
                });
    }


    private void initRefresh() {
        refresh_layout.startRefresh();
        refresh_layout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWorkData(time, true);
                        refresh_layout.finishRefreshing();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWorkData(time, false);
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

    }

}
