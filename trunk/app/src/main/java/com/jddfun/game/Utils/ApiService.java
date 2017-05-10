package com.jddfun.game.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by MACHINE on 2017/3/24.
 */

public interface ApiService {


    //微信支付
    @POST("api/wap/order/normal")
    Call<ResponseBody> WXpay(@Header("Authorization") String authorization, @Body RequestBody user);

    //游戏列表
    @POST("api/game/list")
    Call<ResponseBody> gameList(@Header("Authorization") String authorization, @Header("App-Version") String version, @Header("App-Channel") String chanel, @Body RequestBody user);

    //福利列表
    @POST("api/navigator/list")
    Call<ResponseBody> fuliList(@Header("Authorization") String authorization, @Header("App-Version") String version, @Header("App-Channel") String chanel);

    //签到列表
    @GET("api/signed/list")
    Call<ResponseBody> signList(@Header("Authorization") String authorization, @Header("App-Version") String version, @Header("App-Channel") String chanel);

    //签到
    @GET("api/signed/signing")
    Call<ResponseBody> sign(@Header("Authorization") String authorization, @Header("App-Version") String version, @Header("App-Channel") String chanel);

    //盈利榜
    @GET("api/history/profitNew")
    Call<ResponseBody> profitNew(@Header("Authorization") String authorization, @Header("App-Version") String version, @Header("App-Channel") String chanel);


    /**
     * 上传一张图片
     *
     * @param description
     * @param imgs
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs);


    /**
     * 上传三张图片
     *
     * @param description
     * @param imgs
     * @param imgs1
     * @param imgs3
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs1,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs3);

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("fileName") RequestBody description,
                              @Part MultipartBody.Part file);

}
