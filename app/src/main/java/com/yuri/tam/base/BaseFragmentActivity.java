package com.yuri.tam.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.common.utils.BarUtils;
import com.common.utils.ToastUtils;
import com.yuri.tam.common.constant.SysCode;
import com.yuri.tam.common.utils.PermissionUtils;

/**
 * base FragmentActivity
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年06月07日
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
    private static final String TAG = BaseFragmentActivity.class.getSimpleName();

    //是否禁用返回键
    private boolean isBanBackKey = false;
    //是否主界面
    private boolean isMainActivity = false;
    private long exitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setTransparentStatusBar(this);

        //状态栏屏蔽下拉
        //Settings.System.putInt(getContentResolver(), "status_bar_disabled", 1);
        //getWindow().addFlags(3);//屏蔽home键
        //getWindow().addFlags(5);//屏蔽菜单键
    }

    /**
     * 执行初始化方法
     *
     * @author 谭忠扬-YuriTam
     * @time 2017年3月31日
     */
    protected void onStartInitMethod(){
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化
     *
     * @author 谭忠扬-YuriTam
     * @time 2016年10月9日
     */
    protected abstract void initView();

    /**
     * 初始化事件
     *
     * @author 谭忠扬-YuriTam
     * @time 2016年10月9日
     */
    protected abstract void initEvent();

    /**
     * 初始化数据
     *
     * @author 谭忠扬-YuriTam
     * @time 2016年10月9日
     */
    protected abstract void initData();

    /**
     * 跳转到其它页面
     *
     * @author 谭忠扬-YuriTam
     * @date 2016年9月14日
     * @param tarActivity
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(BaseFragmentActivity.this, tarActivity);
        startActivity(intent);
    }

    /**
     * 跳转到其它页面-带参数
     *
     * @author 谭忠扬-YuriTam
     * @date 2016年9月14日
     * @param tarActivity
     * @param mBundle
     */
    protected void intent2Activity(Class<? extends Activity> tarActivity, Bundle mBundle) {
        Intent intent = new Intent(BaseFragmentActivity.this, tarActivity);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    /**
     * 跳转到其他界面
     *
     * @author 谭忠扬-YuriTam
     * @time 2017年5月5日
     * @param tarActivity
     */
    protected void intent2ActivityForResult(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(BaseFragmentActivity.this, tarActivity);
        startActivityForResult(intent, SysCode.REQ_CODE);
    }

    /**
     * 跳转到其他界面，带参数
     *
     * @author 谭忠扬-YuriTam
     * @time 2017年5月5日
     * @param tarActivity
     * @param mBundle
     */
    protected void intent2ActivityForResult(Class<? extends Activity> tarActivity, Bundle mBundle) {
        Intent intent = new Intent(BaseFragmentActivity.this, tarActivity);
        intent.putExtras(mBundle);
        startActivityForResult(intent, SysCode.REQ_CODE);
    }

    /**
     * 弹出提示信息
     *
     * @author 谭忠扬-YuriTam
     * @date 2016年9月14日
     * @param msg
     */
    protected void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 拦截HOME键
     *
     * @author 谭忠扬-YuriTam
     * @time 2017年4月5日
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
                //拦截HOME键
                case KeyEvent.KEYCODE_HOME:
                    //判断些界面是为主页
                    if (isMainActivity()){
                        return false;
                    }
                    break;
                //拦截返回键
                case KeyEvent.KEYCODE_BACK:
                    //判断是否禁用返回键
                    if(isBanBackKey()){
                        return true;
                    }
                    //判断些界面是为主页
                    if (isMainActivity()){
                        if((System.currentTimeMillis() - exitTime) > 2000){
                            showToast("再按一次退出程序");
                            exitTime = System.currentTimeMillis();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置是否退出程序
     *
     * @param isBanBackKey
     */
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
}
