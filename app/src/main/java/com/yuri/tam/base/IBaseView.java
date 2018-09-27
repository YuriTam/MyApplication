package com.yuri.tam.base;

/**
 * P层回调接口基类
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年6月5日
 */
public interface IBaseView<T> {

    /**
     * 设置当前业务的操作对象
     *
     * @param presenter
     */
    void setPresenter(T presenter);
}
