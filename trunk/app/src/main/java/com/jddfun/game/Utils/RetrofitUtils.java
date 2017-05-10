package com.jddfun.game.Utils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MACHINE on 2017/3/27.
 */

public class RetrofitUtils {
    private final static String TAG = "OkHttpProvider";
    private static Retrofit defaultInstance;

    public static ApiService postParam() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).connectTimeout(10000, TimeUnit.SECONDS).retryOnConnectionFailure(true);


        defaultInstance = new Retrofit.Builder()
                .baseUrl(Constants.BASICURL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = defaultInstance.create(ApiService.class);

        return apiService;
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            LogUtils.debug("okhttp--->", "" + request.url() + chain.connection() + request.headers());
//
//            return chain.proceed(request);


            Request request = chain.request();
            //the request url
            String url = request.url().toString();
            //the request method
            String method = request.method();
            long t1 = System.nanoTime();
            LogUtils.d(TAG, String.format(Locale.getDefault(), "Sending %s request [url = %s]", method, url));
            //the request body
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                StringBuilder sb = new StringBuilder("Request Body [");
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
                sb.append("]");
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
