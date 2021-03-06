package com.yuri.tam.client.fragment.mine;

import com.yuri.tam.base.IBasePresenter;
import com.yuri.tam.base.IBaseView;

/**
 * 操作接口
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年10月9日
 */
public interface MineContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 界面状态
         *
         * @return
         */
        boolean isActive();
    }

    interface Presenter extends IBasePresenter {


    }
}
