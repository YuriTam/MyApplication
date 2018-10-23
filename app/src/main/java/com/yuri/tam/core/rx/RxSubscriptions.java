package com.yuri.tam.core.rx;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 管理 CompositeSubscription
 */
public class RxSubscriptions {
    private static CompositeDisposable mSubscriptions = new CompositeDisposable ();

    public static boolean isDisposed() {
        return mSubscriptions.isDisposed();
    }

    public static void add(Disposable disposable) {
        if (disposable != null) {
            mSubscriptions.add(disposable);
        }
    }

    public static void remove(Disposable disposable) {
        if (disposable != null) {
            mSubscriptions.remove(disposable);
        }
    }

    public static void clear() {
        mSubscriptions.clear();
    }

    public static void dispose() {
        mSubscriptions.dispose();
    }

}
