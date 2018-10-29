package com.yuri.tam.client.activity.setting;

import android.os.Bundle;

import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;
import com.yuri.tam.common.widget.TitleBuilder;

/**
 * 设置界面
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月28日
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTitle();
    }

    /**
     * 初始化标题栏
     */
    private void initTitle(){
        new TitleBuilder(this)
                .setLeftImage(R.drawable.arrow_icon)
                .setExternalTitleBgColor(getResources().getColor(R.color.holo_blue_light))
                .setTitleText(getString(R.string.setting))
                .build();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }
}
