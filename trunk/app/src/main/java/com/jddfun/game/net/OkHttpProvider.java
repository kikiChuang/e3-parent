package com.jddfun.game.net;

import android.text.TextUtils;

import com.jakewharton.rxbinding.internal.Preconditions;
import com.jddfun.game.JDDApplication;
import com.jddfun.game.Utils.Constants;
import com.jddfun.game.Utils.LogUtils;
import com.jddfun.game.Utils.NetworkUtil;
import com.jddfun.game.Utils.UtilsTools;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/**
 * Created by _SOLID
 * Date:2016/12/3
 * Time:13:44
 */

public class OkHttpProvider {

    private final static long DEFAULT_CONNECT_TIMEOUT = 10;
    private final static long DEFAULT_WRITE_TIMEOUT = 30;
    private final static long DEFAULT_READ_TIMEOUT = 30;
    private final static String TAG = OkHttpProvider.class.getSimpleName();

    public static OkHttpClient getDefaultOkHttpClient() {
        // return getOkHttpClient(new CacheControlInterceptor());
        return getOkHttpClient(new FromNetWorkControlInterceptor());
    }

    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(new FromNetWorkControlInterceptor());
    }

    private static OkHttpClient getOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存
        File httpCacheDirectory = new File(JDDApplication.mApp.getCacheDir(), "OkHttpCache");
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 100 * 1024 * 1024));
        //设置拦截器
        httpClientBuilder.addInterceptor(new UserAgentInterceptor("Android Device"));
        httpClientBuilder.addInterceptor(new LoggingInterceptor());
        httpClientBuilder.addInterceptor(cacheControl);
        httpClientBuilder.addNetworkInterceptor(cacheControl);
        return httpClientBuilder.build();
    }


    /**
     * 没有网络的情况下就从缓存中取
     * 有网络的情况则从网络获取
     */
    private static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isConnected(JDDApplication.getAppContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetworkUtil.isConnected(JDDApplication.getAppContext())) {
                int maxAge = 60 * 60 * 2;//默认缓存两个小时
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=" + maxAge;
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();

            } else {
                int maxStale = 60 * 60 * 24 * 30;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    /**
     * 强制从网络获取数据
     */
    private static class FromNetWorkControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();

            Response response = chain.proceed(request);

            return response;
        }
    }


    private static class UserAgentInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final String userAgentHeaderValue;
        private static final String AUTHORIZATION = "Authorization";
        private static final String APP_VERSION = "App-Version";
        private static final String APP_CHANNEL = "App-Channel";

        UserAgentInterceptor(String userAgentHeaderValue) {
            this.userAgentHeaderValue = Preconditions.checkNotNull(userAgentHeaderValue, "userAgentHeaderValue = null");
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                    .addHeader(AUTHORIZATION, UtilsTools.getInstance().getString("accessToken", ""))
                    .addHeader(APP_VERSION, UtilsTools.getInstance().getVersionName(JDDApplication.mApp))
                    .addHeader(APP_CHANNEL, Constants.Constants_APP_CHANNEL)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //the request url
            String url = request.url().toString();
            //the request method
            String method = request.method();
            long t1 = System.nanoTime();
            LogUtils.d(TAG, String.format(Locale.CHINA, "Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            //the request body
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                StringBuilder sb = new StringBuilder("Request Body [   ");
                okio.Buffer buffer = new okio.Buffer();
                requestBody.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }

                if (isPlaintext(buffer)) {
                    sb.append(buffer.readString(charset));
                    sb.append(" (Content-Type = ").append(contentType != null ? contentType.toString() : "").append(",")
                            .append(requestBody.contentLength()).append("-byte body)");
                } else {
                    sb.append(" (Content-Type = ").append(contentType != null ? contentType.toString() : "")
                            .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
                }
                sb.append("  ]");
                LogUtils.d(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            //the response time
            LogUtils.d(TAG, String.format(Locale.getDefault(), "Received response for [url = %s] in %.1fms", url, (t2 - t1) / 1e6d));

            //the response state
            LogUtils.d(TAG, String.format(Locale.CHINA, "Received response is %s ,message[%s],code[%d]", response.isSuccessful() ? "success" : "fail", response.message(), response.code()));

            //the response data
            ResponseBody body = response.body();

            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
            LogUtils.d(TAG, String.format("Received response json string [%s]", bodyString));
            return response;
        }

        static boolean isPlaintext(Buffer buffer) {
            try {
                Buffer prefix = new Buffer();
                long byteCount = buffer.size() < 64 ? buffer.size() : 64;
                buffer.copyTo(prefix, 0, byteCount);
                for (int i = 0; i < 16; i++) {
                    if (prefix.exhausted()) {
                        break;
                    }
                    int codePoint = prefix.readUtf8CodePoint();
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false;
                    }
                }
                return true;
            } catch (EOFException e) {
                return false; // Truncated UTF-8 sequence.
            }
        }

    }
}
