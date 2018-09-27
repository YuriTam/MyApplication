package com.yuri.tam.client.activity.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;
import com.yuri.tam.client.fragment.left.LeftFragment;
import com.yuri.tam.client.fragment.main.MainFragment;
import com.yuri.tam.common.widget.DragLayout;
import com.yuri.tam.core.api.ApiRepository;
import com.yuri.tam.core.rx.Event;
import com.yuri.tam.core.rx.RxBus;

import butterknife.BindView;

/**
 * 主界面
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月7日
 */
public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.draglayout)
    DragLayout mDragLayout;

    private LeftFragment leftFragment;
    private MainFragment mainFragment;

    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMainActivity(true);

        new MainPresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        leftFragment = new LeftFragment();
        mainFragment = new MainFragment();

        transaction.replace(R.id.contentLeft, leftFragment, "");
        transaction.replace(R.id.contentMain, mainFragment, "");
        transaction.commit();
    }

    @Override
    protected void initEvent() {
        doUseRxBus();
    }

    @Override
    protected void initData() {
        RxBus.getDefault().post(new Event<>(1000, "RxBus发送测试消息"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onStart();
    }

    @Override
    protected void onRxBusEvent(Event event) {
        super.onRxBusEvent(event);
        mLog.debug("event code = {}, event message = {}", event.getCode(), event.getData());
    }

    /**
     * 打开左滑界面
     */
    public void open() {
        mDragLayout.open(true);
    }

    /**
     * 关闭侧滑
     */
    public void close() {
        mDragLayout.close();
    }

    @Override
    public void onBackPressed() {
        // 如果此时左边菜单是开启的状态，则先恢复
        if (mDragLayout.getStatus() == DragLayout.Status.Open) {
            mDragLayout.close();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
