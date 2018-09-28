package com.yuri.tam.core.aop;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.yuri.tam.common.utils.PermissionUtils;
import com.yuri.tam.core.aop.annotation.Permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动态申请系统权限
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月27日
 */
@Aspect
public class PermissionAspect {
    private final Logger mLog = LoggerFactory.getLogger(PermissionAspect.class.getSimpleName());
    private final int REQUEST_CODE = 1000;

    /**
     * 需要申请权限的方法
     *
     * @param permission
     */
    @Pointcut("execution(@com.yuri.tam.core.aop.annotation.Permission * *(..)) && @annotation(permission)")
    public void pointcutPermissionMethod(Permission permission) {}

    @Around("pointcutPermissionMethod(permission)")
    public void runOnMainThreadProcess(ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        if (permission == null || permission.value() == null || permission.value().length == 0){
            joinPoint.proceed();
            return;
        }
        try {
            String[] permissions = permission.value();
            Object object = joinPoint.getThis();
            mLog.debug("----------- 动态申请权限 -------------");
            for (String value : permissions){
                mLog.debug("permission = " + value);
            }
            if (object instanceof Activity) {
                Activity activity = (Activity) object;
                PermissionUtils.requestPermissionsResult(activity, REQUEST_CODE, permissions, new PermissionResult(joinPoint, activity));
            } else if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                PermissionUtils.requestPermissionsResult(fragment, REQUEST_CODE, permissions, new PermissionResult(joinPoint, fragment.getActivity()));
            } else if (object instanceof android.support.v4.app.Fragment) {
                android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) object;
                PermissionUtils.requestPermissionsResult(fragment, REQUEST_CODE, permissions, new PermissionResult(joinPoint, fragment.getActivity()));
            } else {
                joinPoint.proceed();
            }
        }catch (Exception e){
            joinPoint.proceed();
        }
    }

    /**
     * 动态申请权限结果回调
     */
    class PermissionResult implements PermissionUtils.PermissionListener{

        ProceedingJoinPoint joinPoint;
        Context context;

        public PermissionResult(ProceedingJoinPoint joinPoint, Context context) {
            this.joinPoint = joinPoint;
            this.context = context;
        }

        @Override
        public void onPermissionGranted() {
            try {
                mLog.debug("----------- 动态申请权限 ：成功 -------------");
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onPermissionDenied() {
            mLog.debug("----------- 动态申请权限 ：失败 -------------");
            PermissionUtils.showTipsDialog(context);
        }
    }

}
