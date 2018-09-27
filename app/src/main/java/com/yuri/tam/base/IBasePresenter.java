package com.yuri.tam.base;

/**
 * P层操作接口基类
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年6月5日
 */
public interface IBasePresenter {

    /**
     * 开始
     */
    void onStart();

    /**
     * 暂停
     */
    void onPause();

    /**
     * 摧毁
     */
    void onDestroy();
}
