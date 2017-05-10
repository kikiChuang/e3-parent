package com.jddfun.game.Act;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.RetrofitUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.WXPayBean;
import com.jddfun.game.bean.WeixinPayResult;
import com.zwxpay.android.h5_library.manager.CheckOderManager;
import com.zwxpay.android.h5_library.manager.WebViewManager;
import com.zwxpay.android.h5_library.utils.HttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DutyAct extends JDDBaseActivity {

    @BindView(R.id.pay)
    EditText mPay;
    @BindView(R.id.btn_pay)
    Button mBtnPay;
    private boolean isPaying = false;
    private String prepay_id;
    protected ProgressDialog loadingDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pay)
    public void onClick() {
//        getPay();
//        Log.d("JDD","DDDDD==="+internetConnected);
        String intTotlaMoney = mPay.getText().toString().trim();
        method(intTotlaMoney);
    }

    private void getPay() {
        String intTotlaMoney = mPay.getText().toString().trim();
        if (TextUtils.isEmpty(intTotlaMoney)) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!HttpClientManager.isInternetConnected(this)) {
            Toast.makeText(this, "网路连接失败,请稍后再试！", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

   private void method(String value){

       WXPayBean wxPayBean = new WXPayBean();
       wxPayBean.setPayType("1");
       wxPayBean.setSource("1");
       wxPayBean.setValue(value);
       String s = UtilsTools.getInstance().toJson(wxPayBean);
       final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
       Call<ResponseBody> wxPayCall = RetrofitUtils.postParam().WXpay(UtilsTools.getInstance().getString("accessToken", ""), requestBody);
       wxPayCall.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try {
                   String string = response.body().string();
                        Log.d("JDD","PAYYY=="+string);
                   JSONObject jsonObject = new JSONObject(string);
                   int code = jsonObject.getInt("code");
                   if(code==200){
                       String prepay_url =jsonObject.getJSONObject("data").getString("prepay_url") + "&type=android";
                       new WebViewManager(DutyAct.this, true).showWeiXinView(prepay_url);
                   }

               } catch (IOException e) {
                   e.printStackTrace();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               Log.d("JDD","PAYYY=dfgdsgf="+t.toString());
           }
       });

   }

    private class WXh5PayTask extends AsyncTask<Void, Void, WeixinPayResult> {
        private String money;
        private String payType;

        public WXh5PayTask(String money, String payType) {
            this.money = money;
            this.payType = payType;
        }

        @Override
        protected void onPreExecute() {
//            showLoading(false, "正在加载订单...");
        }

        @Override
        protected WeixinPayResult doInBackground(Void... arg0) {


            return null;
        }

        @Override
        protected void onPostExecute(WeixinPayResult weixinPayResult) {
            dismissMyLoading();
            if (weixinPayResult != null && !"".equals(weixinPayResult)) {
                if ("200".equals(weixinPayResult.getCode())) {
                            String prepay_url = weixinPayResult.getData().getPrepay_url() + "&type=android";
                            new WebViewManager(DutyAct.this, true).showWeiXinView(prepay_url);
                            isPaying = true;
                }
            } else {
                Toast.makeText(DutyAct.this, "系统繁忙", Toast.LENGTH_SHORT).show();
            }
//            textview.setText("初始");
        }
    }

    public void showLoading(boolean cancelble, String str) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setCancelable(cancelble);

        }
        loadingDialog.show();
        loadingDialog.setMessage(str);
    }

    public void dismissMyLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaying) {
//            textview.setText("查询中。。。");
            new CheckOderManager().checkState(this, prepay_id, new CheckOderManager.QueryPayListener() {
                @Override
                public void getPayState(String payState) {
                    //返回支付状态，做对应的UI和业务操作
                    if ("SUCCESS".equalsIgnoreCase(payState)) {
//                        textview.setText("支付成功");
                    } else if ("NOTPAY".equalsIgnoreCase(payState)) {
//                        textview.setText("未支付");
                    } else if ("CLOSED".equalsIgnoreCase(payState)) {
//                        textview.setText("已关闭");
                    } else if ("PAYERROR".equalsIgnoreCase(payState)) {
//                        textview.setText("支付失败");
                    } else {
//                        textview.setText("支付失败");
                    }
                    //把支付标记还原
                    isPaying = false;
                }
            });

        }
    }
}
