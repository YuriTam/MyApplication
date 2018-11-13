package com.yuri.tam.client.activity.version;

import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.core.api.IDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 版本信息面接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年11月12日
 */
public class VersionPresenter extends BasePresenter implements VersionContract.Presenter {

    private final VersionContract.View mView;

    public VersionPresenter(VersionContract.View view, IDataSource repository) {
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
