package com.yuri.tam.core.http;

import com.yuri.tam.core.bean.UpdateInfo;
import com.yuri.tam.core.bean.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * http接口
 *
 * @author 谭忠扬-YuriTam
 * @time 2016年11月12日
 */
public interface HttpService {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 返回用户信息
     */
    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
    @POST("user/login")
    Observable<UserInfo> login(@Query("userName") String userName, @Query("password") String password);

    /**
     * 检查应用版本信息
     *
     * @return 应用信息
     */
    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
    @GET("app/check/update")
    Observable<UpdateInfo> checkUpdate();

    /**
     * 文件下载
     *
     * @param url 下载地址
     * @return 返回下载数据
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    /**
     * 文件下载
     *
     * @param range 开始位置
     * @param url 下载地址
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("Range") String range, @Url String url);

    /**
     * 文件下载
     *
     * @param headers 请求头信息
     * @param url 下载地址
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@HeaderMap Map<String, String> headers, @Url String url);

}
