package com.yuri.tam.client.fragment.mine;

import com.yuri.tam.base.BasePresenter;
import com.yuri.tam.core.api.IDataSource;

import static com.common.utils.Utils.checkNotNull;

/**
 * 我的接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class MinePresenter extends BasePresenter implements MineContract.Presenter {

    private final MineContract.View mView;

    public MinePresenter(MineContract.View view, IDataSource repository) {
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
