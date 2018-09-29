package com.yuri.tam.client.fragment.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.client.fragment.coupon.CouponFragment;
import com.yuri.tam.client.fragment.home.HomeFragment;
import com.yuri.tam.client.fragment.mine.MineFragment;
import com.yuri.tam.client.fragment.service.ServiceFragment;
import com.yuri.tam.common.utils.BottomNavigationViewHelper;
import com.yuri.tam.core.api.ApiRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主界面窗口
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月7日
 */
public class MainFragment extends BaseFragment implements BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {

    @BindView(R.id.foot_navigation)
    BottomNavigationView mNavigationView;

    private MainContract.Presenter mPresenter;

    private List<Fragment> mFragmentList;
    private Fragment mCurrentFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MainPresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    protected void initEvent() {
        mNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        BottomNavigationViewHelper.disableShiftMode(mNavigationView);

        //初始化子界面
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new CouponFragment());
        mFragmentList.add(new ServiceFragment());
        mFragmentList.add(new MineFragment());

        showFragment(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                showFragment(0);
                return true;
            case R.id.nav_coupon:
                showFragment(1);
                return true;
            case R.id.nav_service:
                showFragment(2);
                return true;
            case R.id.nav_mine:
                showFragment(3);
                return true;
            default:
                return false;
        }
    }

    /**
     * 显示相应子界面
     *
     * @param index 下标
     */
    public void showFragment(int index) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        mCurrentFragment = mFragmentList.get(index);
        if (!mCurrentFragment.isAdded()) {
            transaction.add(R.id.main_container, mCurrentFragment);
        }
        transaction.show(mCurrentFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
