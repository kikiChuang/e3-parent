package com.jddfun.game.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jddfun.game.R;
import com.jddfun.game.Utils.AndroidUtil;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.DeviceUuidFactory;
import com.jddfun.game.manager.AccountManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebViewDialog implements DialogInterface.OnDismissListener {

    public Dialog dialog;

    private Context context;

    private String url;
    private WebView webView;
    private Map<String, String> header;

    public WebViewDialog(Context context, String url) {
        this.context = context;
        dialog = new Dialog(context);
        header = new HashMap<>();
        header.put("Authorization", Constants.Constants_APP_CHANNEL);
        this.url = url+"?channel=" + Constants.Constants_APP_CHANNEL;
        initDialog();
    }

    private void initDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(context, R.layout.h5_dialog, null);
        webView = (WebView) view.findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new AppJsInterface(), "NativeCall");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url,header);
                return true;
            }


        });

        webView.loadUrl(url,header);
         dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = AndroidUtil.getDeviceWidth((Activity) context) - 100;
        dialogWindow.setAttributes(params);
        dialog.setCancelable(true);
        View close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
        dialog.setOnDismissListener(this);

    }

    public void showDialog() {
        dialog.show();
    }

    public void cancelDialog() {
        dialog.dismiss();
    }

    public boolean dialogIsShow() {
        return dialog.isShowing();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }


    public final class AppJsInterface {


        /**
         * 获取用户登录token
         */
        @JavascriptInterface
        public String getUserData() {
            String userId = "";
            String userName = "";
            String userToken = "";
            String userType = "";
            if (AccountManager.getInstance().getCurrentUser() != null) {
                userId = AccountManager.getInstance().getCurrentUser().getUserId() + "";
                userName = AccountManager.getInstance().getCurrentUser().getNickname();
                userToken = AccountManager.getInstance().getCurrentUser().getUserToken();
                userType = AccountManager.getInstance().getCurrentUser().getUserType() + "";
            }
            try {
                JSONObject params = new JSONObject();
                params.put("userId", userId);
                params.put("userName", userName);
                params.put("userToken", userToken);
                params.put("userType", userType);
                return params.toString();
            } catch (Exception e) {

            }
            return "";
        }

        /**
         * 获取app信息
         */
        @JavascriptInterface
        public String getProductData() {
            String uuid = new DeviceUuidFactory(context).getDeviceUuid().toString();
            String platformCode = "Android";
            String platformVersion = android.os.Build.VERSION.RELEASE;
            String appVersion = AndroidUtil.getVersion();
            int commendId = 0;
            String commendName = "";
            String server = Constants.BASICURL;

            server = server.substring(server.indexOf("//") + 2);
            server = server.substring(0, server.indexOf("/"));

            try {
                JSONObject params = new JSONObject();
                params.put("uuid", uuid);
                params.put("deviceId", AndroidUtil.getDiviceId());
                params.put("platformCode", platformCode);
                params.put("platformVersion", platformVersion);
                params.put("appVersion", appVersion);
                params.put("cmdId", commendId);
                params.put("cmdName", commendName);
                params.put("server", server);
                params.put("appChannel", Constants.Constants_APP_CHANNEL);
                return params.toString();
            } catch (Exception e) {
            }
            return "";
        }


    }
}
