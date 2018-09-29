package com.yuri.tam.common.widget.gridpage;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Kol(Fang Qiang) on 2017/7/5.
 * usage:
 * modify by 谭忠扬-YuriTam
 */
public class PointDrawable extends Drawable {

    private int mSize = 0;
    private Paint mPaint;

    public PointDrawable(int size, int color){
        this.mSize = size;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    private Canvas canvas;
    @Override
    public void draw(@NonNull Canvas canvas) {
        this.canvas = canvas;
        canvas.drawCircle(mSize /2, mSize /2, mSize /2, mPaint);
    }

    // 重新设置颜色
    public void setColor(int color){
        mPaint.setColor(color);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
