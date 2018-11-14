package com.yuri.tam.client.fragment.home;

import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.core.api.IDataSource;

import static com.common.utils.Utils.checkNotNull;

/**
 * 首页接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class HomePresenter extends BasePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;

    public HomePresenter(HomeContract.View view, IDataSource repository) {
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
