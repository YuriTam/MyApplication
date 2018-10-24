package com.yuri.tam.core.rx;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 管理订阅
 *
 * @author 谭忠扬
 * @time 2018年10月24日
 */
public class RxSubscriptions {
    private static CompositeDisposable mSubscriptions = new CompositeDisposable ();

    /**
     * 判断是否订阅
     *
     * @return
     */
    public static boolean isDisposed() {
        return mSubscriptions.isDisposed();
    }

    /**
     * 添加订阅
     *
     * @param disposable
     */
    public static void add(Disposable disposable) {
        if (disposable != null) {
            mSubscriptions.add(disposable);
        }
    }

    /**
     * 移除订阅
     *
     * @param disposable
     */
    public static void remove(Disposable disposable) {
        if (disposable != null) {
            mSubscriptions.remove(disposable);
        }
    }

    /**
     * 清空所有订阅
     */
    public static void clear() {
        mSubscriptions.clear();
    }

    /**
     * 取消所有订阅
     */
    public static void dispose() {
        mSubscriptions.dispose();
    }

}
