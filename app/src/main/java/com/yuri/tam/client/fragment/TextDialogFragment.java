package com.yuri.tam.client.fragment;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.common.utils.AnimationLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 自定义提示弹出层
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年9月7日
 */
public class TextDialogFragment extends DialogFragment {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DEFAULT_RADIUS = 6;
    public static final int DIALOG_TYPE_INFO = 0;
    public static final int DIALOG_TYPE_HELP = 1;
    public static final int DIALOG_TYPE_WRONG = 2;
    public static final int DIALOG_TYPE_SUCCESS = 3;
    public static final int DIALOG_TYPE_WARNING = 4;
    public static final int DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO;

    @BindView(R.id.iv_logo_icon)
    ImageView ivLogoIcon;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_btn_group)
    LinearLayout llBtnGroup;
    @BindView(R.id.tv_positive)
    TextView tvPositive;
    @BindView(R.id.tv_negative)
    TextView tvNegative;
    private Unbinder unbinder;

    private String mTitleText;
    private String mContentText;
    private String mPositionText;
    private String mNegativeText;

    private AnimationSet mAnimIn, mAnimOut;
    private View mDialogView;
    private int mGravity;
    private int mDialogType = DIALOG_TYPE_DEFAULT;
    private OnDialogListener mListener;
    private MyDownTimer mDownTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ic_dialog_style);
        //加载动画
        mAnimIn = AnimationLoader.getInAnimation(getContext());
        mAnimOut = AnimationLoader.getOutAnimation(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_text, container);
        unbinder = ButterKnife.bind(this, contentView);
        mDialogView = getDialog().getWindow().getDecorView().findViewById(android.R.id.content);
        getDialog().getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (UIUtils.getScreenSize(getContext()).x * 0.75);
        getDialog().getWindow().setAttributes(params);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置弹出层图标
        ivLogoIcon.setBackgroundResource(getLogoResId(getDialogType()));
        //设置绘制的三角形
        ImageView triangleIv = new ImageView(getContext());
        triangleIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(getContext(), 10)));
        triangleIv.setImageBitmap(createTriangel((int) (UIUtils.getScreenSize(getContext()).x * 0.75), UIUtils.dp2px(getContext(), 10)));
        topLayout.addView(triangleIv);
        //设置上圆角
        setTopCorners(llTop, getDialogType());
        //设置下圆角
        setBottomCorners(llBtnGroup);
        //标题
        if (!TextUtils.isEmpty(getTitleText())) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(getTitleText());
        }
        //内容
        if (!TextUtils.isEmpty(getContentText())) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(getContentText());
            tvContent.setGravity(mGravity);
        }
        //确定按钮
        if (!TextUtils.isEmpty(getPositionText())) {
            tvPositive.setVisibility(View.VISIBLE);
            tvPositive.setText(getPositionText());
            //设置确认按钮背景样式
            setBtnBackground(tvPositive, true);
        }
        //取消按钮
        if (!TextUtils.isEmpty(getNegativeText())) {
            tvNegative.setVisibility(View.VISIBLE);
            tvNegative.setText(getNegativeText());
            //设置确认按钮背景样式，取消按钮使用默认样式
            setBtnBackground(tvNegative, false);
        }
        //开始倒计时
        if (mDownTimer != null){
            mDownTimer.start();
        }
    }

    @OnClick({R.id.tv_positive, R.id.tv_negative})
    public void onClicked(View view) {
        if (UIUtils.isDoubleClick()) {
            return;
        }
        dismiss();
        switch (view.getId()) {
            case R.id.tv_positive:
                if (mListener != null) {
                    mListener.onConfirm();
                }
                break;
            case R.id.tv_negative:
                if (mListener != null) {
                    mListener.onCancel();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mDialogView != null) {
            mDialogView.startAnimation(mAnimIn);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mDialogView != null) {
            mDialogView.startAnimation(mAnimOut);
        }
        if (mDownTimer != null) {
            mDownTimer.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDownTimer != null) mDownTimer.cancel();
        unbinder.unbind();
    }

    /**
     * 获取标题上的图标
     *
     * @param mDialogType
     * @return
     */
    private int getLogoResId(int mDialogType) {
        switch (mDialogType) {
            case DIALOG_TYPE_INFO:
                return R.drawable.ic_info;
            case DIALOG_TYPE_HELP:
                return R.drawable.ic_help;
            case DIALOG_TYPE_WRONG:
                return R.drawable.ic_wrong;
            case DIALOG_TYPE_SUCCESS:
                return R.drawable.ic_success;
            case DIALOG_TYPE_WARNING:
                return R.drawable.ic_warning;
            default:
                return R.drawable.ic_info;
        }
    }

    /**
     * 根据背景颜色
     *
     * @param mDialogType
     * @return
     */
    private int getColorResId(int mDialogType) {
        switch (mDialogType) {
            case DIALOG_TYPE_INFO:
                return R.color.color_type_info;
            case DIALOG_TYPE_HELP:
                return R.color.color_type_help;
            case DIALOG_TYPE_WRONG:
                return R.color.color_type_wrong;
            case DIALOG_TYPE_SUCCESS:
                return R.color.color_type_success;
            case DIALOG_TYPE_WARNING:
                return R.color.color_type_warning;
            default:
                return R.color.color_type_info;
        }
    }

    /**
     * 获取按钮样式
     *
     * @param mDialogType
     * @return
     */
    private int getSelBtn(int mDialogType) {
        switch (mDialogType) {
            case DIALOG_TYPE_INFO:
                return R.drawable.sel_btn_info;
            case DIALOG_TYPE_HELP:
                return R.drawable.sel_btn_help;
            case DIALOG_TYPE_WRONG:
                return R.drawable.sel_btn_wrong;
            case DIALOG_TYPE_SUCCESS:
                return R.drawable.sel_btn_success;
            case DIALOG_TYPE_WARNING:
                return R.drawable.sel_btn_warning;
            default:
                return R.drawable.sel_btn;
        }
    }

    /**
     * 绘制三角形
     *
     * @param width  宽
     * @param height 高
     * @return
     */
    private Bitmap createTriangel(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, getContext().getResources().getColor(getColorResId(getDialogType())));
    }

    /**
     * 绘图三角形
     *
     * @param width           宽
     * @param height          高
     * @param backgroundColor 背景颜色
     * @return
     */
    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width / 2, height);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;
    }

    /**
     * 设置按钮背景样式
     *
     * @param textView
     */
    private void setBtnBackground(final TextView textView, boolean isPosition) {
        if (isPosition) {
            textView.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(getDialogType())),
                    getContext().getResources().getColor(R.color.color_dialog_gray)));
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(getDialogType())));
        } else {
            textView.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(DIALOG_TYPE_DEFAULT)),
                    getContext().getResources().getColor(R.color.color_dialog_gray)));
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(DIALOG_TYPE_DEFAULT)));
        }
    }

    /**
     * 设置上圆角
     *
     * @param llTop
     * @param dialogType
     */
    private void setTopCorners(View llTop, int dialogType) {
        int radius = UIUtils.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(getColorResId(dialogType)));
        llTop.setBackgroundDrawable(shapeDrawable);
    }

    /**
     * 设置视图下圆角
     *
     * @param llBtnGroup
     */
    private void setBottomCorners(View llBtnGroup) {
        int radius = UIUtils.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }

    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static TextDialogFragment newInstance() {
        return new TextDialogFragment();
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setTitleText(String titleText) {
        this.mTitleText = titleText;
    }

    public String getContentText() {
        return mContentText;
    }

    public void setContentText(String contentText, int gravity) {
        this.mContentText = contentText;
        this.mGravity = gravity;
    }

    public String getPositionText() {
        return mPositionText;
    }

    public void setPositionText(String positionText) {
        this.mPositionText = positionText;
    }

    public void setPositionText(String positionText, int seconds) {
        this.mPositionText = positionText;
        setDownTimer(seconds, true);
    }

    public String getNegativeText() {
        return mNegativeText;
    }

    public void setNegativeText(String negativeText) {
        this.mNegativeText = negativeText;
    }

    public void setNegativeText(String negativeText, int seconds) {
        this.mNegativeText = negativeText;
        setDownTimer(seconds, false);
    }

    public int getDialogType() {
        return mDialogType;
    }

    public void setDialogType(int dialogType) {
        this.mDialogType = dialogType;
    }

    public void setDownTimer(long seconds, boolean isConfirm) {
        if (mDownTimer == null) {
            mDownTimer = new MyDownTimer((seconds + 1) * 1000, isConfirm);
        }
    }

    public void setDialogListener(OnDialogListener listener) {
        mListener = listener;
    }

    public interface OnDialogListener {
        void onCancel();

        void onConfirm();
    }

    /**
     * 倒计时
     */
    class MyDownTimer extends CountDownTimer {

        //记录是哪个按钮需要做超时处理
        private boolean isConfirm;

        public MyDownTimer(long millisInFuture, boolean isConfirm) {
            super(millisInFuture, 1000);
            this.isConfirm = isConfirm;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!isAdded()) {
                return;
            }
            if (isConfirm) {
                tvPositive.setText(getPositionText() + "（" + millisUntilFinished / 1000 + "）");
            } else {
                tvNegative.setText(getNegativeText() + "（" + millisUntilFinished / 1000 + "）");
            }
        }

        @Override
        public void onFinish() {
            if (isAdded()) {
                if (isConfirm) {
                    tvPositive.setText(getPositionText() + "（0）");
                } else {
                    tvNegative.setText(getNegativeText() + "（0）");
                }
            }
            dismiss();
            if (mListener != null) {
                mListener.onConfirm();
            }
        }
    }
}
