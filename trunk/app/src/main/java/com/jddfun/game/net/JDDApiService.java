package com.jddfun.game.net;

import com.jddfun.game.bean.ActivityOrderParams;
import com.jddfun.game.bean.Banner;
import com.jddfun.game.bean.BeetingRecordBean;
import com.jddfun.game.bean.CombineRes;
import com.jddfun.game.bean.CombineReuqst;
import com.jddfun.game.bean.CoterielistBean;
import com.jddfun.game.bean.CoterielistInfo;
import com.jddfun.game.bean.CoteriemineInfo;
import com.jddfun.game.bean.DerbrisInfo;
import com.jddfun.game.bean.ForgetPwdReq;
import com.jddfun.game.bean.Game;
import com.jddfun.game.bean.GameResquest;
import com.jddfun.game.bean.GetReceiverInfoBean;
import com.jddfun.game.bean.GetWealParams;
import com.jddfun.game.bean.H5OrderResponse;
import com.jddfun.game.bean.InfoName;
import com.jddfun.game.bean.Nickname;
import com.jddfun.game.bean.NormalOrderParams;
import com.jddfun.game.bean.Notice;
import com.jddfun.game.bean.NotifyState;
import com.jddfun.game.bean.OrderResponse;
import com.jddfun.game.bean.OrderStatusParams;
import com.jddfun.game.bean.PageReqParams;
import com.jddfun.game.bean.PageRequest;
import com.jddfun.game.bean.PayTypeBean;
import com.jddfun.game.bean.PhoneBindCodeBean;
import com.jddfun.game.bean.PraiseInfo;
import com.jddfun.game.bean.PrizeGet;
import com.jddfun.game.bean.ProfitRulesBean;
import com.jddfun.game.bean.PushedMessagesBean;
import com.jddfun.game.bean.PushedMessagesInfo;
import com.jddfun.game.bean.QQLoginReq;
import com.jddfun.game.bean.RechargeBean;
import com.jddfun.game.bean.RegisterBean;
import com.jddfun.game.bean.SaveReceiverInfo;
import com.jddfun.game.bean.SendVerCode;
import com.jddfun.game.bean.SignInfo;
import com.jddfun.game.bean.SortingInfo;
import com.jddfun.game.bean.TokenParams;
import com.jddfun.game.bean.TokenRes;
import com.jddfun.game.bean.TransDetailsInfo;
import com.jddfun.game.bean.UnreadCountBean;
import com.jddfun.game.bean.UpdateInfo;
import com.jddfun.game.bean.UpdateOneMessageStatusBean;
import com.jddfun.game.bean.UploadChannelInfo;
import com.jddfun.game.bean.User;
import com.jddfun.game.bean.UserPersonalBean;
import com.jddfun.game.bean.UserPhoneBindInfo;
import com.jddfun.game.bean.UserPhoneBindInfoBean;
import com.jddfun.game.bean.Weal;
import com.jddfun.game.bean.WealDebris;
import com.jddfun.game.bean.WealRequestParam;
import com.jddfun.game.bean.WxLogin;
import com.jddfun.game.bean.YouMengToken;
import com.jddfun.game.net.retrofit.HttpResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by _SOLID
 * Date:2016/8/3
 * Time:9:28
 */
public interface JDDApiService {
    /**
     * 获取福利信息
     * 1 待领取
     * 2 审核中
     * 3 已发放
     *
     * @return
     */
    //渠道上报
    @POST("api/app/usercenter/uploadChannel")
    Observable<HttpResult<String>> uploadChannel(@Body UploadChannelInfo param);


    //检测版本更新
    @POST("api/app/usercenter/appVersionUpdate")
    Observable<HttpResult<UpdateInfo>> checkVersionUpdate();

    //获取福利列表
    @POST("api/app/welfare/awardsListByApp")
    Observable<HttpResult<List<Weal>>> getWeals(@Body WealRequestParam param);

    //获取我的信息
    @GET("api/app/usercenter/getUserPersonalInfo")
    Observable<HttpResult<UserPersonalBean>> getUserPersonalInfo();

    @POST("api/app/welfare/drawAwardsByApp")
    Observable<HttpResult<PrizeGet>> getPrize(@Body GetWealParams wealRequestParam);

    //修改名字
    @POST("api/app/usercenter/updateNickname")
    Observable<HttpResult<InfoName>> setName(@Body Nickname nickname);

    //获取轮播图
    @POST("api/banner/list")
    Observable<HttpResult<List<Banner>>> getBannerInfo();

    //获取跑马灯
    @POST("api/notice/list")
    Observable<HttpResult<List<Notice>>> getNotice();

    //获取签到信息
    @GET("api/signed/list")
    Observable<HttpResult<SignInfo>> getSignIn();

    //签到
    @GET("api/signed/signing")
    Observable<HttpResult<String>> sign();

    //保存地址
    @POST("api/app/usercenter/saveReceiverInfo")
    Observable<HttpResult<String>> saveReceiverInfo(@Body SaveReceiverInfo mSaveReceiverInfo);

    //充值记录
    @POST("api/app/usercenter/getTransDetails")
    Observable<HttpResult<List<RechargeBean>>> getTransDetails(@Body TransDetailsInfo mTransDetailsInfo);

    //金叶子记录
    @POST("api/app/usercenter/findLeafList")
    Observable<HttpResult<List<BeetingRecordBean>>> getBeetingRecord(@Body TransDetailsInfo mTransDetailsInfo);

    //获取地址
    @GET("api/app/usercenter/getReceiverInfo")
    Observable<HttpResult<GetReceiverInfoBean>> getReceiverInfo();

    //获取绑定手机验证码
    @POST("api/app/usercenter/userPhoneBindCode")
    Observable<HttpResult<String>> userPhoneBindCode(@Body PhoneBindCodeBean mPhoneBindCodeBean);

    //手机绑定
    @POST("api/app/usercenter/userPhoneBind")
    Observable<HttpResult<String>> userPhoneBind(@Body UserPhoneBindInfo mUserPhoneBindInfo);

    //获取手机绑定信息
    @GET("api/app/usercenter/getUserPhoneBindInfo")
    Observable<HttpResult<UserPhoneBindInfoBean>> getUserPhoneBindInfo();

    //推送接口
    @POST("api/app/device/regis")
    Observable<HttpResult<Object>> postToken(@Body YouMengToken youmengToken);

    //消息列表
    @POST("api/app/usermessage/getMsgList")
    Observable<HttpResult<PushedMessagesBean>> getPushedMessages(@Body PushedMessagesInfo mPushedMessagesInfo);

    //将消息设置为已读
    @POST("api/app/usermessage/updateOneMessageStatus")
    Observable<HttpResult<String>> updateOneMessageStatus(@Body UpdateOneMessageStatusBean mupdateOneMessageStatusBean);

    @GET("api/app/usercenter/propFlag")
    Observable<HttpResult<NotifyState>> getNotifyStates();

    @POST("api/app/usercenter/setPropFlag")
    Observable<HttpResult<Object>> setNotifyStates(@Body NotifyState notifyState);

    //忘记密码
    @POST("api/app/userpassword/newForgetpassword")
    Observable<HttpResult<String>> newForgetpassword(@Body ForgetPwdReq mnewForgetpasswordInfo);

    @POST("api/app/order/activity")
    Observable<HttpResult<OrderResponse>> getActivityOrder(@Body ActivityOrderParams activityOrderParams);


    @POST("api/app/order/normal")
    Observable<HttpResult<OrderResponse>> getNormalOrder(@Body NormalOrderParams normalOrderParams);


    //获取支付方式
    @GET("api/app/pay/initApp")
    Observable<HttpResult<List<PayTypeBean>>> getPayType();

    //排行榜初始化 今日
    @GET("api/app/ranking/init")
    Observable<HttpResult<SortingInfo>> init();

    //排行榜初始化 昨日
    @GET("api/app/ranking/yesterdayInit")
    Observable<HttpResult<SortingInfo>> yesterdayInit();

    //橡皮擦的使用
    @GET("api/app/ranking/clear")
    Observable<HttpResult<String>> clear();

    //规则
    @GET("api/app/ranking/profitRules")
    Observable<HttpResult<ProfitRulesBean>> profitRules();

    //游戏列表
    @POST("api/game/list")
    Observable<HttpResult<List<Game>>> getGames(@Body GameResquest gameResquest);

    //H5普通订单
    @POST("api/wap/order/normal")
    Observable<HttpResult<H5OrderResponse>> getH5NormalOrder(@Body NormalOrderParams normalOrderParams);

    @POST("api/wap/order/activity")
    Observable<HttpResult<H5OrderResponse>> getH5ActivityOrder(@Body ActivityOrderParams activityOrderParams);

    //分享圈全部
    @POST("api/app/coterie/list")
    Observable<HttpResult<CoterielistBean>> coterielist(@Body CoterielistInfo mcoterielistInfo);

    @POST("api/app/pay/status")
    Observable<HttpResult<Object>> getOrderStatus(@Body OrderStatusParams orderStatusParams);

    //分享圈我的
    @POST("api/app/coterie/mine")
    Observable<HttpResult<CoteriemineInfo>> coteriemine(@Body CoterielistInfo mcoterielistInfo);


    //修改头像
    @Multipart
    @POST("api/app/usercenter/updateHeadImg")
    Observable<HttpResult<Object>> upLoadImage(@Part MultipartBody.Part image);


    //点赞
    @POST("api/app/coterie/praise")
    Observable<HttpResult<String>> praise(@Body PraiseInfo mPraiseInfo);


    //发布分享全(多图)
    @Multipart
    @POST("api/app/coterie/publish")
    Observable<HttpResult<Object>> publish(@Part() List<MultipartBody.Part> parts);


    //消息未读
    @POST("api/app/usermessage/getUnreadCount")
    Observable<HttpResult<UnreadCountBean>> requestUnReaderNum();

    //微信登入
    @POST("api/oauth/wechatprovider/login")
    Observable<HttpResult<String>> wxlogin(@Body WxLogin code);

    //登录，注：需要走api_platform
    @POST("api/user/login")
    Observable<HttpResult<String>> login(@Body User user);

    //获取token，注：需要走api_platform
    @POST("api/user/accessToken")
    Observable<HttpResult<TokenRes>> requestToken(@Body TokenParams tokenParams);

    //获取碎片包
    @POST("api/fragment/bagMe")
    Observable<HttpResult<DerbrisInfo>> getDebris(@Body PageRequest pageRequest);

    //碎片合成
    @POST("api/fragment/combine")
    Observable<HttpResult<CombineRes>> deBrisCombine(@Body CombineReuqst combineReuqst);

    //QQ登录
    @POST("api/oauth/qqprovider/appLogin")
    Observable<HttpResult<String>> qqLogin(@Body QQLoginReq qqLoginReq);


    //忘记密码发送验证码
    @POST("api/app/userpassword/sendForgetVerificationCode")
    Observable<HttpResult<String>> sendForgetVerificationCode(@Body SendVerCode sendVerCode);

    //发送验证码
    @POST("api/app/userregister/sendVerificationCode")
    Observable<HttpResult<String>> sendVer(@Body SendVerCode sendVerCode);


    //注册
    @POST("api/app/userregister/register")
    Observable<HttpResult<String>> register(@Body RegisterBean registerBean);

    //获取奖品列表
    @POST("api/app/welfare/awardsListByAppNew")
    Observable<HttpResult<List<Weal>>> getWeals(@Body PageReqParams pageReqParams);

    //获取碎片列表
    @POST("api/fragment/findUserFragmentRecordForApp")
    Observable<HttpResult<List<WealDebris>>> getWealDebris(@Body PageReqParams pageReqParams);
}
