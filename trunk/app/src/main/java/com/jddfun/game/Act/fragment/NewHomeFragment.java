package com.jddfun.game.Act.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jddfun.game.Adapter.HomeAdapter;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.DataInfoCache;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.Banner;
import com.jddfun.game.bean.Game;
import com.jddfun.game.bean.GameResquest;
import com.jddfun.game.bean.HomeItemData;
import com.jddfun.game.bean.Notice;
import com.jddfun.game.bean.UploadChannelInfo;
import com.jddfun.game.bean.UserPersonalBean;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.manager.AccountManager;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 首页
 * Created by MACHINE on 2017/3/22.
 */

public class NewHomeFragment extends BaseFragment {


    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<HomeItemData> pageData = new ArrayList<>();
    private HomeAdapter homeAdapter;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_new_home);
        refreshLayout = getViewById(R.id.refreshLayout);
        recyclerView = getViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        homeAdapter = new HomeAdapter();
        recyclerView.setAdapter(homeAdapter);
        if (UtilsTools.isNetworkAvailable(getActivity())) {
            requestHomePageData();
        } else {
            setNativeData();
        }
    }


    private void requestHomePageData() {
        pageData.clear();
        if (JDDUtils.isLogin()) {
            requestLoginData(false);
        } else {
            requestUnLoginData(null);
        }

    }

    private void isFirst() {
        if ("".equals(UtilsTools.getInstance().getString(Constants.FIRST, ""))) {
            uploadChannel();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    private void setGameData(List<Game> list) {
        if (list != null && list.size() > 0) {
            HomeItemData homeItem;
            for (int i = 0; i < list.size(); i++) {
                homeItem = new HomeItemData();
                homeItem.setType(HomeItemData.TYPE_GAME);
                homeItem.setGame(list.get(i));
                if (!pageData.contains(homeItem)) {
                    pageData.add(homeItem);
                }
            }
            if (list.size() % 2 == 1) {
                homeItem = new HomeItemData();
                homeItem.setType(HomeItemData.TYPE_GAME);
                Game game = new Game();
                game.setId(-1);
                homeItem.setGame(game);
                if (!pageData.contains(homeItem)) {
                    pageData.add(homeItem);
                }
            }

            homeAdapter.setData(pageData);
        } else {
            homeAdapter.setData(pageData);
        }
    }


    private void requestLoginData(final boolean requestUserDataOnly) {
        ServiceFactory.getInstance().createService(JDDApiService.class).getUserPersonalInfo()
                .compose(RxUtils.<HttpResult<UserPersonalBean>>defaultSchedulers())
                .compose(this.<HttpResult<UserPersonalBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<UserPersonalBean>() {
                    @Override
                    public void onSuccess(UserPersonalBean userInfo) {
                        if (userInfo != null) {
                            userInfo.setUserToken(UtilsTools.getInstance().getString("accessToken", ""));
                            AccountManager.getInstance().setCurrentUser(userInfo);
                        }

                        if (!requestUserDataOnly) {
                            HomeItemData homeItem = new HomeItemData();
                            homeItem.setUserPersonalBean(userInfo);
                            homeItem.setType(HomeItemData.TYPE_HEAD);
                            requestUnLoginData(homeItem);
                        } else {
                            if (homeAdapter != null) {
                                homeAdapter.notifyYeZi();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    private void requestUnLoginData(final HomeItemData homeItemData) {
        ServiceFactory.getInstance().createService(JDDApiService.class).getBannerInfo()
                .compose(RxUtils.<HttpResult<List<Banner>>>defaultSchedulers())
                .compose(this.<HttpResult<List<Banner>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<Banner>>() {
                    @Override
                    public void onSuccess(List<Banner> list) {
                        HomeItemData homeData = homeItemData;
                        if (homeData == null) {
                            homeData = new HomeItemData();
                            homeData.setType(HomeItemData.TYPE_HEAD);
                        }
                        ArrayList<Banner> banners = new ArrayList<Banner>();
                        if (list != null && list.size() > 0) {
                            banners.addAll(list);
                        }
                        homeData.setBanners(banners);
                        requestBroadText(homeData);
                        DataInfoCache.saveListCache(getActivity(), (ArrayList) list, "mBanner");
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        refreshLayout.setRefreshing(false);
                    }

                });

    }

    @Override
    protected void setListener() {
        isFirst();
        RxBus.getInstance()
                .toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(final JDDEvent Jddevent) {
                        int type = Jddevent.getType();

                        if (type == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED) {
                            requestHomePageData();
                        }

                        //签到，登录状态改变，支付成功，游戏返回
                        if (type == JDDEvent.TYPE_SIGN_SUCCESS || type == JDDEvent.TYPE_NOTFIY_PAY_SUCCESS
                                || type == JDDEvent.TYPE_GAME_BACK || Jddevent.getType() == JDDEvent.TYPE_BIND_PHONE_SUCCESS
                                || Jddevent.getType() == JDDEvent.TYPE_CLEAR_SUCCESS) {
                            if (JDDUtils.isLogin()) {
                                requestLoginData(true);
                            } else {
                                if (homeAdapter != null) {
                                    homeAdapter.notifyYeZi();
                                }
                            }
                        }


                    }
                });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestHomePageData();
            }
        });
    }

    //请求跑马灯数据
    private void requestBroadText(final HomeItemData homeItemdata) {
        ServiceFactory.getInstance().createService(JDDApiService.class).getNotice()
                .compose(RxUtils.<HttpResult<List<Notice>>>defaultSchedulers())
                .compose(this.<HttpResult<List<Notice>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<Notice>>() {
                    @Override
                    public void onSuccess(List<Notice> list) {
                        ArrayList<Notice> mNotices = new ArrayList<Notice>();
                        if (list != null && list.size() > 0) {
                            mNotices.addAll(list);
                        }
                        homeItemdata.setNotices(mNotices);
                        requestGameList(homeItemdata);
                        DataInfoCache.saveListCache(getActivity(), mNotices, "mNotice");
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        refreshLayout.setRefreshing(false);
                        setNativeData();
                    }

                });
    }

    public void setNativeData() {
        ArrayList<Banner> mBanner = DataInfoCache.loadListCache(getActivity(), "mBanner");
        ArrayList<Notice> mNotices = DataInfoCache.loadListCache(getActivity(), "mNotice");
        ArrayList<Game> mGame = DataInfoCache.loadListCache(getActivity(), "mGame");


        HomeItemData homeData = new HomeItemData();
        homeData.setType(HomeItemData.TYPE_HEAD);
        ArrayList<Banner> banners = new ArrayList<Banner>();
        if (mBanner != null && mBanner.size() > 0) {
            banners.addAll(mBanner);
        }
        homeData.setBanners(banners);

        if (mNotices != null && mNotices.size() > 0) {
            mNotices.addAll(mNotices);
        }
        homeData.setNotices(mNotices);
        requestGameList(homeData);
        setGameData(mGame);
    }

    //渠道发送
    private void uploadChannel() {
        UploadChannelInfo gameResquest = new UploadChannelInfo();
        gameResquest.setUuid(UtilsTools.getInstance().getUUid());
        gameResquest.setUserAgent(UtilsTools.getInstance().getLocalIPAddress());
        gameResquest.setAppVersion(String.valueOf(UtilsTools.getInstance().getVersionCode(getActivity())));
        gameResquest.setPlatformCode("Android");
        gameResquest.setPlatformVersion(UtilsTools.getInstance().getSystemVersion());
        gameResquest.setChannelCode(Constants.Constants_APP_CHANNEL);


        ServiceFactory.getInstance().createService(JDDApiService.class).uploadChannel(gameResquest)
                .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                .compose(this.<HttpResult<String>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<String>() {
                    @Override
                    public void onSuccess(String list) {
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }


    //请求游戏数据
    private void requestGameList(HomeItemData homeItemData) {
        if (homeItemData != null) {
            if (!pageData.contains(homeItemData)) {
                pageData.add(homeItemData);
            }
        }

        GameResquest gameResquest = new GameResquest();
        gameResquest.setPage(1);
        gameResquest.setPageSize(10);
        ServiceFactory.getInstance().createService(JDDApiService.class).getGames(gameResquest)
                .compose(RxUtils.<HttpResult<List<Game>>>defaultSchedulers())
                .compose(this.<HttpResult<List<Game>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<Game>>() {
                    @Override
                    public void onSuccess(List<Game> list) {
                        setGameData(list);
                        refreshLayout.setRefreshing(false);
                        DataInfoCache.saveListCache(getActivity(), (ArrayList) list, "mGame");
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        refreshLayout.setRefreshing(false);
                    }
                });
    }


}
