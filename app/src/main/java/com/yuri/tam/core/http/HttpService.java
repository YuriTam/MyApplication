package com.yuri.tam.core.http;

import com.yuri.tam.core.bean.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
     * 上送终端信息
     *
     * @param terminalInfo 终端信息
     * @return 上传结果
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @POST("upload/terminal/infos")
//    Observable<RespInfo> uploadTerminalInfo(@Body UTerminalInfo terminalInfo);

    /**
     * 获取商店应用列表
     *
     * @param sn 机身号
     * @return 应用列表
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @GET("get/app/list")
//    Observable<List<AppVersionInfo>> getStoreAppList(@Query("sn") String sn);

    /**
     * 检查设备有无计划
     *
     * @param sn 机身号
     * @return 返回计划信息
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @GET("check/device/plan")
//    Observable<RespPlanInfo> checkDevicePlan(@Query("sn") String sn);

    /**
     * 上传计划执行结果信息
     *
     * @param resultInfo 计划结果信息
     * @return 返回上传结果
     */
//    @Headers({"Content-Type: application/json;charset=UTF-8","Accept: application/json"})
//    @POST("notify/device/result")
//    Observable<RespInfo> uploadPlanResult(@Body UResultInfo resultInfo);

    /**
     * 文件下载
     *
     * @param url 下载地址
     * @return 返回下载数据
     */
//    @Streaming
//    @GET
//    Observable<ResponseBody> download(@Url String url);

    /**
     * 文件下载
     *
     * @param range 开始位置
     * @param url 下载地址
     * @return
     */
//    @Streaming
//    @GET
//    Observable<ResponseBody> download(@Header("Range") String range, @Url String url);

    /**
     * 文件下载
     *
     * @param headers 请求头信息
     * @param url 下载地址
     * @return
     */
//    @Streaming
//    @GET
//    Observable<ResponseBody> download(@HeaderMap Map<String, String> headers, @Url String url);
}
