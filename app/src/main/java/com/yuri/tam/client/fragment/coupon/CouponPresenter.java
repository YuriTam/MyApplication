package com.yuri.tam.client.fragment.coupon;

import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.core.api.IDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 优惠券接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class CouponPresenter extends BasePresenter implements CouponContract.Presenter {

    private final CouponContract.View mView;

    public CouponPresenter(CouponContract.View view, IDataSource repository) {
        super(view, repository);
        mView = checkNotNull(view);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

}
