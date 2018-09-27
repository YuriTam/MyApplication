package com.yuri.tam.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.common.utils.BarUtils;
import com.common.utils.NetworkUtils;
import com.common.utils.ToastUtils;
import com.yuri.tam.R;
import com.yuri.tam.client.fragment.ProgressDialogFragment;
import com.yuri.tam.common.constant.SysCode;
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
import io.reactivex.disposables.CompositeDisposable;
import java8.util.Optional;

/**
 * Activity 基类封装
 *
 * @author 谭忠扬-YuriTam
 * @time 2016年9月14日
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final Logger mLog = LoggerFactory.getLogger(getClass().getSimpleName());
    //订阅管理
    private final CompositeDisposable mDisposables = new CompositeDisposable();
    //退出的间隔时间
    private final int INTERNAL_TIME = 2000;
    //记录最后一次返回时间
    private long mLastTime;
    //是否禁用返回键
    private boolean isBanBackKey = false;
    //是否主界面
    private boolean isMainActivity = false;
    //计时器
    private MyCountTimer mCountTimer;
    private DownTimerListener mTimerListener;
    //加载弹出层
    private ProgressDialogFragment mWaitDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        BarUtils.setTransparentStatusBar(this);
        //设置布局文件
        setContentView(layoutId());
        ButterKnife.bind(this);

        //状态栏屏蔽下拉
        //Settings.System.putInt(getContentResolver(), "status_bar_disabled", 1);
        //getWindow().addFlags(3);//屏蔽home键
        //getWindow().addFlags(5);//屏蔽菜单键

        initView();
        initEvent();
        initData();
    }

    /**
     * 设置布局文件
     */
    protected abstract int layoutId();

    /**
     * 初始化
     */
    protected abstract void initView();

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
                    switch (event.getCode()) {
                        case Event.EVENT_FINISH:
                            finish();
                            break;
                        default:
                            onRxBusEvent(event);
                            break;
                    }
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
        Intent intent = new Intent(BaseActivity.this, tarActivity);
        startActivity(intent);
    }

    /**
     * 跳转到其它页面-带参数
     *
     * @param tarActivity
     * @param mBundle
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity, Bundle mBundle) {
        Intent intent = new Intent(BaseActivity.this, tarActivity);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    /**
     * 跳转到其他界面
     *
     * @param tarActivity
     */
    protected void intent2ActivityForResult(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(BaseActivity.this, tarActivity);
        startActivityForResult(intent, SysCode.REQ_CODE);
    }

    /**
     * 跳转到其他界面，带参数
     *
     * @param tarActivity
     * @param mBundle
     */
    protected void intent2ActivityForResult(Class<? extends Activity> tarActivity, Bundle mBundle) {
        Intent intent = new Intent(BaseActivity.this, tarActivity);
        intent.putExtras(mBundle);
        startActivityForResult(intent, SysCode.REQ_CODE);
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
            mWaitDialogFragment.show(getSupportFragmentManager(), null);
        } else {
            if (mWaitDialogFragment != null) {
                mWaitDialogFragment.dismiss();
                mWaitDialogFragment = null;
            }
        }
    }

    /**
     * 弹出提示信息
     *
     * @param message
     */
    protected void showToast(String message) {
        Optional.ofNullable(message)
                .filter(s -> !TextUtils.isEmpty(s))
                .ifPresent(s -> ToastUtils.show(s));
    }

    /**
     * 异常提示语
     *
     * @param e 异常类型
     */
    protected void showExceptionTips(Throwable e){
        if (e == null) return;
        if (!NetworkUtils.isAvailable(this)){
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

    /**
     * 拦截HOME键
     */
    @Override
    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_HOME: //拦截HOME键
                    //判断些界面是为主页
                    if (isMainActivity()) return true;
                    break;
                case KeyEvent.KEYCODE_BACK: //拦截返回键
                    //判断是否禁用返回键
                    if(isBanBackKey()) return true;
                    //判断些界面是为主页
                    if (isMainActivity()){
                        if((System.currentTimeMillis() - mLastTime) > INTERNAL_TIME){
                            showToast("再按一次退出程序");
                            mLastTime = System.currentTimeMillis();
                            return true;
                        }
                    }
                    finish();
                    break;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_left_in);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
        if (mCountTimer != null){
            mCountTimer.cancel();
        }
        mCountTimer = null;
    }

    protected void setBanBackKey(boolean isBanBackKey){
        this.isBanBackKey = isBanBackKey;
    }

    protected boolean isBanBackKey(){
        return isBanBackKey;
    }

    protected boolean isMainActivity() {
        return isMainActivity;
    }

    protected void setMainActivity(boolean isMainActivity) {
        this.isMainActivity = isMainActivity;
    }

    /**
     * 开始倒计时
     *
     * @param timeOut       倒计时时间
     * @param timerListener 倒计时回调
     */
    public void startDownTimer(long timeOut, DownTimerListener timerListener) {
        mTimerListener = timerListener;
        if (mCountTimer == null) {
            mCountTimer = new MyCountTimer(timeOut * 1000);
        }
        mCountTimer.start();
    }

    /**
     * 重新计时
     */
    public void restartDownTimer() {
        if (mCountTimer != null) {
            mCountTimer.cancel();
            mCountTimer.start();
        }
    }

    /**
     * 取消倒计时
     */
    public void cancelDownTimer() {
        if (mCountTimer != null) mCountTimer.cancel();
    }

    /**
     * 倒计时内部类
     *
     * @author 谭忠扬-YuriTam
     * @time 2017年5月10日
     */
    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture) {
            super(millisInFuture, 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mTimerListener != null) {
                mTimerListener.onTick(millisUntilFinished / 1000);
            }
        }

        @Override
        public void onFinish() {
            if (mTimerListener != null) {
                mTimerListener.onFinish();
            }
        }
    }

    /**
     * 倒计时回调接口，回调时间单位为秒
     */
    public interface DownTimerListener {

        void onTick(long secondsUntilFinished);

        void onFinish();
    }
}
