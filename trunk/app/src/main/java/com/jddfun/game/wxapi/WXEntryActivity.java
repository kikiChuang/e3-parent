package com.jddfun.game.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.jddfun.game.Utils.Constants;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.RxBus;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.net.URLEncoder;

/**
 * Created by vincent on 2017/4/11.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private String TAG = "WXEntryActivity";

    private IWXAPI api;

//    private String WX_APP_ID = Constants.WEIXIN_APPID;
//    private String WX_APP_SECRET = Constants.WEIXIN_APPSECRET;
    // 获取第一步的code后，请求以下链接获取access_token
//    private String getCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
//    private String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
//    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID, false);
        api.handleIntent(getIntent(), this);

//        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                String code = ((SendAuth.Resp) resp).code;
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();

//                getAccess_token(code);
//                LoginAct.WX_CODE = code;
                JDDEvent jddEvent = new JDDEvent(JDDEvent.TYPE_WX_LOGIN);
                jddEvent.setCode(code);
                RxBus.getInstance().post(jddEvent);

                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }


//    private void getAccess_token(String code) {
//        LogUtils.debug("---->code=" + code);
//
//        getCodeRequest = getCodeRequest.replace("APPID", urlEncodeUTF8(WX_APP_ID));
//        getCodeRequest = getCodeRequest.replace("SECRET", urlEncodeUTF8(WX_APP_SECRET));
//        getCodeRequest = getCodeRequest.replace("CODE", urlEncodeUTF8(code));
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(getCodeRequest)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(WXEntryActivity.this, "通过code获取Access_token没有成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String s = response.body().string();
//                            JSONObject obj = new JSONObject(s);
//                            String access_token = obj.getString("access_token");
//                            int expires_in = obj.getInt("expires_in");
//                            String refresh_token = obj.getString("refresh_token");
//                            String openid = obj.getString("openid");
//                            String scope = obj.getString("scope");
//                            String unionid = obj.getString("unionid");
//
//                            LoginAct.ACCESS_TOKEN = access_token;
//                            LoginAct.OPENID = openid;
//
////                            getUserInfo(access_token, openid);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//            }
//        });
//    }


//    /**
//     * 获取用户个人信息的URL
//     */
//    private void getUserInfo(String access_token, String openid) {
//        getUserInfo = getUserInfo.replace("ACCESS_TOKEN", urlEncodeUTF8(access_token));
//        getUserInfo = getUserInfo.replace("OPENID", urlEncodeUTF8(openid));
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(getUserInfo)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(WXEntryActivity.this, "通过Access_token获取个人信息没有成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject obj = new JSONObject(response.body().string());
//                            String openid = obj.getString("openid");
//                            String nickname = obj.getString("nickname");
//                            int sex = obj.getInt("sex");
//                            String language = obj.getString("language");
//                            String city = obj.getString("city");
//                            String province = obj.getString("province");
//                            String country = obj.getString("country");
//                            String headimgurl = obj.getString("headimgurl");
//                            String unionid = obj.getString("unionid");
//
////                            WXUserIfo wxUserIfo = (WXUserIfo) UtilsTools.getInstance().fromJson(response.body().string(), WXUserIfo.class);
//
//
//                            Toast.makeText(WXEntryActivity.this, "授权成功：" + nickname, Toast.LENGTH_SHORT).show();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//
//            }
//        });
//
//
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    private String urlEncodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
