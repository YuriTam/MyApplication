package com.yuri.tam.base;

import android.app.Application;
import android.content.Context;

import com.common.utils.ToastUtils;
import com.yuri.tam.common.executor.JobExecutor;
import com.yuri.tam.core.api.ApiRepository;

import java.util.concurrent.Executor;

/**
 * 应用参数初始化
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年02月14日
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    public static Context mContext;
    public static Executor mExecutor;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mExecutor = new JobExecutor();
        //初始化提示工具类
        ToastUtils.init(mContext);
        // FIXME: 2018/6/5 发布版本打开捕捉全局异常
        //ApiCrashHandler.javaApiCrashHandler.getInstance().init(this);
        //初始数据操作库
        ApiRepository.getInstance().initDataSource(mContext);
    }

    //创建单例模式
    private static class SingletonHolder{
        private static final App INSTANCE = new App();
    }

    //获取单例
    public static App getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
