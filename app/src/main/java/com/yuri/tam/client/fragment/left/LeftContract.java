package com.yuri.tam.client.fragment.left;

import android.support.v4.app.FragmentActivity;

import com.yuri.tam.base.IBasePresenter;
import com.yuri.tam.base.IBaseView;

/**
 * 左侧操作接口
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年10月9日
 */
public interface LeftContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 提示已经是最新版本
         */
        void showIsNewestVersion();

        /**
         * 跳转到设置页面
         */
        void intent2Setting();

        /**
         * 提示输入格式不正确
         *
         * @param errMsg 错误提示消息
         */
        void showErr(String errMsg);

        /**
         * 提示输入不可为空
         */
        void showEmpty();

        /**
         * 检查更新弹出层
         *
         * @param active
         */
        void setCheckUpdateIndicator(boolean active);

        /**
         * 异常提示
         *
         * @param e 异常信息
         */
        void showException(Throwable e);

        /**
         * 界面状态
         *
         * @return
         */
        boolean isActive();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 检查版本更新
         *
         * @param activity
         */
        void checkUpdate(FragmentActivity activity);

        /**
         * 安全密码登录
         *
         * @param password
         */
        void login2Setting(String password);
    }
}
