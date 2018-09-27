package com.common.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务工具类
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年06月08日
 */
public class ServiceUtils {

    /**
     * 判断服务是否开启
     *
     * @param context
     * @param className 这里是包名+类名 xxx.xxx.xxx.TestService
     * @return
     */
    public static boolean isServiceRunning(Context context,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size()>0)) {
            return isRunning;
        }

        for (int i=0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }

}
