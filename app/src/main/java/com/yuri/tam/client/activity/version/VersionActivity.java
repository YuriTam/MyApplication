package com.yuri.tam.client.activity.version;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;
import com.yuri.tam.common.widget.TitleBuilder;
import com.yuri.tam.core.api.ApiRepository;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 版本检测界面
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年10月24日
 */
public class VersionActivity extends BaseActivity implements VersionContract.View {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name_version)
    TextView tvNameVersion;

    private VersionContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new VersionPresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_version;
    }

    @Override
    protected void initView() {
        //标题栏
        new TitleBuilder(this)
                .setLeftImage(R.drawable.arrow_icon)
                .setExternalTitleBgColor(getResources().getColor(R.color.holo_blue_light))
                .setTitleText(getString(R.string.version_info))
                .build();
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

    @Override
    public void setPresenter(VersionContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
