package com.jddfun.game.Act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.LogUtils;
import com.jddfun.game.bean.ActivityOrderParams;
import com.jddfun.game.bean.GameExcharge;
import com.jddfun.game.bean.NormalOrderParams;
import com.jddfun.game.bean.OrderResponse;
import com.jddfun.game.bean.OrderStatusParams;
import com.jddfun.game.bean.PayTypeBean;
import com.jddfun.game.dialog.CommonDialog;
import com.jddfun.game.dialog.DialogUtils;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxiaopay.mp.QuerySign;
import com.xiaoxiaopay.mp.XxBeiAPI;
import com.xiaoxiaopay.mp.XxBeiOrder;
import com.xiaoxiaopay.mp.XxBeiResult;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayAct extends JDDBaseActivity {
    @BindView(R.id.pay)
    EditText mPay;
    @BindView(R.id.btn_pay)
    Button mBtnPay;
    @BindView(R.id.money)
    TextView moneyBtn;
    @BindView(R.id.chongzhi_des)
    TextView chongzhiDes;
    @BindView(R.id.wechat_button)
    RelativeLayout wechatButton;
    @BindView(R.id.zhifubao_button)
    RelativeLayout zhifubaoButton;


    @BindView(R.id.back_icon)
    View back_icon;
    private GameExcharge gameExcharge;
    private IWXAPI api;

    private Handler handler = new Handler();

    private OrderResponse orderResponse;

    private String wxType;
    private String zhifuBaoType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);
        ButterKnife.bind(this);

        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra("payData"))) {
            finish();
            return;
        } else {
            String payJson = getIntent().getStringExtra("payData");
            Gson gson = new Gson();
            try {
                gameExcharge = gson.fromJson(payJson, GameExcharge.class);
            } catch (JsonSyntaxException e) {
                LogUtils.e("json解析异常", e.toString());
                finish();
                return;
            }
            if (gameExcharge == null) {
                finish();
                return;
            } else {
                if (gameExcharge.getGameOrderNumber() == null) {
                    //商城充值点击
                    MobclickAgent.onEvent(this, "store_0001");
                } else {
                    //商城礼包点击
                    MobclickAgent.onEvent(this, "store_0002");
                }
            }
        }
        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID);
        api.registerApp(Constants.WEIXIN_APPID);

        getPayType();

        initViewData();
    }


    private void getPayType() {
        ServiceFactory.getInstance().createService(JDDApiService.class).getPayType()
                .compose(RxUtils.<HttpResult<List<PayTypeBean>>>defaultSchedulers())
                .compose(this.<HttpResult<List<PayTypeBean>>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<List<PayTypeBean>>() {
                    @Override
                    public void onSuccess(List<PayTypeBean> list) {
                        if (null != list && list.size() > 0) {
                            for (PayTypeBean dataBean : list) {
                                if (dataBean.getName().contains("微信")) {
                                    wxType = dataBean.getValue();
                                    wechatButton.setVisibility(View.VISIBLE);
                                }

                                if (dataBean.getName().contains("支付宝")) {
                                    zhifuBaoType = dataBean.getValue();
                                    zhifubaoButton.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                    }

                });
    }

    private void initViewData() {
        moneyBtn.setText("¥ " + gameExcharge.getGameRechargeMoney());
    }

    @OnClick({R.id.btn_pay, R.id.wechat_button, R.id.back_icon, R.id.zhifubao_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_button:
                //微信
                //微信点击
                MobclickAgent.onEvent(this, "pay_0002");
                method(Integer.valueOf(gameExcharge.getGameRechargeMoney()), Integer.valueOf(wxType));
                break;
            case R.id.zhifubao_button:
                //支付宝
                //支付宝点击
                MobclickAgent.onEvent(this, "pay_0001");
                // method(Integer.valueOf(gameExcharge.getGameRechargeMoney()), 2);
                method(Integer.valueOf(gameExcharge.getGameRechargeMoney()), Integer.valueOf(zhifuBaoType));
                break;
            case R.id.back_icon:
                onBackPressed();
                break;

        }
    }


    private void method(int value, int payClient) {
        showLoading(false, "正在前往支付...");
        if (gameExcharge.getGameOrderNumber() != null && gameExcharge.getGameOrderNumber().getBizType() != 0) {
            reuqestActivityOrder(payClient);
        } else {
            requestNormalOrder(value, payClient);
        }

    }

    private void reuqestActivityOrder(final int payClient) {
        ActivityOrderParams params = new ActivityOrderParams();
        params.setBizTarget(gameExcharge.getGameOrderNumber().getBizTarget());
        params.setBizType(gameExcharge.getGameOrderNumber().getBizType());
        params.setPayType(payClient);
        params.setSource(1);
        ServiceFactory.getInstance().createService(JDDApiService.class).getActivityOrder(params)
                .compose(RxUtils.<HttpResult<OrderResponse>>defaultSchedulers())
                .compose(this.<HttpResult<OrderResponse>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<OrderResponse>() {
                    @Override
                    public void onSuccess(OrderResponse orderResponse) {
                        if (orderResponse != null) {
                            PayAct.this.orderResponse = orderResponse;
                            payUseClient(payClient, orderResponse);
                        } else {
                            onError(null, -1);
                        }

                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                    }

                });
    }

    private void requestNormalOrder(long value, final int payClient) {
        NormalOrderParams params = new NormalOrderParams();
        params.setPayType(payClient);
        params.setSource(1);
        params.setValue((int) value);

        ServiceFactory.getInstance().createService(JDDApiService.class).getNormalOrder(params)
                .compose(RxUtils.<HttpResult<OrderResponse>>defaultSchedulers())
                .compose(this.<HttpResult<OrderResponse>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<OrderResponse>() {
                    @Override
                    public void onSuccess(OrderResponse orderResponse) {
                        if (orderResponse != null) {
                            PayAct.this.orderResponse = orderResponse;
                            payUseClient(payClient, orderResponse);
                        } else {
                            onError(null, -1);
                        }


                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                    }

                });
    }


    //1微信，2支付宝
    private void payUseClient(int client, OrderResponse orderResponse) {
        switch (client) {
            case 1:
                dismissLoading();
                break;
            case 2:

                //威富通支付宝支付
                RequestMsg msg = new RequestMsg();
                msg.setTokenId(orderResponse.getToken_id());
                // 支付宝wap支付
                msg.setTradeType(MainApplication.PAY_NEW_ZFB_WAP);
                PayPlugin.unifiedH5Pay(PayAct.this, msg);
                dismissLoading();
                break;

            //贝富支付
            case 5:
                JDDApplication.mApp.initBeiFu(orderResponse.getPublicKey());
                XxBeiOrder order = new XxBeiOrder();// 创建订单
                order.setOrderId(orderResponse.getOrderId());// 写入订单号
                order.setOrderName(orderResponse.getOrderName());// 写入商品名
                order.setPrice(new BigDecimal(orderResponse.getPrice()));// 写入总金额
                order.setNotifyurl(orderResponse.getNotifyUrl());// 写入异步地址
                order.setPayType(Integer.valueOf(orderResponse.getPayType()));// 选择支付方式
                order.setSign(orderResponse.getSign());
                XxBeiAPI.pay(this, order, new XxBeiResult() {
                    @Override
                    public void payResult(int resultCode, String text) {
                        LogUtils.i("pay_result", "result_code: " + resultCode + "   result_text: " + text);
                        handlePayResult();
                    }
                });
                dismissLoading();
                break;
        }
    }


    private CommonDialog alertDialog;

    private boolean hasResult = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        handlePayResult();
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void handlePayResult() {
        hasResult = true;
        alertDialog = new CommonDialog(this);
        showLoading(true, "查询订单中....");
        postOrderStatus();
    }

    //获取订单信息，方便刷新
    public void postOrderStatus() {
        OrderStatusParams params = new OrderStatusParams();
        params.setValue(orderResponse.getOrderNum());
        ServiceFactory.getInstance().createService(JDDApiService.class).getOrderStatus(params)
                .compose(RxUtils.<HttpResult<Object>>defaultSchedulers())
                .compose(this.<HttpResult<Object>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object orderResponse) {
                        dismissLoading();
                        sendPostOrderBack(true);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                        sendPostOrderBack(false);
                    }

                });
    }


    public void sendPostOrderBack(boolean isSuccess) {
        if (isSuccess) {
            JDDEvent jddEvent = new JDDEvent(JDDEvent.TYPE_NOTFIY_PAY_SUCCESS);
            if (orderResponse != null) {
                jddEvent.setOrderNum(orderResponse.getOrderNum());
            }
            RxBus.getInstance().post(jddEvent);
            DialogUtils.getPayResultDialog(alertDialog, this, true);
            //支付成功
            MobclickAgent.onEvent(this, "pay_0003");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    finish();
                }
            }, 2000);
        } else {
            //支付失败
            MobclickAgent.onEvent(this, "pay_0004");
            JDDEvent jddEvent = new JDDEvent(JDDEvent.TYPE_NOTFIY_PAY_FAIL);
            if (orderResponse != null) {
                jddEvent.setOrderNum(orderResponse.getOrderNum());
            }
            RxBus.getInstance().post(jddEvent);
            DialogUtils.getPayResultDialog(alertDialog, this, false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                }
            }, 2000);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!hasResult) {
            JDDEvent jddEvent = new JDDEvent(JDDEvent.TYPE_NOTFIY_PAY_FAIL);
            if (orderResponse != null) {
                jddEvent.setOrderNum(orderResponse.getOrderNum());
            }
            RxBus.getInstance().post(jddEvent);
        }
    }
}