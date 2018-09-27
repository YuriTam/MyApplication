package com.yuri.tam.common.executor;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工作线程池
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年6月5日
 */
public class JobExecutor implements Executor {
    private final ThreadPoolExecutor mPoolExecutor;
    private int mCorePoolSize = 5;
    private int mMaximumPoolSize = 10;
    private long mKeepAliveTime = 10;

    public JobExecutor() {
        this.mPoolExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, mKeepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), new JobThreadFactory());
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        this.mPoolExecutor.execute(runnable);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "android_" + counter++);
        }
    }
}
