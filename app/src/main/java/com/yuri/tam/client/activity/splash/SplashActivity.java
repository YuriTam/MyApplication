package com.yuri.tam.client.activity.splash;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;
import com.yuri.tam.client.activity.main.MainActivity;

import butterknife.BindView;

/**
 * 启动页
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月7日
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash_bg)
    ImageView ivSplashBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        animation.setAnimationListener(mAnimationListener);
        ivSplashBg.setAnimation(animation);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 动画监听器
     */
    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            intent2Activity(MainActivity.class);
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
