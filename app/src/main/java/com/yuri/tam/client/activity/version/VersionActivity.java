package com.yuri.tam.client.activity.version;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;
import com.yuri.tam.common.widget.TitleBuilder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 版本检测界面
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年10月24日
 */
public class VersionActivity extends BaseActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name_version)
    TextView tvNameVersion;

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
                .setTitleText(getString(R.string.version_info))
                .build();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_version;
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

    @OnClick({R.id.title_iv_left, R.id.btn_check_update})
    public void onClick(View view){
        if (UIUtils.isDoubleClick()) return;
        switch (view.getId()){
            case R.id.title_iv_left:
                finish();
                break;
            case R.id.btn_check_update:
                showToast("已是最新版本");
                break;
            default:
                break;
        }
    }

}
