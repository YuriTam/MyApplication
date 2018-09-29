package com.yuri.tam.core.bean;

import android.text.TextUtils;

import com.common.utils.ConvertUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.App;

import java8.util.Optional;

/**
 * 应用更新信息实体类
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年12月05日
 */
public class UpdateInfo {

    private String url;            //下载地址
    private String apkName;        //应用名称
    private String packageName;    //应用包名
    private int versionCode;       //版本号
    private String versionName;    //版本名
    private long size;             //文件大小
    private String md5;            //md5签名
    private String updateMessage;  //更新内容
    private boolean forceUpdate;   //是否强制更新

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(apkName)
                .filter(s -> !TextUtils.isEmpty(s))
                .ifPresent(s -> sb.append(App.sContext.getString(R.string.apk_name_desc) + s + "\n"));
        Optional.ofNullable(versionName)
                .filter(s -> !TextUtils.isEmpty(s))
                .ifPresent(s -> sb.append(App.sContext.getString(R.string.apk_version_name) + s + "\n"));
        Optional.ofNullable(size)
                .filter(s -> s != 0)
                .ifPresent(s -> sb.append(App.sContext.getString(R.string.apk_size) + ConvertUtils.byte2FitSize(s) + "\n"));
        sb.append(App.sContext.getString(R.string.update_message) + "\n");
        sb.append(Optional.ofNullable(updateMessage).orElse(""));
        return sb.toString();
    }
}
