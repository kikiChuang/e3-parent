package com.jddfun.game.Act;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.DataCleanManager;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.UserPhoneBindInfoBean;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 * Created by MACHINE on 2017/4/6.
 */

public class SetUpAct extends JDDBaseActivity implements View.OnClickListener {

    @BindView(R.id.act_head_head)
    LinearLayout act_head_head;
    @BindView(R.id.iv_iv_back)
    ImageView iv_iv_back;
    @BindView(R.id.tv_activity_title)
    TextView tv_activity_title;
    @BindView(R.id.set_up_notice_rl)
    RelativeLayout set_up_notice_rl;
    @BindView(R.id.phone_binding)
    RelativeLayout phone_binding;
    @BindView(R.id.iv_back_rl)
    RelativeLayout iv_back_rl;
    @BindView(R.id.set_up_cache)
    TextView set_up_cachel;
    @BindView(R.id.set_up_bind)
    ImageView set_up_bind;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.set_up_no_bing)
    TextView set_up_no_bing;
    @BindView(R.id.set_up_withdraw_from)
    TextView set_up_withdraw_from;


    String bindPho = "";
    private boolean mExistsFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        act_head_head.setBackgroundResource(R.mipmap.head_bag);
        iv_iv_back.setImageResource(R.mipmap.left_back);
        tv_activity_title.setTextColor(Color.parseColor("#ffffff"));
        tv_activity_title.setText("设置");
        try {
            set_up_cachel.setText(DataCleanManager.getTotalCacheSize(SetUpAct.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        set_up_withdraw_from.setVisibility(JDDUtils.isLogin() ? View.VISIBLE : View.GONE);
        getUserPhoneBindInfo();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                bindPho = data.getStringExtra("phone");

                set_up_bind.setVisibility(View.VISIBLE);
                set_up_bind.setImageResource(R.mipmap.bind);
                set_up_no_bing.setText(bindPho);
                iv_right.setVisibility(View.GONE);
                //绑定成功 刷新金叶子
                RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_BIND_PHONE_SUCCESS));
                mExistsFlag = true;
                break;
        }
    }

    @OnClick({R.id.set_up_notice_rl, R.id.phone_binding, R.id.set_up_clean_up_rl, R.id.set_up_help_center, R.id.frag_info_set_up, R.id.iv_back_rl, R.id.set_up_withdraw_from})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_up_notice_rl: //通知中心
                JDDUtils.ifJumpToLoginAct(this, new JDDUtils.JumpListener() {
                    @Override
                    public void jumpToTarget() {
                        startActivity(new Intent(SetUpAct.this, IsNotification.class));
                    }
                });
                break;
            case R.id.phone_binding: //手机绑定
                //手机绑定点击
                JDDUtils.ifJumpToLoginAct(this, new JDDUtils.JumpListener() {
                    @Override
                    public void jumpToTarget() {
                        if (mExistsFlag) {
                            MobclickAgent.onEvent(SetUpAct.this, "setting_0001");
                            UtilsTools.getInstance().show("手机已绑定");
                        } else {
                            startActivityForResult(new Intent(SetUpAct.this, PhonebindingActivity.class).putExtra("pop", bindPho), 1);
                        }
                    }
                });
                break;
            case R.id.iv_back_rl:
                finish();
                break;
            case R.id.set_up_clean_up_rl: //清除缓存
                //清楚缓存点击
                MobclickAgent.onEvent(this, "setting_0003");
                DataCleanManager.clearAllCache(SetUpAct.this);
                UtilsTools.getInstance().show(" 清除成功");
                set_up_cachel.setText("0KB");
                break;
            case R.id.set_up_help_center: // 帮助中心
                //帮助中心点击
                MobclickAgent.onEvent(this, "setting_0004");
                JDDUtils.jumpToH5Activity(this, Constants.HELP_CENTER + "0", "帮助中心", false);
                break;
            case R.id.frag_info_set_up: //关于我们
                //关于我们
                MobclickAgent.onEvent(this, "setting_0005");
                startActivity(new Intent(SetUpAct.this, AboutUsAct.class));
                break;
            case R.id.set_up_withdraw_from: //退出登录
                //退出登录
                MobclickAgent.onEvent(this, "setting_0006");
                JDDUtils.WithdrawFrom();
                UtilsTools.getInstance().show("退出登录");

                //退出QQ登录
                Tencent tencent = Tencent.createInstance(Constants.QQ_APPID, this.getApplicationContext());
                if (tencent.isSessionValid()) {
                    tencent.logout(this.getApplicationContext());
                }

                finish();
                break;
        }
    }

    public void getUserPhoneBindInfo() {
        ServiceFactory.getInstance().createService(JDDApiService.class).getUserPhoneBindInfo()
                .compose(RxUtils.<HttpResult<UserPhoneBindInfoBean>>defaultSchedulers())
                .compose(this.<HttpResult<UserPhoneBindInfoBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<UserPhoneBindInfoBean>() {
                    @Override
                    public void onSuccess(UserPhoneBindInfoBean list) {
                        if (list.isExistsFlag()) {
                            bindPho = list.getPhone();
                            mExistsFlag = list.isExistsFlag();

                            set_up_bind.setVisibility(View.VISIBLE);
                            set_up_bind.setImageResource(R.mipmap.bind);
                            set_up_no_bing.setText(bindPho);
                            iv_right.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }
}
