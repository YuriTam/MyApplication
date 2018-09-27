package com.yuri.tam.client.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.tam.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 加载弹出层
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年6月5日
 */
public class ProgressDialogFragment extends DialogFragment {
    @BindView(R.id.iv_point)
    ImageView ivPoint;
    @BindView(R.id.tv_tip_title)
    TextView tvTipTitle;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private Unbinder unbinder;
    private String mTipTitle;
    private String mTip;
    private OnDialogListener mListener;

    public void setTipTitle(String tipTitle) {
        this.mTipTitle = tipTitle;
        if (tvTipTitle != null && !TextUtils.isEmpty(tipTitle)) {
            tvTipTitle.setVisibility(View.VISIBLE);
            tvTipTitle.setText(tipTitle);
        }
    }

    public void setTip(String tip) {
        this.mTip = tip;
        if (tvTip != null && !TextUtils.isEmpty(tip)) {
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(tip);
        }
    }

    public String getTip() {
        return mTip;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if (mListener != null) {
                    mListener.onBackPressed();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_process, container);
        unbinder = ButterKnife.bind(this, rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().gravity = Gravity.CENTER;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.37f, Animation.RELATIVE_TO_PARENT, 0.37f);
        ra.setDuration(2000);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setRepeatMode(Animation.RESTART);
        ivPoint.startAnimation(ra);

        if (!TextUtils.isEmpty(mTipTitle)) {
            tvTipTitle.setVisibility(View.VISIBLE);
            tvTipTitle.setText(mTipTitle);
        }

        if (!TextUtils.isEmpty(mTip)) {
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(mTip);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ivPoint.clearAnimation();
        unbinder.unbind();
    }

    public void setOnDialogListener(OnDialogListener listener) {
        mListener = listener;
    }

    public interface OnDialogListener {
        void onBackPressed();
    }
}
