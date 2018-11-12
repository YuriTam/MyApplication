package com.yuri.tam.client.fragment.coupon;

import com.common.utils.TimeUtils;
import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.common.constant.SysConstant;
import com.yuri.tam.core.api.IDataSource;
import com.yuri.tam.core.bean.CouponInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 优惠券接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class CouponPresenter extends BasePresenter implements CouponContract.Presenter {

    private final CouponContract.View mView;

    private int currentPage = 0;  //当前页数
    private int currentCount = 0; //当前页交易记录数

    public CouponPresenter(CouponContract.View view, IDataSource repository) {
        super(view, repository);
        mView = checkNotNull(view);
    }

    @Override
    public void onStart() {
        if (mIsFirstAction){
            postDelay(() -> getCouponList());
            mIsFirstAction = false;
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void refreshCouponList() {
        currentCount = 0;
        currentPage = 0;
        getCouponList();
    }

    @Override
    public void getCouponList() {
        List<CouponInfo> couponList = new ArrayList<>();
        // FIXME: 2018/11/12 测试数据
        for (int i = 0; i <= 5; i++){
            CouponInfo info = new CouponInfo();
            info.setCouponType(0);
            info.setAmount("000000125000");
            info.setCouponNo("20181112163210000" + i);
            info.setExpTime(TimeUtils.getCurTimeString());
            info.setUsed(i == 2);
            couponList.add(info);
        }
        if (couponList == null || couponList.size() == 0) {
            postMainThread(() -> mView.showEmptyList());
            return;
        }
        currentPage = 1;
        currentCount = couponList.size();
        mLog.debug("当前页数：{}，当前页交易记录数：{}", currentPage, currentCount);
        postMainThread(() -> mView.onCouponList(couponList, false));
    }

    @Override
    public void getMoreCouponList() {
        if (currentCount < SysConstant.MAX_PAGE_SIZE) {
            postMainThread(() -> mView.showNotMoreData());
            return;
        }
        // FIXME: 2018/11/12 暂不实现
        List<CouponInfo> couponList = null;
        if (couponList == null || couponList.size() == 0) {
            postMainThread(() -> mView.showNotMoreData());
            return;
        }
        ++currentPage;
        currentCount = couponList.size();
        mLog.debug("当前页数：{}，当前页卡卷数量：{}", currentPage, currentCount);
        postMainThread(() -> mView.onCouponList(couponList, false));
    }

}
