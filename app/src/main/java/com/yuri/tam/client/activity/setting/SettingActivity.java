package com.yuri.tam.client.activity.setting;

import android.os.Bundle;
import android.view.View;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;
import com.yuri.tam.common.widget.TitleBuilder;

import butterknife.OnClick;

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

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        //标题栏
        new TitleBuilder(this)
                .setLeftImage(R.drawable.arrow_icon)
                .setExternalTitleBgColor(getResources().getColor(R.color.holo_blue_light))
                .setTitleText(getString(R.string.setting))
                .build();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.title_iv_left, R.id.ll_merchant_setting, R.id.ll_key_setting, R.id.ll_system_setting,
            R.id.ll_trans_setting, R.id.ll_sign_setting, R.id.ll_other_setting, R.id.ll_version_info})
    public void onViewClicked(View view) {
        if (UIUtils.isDoubleClick()) return;
        switch (view.getId()) {
            case R.id.title_iv_left:
                finish();
                break;
            case R.id.ll_merchant_setting:
                showToast("敬请期待...");
                break;
            case R.id.ll_key_setting:
                showToast("敬请期待...");
                break;
            case R.id.ll_system_setting:
                showToast("敬请期待...");
                break;
            case R.id.ll_trans_setting:
                showToast("敬请期待...");
                break;
            case R.id.ll_sign_setting:
                showToast("敬请期待...");
                break;
            case R.id.ll_other_setting:
                showToast("敬请期待...");
                break;
            case R.id.ll_version_info:
                showToast("敬请期待...");
                break;
            default:
                break;
        }
    }
}
