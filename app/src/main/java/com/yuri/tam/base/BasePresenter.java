package com.yuri.tam.base;

import android.os.Handler;
import android.os.Looper;

import com.yuri.tam.core.aop.annotation.RunOnMainThread;
import com.yuri.tam.core.api.IDataSource;
import com.yuri.tam.core.rx.Event;
import com.yuri.tam.core.rx.RxBus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * P层基类
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月27日
 */
public class BasePresenter {
    protected final Logger mLog = LoggerFactory.getLogger(getClass().getSimpleName());
    private static final long DEFAULT_DELAY_MILLIS = 1000;
    //订阅管理
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    protected final IBaseView mView;
    protected final IDataSource mRepository;
    protected final Handler mHandler;

    //是否首次进入
    protected boolean mIsFirstAction = true;

    public BasePresenter(IBaseView view, IDataSource repository){
        mView = checkNotNull(view);
        mRepository = checkNotNull(repository);
        mView.setPresenter(this);
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 订阅事件
     */
    protected void doUseRxBus(){
        mDisposables.add(RxBus.getDefault()
                .register(Event.class, event -> {
                    if (event == null) return;
                    onRxBusEvent(event);
                }));
    }

    /**
     * 交给子类重写处理订阅事件
     *
     * @param event 事件信息
     */
    protected void onRxBusEvent(Event event){}

    /**
     * 取消订阅
     */
    protected void doClearRxBus(){
        mDisposables.clear();
    }

    /**
     * 切换到主线程
     *
     * @param runnable 线程
     */
    @RunOnMainThread
    protected void postMainThread(Runnable runnable) {
        runnable.run();
    }

    /**
     * 默认延时处理
     *
     * @param runnable
     */
    protected void postDelay(Runnable runnable){
        postDelay(runnable, DEFAULT_DELAY_MILLIS);
    }

    /**
     * 延时处理
     *
     * @param runnable
     * @param delayMillis 延时时间
     */
    protected void postDelay(Runnable runnable, long delayMillis){
        mHandler.postDelayed(runnable, delayMillis);
    }

}
