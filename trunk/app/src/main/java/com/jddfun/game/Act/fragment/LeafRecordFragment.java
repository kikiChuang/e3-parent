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
import com.jddfun.game.Adapter.LeafRecordAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.TimeUtils;
import com.jddfun.game.bean.BeetingRecordBean;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

/**
 * 金叶子记录
 * Created by MACHINE on 2017/4/5.
 */

public class LeafRecordFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mFrag_record_calendar;
    private LinearLayout layout;
    private PopupWindow popupWindow;
    private RecyclerView mFrag_leaf_rl;
    private TextView leaf_time_type;
    private int mPage = 1;
    private String  Time = TimeUtils.getNextDay(0,TimeUtils.TimeType2) ;
    private LinearLayout mEmpty_view;
    private TextView mEmpty_des1;
    private TextView mEmpty_des2;
    private TwinklingRefreshLayout mFrag_leaf_record_trl;
    private LeafRecordAdapter mMLeafRecordAdapter;
    private GradientDrawable mMyGrad;
    private GradientDrawable mMyGrad2;
    private GradientDrawable mMyGrad3;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_leaf_record);
        mFrag_record_calendar = getViewById(R.id.frag_record_calendar);
        mFrag_leaf_rl = getViewById(R.id.frag_leaf_rl);
        leaf_time_type = getViewById(R.id.leaf_time_type);
        mEmpty_view = getViewById(R.id.empty_view);
        mEmpty_des1 = getViewById(R.id.empty_des1);
        mEmpty_des2 = getViewById(R.id.empty_des2);
        mFrag_leaf_record_trl = getViewById(R.id.frag_leaf_record_trl);

        mFrag_record_calendar.setOnClickListener(this);

        mEmpty_des1.setText("还没有金叶子记录");
        mEmpty_des2.setVisibility(View.GONE);

        mMLeafRecordAdapter = new LeafRecordAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFrag_leaf_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_leaf_rl.setAdapter(mMLeafRecordAdapter);
        mFrag_leaf_rl.setItemAnimator(new DefaultItemAnimator());


        mFrag_leaf_record_trl.startRefresh();
        mFrag_leaf_record_trl.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        getWorkData(Time,mPage);
                        mFrag_leaf_record_trl.setEnableLoadmore(true);
                        refreshLayout.finishRefreshing();
                    }
                },1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        getWorkData(Time,mPage);
                        refreshLayout.finishLoadmore();
                    }
                },1000);
            }
        });
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
            case R.id.frag_record_calendar:
                showPopupWindow(mFrag_record_calendar);
                break;
            case R.id.record_pow_today:
                Time = TimeUtils.getNextDay(0,TimeUtils.TimeType2);
                mPage = 1;
                getWorkData(Time,mPage);
                leaf_time_type.setText("当天");
                popupWindow.dismiss();
                break;
            case R.id.record_pow_yesterday:
                Time = TimeUtils.getNextDay(1,TimeUtils.TimeType2);
                mPage = 1;
                getWorkData(Time,mPage);
                leaf_time_type.setText("昨天");
                popupWindow.dismiss();
                break;
            case R.id.record_pow_eve:
                Time = TimeUtils.getNextDay(2,TimeUtils.TimeType2);
                mPage = 1;
                getWorkData(Time,mPage);
                char[] i = TimeUtils.getNextDay(2,TimeUtils.sun).toCharArray();
                if(String.valueOf(i[0]).equals("0")){
                    leaf_time_type.setText(i[1]+"日");
                }else{
                    leaf_time_type.setText(TimeUtils.getNextDay(2,TimeUtils.sun)+"日");
                }
                popupWindow.dismiss();
                break;
        }
    }

    public void showPopupWindow(View parent) {
        layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.record_popow, null);
        popupWindow = new PopupWindow(layout, 300, 300);
        popupWindow.setFocusable(true);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(parent, xpos - 650, 10);
        final TextView record_pow_today = (TextView) layout.findViewById(R.id.record_pow_today);
        final TextView record_pow_yesterday = (TextView) layout.findViewById(R.id.record_pow_yesterday);
        final TextView record_pow_eve = (TextView) layout.findViewById(R.id.record_pow_eve);
        record_pow_eve.setText(TimeUtils.getNextDay(2,TimeUtils.sun)+"号");

        mMyGrad = (GradientDrawable)record_pow_today.getBackground();
        mMyGrad2 = (GradientDrawable)record_pow_yesterday.getBackground();
        mMyGrad3 = (GradientDrawable)record_pow_eve.getBackground();

        mMyGrad3.setColor(Color.parseColor("#FFFFFF"));
        mMyGrad2.setColor(Color.parseColor("#FFFFFF"));
        mMyGrad.setColor(Color.parseColor("#FFFFFF"));

        if(leaf_time_type.getText().equals("当天")){
            mMyGrad.setColor(Color.parseColor("#ff524c"));
            record_pow_today.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(leaf_time_type.getText().equals("昨天")){
            mMyGrad2.setColor(Color.parseColor("#ff524c"));
            record_pow_yesterday.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            mMyGrad3.setColor(Color.parseColor("#ff524c"));
            record_pow_eve.setTextColor(Color.parseColor("#FFFFFF"));
        }

        record_pow_today.setOnClickListener(this);
        record_pow_yesterday.setOnClickListener(this);
        record_pow_eve.setOnClickListener(this);

    }

    public void getWorkData(String time, final int po) {
        TransDetailsInfo mTransDetailsInfo = new TransDetailsInfo();
        mTransDetailsInfo.setPage(po);
        mTransDetailsInfo.setPageSize(Constants.pageSize);
        mTransDetailsInfo.setParams(time);
        ServiceFactory.getInstance().createService(JDDApiService.class).getBeetingRecord(mTransDetailsInfo)
                .compose(RxUtils.<HttpResult<List<BeetingRecordBean>>>defaultSchedulers())
                .compose(this.<HttpResult<List<BeetingRecordBean>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<BeetingRecordBean>>() {

                    @Override
                    public void onSuccess(List<BeetingRecordBean> list) {
                        if(mPage ==1){
                            if(list.size() == 0){
                                mFrag_leaf_record_trl.setVisibility(View.GONE);
                                mEmpty_view.setVisibility(View.VISIBLE);
                            }else{
                                mFrag_leaf_record_trl.setVisibility(View.VISIBLE);
                                mEmpty_view.setVisibility(View.GONE);
                            }
                            mMLeafRecordAdapter.setData(list);
                        }else{
                            if(list.size() == 0){
                                mFrag_leaf_record_trl.setEnableLoadmore(false);
                            }else{
                                mFrag_leaf_record_trl.setEnableLoadmore(true);
                            }
                            mMLeafRecordAdapter.appendDatas(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

}
