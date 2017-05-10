package com.jddfun.game.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jddfun.game.Act.InfoReceivingActivity;
import com.jddfun.game.Adapter.SignedAdapter;
import com.jddfun.game.Adapter.showArwardDialogAdapter;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.R;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.ToastUtils;
import com.jddfun.game.View.ContainsEmojiEditText;
import com.jddfun.game.View.MyScrollView;
import com.jddfun.game.bean.CombineRes;
import com.jddfun.game.bean.CombineReuqst;
import com.jddfun.game.bean.Debris;
import com.jddfun.game.bean.GetReceiverInfoBean;
import com.jddfun.game.bean.ProfitRulesBean;
import com.jddfun.game.bean.SignInfo;
import com.jddfun.game.bean.SortingInfo;
import com.jddfun.game.bean.Weal;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

/**
 * 创建dialog总类
 *
 * @author yuanyaming
 */
public class DialogUtils {

    public interface reviseNameInterFace {
        void setName(String name);
    }

    public interface Dialogrubber {
        void actrubber();
    }

    public interface popwInface {
        void oncli(View v);

        void dismiss();
    }

    public static PopupWindow downPopwindow(Context constants, String title, View v, int popWidth, final popwInface mpopwInface) {
        View contentView = LayoutInflater.from(constants).inflate(R.layout.pop_down, null);
        final PopupWindow popWindow = new PopupWindow(contentView, 380, 460);
        popWindow.setAnimationStyle(R.style.anim);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.setOutsideTouchable(true);
        TextView pop_down_tv = (TextView) contentView.findViewById(R.id.pop_down_tv);
        pop_down_tv.setText(title);
        int popupWidth = popWindow.getWidth();
        popWindow.showAsDropDown(v, (popWidth - popupWidth) / 2, 0);
        pop_down_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mpopwInface.oncli(v);
                popWindow.dismiss();
            }
        });
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mpopwInface.dismiss();
            }
        });
        return popWindow;
    }

    /**
     * 规则
     *
     * @param context
     */
    public static void showArwardDialog(final AlertDialog builder1, final Context context, ProfitRulesBean mMprofitRulesBean) {
//		builder1.setCanceledOnTouchOutside(false);  //点击屏幕不消失


        View view = LayoutInflater.from(context).inflate(R.layout.dialog_broadcast, null);
        TextView dialog_broadcase_context = (TextView) view.findViewById(R.id.dialog_broadcase_context);
        RecyclerView dialog_broadcase_rl = (RecyclerView) view.findViewById(R.id.dialog_broadcase_rl);
        TextView dialog_broadcase_ruleDescBottom = (TextView) view.findViewById(R.id.dialog_broadcase_ruleDescBottom);
        MyScrollView dialog_broadcase = (MyScrollView) view.findViewById(R.id.dialog_broadcase);
        dialog_broadcase.smoothScrollTo(0, 20);


        dialog_broadcase_context.setText(mMprofitRulesBean.getRuleDescTop().replace("\\", " ").replace("n", "\n"));
        dialog_broadcase_ruleDescBottom.setText(mMprofitRulesBean.getRuleDescBottom().replace("\\", " ").replace("n", "\n"));
        List<ProfitRulesBean.RuleDetailsBean> ruleDetails = mMprofitRulesBean.getRuleDetails();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        showArwardDialogAdapter mPersonalCenterfAdapter = new showArwardDialogAdapter(context, ruleDetails);
        dialog_broadcase_rl.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        dialog_broadcase_rl.setAdapter(mPersonalCenterfAdapter);
        dialog_broadcase_rl.setItemAnimator(new DefaultItemAnimator());


        view.findViewById(R.id.dialog_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                builder1.dismiss();
            }
        });
        builder1.setView(view, 0, 0, 0, 0);
        builder1.show();
    }

    //擦除
    public static void rubber(final CommonDialog builder1, final Context context, List<SortingInfo.EraserListBean> mEraserList, int mEraserMe, int mEraserTimes, final Dialogrubber dioIn) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_dialog_rubber, null);
        TextView dialog_rubber_frequency = (TextView) view.findViewById(R.id.dialog_rubber_frequency);
        TextView dialot_runnber_money = (TextView) view.findViewById(R.id.dialot_runnber_money);
        if (mEraserList.get(mEraserMe).getLabel().equals("0")) {
            dialog_rubber_frequency.setText("擦除" + (mEraserTimes - mEraserMe) + "/" + mEraserTimes);
            dialot_runnber_money.setText("免费擦除)");
        } else {
            dialog_rubber_frequency.setText("擦除" + (mEraserTimes - mEraserMe) + "/" + mEraserTimes);
            dialot_runnber_money.setText(mEraserList.get(mEraserMe).getLabel() + ")");
        }
        view.findViewById(R.id.dialog_cubber_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dioIn.actrubber();
                builder1.dismiss();
            }
        });

        view.findViewById(R.id.ubber_dialog_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                builder1.dismiss();
            }
        });
        builder1.setContentView(view);
        builder1.show();
    }

    //修改姓名
    public static void reviseName(final CommonDialog builder1, final Context context, final reviseNameInterFace reviseInter) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_revise_name, null);
        view.findViewById(R.id.ubber_dialog_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                builder1.dismiss();
            }
        });
        view.findViewById(R.id.revise_name_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reviseInter.setName(((ContainsEmojiEditText) view.findViewById(R.id.revise_setName)).getText().toString());
                builder1.dismiss();
            }
        });
        builder1.setContentView(view);
        builder1.show();
    }

    //恭喜福利获取
    public static void wealSuccess(final CommonDialog builder1, final RxFragment context, final Weal weal) {
//        if (weal.isVitrul()) {
//            wealSuccessVirturlAddress(builder1, context, weal);
//            return;
//        }
        ServiceFactory.getInstance().createService(JDDApiService.class).getReceiverInfo()
                .compose(RxUtils.<HttpResult<GetReceiverInfoBean>>defaultSchedulers())
                .compose(context.<HttpResult<GetReceiverInfoBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<GetReceiverInfoBean>() {
                    @Override
                    public void onSuccess(GetReceiverInfoBean userInfo) {

                        if (TextUtils.isEmpty(userInfo.getReceiverAddress())) {
                            wealSuccessNoAddress(builder1, context, weal);
                        } else {
                            wealSuccessHasAddress(builder1, context, weal, userInfo);
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        ToastUtils.show(context.getContext(), "网络请求失败");
                    }
                });


    }

    private static void wealSuccessHasAddress(final CommonDialog builder1, final RxFragment context, final Weal weal, GetReceiverInfoBean userInfo) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.dialog_weal_success01, null);
        builder1.getWindow().setBackgroundDrawable(new BitmapDrawable());
        ((TextView) view.findViewById(R.id.prize_obj)).setText("在排行榜 获得 " + weal.getAwardsName());
        ((TextView) view.findViewById(R.id.username)).setText(userInfo.getReceiverName());
        ((TextView) view.findViewById(R.id.concact)).setText(userInfo.getReceiverMobile());
        ((TextView) view.findViewById(R.id.address)).setText(userInfo.getReceiverAddress());
        view.findViewById(R.id.modify_address).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getContext(), InfoReceivingActivity.class));
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JDDUtils.getPrize(weal, context, new JDDUtils.GetPrizeCallBack() {
                    @Override
                    public void getSuccess() {
                        //确认领取
                        ToastUtils.show(context.getContext(), "获取成功");
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_WEAL_SUCCESS));
                        builder1.dismiss();
                    }

                    @Override
                    public void getFailed() {
                        ToastUtils.show(context.getContext(), "获取失败");
                        builder1.dismiss();
                    }
                });
            }
        });
        builder1.setContentView(view);
        builder1.show();
    }

    private static void wealSuccessNoAddress(final CommonDialog builder1, final RxFragment context, final Weal weal) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.dialog_weal_success02, null);
        ((TextView) view.findViewById(R.id.prize_obj)).setText("在排行榜 获得 " + weal.getAwardsName());
        builder1.setContentView(view);
        view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getContext(), InfoReceivingActivity.class));
            }
        });
        builder1.show();
    }


    private static void wealSuccessVirturlAddress(final CommonDialog builder1, final RxFragment context, final Weal weal) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.dialog_weal_success03, null);
        ((TextView) view.findViewById(R.id.prize_obj)).setText("在排行榜 获得 " + weal.getAwardsName());
        ((TextView) view.findViewById(R.id.text)).setText(weal.getAwardsName() + "已发放至您的账户中");
        builder1.setContentView(view);
        view.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JDDUtils.getPrize(weal, context, new JDDUtils.GetPrizeCallBack() {
                    @Override
                    public void getSuccess() {
                        //确认领取
                        ToastUtils.show(context.getContext(), "获取成功");
                        builder1.dismiss();
                        RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_WEAL_SUCCESS));
                    }

                    @Override
                    public void getFailed() {
                        ToastUtils.show(context.getContext(), "获取失败");
                        builder1.dismiss();

                    }
                });
            }
        });
        builder1.show();
    }


    public static void getPrizeInfo(final CommonDialog builder1, final RxFragment context, final Weal weal) {
        View view = LayoutInflater.from(context.getContext()).inflate(R.layout.dialog_weal_has_get, null);
        ImageView getImage = (ImageView) view.findViewById(R.id.get_image);
        TextView getInfo = (TextView) view.findViewById(R.id.get_info);
        Button confirm_or_paste = (Button) view.findViewById(R.id.confirm_or_paste);
        if (!weal.isVitrul()) {
            getImage.setImageResource(R.mipmap.entity_gift_icon);
            getInfo.setText(weal.getReceiveRemark());
            confirm_or_paste.setText("复制快递单号");
        } else {
            getInfo.setText(weal.getReceiveRemark());
            getImage.setImageResource(R.mipmap.viturl_gift_icon);
            confirm_or_paste.setText("确认");
        }
        confirm_or_paste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!weal.isVitrul()) {
                    ClipboardManager cm = (ClipboardManager) context.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(weal.getReceiveRemark());
                    ToastUtils.show(context.getContext(), "已复制");
                    builder1.dismiss();
                } else {
                    builder1.dismiss();
                }
            }
        });
        builder1.setContentView(view);
        builder1.show();
    }


    public static void getPayResultDialog(final CommonDialog dialog, final Context context, boolean isSuccess) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay_result, null);
        ImageView getImage = (ImageView) view.findViewById(R.id.get_image);
        TextView payDes1 = (TextView) view.findViewById(R.id.pay_des1);
        TextView payDes2 = (TextView) view.findViewById(R.id.pay_des2);
        getImage.setImageResource(isSuccess ? R.mipmap.pay_success_icon : R.mipmap.pay_fail_icon);
        payDes1.setText(isSuccess ? "支付成功" : "支付失败");
        payDes2.setText(isSuccess ? "金叶子极速升级爆表" : "很遗憾，金叶子未充值成功");
        dialog.setContentView(view);
        dialog.show();
    }


    public static void showFirstLogineward(final CommonDialog dialog, final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_first_login, null);


        dialog.setContentView(view);
        dialog.show();
    }


    public static void showSignInDialog(final CommonDialog dialog, final RxAppCompatActivity context, final SignInfo signInfo) {

        View view = LayoutInflater.from(context).inflate(R.layout.signed, null);
        final Button btn_sign = (Button) view.findViewById(R.id.btn_sign);
        final RecyclerView recyView_sing = (RecyclerView) view.findViewById(R.id.recyView_sing);

        final SignedAdapter signedAdapter = new SignedAdapter(signInfo);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(JDDApplication.getAppContext(), 4, GridLayoutManager.VERTICAL, false);
        recyView_sing.setAdapter(signedAdapter);
        recyView_sing.setLayoutManager(gridLayoutManager);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceFactory.getInstance().createService(JDDApiService.class).sign()
                        .compose(RxUtils.<HttpResult<String>>defaultSchedulers())
                        .compose(context.<HttpResult<String>>bindToLifecycle())
                        .subscribe(new HttpResultSubscriber<String>() {
                            @Override
                            public void onSuccess(String num) {
                                if (!TextUtils.isEmpty(num)) {
                                    ToastUtils.show(context, "恭喜获得金叶子*" + num);
                                    RxBus.getInstance().post(new JDDEvent(JDDEvent.TYPE_SIGN_SUCCESS));
                                } else {
                                    onError(null, -1);
                                }

                            }

                            @Override
                            public void onError(Throwable e, int code) {
                                if (code == 101) {
                                    ToastUtils.show(context, "你今天已经签到过了");
                                } else if (code == 401) {
                                    ToastUtils.show(context, "用户没有登录");
                                } else {
                                    ToastUtils.show(context, "签到失败");
                                }
                            }

                        });
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }


    public static void showCompoundDialog(final CommonDialog builder1, final RxAppCompatActivity context, final Debris debris, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.compound_dialog_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        TextView title01 = (TextView) view.findViewById(R.id.title_01);
        TextView title02 = (TextView) view.findViewById(R.id.title_02);
        View close = view.findViewById(R.id.close);
        title01.setText(debris.getRemark1());
        title02.setText("碎片可在" + debris.getFragmentSource() + " 中获得");
        Glide.with(context).load(debris.getImage()).into(imageView);
        Button confirm_or_num = (Button) view.findViewById(R.id.confirm_or_num);
        builder1.setCanceledOnTouchOutside(true);
        if (debris.canCompound()) {
            confirm_or_num.setText("合成碎片");
            confirm_or_num.setClickable(true);
            confirm_or_num.setBackgroundResource(R.mipmap.combine_color_bg);
            confirm_or_num.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CombineReuqst request = new CombineReuqst();
                    request.setFragmentId(debris.getFragmentId());
                    ServiceFactory.getInstance().createService(JDDApiService.class).deBrisCombine(request)
                            .compose(RxUtils.<HttpResult<CombineRes>>defaultSchedulers())
                            .compose(context.<HttpResult<CombineRes>>bindToLifecycle())
                            .subscribe(new HttpResultSubscriber<CombineRes>() {
                                @Override
                                public void onSuccess(CombineRes combineRes) {
                                    if (combineRes.isSuccess()) {
                                        new CombineSuccessDialog(context, debris.getAwardsImage(), debris.getAwardsName()).show();
                                        JDDEvent jddEvent = new JDDEvent(JDDEvent.TYPE_NOTIFY_COMBINE_SUCCESS);
                                        jddEvent.setPosition(position);
                                        RxBus.getInstance().post(jddEvent);
                                        builder1.dismiss();
                                    } else {
                                        onError(new Throwable(), -1);
                                    }
                                }

                                @Override
                                public void onError(Throwable e, int code) {
                                    ToastUtils.show(context, "合成失败");
                                    builder1.dismiss();
                                }

                            });

                }
            });
        } else {
            confirm_or_num.setText(debris.getFragmentNum() + "/" + debris.getChangeNum());
            confirm_or_num.setClickable(false);
            confirm_or_num.setBackgroundResource(R.mipmap.combine_gray_bg);
        }
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder1.dismiss();
            }
        });
        builder1.setContentView(view);
        builder1.show();
    }


}
