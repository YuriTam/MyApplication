package com.yuri.tam.client.fragment.service;

import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.core.api.IDataSource;

import static com.common.utils.Utils.checkNotNull;

/**
 * 服务接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class ServicePresenter extends BasePresenter implements ServiceContract.Presenter {

    private final ServiceContract.View mView;

    public ServicePresenter(ServiceContract.View view, IDataSource repository) {
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
