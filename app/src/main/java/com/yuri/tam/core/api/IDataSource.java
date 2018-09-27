package com.yuri.tam.core.api;

import android.content.Context;

/**
 * 数据操作接口
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年02月02日
 */
public interface IDataSource {

    /**
     * 初始化相关数据
     */
    void initDataSource(Context context);

    /**
     * 保存数据到map内存表
     *
     * @param key   键值
     * @param value 数据
     */
    void setParamValue(String key, String value);

    /**
     * 获取map内存表数据
     *
     * @param key 键值
     * @param defValue 默认数据
     * @return 数据
     */
    String getParamValue(String key, String defValue);

    /**
     * 同步更新数据，即写入到文件中
     */
    void syncParamValue();
}
