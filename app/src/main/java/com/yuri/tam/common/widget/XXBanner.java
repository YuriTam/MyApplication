package com.yuri.tam.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.stx.xhb.xbanner.XBanner;

/**
 * Created by Kol(Fang Qiang) on 2017/7/6.
 * usage:
 */
public class XXBanner extends XBanner {

    public XXBanner(Context context) {
        super(context);
    }

    public XXBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XXBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //禁止父类拦截触摸事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }
}
