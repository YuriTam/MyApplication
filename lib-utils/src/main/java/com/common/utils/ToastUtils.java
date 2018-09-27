package com.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 提示工具类
 *
 * @author 谭忠扬-YuriTam
 * @Time 2017年8月3日
 */
public class ToastUtils {

    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static Context mContext;

    public static void init(Context context) {
        ToastUtils.mContext = context.getApplicationContext();
    }

    public static void show(int resId) {
        show(mContext.getString(resId));
    }

    public static void show(int resId, int gravity) {
        show(mContext.getString(resId), gravity, 0, 0);
    }

    public static void show(String msg, int gravity) {
        show(msg, gravity, 0, 0);
    }

    public static void show(int resId, int gravity, int offX, int offY) {
        show(mContext.getString(resId), gravity, offX, offY);
    }

    public static void show(String msg) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);

            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void show(String msg, int gravity, int offX, int offY) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, offX, offY);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
