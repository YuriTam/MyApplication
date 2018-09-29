package com.yuri.tam.client.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.utils.UIUtils;
import com.stx.xhb.xbanner.XBanner;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.client.activity.main.MainActivity;
import com.yuri.tam.common.widget.TitleBuilder;
import com.yuri.tam.common.widget.XXBanner;
import com.yuri.tam.core.api.ApiRepository;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月29日
 */
public class HomeFragment extends BaseFragment implements HomeContract.View, XBanner.XBannerAdapter {
    private Integer[] mImages = {R.mipmap.ad_point,R.mipmap.ad_quick,R.mipmap.ad_union};

    @BindView(R.id.banner)
    XXBanner mBanner;

    private HomeContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HomePresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initTitle(view);

        return view;
    }

    //初始化标题
    private void initTitle(View view) {
        new TitleBuilder(view)
                .setExternalTitleBgColor(getResources().getColor(R.color.holo_blue_light))
                .setLeftImage(R.drawable.slide_bar_icon)
                .setTitleText(getString(R.string.home))
                .build();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        mBanner.setData(Arrays.asList(mImages), null);
        mBanner.setmAdapter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void loadBanner(XBanner banner, Object model, View view, int position) {
        ((ImageView) view).setImageResource(mImages[position]);
    }

    @OnClick({R.id.title_iv_left})
    public void onClick(View view) {
        if (UIUtils.isDoubleClick()) return;
        switch (view.getId()) {
            case R.id.title_iv_left:
                ((MainActivity) getActivity()).open();
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
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
