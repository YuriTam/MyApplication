package com.yuri.tam.core.download.listener;

/**
 * 版本检查结果回调
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年12月05日
 */
public interface OnCheckListener {

    /**
     * 开始检测
     */
    void onStart();

    /**
     * 检查结果
     *
     * @param hasNewVersion 是否有新版本
     */
    void onResult(boolean hasNewVersion);

    /**
     * 异常
     *
     * @param e
     */
    void onError(Throwable e);
}
