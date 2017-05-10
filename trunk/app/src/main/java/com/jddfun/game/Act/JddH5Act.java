package com.jddfun.game.Act;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jddfun.game.Act.base.BaseGameAct;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.RxBus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xuhongliang on 2017/4/12.
 */

public class JddH5Act extends BaseGameAct {


    public static String WEB_URL = "WEB_URL";
    public static final String TITLE_NAME = "TITLE_NAME";
    public static final String IS_GAME = "IS_GAME";
    private String title = "";
    private String web_url = "";

    @BindView(R.id.iv_iv_back)
    ImageView ivIvBack;
    @BindView(R.id.iv_head_right)
    ImageView ivHeadRight;
    @BindView(R.id.tv_activity_title)
    TextView tvActivityTitle;
    WebView mWebView;
    private AppJsInterface appJsInterface;
    private ProgressBar progressBar;
    private boolean isGame;
    private LinearLayout jdd_h5_root;
    private Map<String, String> header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        setContentView(R.layout.activity_shop);
        header = new HashMap<>();
        header.put("Authorization", Constants.Constants_APP_CHANNEL);
        ButterKnife.bind(this);
        web_url = getIntent().getStringExtra(WEB_URL);
        // title = getIntent().getStringExtra(TITLE_NAME);
        isGame = getIntent().getBooleanExtra(IS_GAME, false);
        jdd_h5_root = (LinearLayout) findViewById(R.id.jdd_h5_root);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mWebView = new WebView(JDDApplication.getAppContext());
        mWebView.setLayoutParams(params);
        jdd_h5_root.addView(mWebView);
        initTopBar();
        //游戏或者抽奖+channel
//        if (isGame || web_url.contains("ring")) {
        web_url = web_url + "?channel=" + Constants.Constants_APP_CHANNEL;
        //}
        WebSettings webSettings = mWebView.getSettings();
        progressBar = (ProgressBar) findViewById(R.id.progress);

        String userAgentString = webSettings.getUserAgentString();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString(userAgentString + "；LKCZ/Android");
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = view.getTitle();
                tvActivityTitle.setText(title);

            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }

        });
        // 设置具体WebViewClient
        appJsInterface = new AppJsInterface();
        mWebView.addJavascriptInterface(appJsInterface, "NativeCall");
        mWebView.loadUrl(web_url, header);
        initEvent();
    }

    private void initEvent() {
        RxBus.getInstance()
                .toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(JDDEvent Jddevent) {
                        //支付成功
                        if (Jddevent.getType() == JDDEvent.TYPE_NOTFIY_PAY_SUCCESS) {
                            if (web_url.contains("shopping")) {
                                if (mWebView != null) {
                                    mWebView.reload();
                                }
                            }
                            if (appJsInterface != null) {
                                appJsInterface.setPayResult(true, Jddevent.getOrderNum());
                            }
                        } else if (Jddevent.getType() == JDDEvent.TYPE_NOTFIY_PAY_FAIL) {
                            if (appJsInterface != null) {
                                appJsInterface.setPayResult(false, Jddevent.getOrderNum());
                            }
                        } else if (Jddevent.getType() == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED) {
                            if (JDDUtils.isLogin() && mWebView != null) {
                                mWebView.reload();
                            }
                        }
                    }
                });
    }


    @OnClick({R.id.iv_iv_back, R.id.iv_head_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_iv_back:
                onBackPressed();
                break;
            case R.id.iv_head_right:

                int type = 0;
                if (web_url.contains("shopping")) {
                    //商城
                    type = 4;
                } else if (web_url.contains("ring")) {
                    //欢乐套圈
                    type = 2;
                } else if (web_url.contains("article")) {
                    //分享
                    type = 7;
                }
                JDDUtils.jumpToH5Activity(this, Constants.HELP_CENTER + type, "帮助中心", false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setConfigCallback(null);
        if (jdd_h5_root != null) {
            if (mWebView != null) {
                jdd_h5_root.removeView(mWebView);
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        }
    }


    private void initTopBar() {
        ivIvBack.setImageResource(R.mipmap.left_back);
        tvActivityTitle.setText(title);

        if (web_url.contains("help") || isGame||web_url.contains("article")) {
            ivHeadRight.setVisibility(View.GONE);
        } else {
            ivHeadRight.setImageResource(R.mipmap.question_icon);
            ivHeadRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return;
                }
                //文章详情发广播刷新
                if (web_url.contains("article")) {
                    RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_REFRESH_ARTICLE));
                }

                RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_GAME_BACK));
                finish();
            }
        });
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
        }
    }

}
