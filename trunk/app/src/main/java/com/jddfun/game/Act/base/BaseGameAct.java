package com.jddfun.game.Act.base;

import android.content.Intent;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;

import com.jddfun.game.Act.LoginModeAct;
import com.jddfun.game.Act.PayAct;
import com.jddfun.game.Utils.AndroidUtil;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.DeviceUuidFactory;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.LogUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.manager.AccountManager;

import org.json.JSONObject;

/**
 * Created by xuhongliang on 2017/4/10.
 */

public class BaseGameAct extends JDDBaseActivity {

    protected static final int GAME_PAY_REQUEST = 1;


    public final class AppJsInterface {

        protected String GameOrderNumber = "";
        public int GameRechargeState = 0;
        public int GameRechangeSuccess = 0;


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
            String uuid = new DeviceUuidFactory(BaseGameAct.this).getDeviceUuid().toString();
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

        /**
         * 游戏登录
         */
        @JavascriptInterface
        public void gameLogin() {
            String token = UtilsTools.getInstance().getString("accessToken", "");
            if (TextUtils.isEmpty(token)) {
                startActivity(new Intent(BaseGameAct.this, LoginModeAct.class));
            }
        }

        /**
         * 游戏充值
         */
        @JavascriptInterface
        public void gameRecharge(final String result) {
            JDDUtils.ifJumpToLoginAct(BaseGameAct.this, new JDDUtils.JumpListener() {
                @Override
                public void jumpToTarget() {
                    LogUtils.e("gamejdd", "进入游戏充值方法gameRecharge");
                    AppJsInterface.this.GameRechargeState = 0;
                    AppJsInterface.this.GameRechangeSuccess = 0;
                    if (!TextUtils.isEmpty(result)) {
                        Intent intent = new Intent(BaseGameAct.this, PayAct.class);
                        intent.putExtra("payData", result);
                        BaseGameAct.this.startActivityForResult(intent, GAME_PAY_REQUEST);
                    }
                }
            });


        }

        public void setPayResult(boolean isSuccess, String orderNum) {
            if (isSuccess) {
                this.GameRechangeSuccess = 1;
                this.GameRechargeState = 1;
                this.GameOrderNumber = orderNum;
            } else {
                this.GameRechangeSuccess = 0;
                this.GameRechargeState = 1;
                this.GameOrderNumber = orderNum;
            }

        }


        /**
         * 游戏充值结束返回
         *
         * @return
         */
        @JavascriptInterface
        public String gameBack() {
            String result = "";
            JSONObject jo = new JSONObject();
            try {
                jo.put("GameOrderNumber", GameOrderNumber);
                jo.put("GameRechangeSuccess", GameRechangeSuccess);
                jo.put("GameRechargeState", GameRechargeState);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = jo.toString();
            LogUtils.e("gamejdd", "gameBack:" + result);

            return result;

        }

        /**
         * 返回主界面
         *
         * @return
         */
        @JavascriptInterface
        public void backHome() {
            onBackPressed();
        }


        /**
         * 保持屏幕常亮
         */
        @JavascriptInterface
        public void idleTimerDisabled() {
            BaseGameAct.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BaseGameAct.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });

        }
    }

}