package com.yuri.tam.core.http;

import android.content.Context;
import android.text.TextUtils;

import com.yuri.tam.base.App;
import com.yuri.tam.core.http.cookie.CookieJarImpl;
import com.yuri.tam.core.http.cookie.store.PersistentCookieStore;
import com.yuri.tam.core.http.interceptor.BaseInterceptor;
import com.yuri.tam.core.http.interceptor.CacheInterceptor;
import com.yuri.tam.core.http.interceptor.logging.Level;
import com.yuri.tam.core.http.interceptor.logging.LoggingInterceptor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient封装单例类, 实现网络请求
 * Created by goldze on 2017/5/10.
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //最大连接个数
    private static final int MAX_IDLE_CONNECTIONS = 8;
    //存活时间
    private static final int KEEP_ALIVE_DURATION = 15;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String mBaseUrl = "https://www.oschina.net/";

    private static Context mContext = App.sContext;

    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofit;

    private Cache mCache = null;
    private File mHttpCacheDirectory;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(mBaseUrl, null);
    }

    private RetrofitClient(String baseUrl, Map<String, String> headers) {
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = mBaseUrl;
        }
        if (mHttpCacheDirectory == null) {
            mHttpCacheDirectory = new File(mContext.getCacheDir(), "sub_cache");
        }
        try {
            if (mCache == null) {
                mCache = new Cache(mHttpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        mOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .cache(mCache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(true) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("log-header", "I am the log request header.") // 添加请求头, 注意 key 和 value 都不能是中文
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.SECONDS))
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return mRetrofit.create(service);
    }

    /**
     * execute your customer API
     * For example:
     * MyApiService service = RetrofitClient.getInstance().create(MyApiService.class);
     * <p>
     * @param subscriber
     * RetrofitClient.getInstance().execute(service.login("name", "password"), subscriber)
     */
    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }
}
