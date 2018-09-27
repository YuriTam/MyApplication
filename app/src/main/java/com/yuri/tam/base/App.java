package com.yuri.tam.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.common.utils.ToastUtils;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yuri.tam.BuildConfig;
import com.yuri.tam.common.executor.JobExecutor;
import com.yuri.tam.core.api.ApiCrashHandler;
import com.yuri.tam.core.api.ApiRepository;

import java.util.concurrent.Executor;

/**
 * 应用参数初始化
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年02月14日
 */
public class App extends Application {

    //全局上下文
    public static Context sContext;
    //全局线程池
    public static Executor sExecutor;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        sExecutor = new JobExecutor();
        //异常捕捉
        if (!BuildConfig.DEBUG){
            ApiCrashHandler.getInstance().init(this);
        }
        //UI检测
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        //内存泄漏检测
        LeakCanary.install(this);
        //初始化提示工具类
        ToastUtils.init(this);
        //初始数据操作库
        ApiRepository.getInstance().initDataSource(this);
    }

    //创建单例模式
    private static class SingletonHolder{
        private static final App INSTANCE = new App();
    }

    //获取单例
    public static App getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 是否存在SD卡
     *
     * @return
     */
    private boolean idExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

}
