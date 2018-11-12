package com.yuri.tam.client.fragment.coupon;

import com.yuri.tam.base.IBasePresenter;
import com.yuri.tam.base.IBaseView;
import com.yuri.tam.core.bean.CouponInfo;

import java.util.List;

/**
 * 操作接口
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年10月9日
 */
public interface CouponContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 返回卡卷列表
         *
         * @param couponList 卡卷列表
         * @param isSearch  是否查询操作（查询成功返回一条记录）
         */
        void onCouponList(List<CouponInfo> couponList, boolean isSearch);

        /**
         * 清空列表数据
         */
        void showClearList();

        /**
         * 提示没有更多数据
         */
        void showNotMoreData();

        /**
         * 提示暂无交易记录
         */
        void showEmptyList();

        /**
         * 界面状态
         *
         * @return
         */
        boolean isActive();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 重新获取卡卷列表
         */
        void refreshCouponList();

        /**
         * 查询卡卷记录(默认每页为20条)
         */
        void getCouponList();

        /**
         * 查询更多数据（即下页的数据）
         */
        void getMoreCouponList();

    }
}
