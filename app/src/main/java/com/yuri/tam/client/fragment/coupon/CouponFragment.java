package com.yuri.tam.client.fragment.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.common.widget.TitleBuilder;

/**
 * 优惠券
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月29日
 */
public class CouponFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        initTitle(view);

        return view;
    }

    //初始化标题
    private void initTitle(View view) {
        new TitleBuilder(view)
                .setExternalTitleBgColor(getResources().getColor(R.color.holo_blue_light))
                .setTitleText("优惠券")
                .build();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }
}