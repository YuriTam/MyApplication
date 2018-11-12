package com.yuri.tam.client.fragment.main;

import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.core.api.IDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 主界面接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class MainPresenter extends BasePresenter implements MainContract.Presenter {

    private final MainContract.View mView;

    public MainPresenter(MainContract.View view, IDataSource repository) {
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
