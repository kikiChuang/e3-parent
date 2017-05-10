package com.jddfun.game.Act.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jddfun.game.Act.FundDetailAct;
import com.jddfun.game.Act.InfoReceivingActivity;
import com.jddfun.game.Act.NoticeCentralityAct;
import com.jddfun.game.Act.SetUpAct;
import com.jddfun.game.Act.WealAct;
import com.jddfun.game.Act.base.JDDBaseActivity;
import com.jddfun.game.R;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.JDDUtils;
import com.jddfun.game.Utils.ToastUtils;
import com.jddfun.game.Utils.UriUtils;
import com.jddfun.game.Utils.UtilsTools;
import com.jddfun.game.bean.InfoName;
import com.jddfun.game.bean.Nickname;
import com.jddfun.game.bean.UnreadCountBean;
import com.jddfun.game.bean.UserPersonalBean;
import com.jddfun.game.dialog.CommonDialog;
import com.jddfun.game.dialog.DialogUtils;
import com.jddfun.game.event.JDDEvent;
import com.jddfun.game.manager.AccountManager;
import com.jddfun.game.net.JDDApiService;
import com.jddfun.game.net.RxBus;
import com.jddfun.game.net.retrofit.HttpResult;
import com.jddfun.game.net.retrofit.RxUtils;
import com.jddfun.game.net.retrofit.factory.ServiceFactory;
import com.jddfun.game.net.retrofit.subscriber.HttpResultSubscriber;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 我的界面
 * Created by MACHINE on 2017/3/22.
 */

public class InfoFragment extends BaseFragment implements View.OnClickListener {

    private TextView mCount;
    private TextView mSafe;
    private CircleImageView iv_personal_icon;
    private FrameLayout llInfo;
    private ImageView iv_iv_back;
    private ImageView iv_head_right;
    private RelativeLayout fra_info_money;
    private LinearLayout revise_name;
    private Dialog dialog;
    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;
    private TextView cancel;
    private RelativeLayout mMFrag_info_set_up;
    private RelativeLayout mFrag_info_receiving_center;
    private LinearLayout iv_head_right_ll;
    private TextView mInfo_useAmount;
    private TextView info_name;
    private ImageView mInfo_imageView2;
    private RelativeLayout mInfo_client_service_center;
    private RelativeLayout mInfo_safe;
    private TextView mInfo_recharge;

    private View msg_icon;

    private final int TAKE_A_PICTURE = 1; //拍照
    private final int PHOTO_ALBUM = 3;//相册
    private final int UPDATA = 2;//刷新
    private final int CHOOSE_BIG_PICTURE = 4; //裁剪

    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_info);
        llInfo = getViewById(R.id.ll_info);
        mCount = getViewById(R.id.count);
        mSafe = getViewById(R.id.safe);
        iv_personal_icon = getViewById(R.id.iv_personal_icon);
        fra_info_money = getViewById(R.id.fra_info_money);
        iv_iv_back = getViewById(R.id.iv_iv_back);
        iv_head_right = getViewById(R.id.iv_head_right);
        revise_name = getViewById(R.id.revise_name);
        mMFrag_info_set_up = getViewById(R.id.frag_info_set_up);
        mFrag_info_receiving_center = getViewById(R.id.frag_info_receiving_center);
        iv_head_right_ll = getViewById(R.id.iv_head_right_ll);
        mInfo_useAmount = getViewById(R.id.info_useAmount);
        info_name = getViewById(R.id.info_name);
        mInfo_imageView2 = getViewById(R.id.info_imageView2);
        mInfo_client_service_center = getViewById(R.id.info_client_service_center);
        mInfo_safe = getViewById(R.id.info_safe);
        mInfo_recharge = getViewById(R.id.info_recharge);
        msg_icon = getViewById(R.id.msg_icon);
        networkData();
        getHasUnReaderMsg();
        RxBus.getInstance().toObservable(JDDEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<JDDEvent>bindToLifecycle())
                .subscribe(new Action1<JDDEvent>() {
                    @Override
                    public void call(JDDEvent Jddevent) {
                        if (Jddevent.getType() == JDDEvent.TYPE_NOTFIY_LOGIN_STATE_CHANGED) {
                            if (!JDDUtils.isLogin()) {
                                iv_personal_icon.setImageResource(R.mipmap.tacitly_approve);
                                info_name.setText("登录·注册");
                                mInfo_useAmount.setText("0");
                                mInfo_imageView2.setVisibility(View.GONE);
                                revise_name.setOnClickListener(null);
                                msg_icon.setVisibility(View.GONE);
                            } else {
                                networkData();
                                getHasUnReaderMsg();
                            }

                        } else if (Jddevent.getType() == JDDEvent.TYPE_GAME_BACK || Jddevent.getType() == Jddevent.TYPE_SIGN_SUCCESS
                                || Jddevent.getType() == JDDEvent.TYPE_BIND_PHONE_SUCCESS || Jddevent.getType() == JDDEvent.TYPE_NOTFIY_PAY_SUCCESS
                                || Jddevent.getType() == JDDEvent.TYPE_CLEAR_SUCCESS) {
                            if (JDDUtils.isLogin()) {
                                networkData();
                            }
                        }

                    }
                });
    }

    @Override
    public void onUserVisible() {
        super.onUserInvisible();
        getHasUnReaderMsg();
    }

    private void setData(UserPersonalBean mUserPersonalBean) {
        mInfo_useAmount.setText(String.valueOf(mUserPersonalBean.getUseAmount()));
        info_name.setText(mUserPersonalBean.getNickname());
        if (mUserPersonalBean.isNicknameFlag()) {
            mInfo_imageView2.setVisibility(View.VISIBLE);
            revise_name.setOnClickListener(this);
        } else {
            mInfo_imageView2.setVisibility(View.GONE);
            revise_name.setOnClickListener(null);
        }
        if (TextUtils.isEmpty(mUserPersonalBean.getHeadImg())) {
            iv_personal_icon.setImageResource(R.mipmap.tacitly_approve);
        } else {
            Glide.with(getActivity())
                    .load(mUserPersonalBean.getHeadImg())
                    .into(iv_personal_icon);
        }
    }

    @Override
    protected void setListener() {
        fra_info_money.setOnClickListener(this);
        iv_personal_icon.setOnClickListener(this);
        info_name.setOnClickListener(this);
        revise_name.setOnClickListener(this);
        mMFrag_info_set_up.setOnClickListener(this);
        mFrag_info_receiving_center.setOnClickListener(this);
        iv_head_right.setOnClickListener(this);
        iv_head_right_ll.setOnClickListener(this);
        iv_head_right.setOnClickListener(this);
        mInfo_client_service_center.setOnClickListener(this);
        mInfo_safe.setOnClickListener(this);
        mInfo_recharge.setOnClickListener(this);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(final View v) {
        //点击设置
        if (v.getId() == R.id.frag_info_set_up) {
            //设置点击
            MobclickAgent.onEvent(getActivity(), "mine_0009");
            startActivity(new Intent(getActivity(), SetUpAct.class));
            return;
        }


        //点击充值
        if (v.getId() == R.id.info_recharge) {
            //我的充值点击
            MobclickAgent.onEvent(getActivity(), "mine_0004");
            JDDUtils.jumpToH5Activity(getActivity(), Constants.SHOP_URL, "商城", false);
            return;
        }


        JDDUtils.ifJumpToLoginAct(getActivity(), new JDDUtils.JumpListener() {

            @Override
            public void jumpToTarget() {
                switch (v.getId()) {
                    case R.id.info_client_service_center: //客服中心
                        //客服点击
                        MobclickAgent.onEvent(getActivity(), "mine_0008");
                        if (Unicorn.isServiceAvailable()) {
                            setUserData();
                        }
                        break;
                    case R.id.fra_info_money: //资金明细
                        //资金明细点击
                        MobclickAgent.onEvent(getActivity(), "mine_0005");
                        JDDUtils.ifJumpToLoginAct(getActivity(), new JDDUtils.JumpListener() {
                            @Override
                            public void jumpToTarget() {
                                startActivity(new Intent(getActivity(), FundDetailAct.class));
                            }
                        });
                        break;
                    case R.id.iv_personal_icon:  //头像
                        //我的头像点击
                        MobclickAgent.onEvent(getActivity(), "mine_0002");
                        showDialog();
                        break;
                    case R.id.info_name:  //登录注册
                        //登录注册点击
                        MobclickAgent.onEvent(getActivity(), "mine_0002");
                        showDialog();
                        break;
                    case R.id.info_safe: //福利
                        //我的福利点击
                        MobclickAgent.onEvent(getActivity(), "mine_0006");
                        startActivity(new Intent(getActivity(), WealAct.class));
                        break;
                    case R.id.revise_name: //修改姓名
                        //我的昵称
                        MobclickAgent.onEvent(getActivity(), "mine_0003");
                        DialogUtils.reviseName(new CommonDialog(getActivity()), getActivity(), new DialogUtils.reviseNameInterFace() {
                            @Override
                            public void setName(String name) {
                                SetName(name);
                            }
                        });
                        break;
                    case R.id.btn_cancel:
                        dialog.dismiss();
                        break;
                    case R.id.takePhoto: //拍摄
                        select(TAKE_A_PICTURE);
                        dialog.dismiss();
                        break;
                    case R.id.choosePhoto: //本地相册
                        select(PHOTO_ALBUM);
                        dialog.dismiss();
                        break;
                    case R.id.frag_info_receiving_center: //收货地址
                        //我的收货信息点击
                        MobclickAgent.onEvent(getActivity(), "mine_0007");
                        startActivity(new Intent(getActivity(), InfoReceivingActivity.class));
                        break;
                    case R.id.iv_head_right_ll: //信封
                    case R.id.iv_head_right:
                        //我的消息点击
                        MobclickAgent.onEvent(getActivity(), "mine_0001");
                        startActivity(new Intent(getActivity(), NoticeCentralityAct.class));
                        break;
                }
            }
        });
    }


    private void select(int type) {
        if (type == TAKE_A_PICTURE) {
            ((JDDBaseActivity) getActivity()).checkPermission(new JDDBaseActivity.CheckPermListener() {
                @Override
                public void superPermission() {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_A_PICTURE);
                }
            }, R.string.camera, Manifest.permission.CAMERA);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, TAKE_A_PICTURE);
        }
    }

    private void setUserData() {
        YSFUserInfo userInfo = new YSFUserInfo();
        userInfo.userId = String.valueOf(AccountManager.getInstance().getCurrentUser().getUserId());
        String userData = "[";

        //用户信息
        userData += "{\"key\":\"username\", \"label\":\"用户名\", \"value\":\"" + AccountManager.getInstance().getCurrentUser().getLoginname() + "\"}," +
                "{\"key\":\"nickname\", \"label\":\"昵称\", \"value\":\"" + AccountManager.getInstance().getCurrentUser().getNickname() + "\"}," +
                "{\"key\":\"realname\", \"label\":\"真实姓名\", \"value\":\"" + AccountManager.getInstance().getCurrentUser().getNickname() + "\"}," +
                "{\"key\":\"reg_date\", \"label\":\"注册时间\", \"value\":\"" + "0.00" + "\"}," +
                "{\"key\":\"usermoney\", \"label\":\"余额\", \"value\":\"" + AccountManager.getInstance().getCurrentUser().getUseAmount() + "\"}," +
                "{\"key\":\"ismobilevalied\", \"label\":\"手机认证\", \"value\":\"" + AccountManager.getInstance().getCurrentUser().getPhone() + "\"},";

        userData += "{\"key\":\"cmdname\", \"label\":\"渠道号\", \"value\":\"" + Constants.Constants_APP_CHANNEL + "\"}," +
                "{\"key\":\"version\", \"label\":\"版本号\", \"value\":\"" + UtilsTools.getInstance().getVersionCode(getActivity()) + "\"}";
        userData += "]";
        userInfo.data = userData;
        Unicorn.setUserInfo(userInfo);
        ConsultSource source = new ConsultSource("", "消息", "custom information string");
        Unicorn.openServiceActivity(getActivity(), "客服中心", source);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_A_PICTURE:
                if (data != null) {
                    final Uri mImageCaptureUri = data.getData();
                    if (mImageCaptureUri != null) {
                        Luban.get(getActivity())
                                .load(new File(UriUtils.getPath(getActivity(), mImageCaptureUri)))
                                .putGear(Luban.THIRD_GEAR)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        try {
                                            onPostSingleFileWithMultipart(file, MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageCaptureUri));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                }).launch();
                    } else {
                        final Bundle extras = data.getExtras();
                        if (extras != null) {
                            final Bitmap image = extras.getParcelable("data");
                            Luban.get(getActivity())
                                    .load(new File(UtilsTools.saveImage(getActivity(), image).toString()))
                                    .putGear(Luban.THIRD_GEAR)
                                    .setCompressListener(new OnCompressListener() {
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onSuccess(File file) {
                                            onPostSingleFileWithMultipart(file, image);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }
                                    }).launch();
                        }
                    }
                }
                break;
            case UPDATA:
                networkData();
                break;
            case CHOOSE_BIG_PICTURE:
               final Uri imaUri = data.getData();
                if (imaUri != null) {
                    Luban.get(getActivity())
                            .load(new File(UriUtils.getPath(getActivity(), imaUri)))
                            .putGear(Luban.THIRD_GEAR)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    try {
                                        onPostSingleFileWithMultipart(file, MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imaUri));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            }).launch();

                }
                break;
        }
    }



    private void showDialog() {
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.frag_dialog_info, null);
        choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
        takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
        cancel = (TextView) inflate.findViewById(R.id.btn_cancel);
        inflate.setMinimumWidth(10000); //设置dialog全屏
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }


    //------------------------网络
    //获取个人信息
    private void networkData() {
        ServiceFactory.getInstance().createService(JDDApiService.class).getUserPersonalInfo()
                .compose(RxUtils.<HttpResult<UserPersonalBean>>defaultSchedulers())
                .compose(this.<HttpResult<UserPersonalBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<UserPersonalBean>() {
                    @Override
                    public void onSuccess(UserPersonalBean list) {
                        mInfo_imageView2.setVisibility(View.VISIBLE);
                        setData(list);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        info_name.setText("登录·注册");
                        mInfo_imageView2.setVisibility(View.GONE);
                    }
                });

    }


    //修改名字
    private void SetName(final String name) {
        Nickname mNickname = new Nickname();
        mNickname.setNickname(name);
        ServiceFactory.getInstance().createService(JDDApiService.class).setName(mNickname)
                .compose(RxUtils.<HttpResult<InfoName>>defaultSchedulers())
                .compose(this.<HttpResult<InfoName>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<InfoName>() {
                    @Override
                    public void onSuccess(InfoName mInfoName) {
                        mInfo_imageView2.setVisibility(View.GONE);
                        revise_name.setOnClickListener(null);
                        info_name.setText(name);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        if (code == 101) {
                            UtilsTools.getInstance().show("只能修改一次");
                        }
                    }
                });
    }

    public void onPostSingleFileWithMultipart(File file, final Bitmap image) {
        showLoading(false, "上传头像中...");
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "", requestFile);
        ServiceFactory.getInstance()
                .createService(JDDApiService.class)
                .upLoadImage(body)
                .compose(RxUtils.<HttpResult<Object>>defaultSchedulers())
                .compose(this.<HttpResult<Object>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object list) {
                        dismissLoading();
                        ToastUtils.show(getContext(), "上传头像成功");
                        iv_personal_icon.setImageBitmap(image);
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                        dismissLoading();
                        ToastUtils.show(getContext(), "上传头像失败");
                    }
                });

    }


    public void getHasUnReaderMsg() {
        if (!JDDUtils.isLogin()) {
            return;
        }
        ServiceFactory.getInstance().createService(JDDApiService.class).requestUnReaderNum()
                .compose(RxUtils.<HttpResult<UnreadCountBean>>defaultSchedulers())
                .compose(this.<HttpResult<UnreadCountBean>>bindToLifecycle())
                .subscribe(new HttpResultSubscriber<UnreadCountBean>() {
                    @Override
                    public void onSuccess(UnreadCountBean num) {

                        if (msg_icon == null) {
                            return;
                        }

                        if (num != null && num.getUnRead() > 0) {
                            msg_icon.setVisibility(View.VISIBLE);
                        } else {
                            msg_icon.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e, int code) {
                    }
                });
    }

    /**
     * 通过路径获取Bitmap对象
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {
        Bitmap bm = null;
        InputStream is = null;
        try {
            File outFilePath = new File(path);
            //判断如果当前的文件不存在时，创建该文件一般不会不存在
            if (!outFilePath.exists()) {
                boolean flag = false;
                try {
                    flag = outFilePath.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("---创建文件结果----" + flag);
            }
            is = new FileInputStream(outFilePath);
            bm = BitmapFactory.decodeStream(is);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }
}
