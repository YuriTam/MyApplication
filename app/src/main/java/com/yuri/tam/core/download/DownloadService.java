package com.yuri.tam.core.download;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.common.utils.AppUtils;
import com.common.utils.ConvertUtils;
import com.common.utils.FileUtils;
import com.common.utils.IntentUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yuri.tam.R;
import com.yuri.tam.common.constant.SysConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java8.util.Optional;

/**
 * 下载文件服务
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年12月05日
 */
public class DownloadService extends IntentService {
    private Logger mLog = LoggerFactory.getLogger(DownloadService.class.getSimpleName());
    private static final int SHOW_MD5_ERR = 1000;
    private static final String MD_5 = "md_5";
    private static final String VERSION_NAME = "version_name";
    private static final String DOWNLOAD_URL = "download_url";
    private static final String IS_AUTO_INSTALL = "is_auto_install";

    private Context mContext;
    //MD签名
    private String mMd5Sign;
    //版本名
    private String mVersionName;
    //下载地址
    private String mDownloadUrl;
    //是否自动安装
    private boolean mIsAutoInstall;
    //下载文件名
    private String mFileName;

    //通知栏相关
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private int mNotification_id = 1000;
    private RemoteViews mContentView;

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mMd5Sign = bundle.getString(MD_5, "");
            mVersionName = bundle.getString(VERSION_NAME, "");
            mDownloadUrl = bundle.getString(DOWNLOAD_URL, "");
            mIsAutoInstall = bundle.getBoolean(IS_AUTO_INSTALL, false);
        }
        mLog.debug("mMd5Sign : {}", mMd5Sign);
        mLog.debug("mVersionName : {}", mVersionName);
        mLog.debug("mDownloadUrl : {}", mDownloadUrl);
        mLog.debug("mIsAutoInstall : {}", mIsAutoInstall);
        //下载位置及文件名，如：Payment_V1.0.0_20171205_release.apk
        mFileName = SysConstant.DOWNLOAD_PATH
                + "Payment"
                + Optional.ofNullable(mVersionName).map(s -> "_V" + s).orElse("")
                + Optional.ofNullable(new SimpleDateFormat("yyyyMMdd", SysConstant.LOCALE).format(new Date())).map(s -> "_" + s).get()
                + "_release.apk";
        //判断文件是否存在，如果存在则先删除
        if (FileUtils.isFileExists(mFileName)){
            FileUtils.deleteFile(mFileName);
        }
        mLog.debug("mFileName : {}", mFileName);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (TextUtils.isEmpty(mDownloadUrl) || TextUtils.isEmpty(mFileName)){
            stopSelf();
            return;
        }
        mLog.debug("-------------- 开始下载 ----------------");
        //下载
        FileDownloader.getImpl().create(mDownloadUrl)
                .setPath(mFileName)
                .setAutoRetryTimes(3)
                .setCallbackProgressTimes(600)
                .setMinIntervalUpdateSpeed(500)
                .setTag(mDownloadUrl)
                .setListener(downloadListener)
                .start();
    }

    /**
     * 下载回调
     */
    private FileDownloadListener downloadListener = new FileDownloadListener() {

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            //通知栏提示
            showDownLoadNotification();
        }

        @Override
        protected void connected(BaseDownloadTask task, String tag, boolean isContinue, int soFarBytes, int totalBytes) {
            //更新显示
            mContentView.setTextViewText(R.id.tv_title, getString(R.string.just_connecting));
            mNotificationManager.notify(mNotification_id, mNotification);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            //转化成百分比
            int percent = convert2Percent(soFarBytes, totalBytes);
            mLog.debug("total : {}, percent : {}", soFarBytes, percent + "%");
            //更新显示下载进度
            mContentView.setTextViewText(R.id.tv_percent, percent + "%");
            mContentView.setProgressBar(R.id.progress_bar, 100, percent, false);
            mContentView.setTextViewText(R.id.tv_current_size, ConvertUtils.byte2FitSize(soFarBytes));
            mContentView.setTextViewText(R.id.tv_total_size, ConvertUtils.byte2FitSize(totalBytes));
            mNotificationManager.notify(mNotification_id, mNotification);
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            mLog.debug("下载完成，文件名为 ： {}", task.getFilename());
            //校验MD5是否正确
            if (!checkMd5Value(mFileName)){
                //更新显示
                mContentView.setTextViewText(R.id.tv_title, getString(R.string.md5_check_fail));
                mNotificationManager.notify(mNotification_id, mNotification);
                return;
            }
            //下载完成，判断是否自动安装
            if (mIsAutoInstall){
                mNotificationManager.cancel(mNotification_id);
                AppUtils.installApp(mContext, task.getPath());
            }else {
                mContentView.setTextViewText(R.id.tv_title, getString(R.string.click_to_install));
                Intent installIntent = IntentUtils.getInstallAppIntent(task.getPath());
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, mNotification_id, installIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mNotification.contentIntent = pendingIntent;
                mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(mNotification_id, mNotification);
            }
            stopSelf();
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            //更新显示
            mContentView.setTextViewText(R.id.tv_title, getString(R.string.download_fail));
            mNotificationManager.notify(mNotification_id, mNotification);
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };

    /**
     * 转成下载百分比
     *
     * @param soFarBytes 当前下载量
     * @param totalBytes 文件大小
     * @return
     */
    private int convert2Percent(int soFarBytes, int totalBytes){
        double x_double = soFarBytes * 1.0;
        double tempPercent = x_double / totalBytes;
        // 百分比格式，后面不足2位的用0补齐
        DecimalFormat df1 = new DecimalFormat("0.00"); // ##.00%
        return (int) (Float.parseFloat(df1.format(tempPercent)) * 100);
    }

    /**
     * 通知栏提示下载进度
     */
    private void showDownLoadNotification(){
        //获取消息管理服务
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //在这里我们用自定的view来显示Notification
        mContentView = new RemoteViews(getPackageName(), R.layout.download_notification);
        mContentView.setTextViewText(R.id.tv_title, getString(R.string.start_dwonload));
        mContentView.setTextViewText(R.id.tv_percent, "0%");
        mContentView.setProgressBar(R.id.progress_bar, 100, 0, false);
        mContentView.setTextViewText(R.id.tv_current_size,"0KB");
        mContentView.setTextViewText(R.id.tv_total_size,"0KB");
        //创建消息提示
        mNotification = new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.start_dwonload))
                .setAutoCancel(true)
                .build();
        mNotification.contentView = mContentView;
        mNotificationManager.notify(mNotification_id, mNotification);
    }

    /**
     * 校验文件MD5值，没有则不校验
     *
     * @param fileName
     * @return
     */
    private boolean checkMd5Value(String fileName){
        boolean isSuccess = true;
        if (!TextUtils.isEmpty(mMd5Sign)){
            String md5Value = FileUtils.getFileMD5(fileName);
            if (TextUtils.isEmpty(md5Value)){
                mLog.debug("获取文件MD5值失败");
                isSuccess = false;
            }else {
                mMd5Sign = mMd5Sign.toUpperCase();
                md5Value = md5Value.toUpperCase();
                mLog.debug("mMd5Sign : {}, md5Value : {}", mMd5Sign, md5Value);
                //校验是否相等
                if (!mMd5Sign.equals(md5Value)){
                    mLog.debug("MD5值校验失败，取消安装");
                    isSuccess = false;
                }
            }
        }
        return isSuccess;
    }

}
