package com.jddfun.game.net.retrofit.service;

import com.jddfun.game.Utils.Constants;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by _SOLID
 * Date:2016/7/27
 * Time:14:53
 */
public interface CommonService {
    String BASE_URL = Constants.BASICURL;

    @GET
    Observable<ResponseBody> loadString(@Url String url);

    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String url);
}
