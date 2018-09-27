package com.yuri.tam.client.activity.main;

import com.yuri.tam.base.IBasePresenter;
import com.yuri.tam.base.IBaseView;

/**
 * 操作接口
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年10月9日
 */
public interface MainContract {

    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {


    }
}
