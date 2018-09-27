package com.yuri.tam.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.nineoldandroids.view.ViewHelper;

public class DragLayout extends FrameLayout {
	private View mLeftContent;
	private View mMainContent;
	private View mRightContent;
	private int mWidth;
	private int mHeight;
	private int mRangeLeft;
	private ViewDragHelper mDragHelper;
	private Status mStatus = Status.Close;
	private Direction mDirection = Direction.Left;
	private OnDragListener mDragListener;
	private boolean mScaleEnable = true;
	private int mRightWidth;
	private int mRangeRight;
	
	public interface OnDragListener {
		void onClose();
		
		void onStartOpen(Direction direction);
		
		void onOpen();
		
		void onDrag(float percent);
	}
	
	public static enum Status {
		Open, Close, Draging
	}

	public static enum Direction {
		Left, Right, Default
	}
	
	public Direction getDirction() {
		return mDirection;
	}

	public void setDirction(Direction dirction) {
		mDirection = dirction;
	}

	public DragLayout(Context context) {
		this(context, null);
	}

	public DragLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

        if(getBackground() == null){
            setBackgroundColor(0xff33b5e5);
        }

		mDragHelper = ViewDragHelper.create(this, mCallBack);
		mGestureDetector = new GestureDetectorCompat(context, mYGestureListener);
	}

	SimpleOnGestureListener mYGestureListener = new SimpleOnGestureListener() {
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return Math.abs(distanceX) >= Math.abs(distanceY);
		};
		
		
	};

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//Log.i(TAG, "--onFinishInflate");
		mLeftContent = (View) getChildAt(0);
		mRightContent = getChildAt(1);
		mMainContent = (View) getChildAt(2);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mWidth = mMainContent.getMeasuredWidth();
		mHeight = mMainContent.getMeasuredHeight();
		
		mRightWidth = mRightContent.getMeasuredWidth();
		mRangeLeft = (int) (mWidth * 0.6f);
		mRangeRight = mRightWidth;

        // 设置左边菜单的宽度
        LayoutParams params = (LayoutParams)mLeftContent.getLayoutParams();
        params.height = h;
        params.width = (int)(w * 0.65);
        mLeftContent.setLayoutParams(params);
	}

	private int mMainLeft = 0;

	ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			// 1. 决定当前被拖拽的child是否拖的动。(抽象方法，必须重写)
			return true;
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			// 2. 决定拖拽的范围
			return mWidth;
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			return clampResult(mMainLeft + dx, left);
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			// 4. 决定了当View被拖动时，希望同时引发的其他变化
			//Log.d(TAG, "onViewPositionChanged left: " + left + " dx: " + dx);

			if (changedView == mMainContent) {
				mMainLeft = left;
			} else {
				mMainLeft += dx;
			}

			mMainLeft = clampResult(mMainLeft, mMainLeft);

			if(changedView == mLeftContent || changedView == mRightContent){
				layoutContent();
			}

			dispathDragEvent(mMainLeft);
			invalidate();
		};

		/**
		 * @param releasedChild
		 *            被释放的孩子
		 * @param xvel
		 *            释放时X方向的速度
		 * @param yvel
		 *            释放时Y方向的速度
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			// 5. 决定当childView被释放时，希望做的事情——执行打开/关闭动画，更新状态
            boolean scrollRight = xvel > 1.0f;
			boolean scrollLeft = xvel < -1.0f;
			if (scrollRight || scrollLeft) {
				if (scrollRight && mDirection == Direction.Left) {
					open(true, mDirection);
				} else if (scrollLeft && mDirection == Direction.Right) {
					open(true, mDirection);
				} else {
                    close(true);
				}
				return;
			}

			if (releasedChild == mLeftContent && mMainLeft > mRangeLeft * 0.7f) {
                open(true, mDirection);
			} else if (releasedChild == mMainContent) {
				if (mMainLeft > mRangeLeft * 0.3f)
					open(true, mDirection);
				else if (-mMainLeft > mRangeRight * 0.3f)
					open(true, mDirection);
				else{
                    close(true);
                }

			} else if (releasedChild == mRightContent
					&& -mMainLeft > mRangeRight * 0.7f) {
				open(true, mDirection);
			} else {
				close(true);
			}
		}

		@Override
		public void onViewDragStateChanged(int state) {
			if (mStatus == Status.Close && state == ViewDragHelper.STATE_IDLE
					&& mDirection == Direction.Right) {
				mDirection = Direction.Left;
			}
		}

		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
		}

	};

	private int clampResult(int tempValue, int defaultValue) {
		Integer minLeft = null;
		Integer maxLeft = null;

		if (mDirection == Direction.Left) {
			minLeft = 0;
			maxLeft = 0 + mRangeLeft;
		} else if (mDirection == Direction.Right) {
			minLeft = 0 - mRangeRight;
			maxLeft = 0;
		}

		if (minLeft != null && tempValue < minLeft)
			return minLeft;
		else if (maxLeft != null && tempValue > maxLeft)
			return maxLeft;
		else
			return defaultValue;
	}

	private GestureDetectorCompat mGestureDetector;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	//	Log.i(TAG, "--onMeasure");
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		//Log.i(TAG, "--onLayout");
		layoutContent();
	}

	private void layoutContent() {
		mLeftContent.layout(0, 0, mWidth, mHeight);
		mRightContent.layout(mWidth - mRightWidth, 0, mWidth, mHeight);
		mMainContent.layout(mMainLeft, 0, mMainLeft + mWidth, mHeight);
	}

	@Override
	public void computeScroll() {

		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	public void setDragListener(OnDragListener mDragListener) {
		this.mDragListener = mDragListener;
	}

	/**
	 * 处理其他同步动画
	 *
	 * @param mainLeft
	 */
	protected void dispathDragEvent(int mainLeft) {
		// 注意转换成float
		float percent = 0;
		if (mDirection == Direction.Left)
			percent = mainLeft / (float) mRangeLeft;
		else if (mDirection == Direction.Right)
			percent = Math.abs(mainLeft) / (float) mRangeRight;

		if (mDragListener != null) {
			mDragListener.onDrag(percent);
		}

		// 更新动画
		if (mScaleEnable) {
			animViews(percent);
		}
		// 更新状态
		Status lastStatus = mStatus;
		if (updateStatus() != lastStatus) {
			if(lastStatus == Status.Close && mStatus == Status.Draging){
				mLeftContent.setVisibility(mDirection == Direction.Left ? View.VISIBLE : View.GONE);
				mRightContent.setVisibility(mDirection == Direction.Right ? View.VISIBLE : View.GONE);

				if(mDragListener != null){
					mDragListener.onStartOpen(mDirection);
				}
			}

			if (mStatus == Status.Close) {
				if (mDragListener != null)
					mDragListener.onClose();
			} else if (mStatus == Status.Open) {
				if (mDragListener != null)
					mDragListener.onOpen();
			}
		}

	}

	private Status updateStatus() {
		if (mDirection == Direction.Left) {
			if (mMainLeft == 0) {
				mStatus = Status.Close;
			} else if (mMainLeft == mRangeLeft) {
				mStatus = Status.Open;
			} else {
				mStatus = Status.Draging;
			}
		} else if (mDirection == Direction.Right) {
			if (mMainLeft == 0) {
				mStatus = Status.Close;
			} else if (mMainLeft == 0 - mRangeRight) {
				mStatus = Status.Open;
			} else {
				mStatus = Status.Draging;
			}
		}
		return mStatus;

	}

	private void animViews(float percent) {
		//Log.d(TAG, "percent: " + percent);
		animMainView(percent);

		animBackView(percent);
	}

	private void animBackView(float percent) {
		if (mDirection == Direction.Right) {
			// 右边栏X, Y放大，向左移动, 逐渐显示
			ViewHelper.setScaleX(mRightContent, 0.5f + 0.5f * percent);
			ViewHelper.setScaleY(mRightContent, 0.5f + 0.5f * percent);
			ViewHelper.setTranslationX(mRightContent,
					evaluate(percent, mRightWidth + mRightWidth / 2.0f, 0.0f));

			ViewHelper.setAlpha(mRightContent, percent);
		} else {
			// 左边栏X, Y放大，向右移动, 逐渐显示
			ViewHelper.setScaleX(mLeftContent, 0.5f + 0.5f * percent);
			ViewHelper.setScaleY(mLeftContent, 0.5f + 0.5f * percent);
			ViewHelper.setTranslationX(mLeftContent,
					evaluate(percent, -mWidth / 2f, 0.0f));
			ViewHelper.setAlpha(mLeftContent, percent);
		}
		// 背景逐渐变亮
		getBackground().setColorFilter(
				caculateValue(percent, Color.BLACK, Color.TRANSPARENT),
				PorterDuff.Mode.SRC_OVER);
	}

	private void animMainView(float percent) {
		Float inverseP = null;
		if (mDirection == Direction.Left) {
			inverseP = 1 - percent * 0.25f;
		} else if (mDirection == Direction.Right) {
			inverseP = 1 - percent * 0.25f;
		}
		// 主界面X,Y缩小
		if (inverseP != null) {
			if (mDirection == Direction.Right) {
				ViewHelper.setPivotX(mMainContent, mWidth);
				ViewHelper.setPivotY(mMainContent, mHeight / 2.0f);
			} else {
				ViewHelper.setPivotX(mMainContent, mWidth / 2.0f);
				ViewHelper.setPivotY(mMainContent, mHeight / 2.0f);
			}
			ViewHelper.setScaleX(mMainContent, inverseP);
			ViewHelper.setScaleY(mMainContent, inverseP);
		}
	}

	public Float evaluate(float fraction, Number startValue, Number endValue) {
		float startFloat = startValue.floatValue();
		return startFloat + fraction * (endValue.floatValue() - startFloat);
	}

	private int caculateValue(float fraction, Object start, Object end) {
		
		int startInt = (Integer) start;
		int startIntA = startInt >> 24 & 0xff;
		int startIntR = startInt >> 16 & 0xff;
		int startIntG = startInt >> 8 & 0xff;
		int startIntB = startInt & 0xff;

		int endInt = (Integer) end;
		int endIntA = endInt >> 24 & 0xff;
		int endIntR = endInt >> 16 & 0xff;
		int endIntG = endInt >> 8 & 0xff;
		int endIntB = endInt & 0xff;

		return ((int) (startIntA + (endIntA - startIntA) * fraction)) << 24
				| ((int) (startIntR + (endIntR - startIntR) * fraction)) << 16
				| ((int) (startIntG + (endIntG - startIntG) * fraction)) << 8
				| ((int) (startIntB + (endIntB - startIntB) * fraction));
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {



		return mDragHelper.shouldInterceptTouchEvent(ev)
				& mGestureDetector.onTouchEvent(ev);
	}
	public void close(){
		close(true);
	}
	public void close(boolean withAnim) {

		mMainLeft = 0;
		if (withAnim) {
			if (mDragHelper.smoothSlideViewTo(mMainContent, mMainLeft, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			layoutContent();
			
			dispathDragEvent(mMainLeft);
		}
	}

	public void open(){
		open(true);
	}
	
	public void open(boolean withAnim) {
		open(withAnim, Direction.Left);
	}

	public void open(boolean withAnim, Direction d) {

		mDirection = d;

		if (mDirection == Direction.Left)
			mMainLeft = mRangeLeft;
		else if (mDirection == Direction.Right)
			mMainLeft = -mRangeRight;

		if (withAnim) {
			// 引发动画的开始
			if (mDragHelper.smoothSlideViewTo(mMainContent, mMainLeft, 0)) {
				// 需要在computeScroll中使用continueSettling方法才能将动画继续下去（因为ViewDragHelper使用了scroller）。
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			layoutContent();
			
			dispathDragEvent(mMainLeft);
		}
	}

	private float downX = 0;
    private float downY = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("onTouchEvent");
        // 如果此时已经进入了菜单界面，并且点击了主页面一下，则退回到主页面
        if(event.getAction() == MotionEvent.ACTION_UP){
//            if((mStatus == Status.Open) && (downX == event.getX()) && (downY == event.getY())){
//                if((mStatus == Status.Open) && (event.getX() > mRangeLeft)){
//                    close();
//                }
//                return false;
//            }
            if((mStatus == Status.Open) && (downX == event.getX()) && (downY == event.getY())){
                return false;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downX = event.getX();
            downY = event.getY();
//            return false;
        }
        try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public Status getStatus() {
		return mStatus;
	}

	public void switchScaleEnable() {
		this.mScaleEnable = !mScaleEnable;
		if (!mScaleEnable) {
			animBackView(1.0f);
		}
	}

}
