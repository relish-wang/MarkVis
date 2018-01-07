package com.hustoj.network;


import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.tls.OkHostnameVerifier;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ServiceAccessor {
    private static final String[] SKIPPED_HEADERS = {
            // Request headers
            "Content-Type", "Host", "Connection", "Accept-Encoding",
            // Response headers
            "Server", "Date", "X-Powered-By", "ETag", "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers", "Access-Control-Allow-Methods", "Transfer-Encoding",
            "Proxy-Connection"
    };

    private static final OkHttpClient NORMAL_CLIENT;

    private static volatile Retrofit retrofit;

    static {

        final X509TrustManager trustManager = new X509TrustManagerBuilder()
                .trustWhatSystemTrust().build();
        NORMAL_CLIENT = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 60, TimeUnit.SECONDS))
                .connectTimeout(6000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                // log interceptor should be added at last of NetworkInterceptor list or will miss some data
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                        .setResponseBodyMaxLogBytes(60 * 1024)
                        .setSkippedHeaders(Arrays.asList(SKIPPED_HEADERS)))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Cookie", "hustoj-SESSID=s1uuue0ui6o1714prtlgkvgik0")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .sslSocketFactory(X509TrustManagerBuilder.createSSLSocketFactory(trustManager), trustManager)
                .hostnameVerifier(OkHostnameVerifier.INSTANCE)
                .build();
    }

    /**
     * 如果Sdk中getServerDomain不为null且不为"",
     * 优先使用getServerDomain,否则根据buildType
     * 使用UrlConstant.SERVER_DOMAIN_MAIN
     *
     * @return SERVER_DOMAIN_MAIN
     */
    private static String getBaseUrl() {
        return "http://zjicm.hustoj.com/";
    }


    protected static Retrofit getRetrofit() {
        if (null == retrofit) {
            synchronized (ServiceAccessor.class) {
                if (null == retrofit) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(getBaseUrl())
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//支持RxJava
                            .client(NORMAL_CLIENT)
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static volatile HttpService httpService = null;

    public static HttpService getHttpService() {
        if (null == httpService) {
            synchronized (HttpService.class) {
                if (null == httpService) {
                    httpService = getRetrofit().create(HttpService.class);
                }
            }
        }
        return httpService;
    }
}
