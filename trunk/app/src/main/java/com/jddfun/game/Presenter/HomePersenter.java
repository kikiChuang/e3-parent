package com.jddfun.game.Presenter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jddfun.game.Act.HomeAct;
import com.jddfun.game.Act.PublishAct;
import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.Act.fragment.InfoFragment;
import com.jddfun.game.Act.fragment.NewHomeFragment;
import com.jddfun.game.Act.fragment.ShareFragment;
import com.jddfun.game.R;
import com.jddfun.game.Utils.AndroidUtil;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.View.HomeActView;
import com.jddfun.game.bean.TokenParams;
import com.jddfun.game.bean.TokenRes;
import com.jddfun.game.bean.UpdateInfo;
import com.jddfun.game.bean.UploadChannelInfo;
import com.jddfun.game.dialog.UpdateDialog;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.manager.AccountManager;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by MACHINE on 2017/5/9.
 */

public class HomePersenter extends BasePresenter<HomeActView> implements View.OnClickListener{

    private SparseArray<Fragment> fragments;
    private long exitTime = 0;
    private int tabIndex = 0;


    private ArrayList<TextView> tv_List;
    private Fragment mHomeFragment;
    private Fragment mShareFragment;
    private Fragment mInfoFragment;

    @Override
    public void InfoData() {
        ((HomeAct)mView).checkPermission(new JDDBaseActivity.CheckPermListener() {
                            @Override
                            public void superPermission() {
                                updateToken();
                            }
                        }, R.string.pop,
                Manifest.permission.READ_PHONE_STATE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mView.getIvLaunch().animate()
                    .alpha(0f)
                    .setDuration(1 * 1000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mView.getIvLaunch().setVisibility(View.GONE);
                            checkLoginState();
                        }
                    });
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab_main:
                onTabSelected(0);
                break;
            case R.id.tv_tab_share:
                onTabSelected(1);
                break;
            case R.id.tv_tab_mine:
                onTabSelected(2);
                break;
            case R.id.iv_edit:
                publishShareContent();
                break;
            case R.id.close:
                mView.setPopViewVisib(View.GONE);
                break;
        }
    }

    private void onTabSelected(int index) {
        this.tabIndex = index;
        if (tabIndex == 1) {
            showEdit();
        } else {
            hideEdit();
        }

        for (int i = 0; i < tv_List.size(); i++) {
            if (index == i) {
                tv_List.get(i).setSelected(true);
                showFragment(index);
            } else {
                tv_List.get(i).setSelected(false);
            }
        }
        //每次切换查询未读消息
        if (index == 2) {
            ((ShareFragment) fragments.get(1)).setViewPagePa();
        }
    }

    public void showFragment(int index) {
        int size = fragments.size();
        FragmentTransaction ft = ((HomeAct)mView).getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < size; i++) {
            if (index == i) {
                if (fragments.get(i).isAdded()) {
                    ft.show(fragments.get(i));
                } else {
                    ft.add(R.id.home_fragment, fragments.get(i), fragments.get(i).getClass().getName()).show(fragments.get(i));
                }
            } else {
                ft.hide(fragments.get(i));
            }
        }
        ft.commit();


    }


    private void showEdit() {
        mView.setivEidtVisib(View.VISIBLE);
        if (AccountManager.getInstance().getIsShowPop()) {
            mView.setPopViewVisib(View.VISIBLE);
            AccountManager.getInstance().setIsFirstShowPopNo();
        } else {
            mView.setPopViewVisib(View.GONE);
        }
    }

    private void hideEdit() {
        mView.setivEidtVisib(View.GONE);
        mView.setPopViewVisib(View.GONE);
    }

    public void initData() {
        isFirst();
        UtilsTools.getInstance().putString(Constants.FIRST, "启动");
        ButterKnife.bind(((HomeAct)mView));
        getAndroidDeviceInfo();
        checkUpdate();
        initBottomTab();
        initFragment(mView.getBundle());
        onTabSelected(tabIndex);
        //  new PrefManager(((HomeAct)mView)).setFirstTimeLaunch(true); //是否一直显示封面图
        initEventListener();
    }

    private void checkLoginState() {
        if (JDDUtils.isLogin()) {
            JDDUtils.ifShowSignDialog((HomeAct) mView);
        }
    }

    private void addFragments(Fragment f1, Fragment f2, Fragment f3) {
        fragments = new SparseArray();
        fragments.put(0, f1);
        fragments.put(1, f2);
        fragments.put(2, f3);
    }

    private void initBottomTab() {
        tv_List = new ArrayList<>();
        tv_List.add(mView.getTabMain());
        tv_List.add(mView.getTabShare());
        tv_List.add(mView.getTabMine());
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mHomeFragment = ((HomeAct)mView).getSupportFragmentManager().findFragmentByTag(NewHomeFragment.class.getName());
            mShareFragment = ((HomeAct)mView).getSupportFragmentManager().findFragmentByTag(ShareFragment.class.getName());
            mInfoFragment = ((HomeAct)mView).getSupportFragmentManager().findFragmentByTag(InfoFragment.class.getName());
            addFragments(mHomeFragment, mShareFragment, mInfoFragment);
            ((HomeAct)mView).getSupportFragmentManager().beginTransaction().show(fragments.get(0)).hide(fragments.get(1)).hide(fragments.get(2)).commit();
        } else {
            addFragments(new NewHomeFragment(), new ShareFragment(), new InfoFragment());
        }
    }

    private void publishShareContent() {
        JDDUtils.ifJumpToLoginAct((Context) mView, new JDDUtils.JumpListener() {
            @Override
            public void jumpToTarget() {
                MobclickAgent.onEvent((Context) mView, "share_0005");
                ((HomeAct) mView).startActivity(new Intent((Context) mView, PublishAct.class));
            }
        });
    }

    private void getAndroidDeviceInfo() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) ((HomeAct)mView).getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        AndroidUtil.SCREEN_WIDTH = dm.widthPixels;
        AndroidUtil.SCREEN_HEIGHT = dm.heightPixels;
        AndroidUtil.SCREEN_DENSITY = dm.density;
    }

    public  boolean onKeyDownPer0(int keyCode){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    UtilsTools.getInstance().show("再按一次退出应用");
                    exitTime = System.currentTimeMillis();
                } else {
                    mView.close();
                }
                return true;
        }
        return false;
    }


    //登录与其他状态改变的广播监听
    private void initEventListener() {
        RxBus.getInstance().toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(((HomeAct)mView).<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(JDDEvent Jddevent) {
                        if (Jddevent.getType() == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED) {
                            checkLoginState();
                        } else if (Jddevent.getType() == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED_NO) {
                            ((ShareFragment) fragments.get(1)).setViewPagePa();
                        }
                    }
                });

    }

    public void updateToken() {
        String token = UtilsTools.getInstance().getString("accessToken", "");
        String refresh = UtilsTools.getInstance().getString("refreshToken", "");
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(refresh)) {
            TokenParams params = new TokenParams();
            params.setToken(refresh);
            params.setType(2);
            ServiceFactory.getInstance().createService(JDDApiService.class, Constants.BASICURL_LOGIN).requestToken(params)
                    .compose(RxUtils.<HttpResult<TokenRes>>defaultSchedulers())
                    .compose(((HomeAct)mView).<HttpResult<TokenRes>>bindToLifecycle())
                    .subscribe(new HttpResultSubscriber<TokenRes>() {
                        @Override
                        public void onSuccess(TokenRes userInfo) {
                            if (userInfo != null && !TextUtils.isEmpty(userInfo.getAccessToken()) && !TextUtils.isEmpty(userInfo.getRefreshToken())) {
                                UtilsTools.getInstance().putString("accessToken", userInfo.getAccessToken());
                                UtilsTools.getInstance().putString("refreshToken", userInfo.getRefreshToken());
                                handler.sendEmptyMessageDelayed(1, 2 * 1000);
                                initData();
                            } else {
                                onError(null, -1);
                            }

                        }

                        @Override
                        public void onError(Throwable e, int code) {
                            handler.sendEmptyMessageDelayed(1, 2 * 1000);
                            initData();
                        }
                    });
        } else {
            handler.sendEmptyMessageDelayed(1, 2 * 1000);
            initData();
        }
    }

    private void isFirst() {
        if ("".equals(UtilsTools.getInstance().getString(Constants.FIRST, ""))) {
            UploadChannelInfo gameResquest = new UploadChannelInfo();
            gameResquest.setUuid(UtilsTools.getInstance().getUUid());
            gameResquest.setUserAgent(UtilsTools.getInstance().getLocalIPAddress());
            gameResquest.setAppVersion(String.valueOf(UtilsTools.getInstance().getVersionCode(((HomeAct)mView))));
            gameResquest.setPlatformCode("Android");
            gameResquest.setPlatformVersion(UtilsTools.getInstance().getSystemVersion());
            gameResquest.setChannelCode(Constants.Constants_APP_CHANNEL);


            ServiceFactory.getInstance().createService(JDDApiService.class).uploadChannel(gameResquest)
                    .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                    .compose(((HomeAct)mView).<HttpResult<String>>bindToLifecycle())
                    .subscribe(new HttpResultSubscriber<String>() {
                        @Override
                        public void onSuccess(String list) {
                        }

                        @Override
                        public void onError(Throwable e, int code) {
                        }
                    });

        }
    }
    //检测版本更新
    private void checkUpdate() {

        ServiceFactory.getInstance().createService(JDDApiService.class).checkVersionUpdate()
                .compose(RxUtils.<HttpResult<UpdateInfo>>defaultSchedulers())
                .compose(((HomeAct)mView).<HttpResult<UpdateInfo>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<UpdateInfo>() {
                    @Override
                    public void onSuccess(UpdateInfo updateInfo) {
                        if (null != updateInfo && 9 != updateInfo.getForce()) {
                            //需要升级
                            UpdateDialog updateDialog = new UpdateDialog((Context) mView, updateInfo);
                            updateDialog.showDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {

                    }
                });
    }
}
