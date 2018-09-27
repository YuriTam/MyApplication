package com.yuri.tam.core.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.common.utils.NetworkUtils;
import com.common.utils.ToastUtils;
import com.yuri.tam.common.constant.SysConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常捕捉
 *
 * @author 谭忠扬-YuriTam
 * @time 2016年11月1日
 */
public class ApiCrashHandler implements Thread.UncaughtExceptionHandler {
    private final Logger mLog = LoggerFactory.getLogger(ApiCrashHandler.class.getSimpleName());

    private static ApiCrashHandler mCrashHandler;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    //用来存储设备信息和异常信息
    private Map<String, String> mMapInfo = new HashMap<>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", SysConstant.LOCALE);

    private ApiCrashHandler(){}

    public static ApiCrashHandler getInstance(){
        if(mCrashHandler == null){
            synchronized (ApiCrashHandler.class){
                if(mCrashHandler == null){
                    mCrashHandler = new ApiCrashHandler();
                }
            }
        }
        return mCrashHandler;
    }

    /**
     * 初始化
     * @param mContext
     */
    public void init(Context mContext){
        this.mContext = mContext;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //将异常信息发送给开发人员
        sendCrashInfo2Server();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                mLog.debug("error msg : {}", e.getMessage());
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 处理系统异常
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex){
        if (ex == null) return false;
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                ToastUtils.show("程序出现异常,即将退出");
                Looper.loop();
            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集异常信息
     * @param mContext
     */
    private void collectDeviceInfo(Context mContext){
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mMapInfo.put("versionName", versionName);
                mMapInfo.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            mLog.debug("收集异常信息时发生异常：{}", e.getMessage());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mMapInfo.put(field.getName(), field.get(null).toString());
                mLog.debug("{}：{}", field.getName(), field.get(null).toString());
            } catch (Exception e) {
                mLog.debug("收集异常信息时发生异常：{}", e.getMessage());
            }
        }
    }

    /**
     * 保存异常信息
     *
     * @param ex
     * @return
     */
    private String saveCrashInfo2File(Throwable ex){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mMapInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            long timestamp = System.currentTimeMillis();
            String time = format.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(SysConstant.CRASH_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(SysConstant.CRASH_PATH + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        }catch (Exception e){
            mLog.debug("保存异常信息时发生异常：{}", e.getMessage());
            return null;
        }
    }

    /**
     * 启动时，将异常信息发送给开始人员
     */
    private void sendCrashInfo2Server(){
        //判断是否有网络
        if (!NetworkUtils.isAvailable(mContext)){
            return;
        }
        // FIXME: 2017/12/1 如有需要可在这里实现异常日志上传
    }
}
