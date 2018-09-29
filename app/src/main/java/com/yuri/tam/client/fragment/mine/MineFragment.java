package com.yuri.tam.client.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.common.widget.TitleBuilder;
import com.yuri.tam.core.api.ApiRepository;

import butterknife.OnClick;

/**
 * 我的
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月29日
 */
public class MineFragment extends BaseFragment implements MineContract.View {

    private MineContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MinePresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        initTitle(view);

        return view;
    }

    //初始化标题
    private void initTitle(View view) {
        new TitleBuilder(view)
                .setTitleText(getString(R.string.mine))
                .build();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.ll_payment, R.id.ll_sale, R.id.ll_version})
    public void onClick(View view){
        if (UIUtils.isDoubleClick()) return;
        switch (view.getId()){
            case R.id.ll_payment:
                showToast("敬请期待");
                break;
            case R.id.ll_sale:
                showToast("敬请期待");
                break;
            case R.id.ll_version:
                showToast("敬请期待");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
