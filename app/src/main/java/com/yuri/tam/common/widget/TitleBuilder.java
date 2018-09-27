package com.yuri.tam.common.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuri.tam.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义标题栏
 *
 * @author 谭忠扬
 * @time 2015年9月7日
 */
public class TitleBuilder {

    @BindView(R.id.title_iv_left)
    ImageView titleIvLeft;
    @BindView(R.id.title_tv_left)
    TextView titleTvLeft;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_iv)
    ImageView titleIv;
    @BindView(R.id.title_iv_right)
    ImageView titleIvRight;
    @BindView(R.id.title_tv_right)
    TextView titleTvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;

    public TitleBuilder(Activity activity) {
        ButterKnife.bind(this, activity);
    }

    public TitleBuilder(View view) {
        ButterKnife.bind(this, view);
    }

    public void invisible() {
        llTitle.setVisibility(View.INVISIBLE);
    }
    public void gone() {
        llTitle.setVisibility(View.GONE);
    }

    /**
     * 设置标题背景图片
     *
     * @param resId
     * @return
     */
    public TitleBuilder setInnerTitleBgRes(int resId) {
        rlTitle.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置标题背景图片
     *
     * @param resId
     * @return
     */
    public TitleBuilder setExternalTitleBgRes(int resId) {
        llTitle.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置标题背景颜色
     *
     * @param color
     * @return
     */
    public TitleBuilder setInnerTitleBgColor(int color) {
        rlTitle.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题背景颜色
     *
     * @param color
     * @return
     */
    public TitleBuilder setExternalTitleBgColor(int color) {
        llTitle.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题
     *
     * @param text
     * @return
     */
    public TitleBuilder setTitleText(String text) {
        titleTv.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        titleTv.setText(text);
        return this;
    }

    /**
     * 设置标题图标
     *
     * @param resId
     * @return
     */
    public TitleBuilder setTitleImage(int resId) {
        titleIv.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        titleIv.setImageResource(resId);
        return this;
    }

    /**
     * 设置左标题图标
     *
     * @param resId
     * @return
     */
    public TitleBuilder setLeftImage(int resId) {
        titleIvLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        titleIvLeft.setImageResource(resId);
        return this;
    }

    /**
     * 设置左标题
     *
     * @param text
     * @return
     */
    public TitleBuilder setLeftText(String text) {
        titleTvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        titleTvLeft.setText(text);
        return this;
    }

    /**
     * 监听左标题点击事件
     *
     * @param listener
     * @return
     */
    public TitleBuilder setLeftOnClickListener(OnClickListener listener) {
        if (titleIvLeft.getVisibility() == View.VISIBLE) {
            titleIvLeft.setOnClickListener(listener);
        } else if (titleTvLeft.getVisibility() == View.VISIBLE) {
            titleTvLeft.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置右标题图标
     *
     * @param resId
     * @return
     */
    public TitleBuilder setRightImage(int resId) {
        titleIvRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        titleIvRight.setImageResource(resId);
        return this;
    }

    /**
     * 设置右标题
     *
     * @param text
     * @return
     */
    public TitleBuilder setRightText(String text) {
        titleTvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        titleTvRight.setText(text);
        return this;
    }

    /**
     * 设置右标题点击事件
     *
     * @param listener
     * @return
     */
    public TitleBuilder setRightOnClickListener(OnClickListener listener) {
        if (titleIvRight.getVisibility() == View.VISIBLE) {
            titleIvRight.setOnClickListener(listener);
        } else if (titleTvRight.getVisibility() == View.VISIBLE) {
            titleTvRight.setOnClickListener(listener);
        }
        return this;
    }

    public View build() {
        return rlTitle;
    }

}
