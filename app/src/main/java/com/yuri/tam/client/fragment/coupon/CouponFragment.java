package com.yuri.tam.client.fragment.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.utils.ToastUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.client.adapter.CouponListAdapter;
import com.yuri.tam.common.widget.RecyclerDivider;
import com.yuri.tam.common.widget.TitleBuilder;
import com.yuri.tam.core.api.ApiRepository;
import com.yuri.tam.core.bean.CouponInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 优惠券
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月29日
 */
public class CouponFragment extends BaseFragment implements CouponContract.View, CouponListAdapter.OnItemClickListener {

    @BindView(R.id.rv_coupon_list)
    RecyclerView rvCouponList;

    private List<CouponInfo> mCouponList;
    private boolean isLoading;
    private LinearLayoutManager mLayoutManager;
    private CouponListAdapter mListAdapter;

    private CouponContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CouponPresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        initTitle(view);

        return view;
    }

    //初始化标题
    private void initTitle(View view) {
        new TitleBuilder(view)
                .setExternalTitleBgColor(getResources().getColor(R.color.holo_blue_light))
                .setTitleText(getString(R.string.coupon))
                .build();
    }

    @Override
    protected void initEvent() {
        //监听滑动事件
        rvCouponList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mListAdapter.getItemCount();
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount == (lastVisibleItemPosition + 1) && totalItemCount > 4) {
                    //这里加载更多数据
                    mPresenter.getMoreCouponList();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mLayoutManager = new LinearLayoutManager(mContext);
        rvCouponList.setLayoutManager(mLayoutManager);
        rvCouponList.addItemDecoration(new RecyclerDivider(mContext, 15, RecyclerDivider.HORIZONTAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        //首次加载数据
        mPresenter.onStart();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onCouponList(List<CouponInfo> couponList, boolean isSearch) {
        if (mCouponList == null) {
            mCouponList = new ArrayList<>();
        }
        //搜索时返回一个交易记录，先清除之前的交易记录
        if (mCouponList.size() == 1 && mCouponList.size() != 0 && isSearch) {
            mCouponList.clear();
        }
        mCouponList.addAll(couponList);
        if (mListAdapter == null) {
            mListAdapter = new CouponListAdapter(mContext);
            rvCouponList.setAdapter(mListAdapter);
            mListAdapter.setOnItemClickListener(this);
        }
        mListAdapter.addCouponList(mCouponList);
        //移除上拉加载更多视图
        mListAdapter.notifyItemRemoved(mListAdapter.getItemCount());
        isLoading = false;
    }

    @Override
    public void showClearList() {
        if (mCouponList != null && !mCouponList.isEmpty()){
            mCouponList.clear();
        }
        if (mListAdapter != null){
            mListAdapter.clear();
        }
        rvCouponList.removeAllViews();
    }

    @Override
    public void showNotMoreData() {
        ToastUtils.show(getString(R.string.has_no_more_data));
        //移除上拉加载更多视图
        mListAdapter.notifyItemRemoved(mListAdapter.getItemCount());
        isLoading = false;
    }

    @Override
    public void showEmptyList() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(CouponContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
