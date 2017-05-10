package com.jddfun.game.Act.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jddfun.game.Adapter.AllMesFragmentAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.bean.CoterielistBean;
import com.jddfun.game.bean.CoterielistInfo;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.jddfun.game.R.id.frag_allmessage_rl;
import static com.jddfun.game.R.id.refreshLayout;


/**
 * 全部
 * Created by MACHINE on 2017/3/23.
 */

public class AllMesFragment extends BaseFragment {

    private RecyclerView mFrag_allmessage_rl;
    private View mAllmessage_view;
    private ImageView mAllmessage_ash_down;
    private AllMesFragmentAdapter mMPersonalCenterfAdapter;

    private TextView mEmpty_des1;
    private TextView mEmpty_des2;
    private LinearLayout mEmpty_view;
    private int page = 1;
    private TwinklingRefreshLayout mRefreshLayout;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        // mRefreshLayout.startRefresh();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.allmessage);
        mFrag_allmessage_rl = getViewById(frag_allmessage_rl);
        mAllmessage_view = getViewById(R.id.allmessage_view);
        mAllmessage_ash_down = getViewById(R.id.allmessage_ash_down);

        mRefreshLayout = getViewById(refreshLayout);

        mEmpty_view = getViewById(R.id.empty_view);
        mEmpty_des1 = getViewById(R.id.empty_des1);
        mEmpty_des2 = getViewById(R.id.empty_des2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMPersonalCenterfAdapter = new AllMesFragmentAdapter(getActivity());
        mFrag_allmessage_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mFrag_allmessage_rl.setAdapter(mMPersonalCenterfAdapter);
        mFrag_allmessage_rl.setItemAnimator(new DefaultItemAnimator());

        mRefreshLayout.startRefresh();
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        getWorkData(page);
                        mRefreshLayout.setEnableLoadmore(true);
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

                        if (type == JDDEvent.TYPE_REFRESH_ARTICLE || type == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED) {
                            if (mRefreshLayout != null) {
                                mRefreshLayout.startRefresh();
                            }
                        }
                    }
                });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public void getWorkData(int pas) {
        CoterielistInfo mcoterielistInfo = new CoterielistInfo();
        mcoterielistInfo.setPage(pas);
        mcoterielistInfo.setPageSize(Constants.pageSize);
        ServiceFactory.getInstance().createService(JDDApiService.class).coterielist(mcoterielistInfo)
                .compose(RxUtils.<HttpResult<CoterielistBean>>defaultSchedulers())
                .compose(this.<HttpResult<CoterielistBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<CoterielistBean>() {

                    private CoterielistBean.ListBean mMRaidersBean;
                    private CoterielistBean.ListBean mCoterielistBean;

                    @Override
                    public void onSuccess(CoterielistBean list) {
                        if (page == 1) {
                            mMPersonalCenterfAdapter.showBottom(false);

                            List<CoterielistBean.ListBean> list1 = list.getList();
                            mCoterielistBean = new CoterielistBean.ListBean();
                            mCoterielistBean.setTitle(list.getTips().getTitle() == null ? "" : list.getTips().getTitle());
                            mCoterielistBean.setRemark(list.getTips().getRemark() == null ? "" : list.getTips().getRemark());
                            mCoterielistBean.setId(list.getUserId());
                            mCoterielistBean.setType(1);
                            list1.add(0, mCoterielistBean);

                            mMRaidersBean = new CoterielistBean.ListBean();
                            mMRaidersBean.setTitle(list.getRaiders().getTitle() == null ? "" : list.getRaiders().getTitle());
                            mMRaidersBean.setRemark(list.getRaiders().getRemark() == null ? "" : list.getRaiders().getRemark());
                            mMRaidersBean.setPraise(list.getRaiders().getPraise());
                            mMRaidersBean.setHeadImg(list.getRaiders().getHeadImg() == null ? "" : list.getRaiders().getHeadImg());
                            mMRaidersBean.setId(list.getRaiders().getId());
                            mMRaidersBean.setHavePraise(list.getRaiders().getHavePraise());
                            if (list.getRaiders().getPlatCoterieImgList() != null) {
                                mMRaidersBean.setPlatCoterieImgList(list.getRaiders().getPlatCoterieImgList());
                            }
                            mMRaidersBean.setType(2);
                            list1.add(1, mMRaidersBean);
                            mMPersonalCenterfAdapter.setData(list1);
                        } else {
                            if (list.getList().size() == 0) {
                                mRefreshLayout.setEnableLoadmore(false);
                                mMPersonalCenterfAdapter.showBottom(true);
                            } else {
                                mRefreshLayout.setEnableLoadmore(true);
                                mMPersonalCenterfAdapter.showBottom(false);
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
