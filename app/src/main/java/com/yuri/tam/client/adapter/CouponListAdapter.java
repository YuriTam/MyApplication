package com.yuri.tam.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.utils.TimeUtils;
import com.yuri.tam.R;
import com.yuri.tam.common.constant.SysConstant;
import com.yuri.tam.common.enums.CouponType;
import com.yuri.tam.common.utils.StringUtils;
import com.yuri.tam.core.bean.CouponInfo;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 交易列表适配器
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年08月14日
 */
public class CouponListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = CouponListAdapter.class.getSimpleName();
    private static final int TYPE_TRANS_ITEM = 0;
    private static final int TYPE_LOAD_FOOTER = 1;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CouponInfo> mCouponList;
    private OnItemClickListener mListener;

    public CouponListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 添加交易列表
     *
     * @param couponList
     */
    public void addCouponList(List<CouponInfo> couponList) {
        mCouponList = couponList;
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (mCouponList != null && !mCouponList.isEmpty()) {
            mCouponList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (mCouponList == null) return 0;
        if (mCouponList.size() <= 4) {
            return mCouponList.size();
        }
        return mCouponList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //4为临界值
        if (position + 1 == getItemCount() && getItemCount() > 4) {
            return TYPE_LOAD_FOOTER;
        } else {
            return TYPE_TRANS_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TRANS_ITEM) {
            View view = mInflater.inflate(R.layout.item_coupon_list, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_LOAD_FOOTER) {
            View view = mInflater.inflate(R.layout.item_loading_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == mCouponList.size()) {
            return;
        }
        if (holder instanceof ItemViewHolder) {
            CouponInfo info = mCouponList.get(position);
            ItemViewHolder mHolder = (ItemViewHolder) holder;
            //先初始化为不可见，防止显示时数据混乱
            mHolder.tvTag.setVisibility(View.GONE);

            //卡卷类型
            CouponType type = CouponType.values()[info.getCouponType()];
            mHolder.tvCouponType.setText(type.getName());
            //卡卷编号
            mHolder.tvCouponNo.setText(info.getCouponNo());
            //卡卷金额
            mHolder.tvAmount.setText(StringUtils.formatAmount(info.getAmount(), SysConstant.FUND_DIGITS));
            //有效期
            mHolder.tvExpTime.setText(TimeUtils.date2String(new Date()));
            //是否已使用
            if (info.isUsed()){
                mHolder.tvTag.setVisibility(View.VISIBLE);
                mHolder.tvTag.setText("已使用");
            } else {
                //fixme 判断是否过期
//                mHolder.tvTag.setVisibility(View.VISIBLE);
//                mHolder.tvTag.setText("已过期");
            }
        }
        holder.itemView.setTag(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_coupon_type)
        TextView tvCouponType;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_coupon_no)
        TextView tvCouponNo;
        @BindView(R.id.tv_exp_time)
        TextView tvExpTime;
        @BindView(R.id.tv_tag)
        TextView tvTag;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick((Integer) view.getTag());
            }
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
