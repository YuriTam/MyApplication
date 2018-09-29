package com.yuri.tam.core.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态申请权限注解
 *
 * @author 谭忠扬-YuriTam
 * @time 2019年9月27日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {

    /**
     * 申请的权限
     */
    String[] value() default "";

}
