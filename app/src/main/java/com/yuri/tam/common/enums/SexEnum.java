package com.yuri.tam.common.enums;

/**
 * 性别枚举
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年10月24日
 */
public enum SexEnum {

    BOY("男"),
    GIRL("女");

    private String sex;

    SexEnum(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

}
