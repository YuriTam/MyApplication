package com.yuri.tam.core.aop;

import android.os.Handler;
import android.os.Looper;

import com.yuri.tam.base.App;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据注解切换到相应的线程
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月27日
 */
@Aspect
public class RunOnThreadInjection {
    private Handler mHandler;

    RunOnThreadInjection() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 被@RunOnMainThread注解的方法
     */
    @Pointcut("@annotation(com.yuri.tam.core.aop.annotation.RunOnMainThread)")
    public void annotationWithRunOnMainThread() {}

    /**
     * 被@RunOnWorkThread注解的方法
     */
    @Pointcut("@annotation(com.yuri.tam.core.aop.annotation.RunOnWorkThread)")
    public void annotationWithRunOnWorkThread() {}

    @Around("annotationWithRunOnMainThread()")
    public void runOnMainThreadProcess(ProceedingJoinPoint joinPoint) {
        postMainThread(() -> {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Around("annotationWithRunOnWorkThread()")
    public void runOnWorkThreadProcess(ProceedingJoinPoint joinPoint) {
        postWorkThread(() -> {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * 主线程运行
     *
     * @param runnable
     */
    private void postMainThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    /**
     * 工作线程运行
     *
     * @param runnable
     */
    private void postWorkThread(Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            runnable.run();
        } else {
            App.sExecutor.execute(runnable);
        }
    }
}
