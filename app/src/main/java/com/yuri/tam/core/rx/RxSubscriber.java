package com.yuri.tam.core.rx;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义订阅器
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年06月11日
 */
public abstract class RxSubscriber<T> implements Subscriber<T> {
    private static final Logger mLog = LoggerFactory.getLogger(RxSubscriber.class.getSimpleName());

    @Override
    public void onSubscribe(Subscription subscription) {
        mLog.debug("------------ 开始连接网络 ------------");
    }

    @Override
    public void onError(Throwable e) {
        mLog.debug("--------- onError ---------");
        _onError(e);
    }

    public abstract void _onError(Throwable e);

    @Override
    public void onNext(T t) {
        mLog.debug("--------- onNext ---------");
        _onNext(t);
    }

    public abstract void _onNext(T t);

    @Override
    public void onComplete() {
        mLog.debug("------------ 通讯结束 ------------");
    }
}
