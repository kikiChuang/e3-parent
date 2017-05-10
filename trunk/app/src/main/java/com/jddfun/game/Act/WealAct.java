package com.jddfun.game.Act;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Act.fragment.DebrisFragment;
import com.jddfun.game.Act.fragment.WealFragment;
import com.jddfun.game.Adapter.DebrisAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.BaseUtil;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.View.MyTabView;
import com.jddfun.game.bean.Debris;
import com.jddfun.game.bean.DerbrisInfo;
import com.jddfun.game.bean.PageRequest;
import com.jddfun.game.bean.Weal;
import com.jddfun.game.dialog.DialogUtils;
import com.jddfun.game.dialog.WebViewDialog;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 福利
 * Created by xuhongliang on 2017/4/7.
 */
public class WealAct extends JDDBaseActivity {
    @BindView(R.id.iv_iv_back)
    ImageView ivIvBack;
    @BindView(R.id.iv_head_right)
    ImageView ivHeadRight;
    @BindView(R.id.tv_activity_title)
    TextView tvActivityTitle;
    @BindView(R.id.my_tab)
    MyTabView myTabView;
    @BindView(R.id.weal_v_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private String info;
    private Handler handler = new Handler();

    private String[] tabTitles = new String[]{"碎片记录", "奖品来源"};
    private PagerFragmentAdapter fragmentAdapter;
    private DebrisAdapter debrisAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutView());
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        ivIvBack.setImageResource(R.mipmap.left_back);
        tvActivityTitle.setText("福利");
        ivHeadRight.setImageResource(R.mipmap.question_icon);
        ivHeadRight.setVisibility(View.VISIBLE);
        initDebris();
        fragmentAdapter = new PagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        myTabView.setViewPager(viewPager, tabTitles);
        initEvent();
    }

    private void initDebris() {
        debrisAdapter = new DebrisAdapter(this);
        recycleView.setLayoutManager(new GridLayoutManager(this, 5));
        recycleView.setAdapter(debrisAdapter);
        requestDebris();
    }

    private void requestDebris() {
        PageRequest request = new PageRequest();
        request.setPage(1);
        request.setPageSize(100);
        ServiceFactory.getInstance().createService(JDDApiService.class).getDebris(request)
                .compose(RxUtils.<HttpResult<DerbrisInfo>>defaultSchedulers())
                .compose(this.<HttpResult<DerbrisInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<DerbrisInfo>() {
                    @Override
                    public void onSuccess(DerbrisInfo derbrisInfo) {
                        info = derbrisInfo.getFragmentHelp();
                        if (derbrisInfo != null && derbrisInfo.getFragmentBag() != null && derbrisInfo.getFragmentBag().size() > 0) {
                            tvEmpty.setVisibility(View.GONE);
                            if (derbrisInfo.getFragmentBag().size() > 10) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recycleView.getLayoutParams();
                                params.height = BaseUtil.dip2Px(WealAct.this, 150);
                                recycleView.setLayoutParams(params);
                            }
                            recycleView.setVisibility(View.VISIBLE);
                            debrisAdapter.setData(derbrisInfo.getFragmentBag());

                        } else {
                            onError(null, -1);
                        }

                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        recycleView.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }

                });


    }

    @OnClick(R.id.iv_info)
    public void onClick() {
        //跳转h5
        new WebViewDialog(this, Constants.DEBRIS_DES).showDialog();
    }


    private void initEvent() {
        RxBus.getInstance()
                .toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(final JDDEvent Jddevent) {
                        //领取福利成功
                        if (Jddevent.getType() == JDDEvent.TYPE_WEAL_SUCCESS) {
                            //刷新数据，切换tab
                            ((WealFragment) fragmentAdapter.getItem(1)).refreshData();

                        } else if (Jddevent.getType() == JDDEvent.TYPE_NOTIFY_COMBINE_SUCCESS) {
                            if (handler != null) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isFinishing()) {
                                            return;
                                        }
                                        requestDebris();
                                        ((WealFragment) fragmentAdapter.getItem(1)).refreshData();
                                        if (viewPager != null) {
                                            viewPager.setCurrentItem(1);
                                        }
                                    }
                                }, 400);
                            }
                        }

                    }
                });
    }

    protected int setLayoutView() {
        return R.layout.weal_page_layout;
    }


    @OnClick({R.id.iv_iv_back, R.id.iv_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_iv_back:
                finish();
                break;
            case R.id.iv_head_right:
                JDDUtils.jumpToH5Activity(this, Constants.HELP_CENTER + "3", "帮助中心", false);
                break;
        }
    }


    class PagerFragmentAdapter extends FragmentPagerAdapter {


        private List<Fragment> fragments;

        public PagerFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            Fragment fr1 = new DebrisFragment();
            Fragment fr2 = new WealFragment();
            fragments.add(fr1);
            fragments.add(fr2);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }
}
