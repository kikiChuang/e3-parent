package com.jddfun.game.net.retrofit.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.net.OkHttpProvider;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by _SOLID
 * Date:2016/7/27
 * Time:15:23
 */
public class ServiceFactory {

    private final Gson mGson;
    private OkHttpClient mOkHttpClient;


    private ServiceFactory() {
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        mOkHttpClient = OkHttpProvider.getDefaultOkHttpClient();

    }

    private static class SingletonHolder {
        private static final ServiceFactory INSTANCE = new ServiceFactory();
    }

    public static ServiceFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static ServiceFactory getNoCacheInstance() {
        ServiceFactory factory = SingletonHolder.INSTANCE;
        factory.mOkHttpClient = OkHttpProvider.getOkHttpClient();
        return factory;
    }

    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, Constants.BASICURL);
    }


    //可以传入base url
    public <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }

}
