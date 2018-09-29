package com.yuri.tam.common.widget.gridpage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Kol(Fang Qiang) on 2017/7/6.
 * usage:
 * modify by 谭忠扬-YuriTam
 */
public class GridItemView extends FrameLayout {

    private Context mContext;
    private TextView mItemName;

    public GridItemView(@NonNull Context context) {
        this(context, null);
    }

    public GridItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context){
        this.mContext = context;
        mItemName = new TextView(context);
        mItemName.setTextSize(18);
        mItemName.setTextColor(Color.WHITE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 30;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mItemName.setLayoutParams(params);

        addView(mItemName);
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setItemSource(MyGridView.DataSource data){
        setBackground(new BitmapDrawable(getResources(), data.getBitmap()));
        mItemName.setText(getContext().getString(data.getResId()));
    }

}
