package com.yuri.tam.common.enums;

/**
 * 卡卷类型
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年11月12日
 */
public enum CouponType {

    CASH("现金撤销卷"),

    RED_ENVELOPE("现金红包");

    private String name;

    CouponType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
