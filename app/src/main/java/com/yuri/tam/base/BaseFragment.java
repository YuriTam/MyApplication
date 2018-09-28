package com.yuri.tam.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.NetworkUtils;
import com.common.utils.ToastUtils;
import com.yuri.tam.R;
import com.yuri.tam.client.fragment.ProgressDialogFragment;
import com.yuri.tam.common.utils.PermissionUtils;
import com.yuri.tam.core.rx.Event;
import com.yuri.tam.core.rx.RxBus;

import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 继承Fragment
 *
 * @author 谭忠扬-YuriTam
 * @time 2016年10月13日
 */
public abstract class BaseFragment extends Fragment {
    protected final Logger mLog = LoggerFactory.getLogger(getClass().getSimpleName());
    //订阅管理
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    protected Context mContext;
    protected Activity mActivity;
    Unbinder unbinder;

    //加载弹出层
    private ProgressDialogFragment mWaitDialogFragment;

    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        this.mContext = mContext;
        this.mActivity = (Activity) mContext;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEvent();
        initData();
    }

    /**
     * 初始化UI
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View initView(LayoutInflater inflater,ViewGroup container);

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 订阅事件
     */
    protected void doUseRxBus(){
        mDisposables.add(RxBus.getDefault()
                .register(Event.class, event -> {
                    if (event == null) return;
                    onRxBusEvent(event);
                }));
    }

    /**
     * 交给子类重写处理订阅事件
     *
     * @param event 事件信息
     */
    protected void onRxBusEvent(Event event){}

    /**
     * 跳转到其它页面
     *
     * @param tarActivity
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(mContext, tarActivity);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到其它页面-带参数
     *
     * @param tarActivity
     * @param mBundle
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity, Bundle mBundle) {
        Intent intent = new Intent(mContext, tarActivity);
        intent.putExtras(mBundle);
        mContext.startActivity(intent);
    }

    /**
     * 弹出提示信息
     *
     * @param msg
     */
    protected void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        ToastUtils.show(msg);
    }

    /**
     * 加载弹出层
     *
     * @param active 是否显示
     * @param title 标题
     * @param tip 提示信息
     */
    protected void showWaitingIndicator(boolean active, String title, String tip) {
        if (active) {
            if (mWaitDialogFragment == null) {
                mWaitDialogFragment = new ProgressDialogFragment();
            }
            mWaitDialogFragment.setTip(tip);
            mWaitDialogFragment.setTipTitle(title);
            mWaitDialogFragment.setCancelable(false);
            mWaitDialogFragment.show(getActivity().getSupportFragmentManager(), null);
        } else {
            if (mWaitDialogFragment != null) {
                mWaitDialogFragment.dismiss();
                mWaitDialogFragment = null;
            }
        }
    }

    /**
     * 异常提示语
     *
     * @param e 异常类型
     */
    protected void showExceptionTips(Throwable e){
        if (e == null) return;
        if (!NetworkUtils.isAvailable(getContext())){
            showToast(getString(R.string.net_work_is_not_available));
            return;
        }
        if (e instanceof UnknownHostException){
            showToast(getString(R.string.unknown_host_exception));
        } else if (e instanceof ConnectException || e instanceof ConnectTimeoutException){
            showToast(getString(R.string.connect_exception));
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException){
            showToast(getString(R.string.response_exception));
        } else {
            showToast(getString(R.string.unknown_exception));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
    }
}
