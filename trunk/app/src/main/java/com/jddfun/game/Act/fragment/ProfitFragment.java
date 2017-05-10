package com.jddfun.game.Act.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.gavinrowe.lgw.library.SimpleCountDownTimer;
import com.jddfun.game.Adapter.ProfitFragmentAdapter;
import com.jddfun.game.CustomView.DotsProgressBar;
import com.jddfun.game.R;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.TimeUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.ProfitRulesBean;
import com.jddfun.game.bean.SortingInfo;
import com.jddfun.game.dialog.CommonDialog;
import com.jddfun.game.dialog.DialogUtils;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import static com.jddfun.game.Utils.TimeUtils.dataOne;


/**
 * 盈利榜
 * Created by MACHINE on 2017/4/26.
 */
public class ProfitFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mFrag_profits_rl;
    private ProfitFragmentAdapter mMProfitFragmentAdapter;
    private RelativeLayout mFrag_profits_select;
    private TextView frag_prfits_today;
    private ImageView mFrag_profits_subscript;
    private TextView mFrag_profits_left;
    private TextView mFrag_profits_ranking;
    private ImageView mFrag_award;
    private TextView mFrag_profits_present;
    private TextView mFrag_subordinate;
    private TextView mFrag_profits_final;
    private ImageView mFrag_profits_eraser;
    private List<SortingInfo.EraserListBean> mEraserList;
    private int mEraserMe; //橡皮擦已使用次数
    private int mEraserTimes;//橡皮擦可使用次数
    private ImageView mFratg_sorting_how_get;
    public ProfitRulesBean mMprofitRulesBean;
    public TextView mFrag_profits_gain;
    public DotsProgressBar mDotsProgressBar;
    private ImageView mFrag_advance_profits;
    private ImageView mFrag_advance_profit_two;
    private ImageView mMFrag_advance_profit_tree;
    private TextView mFrag_profits_countdown_data;
    private TextView mFrag_profits_countdown;
    private SimpleCountDownTimer mMSimpleCountDownTimer;
    private boolean mIsOpen = false;
    private PopupWindow mMPopupWindow;
    private int mMFinalAmount;
    private int mMMeAmount;
    private int mSchedule;
    private ImageView mFrag_chest_lid;
    private Spring mSpring;
    private ImageView mProfits_light;
    private boolean isStart = true;
    private TextView mFrag_profit_before_one;
    private TextView mFrag_profit_lower_level;
    private TextView mFrag_profit_paroxysm;

    @Override
    protected void onFirstUserVisible() {

    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_profits);
        mDotsProgressBar = getViewById(R.id.dots_progress_bar_two);
        mFrag_profits_rl = getViewById(R.id.frag_profits_rl);
        mFrag_profits_select = getViewById(R.id.frag_profits_select);
        frag_prfits_today = getViewById(R.id.frag_prfits_today);
        mFrag_profits_subscript = getViewById(R.id.frag_profits_subscript);
        mFrag_profits_left = getViewById(R.id.frag_profits_left);
        mFrag_profits_ranking = getViewById(R.id.frag_profits_ranking);
        mFrag_award = getViewById(R.id.frag_award);

        mFrag_profits_present = getViewById(R.id.frag_profits_present);
        mFrag_subordinate = getViewById(R.id.frag_subordinate);
        mFrag_profits_final = getViewById(R.id.frag_profits_final);
        mFrag_profits_eraser = getViewById(R.id.frag_profits_eraser);
        mFratg_sorting_how_get = getViewById(R.id.fratg_sorting_how_get);
        mFrag_profits_gain = getViewById(R.id.frag_profits_gain);
        mFrag_advance_profits = getViewById(R.id.frag_advance_profits);
        mFrag_advance_profit_two = getViewById(R.id.frag_advance_profit_two);
        mMFrag_advance_profit_tree = getViewById(R.id.mFrag_advance_profit_tree);
        mFrag_profits_countdown_data = getViewById(R.id.frag_profits_countdown_data);
        mFrag_profits_countdown = getViewById(R.id.frag_profits_countdown);
        mFrag_chest_lid = getViewById(R.id.frag_chest_lid);
        mProfits_light = getViewById(R.id.profits_light);

        mFrag_profit_before_one = getViewById(R.id.frag_profit_before_one);
        mFrag_profit_lower_level = getViewById(R.id.frag_profit_lower_level);
        mFrag_profit_paroxysm = getViewById(R.id.frag_profit_paroxysm);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMProfitFragmentAdapter = new ProfitFragmentAdapter(getActivity());
        mFrag_profits_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_profits_rl.setAdapter(mMProfitFragmentAdapter);
        mFrag_profits_rl.setItemAnimator(new DefaultItemAnimator());
        mFrag_profits_select.setOnClickListener(this);
        mFrag_profits_eraser.setOnClickListener(this);
        mFratg_sorting_how_get.setOnClickListener(this);

        getWorkData();
        profitRules();
    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void setListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMSimpleCountDownTimer.cancel();
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void setData(final SortingInfo data) {
        isStart = true;
        final SortingInfo.ProfitMeBean profitMe = data.getProfitMe(); //盈利榜我的排名
        SortingInfo.ProfitNextBean mProfitNextBean = data.getProfitNext();//下级盈利
        mEraserList = data.getEraserList();// 橡皮擦列表
        mFrag_profits_left.setText(String.valueOf(profitMe.getAmount()));
        if (profitMe.getIndex() != 0) {
            mFrag_profits_ranking.setText(String.valueOf(profitMe.getIndex()));
        } else {
            mFrag_profits_ranking.setText("继续努力");
        }

        //最终奖励金额
        mMFinalAmount = data.getFinalAmount();
        //我的盈利
        mMMeAmount = profitMe.getAmount();

        mFrag_profits_present.setText(mProfitNextBean.getAwardsName());
        mFrag_subordinate.setText(mProfitNextBean.getNextAwardsName());
        mFrag_profits_final.setText(mProfitNextBean.getFinalAwardsName());

        mEraserMe = data.getEraserMe();
        mEraserTimes = data.getEraserTimes();

        if (profitMe.getIndex() == 1) {
            if(profitMe.getAmount() >= data.getFinalAmount()){
                mSchedule = 1;
            }else{
                mSchedule = 4;
            }
        } else if (profitMe.getIndex() == 2) {
            if(profitMe.getAmount() >= data.getFinalAmount()){
                mSchedule = 2;
            }else{
                mSchedule = 4;
            }
        } else if (profitMe.getIndex() == 3) {
            if(profitMe.getAmount() >= data.getFinalAmount()){
                mSchedule = 3;
            }else{
                mSchedule = 4;
            }
        } else if (!"".equals(mProfitNextBean.getAwardsName())) {
            mSchedule = 4;
        } else if ("".equals(mProfitNextBean.getAwardsName()) || mProfitNextBean.getAwardsName() == null) {
            mSchedule = 5;
        }

        if(mSchedule < 4){
            mFrag_profit_before_one.setText("第三名");
            mFrag_profit_lower_level.setText("第二名");
            mFrag_profit_paroxysm.setText("第一名");
        }else{
            mFrag_profit_before_one.setText("当前奖励");
            mFrag_profit_lower_level.setText("下级奖励");
            mFrag_profit_paroxysm.setText("终极奖励");
        }

        mDotsProgressBar.startForward(mSchedule, new DotsProgressBar.DotsTurn() {
            @Override
            public void mDotsTurn(int i) {
                if (i == 3) {
                    mFrag_advance_profits.setImageResource(R.mipmap.advance);
                    mFrag_profits_present.setTextColor(Color.parseColor("#ff9966"));
                    if(profitMe.getAmount() >= data.getFinalAmount()){
                        if(profitMe.getIndex() != 1 && profitMe.getIndex() != 2 && isStart){
                            addViewToAnimLayout();
                            Glide.with(getActivity()).load(data.getProfitNext().getAwardsImage()).into(mFrag_award);
                        }
                    }else{
                        if(isStart){
                            Glide.with(getActivity()).load(data.getProfitNext().getAwardsImage()).into(mFrag_award);
                            addViewToAnimLayout();
                        }
                    }
                } else if (i == 2) {
                    mFrag_advance_profit_two.setImageResource(R.mipmap.advance);
                    mFrag_subordinate.setTextColor(Color.parseColor("#ff9966"));
                    if(profitMe.getIndex() != 1 && isStart){
                        addViewToAnimLayout();
                        Glide.with(getActivity()) .load(data.getProfitNext().getNextAwardsImage()).into(mFrag_award);
                    }
                } else if (i == 1) {
                    mMFrag_advance_profit_tree.setImageResource(R.mipmap.advance);
                    mFrag_profits_final.setTextColor(Color.parseColor("#ff9966"));
                    if(isStart){
                        addViewToAnimLayout();
                        Glide.with(getActivity()).load(data.getProfitNext().getFinalAwardsImage()).into(mFrag_award);
                    }
                }
            }
        });

        if (profitMe.getAmount() >= 0) {
            mFrag_profits_eraser.setVisibility(View.GONE);
        } else {
            if (data.getEraserTimes() > data.getEraserMe()) {
                mFrag_profits_eraser.setVisibility(View.VISIBLE);
            } else {
                mFrag_profits_eraser.setVisibility(View.GONE);
            }
        }
    }


    private void setInitialise(){
        mFrag_advance_profits.setImageResource(R.mipmap.advance_no);
        mFrag_profits_present.setTextColor(Color.parseColor("#999999"));
        mFrag_advance_profit_two.setImageResource(R.mipmap.advance_no);
        mFrag_subordinate.setTextColor(Color.parseColor("#999999"));
        mMFrag_advance_profit_tree.setImageResource(R.mipmap.advance_no);
        mFrag_profits_final.setTextColor(Color.parseColor("#999999"));

        mFrag_award.setVisibility(View.GONE);
        mProfits_light.clearAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fratg_sorting_how_get: //规则
                MobclickAgent.onEvent(getActivity(), "ranking_0004");
                DialogUtils.showArwardDialog(new AlertDialog.Builder(getActivity()).create(), getActivity(), mMprofitRulesBean);
//                new WebViewDialog(getActivity(), Constants.RANK_DES).showDialog();

                break;
            case R.id.frag_profits_select:

                if (!mIsOpen) {
                    mFrag_profits_subscript.setImageResource(R.mipmap.ash_up);
                    mIsOpen = true;
                    mMPopupWindow = DialogUtils.downPopwindow(getActivity(), frag_prfits_today.getText().equals("今日排行") ? "昨日排行" : "今日排行", mFrag_profits_select, mFrag_profits_select.getWidth(), new DialogUtils.popwInface() {
                        @Override
                        public void oncli(View v) {
                            frag_prfits_today.setText(frag_prfits_today.getText().equals("今日排行") ? "昨日排行" : "今日排行");
                            if (frag_prfits_today.getText().toString().equals("今日排行")) {
                                MobclickAgent.onEvent(getActivity(), "ranking_0001");
                                getWorkData();
                                mFrag_profits_gain.setText("今日盈利");
                                mIsOpen = false;
                            } else {
                                yesterdayInit();
                                mFrag_profits_gain.setText("昨日盈利");
                                mIsOpen = false;
                            }
                        }

                        @Override
                        public void dismiss() {
                            mFrag_profits_subscript.setImageResource(R.mipmap.ash_down);
                        }
                    });
                } else {
                    mIsOpen = false;
                    mMPopupWindow.dismiss();
                    mFrag_profits_subscript.setImageResource(R.mipmap.ash_down);
                }
                break;
            case R.id.frag_profits_eraser: //橡皮擦
                MobclickAgent.onEvent(getActivity(), "ranking_0005");
                JDDUtils.ifJumpToLoginAct(getActivity(), new JDDUtils.JumpListener() {
                    public void jumpToTarget() {
                        DialogUtils.rubber(new CommonDialog(getActivity()), getActivity(), mEraserList, mEraserMe, mEraserTimes, new DialogUtils.Dialogrubber() {
                            @Override
                            public void actrubber() {
                                runnerClear();
                            }
                        });
                    }
                });
                break;
        }
    }


    //排行榜初始化 今日
    public void getWorkData() {
        ServiceFactory.getInstance().createService(JDDApiService.class).init()
                .compose(RxUtils.<HttpResult<SortingInfo>>defaultSchedulers())
                .compose(this.<HttpResult<SortingInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<SortingInfo>() {

                    private Long mFileData;
                    private String mTimeNine;

                    @Override
                    public void onSuccess(SortingInfo list) {
                        mMProfitFragmentAdapter.setData(list.getRankingList());
                        String ymd = TimeUtils.getTime4();
                        mFrag_profits_countdown.setText("排行倒计时");
                        mTimeNine = dataOne(ymd + " " + "21:00:00", TimeUtils.TimeType);
                        mFileData = Long.parseLong(mTimeNine) - System.currentTimeMillis();
                        if( mFileData < 0){
                            mTimeNine =  TimeUtils.dataOne(TimeUtils.getBehindDay(1,TimeUtils.TimeType2)+" 21:00:00", TimeUtils.TimeType);
                            mFileData = Long.parseLong(mTimeNine) - System.currentTimeMillis();
                        }
                        mMSimpleCountDownTimer = new SimpleCountDownTimer(mFileData, mFrag_profits_countdown_data);
                        mMSimpleCountDownTimer.setOnFinishListener(new SimpleCountDownTimer.OnFinishListener() {
                            @Override
                            public void onFinish() {
                                mTimeNine =  TimeUtils.dataOne(TimeUtils.getBehindDay(1,TimeUtils.TimeType2)+" 21:00:00", TimeUtils.TimeType);
                                mFileData = Long.parseLong(mTimeNine) - System.currentTimeMillis();
                                mMSimpleCountDownTimer = new SimpleCountDownTimer(mFileData, mFrag_profits_countdown_data);
                                mMSimpleCountDownTimer.setOnFinishListener(new SimpleCountDownTimer.OnFinishListener(){
                                    @Override
                                    public void onFinish() {

                                    }
                                }).start();
                            }
                        }).start();
                        setInitialise();
                        setData(list);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    //排行榜初始化 昨日
    public void yesterdayInit() {
        ServiceFactory.getInstance().createService(JDDApiService.class).yesterdayInit()
                .compose(RxUtils.<HttpResult<SortingInfo>>defaultSchedulers())
                .compose(this.<HttpResult<SortingInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<SortingInfo>() {
                    @Override
                    public void onSuccess(SortingInfo list) {
                        mMProfitFragmentAdapter.setData(list.getRankingList());
                        setInitialise();
                        setData(list);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    //橡皮擦的使用
    public void runnerClear() {
        ServiceFactory.getInstance().createService(JDDApiService.class).clear()
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                        UtilsTools.getInstance().show("擦除成功");
                        mFrag_profits_left.setText("0");
                        mEraserMe++;
                        mFrag_profits_eraser.setVisibility(View.GONE);
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_CLEAR_SUCCESS));
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    //规则
    public void profitRules() {
        ServiceFactory.getInstance().createService(JDDApiService.class).profitRules()
                .compose(RxUtils.<HttpResult<ProfitRulesBean>>defaultSchedulers())
                .compose(this.<HttpResult<ProfitRulesBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<ProfitRulesBean>() {


                    @Override
                    public void onSuccess(ProfitRulesBean list) {
                        mMprofitRulesBean = list;
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    //动画
    private void addViewToAnimLayout() {
        isStart = false;
        mFrag_award.setVisibility(View.VISIBLE);

        Animation circle_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.ddqb_anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            mProfits_light.startAnimation(circle_anim);
        }

        SpringSystem springSystem = SpringSystem.create();
        mSpring = springSystem.createSpring();
        mSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 1));
        mSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                mFrag_award.setScaleX(scale);
                mFrag_award.setScaleY(scale);
            }
        });
        mSpring.setEndValue(0.5);
    }
}
