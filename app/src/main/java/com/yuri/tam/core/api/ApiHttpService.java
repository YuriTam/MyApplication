package com.yuri.tam.core.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yuri.tam.BuildConfig;
import com.yuri.tam.common.constant.SysConstant;
import com.yuri.tam.core.bean.UserInfo;
import com.yuri.tam.core.http.HttpService;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit网络请求
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年06月15日
 */
public class ApiHttpService {

    private Retrofit mRetrofit;
    private HttpService mHttpService;

    private ApiHttpService(){
        //统一时间解析类型
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        //构建retrofit对象
        mRetrofit = new Retrofit.Builder()
                .client(createClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
                .build();
        //构建http service对象
        mHttpService = mRetrofit.create(HttpService.class);
    }

    /**
     * 创建OK http客户端
     *
     * @return
     */
    private OkHttpClient createClient(){
        //日志拦截
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //构建OK HTTP对象
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(SysConstant.TIME_OUT_15, TimeUnit.SECONDS)
                .writeTimeout(SysConstant.TIME_OUT_10, TimeUnit.SECONDS)
                .readTimeout(SysConstant.TIME_OUT_10, TimeUnit.SECONDS)
                .cache(new Cache(getCacheDir(), SysConstant.CACHE_SIZE))
                .retryOnConnectionFailure(true)
                .build();
    }

    //在访问ApiHttpService时创建单例
    private static class SingletonHolder{
        private static final ApiHttpService INSTANCE = new ApiHttpService();
    }

    //获取单例
    public static ApiHttpService getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 设置缓存目录
     *
     * @return
     */
    private File getCacheDir(){
        return new File(SysConstant.CACHE_DIR, "responses");
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return 返回用户信息
     */
    public Observable<UserInfo> login(String userName, String password){
        return mHttpService.login(userName, password)
                .timeout(SysConstant.TIME_OUT_60, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
