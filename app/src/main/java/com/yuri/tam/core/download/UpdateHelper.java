package com.yuri.tam.core.download;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;

import com.common.utils.AppUtils;
import com.google.gson.Gson;
import com.yuri.tam.R;
import com.yuri.tam.base.App;
import com.yuri.tam.client.fragment.TextDialogFragment;
import com.yuri.tam.core.api.ApiHttpService;
import com.yuri.tam.core.bean.UpdateInfo;
import com.yuri.tam.core.download.listener.OnCheckListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 检查是否有新版本
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年12月05日
 */
public class UpdateHelper {
    private Logger mLog = LoggerFactory.getLogger(UpdateHelper.class.getSimpleName());
    private static final String MD_5 = "md_5";
    private static final String VERSION_NAME = "version_name";
    private static final String DOWNLOAD_URL = "download_url";
    private static final String IS_AUTO_INSTALL = "is_auto_install";

    //上下文
    private FragmentActivity mActivity;
    //检查新版本地址
    private String mCheckUrl;
    //有新版本是否自动下载
    private boolean mIsAutoInstall;
    //当前应用的版本号
    private int mVersionCode;
    //是否有新版本
    private boolean mIsNewVersion;
    //版本更新信息
    private UpdateInfo mUpdateInfo;
    //检查结果回调
    private OnCheckListener mCheckListener;

    public UpdateHelper(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 检查地址
     *
     * @param checkUrl
     * @return
     */
    public UpdateHelper setCheckUrl(String checkUrl) {
        this.mCheckUrl = checkUrl;
        mLog.debug("mCheckUrl : {}", checkUrl);
        return this;
    }

    /**
     * 自动安装
     *
     * @param isAutoInstall
     * @return
     */
    public UpdateHelper setIsAutoInstall(boolean isAutoInstall) {
        this.mIsAutoInstall = isAutoInstall;
        mLog.debug("mIsAutoInstall : {}", isAutoInstall);
        return this;
    }

    /**
     * 检查结果回调
     *
     * @param checkListener
     * @return
     */
    public UpdateHelper setCheckListener(OnCheckListener checkListener) {
        this.mCheckListener = checkListener;
        return this;
    }

    /**
     * 开始检查
     */
    public void build(){
        if (TextUtils.isEmpty(mCheckUrl)) return;
        //开始检查
        ApiHttpService.getInstance().checkUpdate()
                .subscribe(new Observer<UpdateInfo>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        if (mCheckListener != null) mCheckListener.onStart();
                    }

                    @Override
                    public void onNext(UpdateInfo updateInfo) {
                        if (updateInfo == null){
                            if (mCheckListener != null) mCheckListener.onError(new Error("返回数据为空，检查失败"));
                            return;
                        }
                        mUpdateInfo = updateInfo;
                        mLog.debug("返回应用信息 : {}", new Gson().toJson(mUpdateInfo));
                        //获取当前版本号
                        mVersionCode = AppUtils.getAppVersionCode(App.sContext);
                        //判断应用版本号
                        mIsNewVersion = mUpdateInfo.getVersionCode() > mVersionCode;
                        mLog.debug("当前版本号 : {}, 最新版本号 : {}", mVersionCode, mUpdateInfo.getVersionCode());
                        if (mCheckListener != null) mCheckListener.onResult(mIsNewVersion);
                        if (mIsNewVersion){
                            //如果是强制更新，直接启动下载服务
                            if (mUpdateInfo.isForceUpdate()){
                                mIsAutoInstall = true;  //重置成自动安装
                                startDownloadService();
                                return;
                            }
                            showDialogUpdateInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mCheckListener != null) mCheckListener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 弹出层提示有新版本
     */
    private void showDialogUpdateInfo(){
        if (mUpdateInfo == null) return;
        TextDialogFragment dialogFragment = new TextDialogFragment();
        dialogFragment.setDialogType(TextDialogFragment.DIALOG_TYPE_SUCCESS);
        dialogFragment.setTitleText(App.sContext.getString(R.string.version_info_tips));
        dialogFragment.setContentText(mUpdateInfo.toString(), Gravity.LEFT);
        dialogFragment.setNegativeText(App.sContext.getString(R.string.cancel));
        dialogFragment.setPositionText(App.sContext.getString(R.string.confirm));
        dialogFragment.setDialogListener(new TextDialogFragment.OnDialogListener() {
            @Override
            public void onCancel() {}

            @Override
            public void onConfirm() {
                startDownloadService();
            }
        });
        dialogFragment.show(mActivity.getSupportFragmentManager(), null);
    }

    /**
     * 启动下载服务
     */
    private void startDownloadService(){
        if (mUpdateInfo == null) return;
        Intent intent = new Intent(App.sContext, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putString(MD_5, mUpdateInfo.getMd5());
        bundle.putString(VERSION_NAME, mUpdateInfo.getVersionName());
        bundle.putString(DOWNLOAD_URL, mUpdateInfo.getUrl());
        bundle.putBoolean(IS_AUTO_INSTALL, mIsAutoInstall);
        intent.putExtras(bundle);
        mActivity.startService(intent);
    }
}
