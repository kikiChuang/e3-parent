package com.jddfun.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.jddfun.game.Utils.AndroidUtil;
import com.jddfun.game.Utils.LogUtils;
import com.jddfun.game.push.NotifyMsgHelper;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xiaoxiaopay.mp.QuerySign;
import com.xiaoxiaopay.mp.XxBeiAPI;

import java.io.File;

/**
 * Created by MACHINE on 2017/3/22.
 */

public class JDDApplication extends MultiDexApplication {

    public static JDDApplication mApp;

    private static boolean isInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        initSevenFish();
        initYouMengPush();
        // 友盟分析
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        TwinklingRefreshLayout.setDefaultHeader(ProgressLayout.class.getName());
    }

    public void initBeiFu(String publicKey) {
        if (!isInit) {
            XxBeiAPI.initSDK(this, publicKey, true, new QuerySign() {
                @Override
                public String getSign(String s, String s1) {
                    return "";
                }
            });
        }
        isInit = true;
    }

    //初始化七鱼
    private void initSevenFish() {
        Unicorn.init(this, "9370c4a46583845948225a7297508133", options(), new UnicornImageLoader() {

            @Override
            public Bitmap loadImageSync(String uri, int width, int height) {
                return null;
            }

            @Override
            public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
            }
        });
    }

    // 如果返回值为null，则全部使用默认参数。
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        StatusBarNotificationConfig mStatusBarNotificationConfig = new StatusBarNotificationConfig();
        options.statusBarNotificationConfig = mStatusBarNotificationConfig;
        options.savePowerConfig = new SavePowerConfig();
        return options;
    }

    private void initYouMengPush() {
        LogUtils.i("初始化友盟push", "初始化友盟push.......");
        // 友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                LogUtils.i("youmengToken", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.i("友盟推送", s + "...." + s1);
            }
        });
        mPushAgent.setDebugMode(false);
//        mPushAgent.enable(new IUmengCallback() {
//            @Override
//            public void onSuccess() {
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//            }
//        });
        //推送声音
        mPushAgent.setMuteDurationSeconds(0);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {
                NotifyMsgHelper.dealYouMengMsg(this, context, uMessage);
            }
        };

        mPushAgent.setMessageHandler(messageHandler);
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                NotifyMsgHelper.dealYouMengMsgAction(context, msg);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public static Context getAppContext() {
        return mApp.getApplicationContext();
    }

    @Override
    public File getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }
        return super.getCacheDir();
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = AndroidUtil.getProcessName(this);
        return packageName.equals(processName);
    }

}
