package com.yuri.tam.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yuri.tam.R;

/**
 * 旋转圆形控件
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年9月30日
 */
public class RotateCircleView extends View {

    private Paint mCirclePaint;  //绘制内圆
    private Paint mArcPaint;     //绘制圆环
    private Paint mTextPaint;    //绘制文字
    private int mRadius;  //整个圆的半径
    private int mCenterXY; //中心坐标

    private boolean mAutoRotate = false;

    private float textSize;
    private String text;
    private int textColor;
    private int circleColor;
    private int arcColor;
    private float strokeWidth;
    private int startAngle;
    private int angleSpeed;

    public RotateCircleView(Context context) {
        this(context, null);
    }

    public RotateCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        initViews();
    }

    /**
     * 初始化相应属性
     */
    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RotateCircleView, defStyleAttr, 0);
        if (ta != null){
            text = ta.getString(R.styleable.RotateCircleView_text);
            textSize = ta.getDimension(R.styleable.RotateCircleView_textSize, 15);
            textColor = ta.getColor(R.styleable.RotateCircleView_textColor, 0);
            circleColor = ta.getColor(R.styleable.RotateCircleView_circleColor, 0);
            arcColor = ta.getColor(R.styleable.RotateCircleView_arcColor, 0);
            strokeWidth = ta.getDimension(R.styleable.RotateCircleView_strokeWidth, 30);
            startAngle = ta.getInt(R.styleable.RotateCircleView_startAngle, 0);
            angleSpeed = ta.getInt(R.styleable.RotateCircleView_angleSpeed, 5);
            ta.recycle();
        }
    }

    private void initViews() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(circleColor);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(arcColor);
        mArcPaint.setStrokeWidth(strokeWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //强制宽高相等
        int result = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(result, result);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length = Math.min(getWidth(), getHeight());
        mRadius = length / 2;
        mCenterXY = length / 2;
        //绘制内圆
        canvas.drawCircle(mCenterXY,mCenterXY, mRadius / 2, mCirclePaint);
        //绘制圆环
        RectF rectF = new RectF(getWidth() * 0.1f, getHeight() * 0.1f, getWidth() * 0.9f, getHeight() * 0.9f);
        mArcPaint.setAlpha(255);
        canvas.drawArc(rectF, startAngle + 0, 90, false, mArcPaint);
        canvas.drawArc(rectF, startAngle + 180, 90, false, mArcPaint);
        mArcPaint.setAlpha(100);
        canvas.drawArc(rectF, startAngle + 90, 90, false, mArcPaint);
        canvas.drawArc(rectF, startAngle + 270, 90, false, mArcPaint);
        //绘制文字
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mCenterXY - top/2 - bottom/2);//基线中间点的y轴计算公式
        canvas.drawText(text, 0, text.length(), mCenterXY, baseLineY, mTextPaint);
        //让圆环动起来
        if (mAutoRotate){
            startAngle += angleSpeed;
            if (startAngle == 360) startAngle = 0;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            invalidate();
        }
    }

    /**
     * 圆环开始旋转
     */
    public void startRotate(){
        if (!mAutoRotate){
            mAutoRotate = true;
            invalidate();
        }
    }

    /**
     * 圆环停止旋转
     */
    public void stopRotete(){
        mAutoRotate = false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
