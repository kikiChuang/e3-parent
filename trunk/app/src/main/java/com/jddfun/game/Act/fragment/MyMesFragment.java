package com.jddfun.game.Act.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jddfun.game.Adapter.MyMesFragmentAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.bean.CoterielistInfo;
import com.jddfun.game.bean.CoteriemineInfo;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 我的
 * Created by MACHINE on 2017/3/23.
 */

public class MyMesFragment extends BaseFragment {

    private RecyclerView mFrag_allmessage_rl;
    private LinearLayout mEmpty_view;
    private TextView mEmpty_des1;
    private TextView mEmpty_des2;
    private int page = 1;
    private TwinklingRefreshLayout mMy_mes_trl;
    private MyMesFragmentAdapter mMPersonalCenterfAdapter;


    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
    }

    public void startRefresh(){
        if(mMy_mes_trl!=null){
            mMy_mes_trl.startRefresh();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.my_mes_fragment);
        mEmpty_view = getViewById(R.id.empty_view);
        mFrag_allmessage_rl = getViewById(R.id.frag_allmessage_rl_me);
        mEmpty_des1 = getViewById(R.id.empty_des1);
        mEmpty_des2 = getViewById(R.id.empty_des2);
        mMy_mes_trl = getViewById(R.id.my_mes_trl);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMPersonalCenterfAdapter = new MyMesFragmentAdapter(getActivity());
        mFrag_allmessage_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_allmessage_rl.setAdapter(mMPersonalCenterfAdapter);
        mFrag_allmessage_rl.setItemAnimator(new DefaultItemAnimator());

        mMy_mes_trl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        getWorkData(page);
                        mMy_mes_trl.setEnableLoadmore(true);
                        refreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getWorkData(page);
                        refreshLayout.finishLoadmore();
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void setListener() {
        RxBus.getInstance()
                .toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(final JDDEvent Jddevent) {
                        int type = Jddevent.getType();

                        if (type == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED || type == JDDEvent.TYPE_NOTFIY_PUBLIC_SHARE_SUCCESS) {
                            if (mMy_mes_trl != null) {
                                mMy_mes_trl.startRefresh();
                            }
                        }
                    }
                });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void getWorkData(int pos) {
        CoterielistInfo mcoterielistInfo = new CoterielistInfo();
        mcoterielistInfo.setPage(pos);
        mcoterielistInfo.setPageSize(Constants.pageSize);
        ServiceFactory.getInstance().createService(JDDApiService.class).coteriemine(mcoterielistInfo)
                .compose(RxUtils.<HttpResult<CoteriemineInfo>>defaultSchedulers())
                .compose(this.<HttpResult<CoteriemineInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<CoteriemineInfo>() {
                    @Override
                    public void onSuccess(CoteriemineInfo list) {
                        if (page == 1) {
                            if (list.getList().size() == 0) {
                                mMy_mes_trl.setVisibility(View.GONE);
                                mEmpty_view.setVisibility(View.VISIBLE);
                                mEmpty_des1.setText("分享不积极");
                                mEmpty_des2.setText("注定没福利");
                            } else {
                                mMy_mes_trl.setEnableLoadmore(true);
                                mMy_mes_trl.setVisibility(View.VISIBLE);
                                mEmpty_view.setVisibility(View.GONE);
                                mMPersonalCenterfAdapter.setData(list.getList());
                                mMPersonalCenterfAdapter.showBottom(false);
                            }
                        } else {
                            if (list.getList().size() == 0) {
                                mMPersonalCenterfAdapter.showBottom(true);
                                mMy_mes_trl.setEnableLoadmore(false);
                            } else {
                                mMPersonalCenterfAdapter.showBottom(false);
                                mMy_mes_trl.setEnableLoadmore(true);
                                mMPersonalCenterfAdapter.appendDatas(list.getList());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }
}
