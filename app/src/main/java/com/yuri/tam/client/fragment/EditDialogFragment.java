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
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.common.utils.AnimationLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by xiaox on 2017/1/18.
 */
public class EditDialogFragment extends DialogFragment {

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
    @BindView(R.id.edit_value_1)
    EditText editText1;
    @BindView(R.id.edit_value_2)
    EditText editText2;
    @BindView(R.id.edit_value_3)
    EditText editText3;
    @BindView(R.id.ll_btn_group)
    LinearLayout llBtnGroup;
    @BindView(R.id.tv_positive)
    TextView tvPositive;
    @BindView(R.id.tv_negative)
    TextView tvNegative;
    private Unbinder unbinder;

    private String mTitle;
    private String mPositionText;
    private String mNegativeText;

    private boolean mIsTitleEnable, mIsEditText1Enable, mIsEditText2Enable, mIsEditText3Enable;
    private int mEditText1InputType, mEditText2InputType, mEditText3InputType;
    private int mEditText1Length, mEditText2Length, mEditText3Length;
    private boolean mIsEditText1Password, mIsEditText2Password, mIsEditText3Password;
    private String mEditText1Hit, mEditText2Hit, mEditText3Hit;

    private AnimationSet mAnimIn, mAnimOut;
    private View mDialogView;
    private int mDialogType = DIALOG_TYPE_DEFAULT;
    private OnDialogListener mListener;

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
        View contentView = inflater.inflate(R.layout.dialog_edit, container);
        unbinder = ButterKnife.bind(this, contentView);
        mDialogView = getDialog().getWindow().getDecorView().findViewById(android.R.id.content);
        getDialog().getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (UIUtils.getScreenSize(getContext()).x * 0.8);
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
        triangleIv.setImageBitmap(createTriangel((int) (UIUtils.getScreenSize(getContext()).x * 0.8), UIUtils.dp2px(getContext(), 10)));
        topLayout.addView(triangleIv);
        //设置上圆角
        setTopCorners(llTop, getDialogType());
        //设置下圆角
        setBottomCorners(llBtnGroup);
        //设置标题
        if (mIsTitleEnable) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        }
        if (mIsEditText1Enable) {
            editText1.setVisibility(View.VISIBLE);
            editText1.setHint(mEditText1Hit);
            editText1.setInputType(mEditText1InputType);
            if (mIsEditText1Password) {
                editText1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if (mEditText1Length > 0) {
                editText1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mEditText1Length)});
            }
            if (!mIsEditText2Enable && !mIsEditText3Enable) {
                editText1.setBackgroundResource(R.drawable.shape_no_corner);
            }
        }
        if (mIsEditText2Enable) {
            editText2.setVisibility(View.VISIBLE);
            editText2.setHint(mEditText2Hit);
            editText2.setInputType(mEditText2InputType);
            if (mIsEditText2Password) {
                editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if (mEditText2Length > 0) {
                editText2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mEditText2Length)});
            }
            if (!mIsEditText3Enable) {
                editText2.setBackgroundResource(R.drawable.shape_bottom_corner_no_top_line);
            }
        }
        if (mIsEditText3Enable) {
            editText3.setVisibility(View.VISIBLE);
            editText3.setHint(mEditText3Hit);
            editText3.setInputType(mEditText3InputType);
            if (mIsEditText3Password) {
                editText3.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            if (mEditText3Length > 0) {
                editText3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mEditText3Length)});
            }
        }
        //确定按钮
        if (!TextUtils.isEmpty(getPositionText())) {
            tvPositive.setVisibility(View.VISIBLE);
            tvPositive.setText(getPositionText());
            //设置确认按钮背景样式
            setBtnBackground(tvPositive, true);
        }
        //取消按钮
        if (!TextUtils.isEmpty(getNegativeText())){
            tvNegative.setVisibility(View.VISIBLE);
            tvNegative.setText(getNegativeText());
            //设置确认按钮背景样式，取消按钮使用默认样式
            setBtnBackground(tvNegative, false);
        }
    }

    @OnClick({R.id.tv_positive, R.id.tv_negative})
    public void onClicked(View view) {
        if (UIUtils.isDoubleClick()) {
            return;
        }
        switch (view.getId()){
            case R.id.tv_positive:
                if (mListener != null){
                    List<String> values = new ArrayList<>();
                    if (mIsEditText1Enable) {
                        values.add(editText1.getText().toString());
                        editText1.getText().clear();
                    }
                    if (mIsEditText2Enable) {
                        values.add(editText2.getText().toString());
                        editText2.getText().clear();
                    }
                    if (mIsEditText3Enable) {
                        values.add(editText3.getText().toString());
                        editText3.getText().clear();
                    }
                    //隐藏软盘
                    UIUtils.hideSoftKeyboard(getActivity());
                    mListener.onConfirm(values);
                }
                break;
            case R.id.tv_negative:
                dismiss();
                if (mListener != null){
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
        mDialogView.startAnimation(mAnimIn);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mDialogView.startAnimation(mAnimOut);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        if (isPosition){
            textView.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(getDialogType())),
                    getContext().getResources().getColor(R.color.color_dialog_gray)));
            textView.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(getDialogType())));
        }else {
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

    public void addTitle(String title) {
        mIsTitleEnable = true;
        mTitle = title;
    }

    public void addEditText1(int inputTypt, int length, String hit, boolean isPassword) {
        mEditText1Hit = hit;
        mEditText1InputType = inputTypt;
        mEditText1Length = length;
        mIsEditText1Password = isPassword;
        mIsEditText1Enable = true;
    }

    public void addEditText2(int inputTypt, int length, String hit, boolean isPassword) {
        mEditText2Hit = hit;
        mEditText2InputType = inputTypt;
        mEditText2Length = length;
        mIsEditText2Password = isPassword;
        mIsEditText2Enable = true;
    }

    public void addEditText3(int inputTypt, int length, String hit, boolean isPassword) {
        mEditText3Hit = hit;
        mEditText3InputType = inputTypt;
        mEditText3Length = length;
        mIsEditText3Password = isPassword;
        mIsEditText3Enable = true;
    }

    public String getPositionText() {
        return mPositionText;
    }

    public void setPositionText(String positionText) {
        this.mPositionText = positionText;
    }

    public String getNegativeText() {
        return mNegativeText;
    }

    public void setNegativeText(String negativeText) {
        this.mNegativeText = negativeText;
    }

    public int getDialogType() {
        return mDialogType;
    }

    public void setDialogType(int dialogType) {
        this.mDialogType = dialogType;
    }

    public void setDialogListener(OnDialogListener listener) {
        mListener = listener;
    }

    public interface OnDialogListener {
        void onCancel();

        void onConfirm(List<String> values);
    }
}
