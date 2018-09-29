package com.yuri.tam.common.widget.gridpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kol(Fang Qiang) on 2017/7/6.
 * usage:
 * modify by 谭忠扬-YuriTam
 */
public class PageGridLayout extends LinearLayout implements MyGridView.onItemListener, ViewPager.OnPageChangeListener {

    private Context mContext;
    private int mRow;    //行数
    private int mColumn; //列数
    private int mSelectColor = Color.rgb(6, 146, 177);
    private int mUnSelectColor = Color.DKGRAY;
    private MyViewPager mViewPager;  //网络布局容器
    private MyPagerAdapter mPagerAdapter;
    private List<MyGridView.DataSource> mSourceList;
    private List<MyGridView> mPageList = new ArrayList<>();
    private int mPointSize = 15;
    private int mPointMargin = 15;
    private LinearLayout mPointLayout; //跟随布局滑动的点布局
    private List<View> mPointList = new ArrayList<>();
    //回调点击事件
    private OnItemListener mListener;

    public PageGridLayout(Context context) {
        this(context, null);
    }

    public PageGridLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageGridLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        setOrientation(VERTICAL);
    }

    /**
     * 设置行与列
     *
     * @param row
     * @param column
     */
    public PageGridLayout setGridFormat(int row, int column){
        this.mRow = row;
        this.mColumn = column;
        return this;
    }

    /**
     * 设置点颜色
     *
     * @param selectColor
     * @param unSelectColor
     */
    public PageGridLayout setPointColor(int selectColor, int unSelectColor){
        this.mSelectColor = selectColor;
        this.mUnSelectColor = unSelectColor;
        return this;
    }

    /**
     * 添加资源
     *
     * @param resId 交易名称
     * @param bitmapId 对应的图标
     * @return
     */
    public PageGridLayout addSource(int resId, int bitmapId){
        if (mSourceList == null){
            mSourceList = new ArrayList<>();
        }
        if (resId == 0 || bitmapId == 0) return this;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
        mSourceList.add(new MyGridView.DataSource(resId, bitmap));
        return this;
    }

    /**
     * 添加资源
     *
     * @param resId 交易名称
     * @param bitmapId 对应的图标
     * @param isAvailable 该交易是否可用，不可用则不显示
     * @return
     */
    public PageGridLayout addSource(int resId, int bitmapId, boolean isAvailable){
        return isAvailable ? addSource(resId, bitmapId) : this;
    }

    /**
     * 回调
     *
     * @param listener
     * @return
     */
    public PageGridLayout setListener(OnItemListener listener){
        this.mListener = listener;
        return this;
    }

    /**
     * 开始初始化显示数据
     */
    public void build(){
        if (mSourceList == null || mSourceList.size() == 0) return;
        //初始化左右滑动视图
        mViewPager = new MyViewPager(mContext);
        mViewPager.addOnPageChangeListener(this);
        //初始化网络布局参数
        int margin = 10;
        int height = mSourceList.get(0).getBitmap().getHeight() * mRow + (10 * (mRow - 1));
        //N3缩小边距
        if (Build.MODEL.equals("N3")) margin = 8;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        params.bottomMargin = margin;
        params.topMargin = margin;
        params.leftMargin = margin;
        params.rightMargin = margin;
        mViewPager.setLayoutParams(params);
        //初始网络下脚的点视图参数
        mPointLayout = new LinearLayout(mContext);
        mPointLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams pointParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pointParams.gravity = Gravity.CENTER_HORIZONTAL;
        pointParams.topMargin = Build.MODEL.equals("N3") ? 0 : 5;
        mPointLayout.setLayoutParams(pointParams);
        //添加视图
        addView(mViewPager);
        addView(mPointLayout);
        //添加适配器
        if (mPagerAdapter == null) {
            mPagerAdapter = new MyPagerAdapter();
        }
        mViewPager.setAdapter(mPagerAdapter);
        //整理每个页面的数据
        showItemPageLayout();
    }

    /**
     * 显示各个子界面的视图
     */
    private void showItemPageLayout(){
        //整理每个页面的数据
        mPageList.clear();
        int startIndex = 0;
        int endIndex = 0;
        while (startIndex < mSourceList.size()){
            MyGridView gridView = new MyGridView(mContext);
            gridView.setNumColumns(mColumn);
            endIndex = Math.min(startIndex + (mRow * mColumn), mSourceList.size());
            gridView.addSourceList(mSourceList.subList(startIndex, endIndex));
            gridView.setListener(this);
            mPageList.add(gridView);
            startIndex = endIndex;
        }
        mPagerAdapter.notifyDataSetChanged();
        showPointLayout();
    }

    /**
     * 显示点数量
     */
    private void showPointLayout(){
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter == null || adapter.getCount() == 0){
            mPointLayout.removeAllViews();
            mPointList.clear();
            mPointLayout.invalidate();
            return;
        }
        // 如果新的点数量和原来的相同，则不需要改变
        if(adapter.getCount() == mPointList.size()){
            return;
        }
        //N3像素像，相应缩小点的边距与大小
        if (Build.MODEL.equals("N3")){
            mPointSize = 10;
            mPointMargin = 10;
        }
        // 增加新点数
        if(adapter.getCount() > mPointList.size()){
            int addCount = adapter.getCount() - mPointList.size();
            for (int i = 0; i < addCount; i++) {
                View view = new View(mContext);
                view.setBackground(new PointDrawable(mPointSize, Color.WHITE));
                LayoutParams params = new LayoutParams(mPointSize, mPointSize);
                if(i != 0 || mPointList.size() != 0){
                    params.leftMargin = mPointMargin;
                }
                view.setLayoutParams(params);
                mPointList.add(view);
                mPointLayout.addView(view);
            }
        }else{
            // 删除点
            for (int i = adapter.getCount(); i < mPointList.size(); i++) {
                mPointLayout.removeView(mPointList.get(adapter.getCount()));
                mPointList.remove(adapter.getCount());
            }
        }
        refreshPoints();
    }

    /**
     * 单纯刷新点颜色
     */
    private void refreshPoints(){
        for (int i = 0; i < mPointList.size(); i++) {
            ((PointDrawable) mPointList.get(i).getBackground()).setColor(mUnSelectColor);
            mPointList.get(i).invalidate();
        }
        ((PointDrawable) mPointList.get(mViewPager.getCurrentItem()).getBackground()).setColor(mSelectColor);
        mPointList.get(mViewPager.getCurrentItem()).invalidate();
    }

    @Override
    public void onItemSelect(int position) {
        if(mListener != null){
            int index = mViewPager.getCurrentItem() * (mRow * mColumn) + position;
            MyGridView.DataSource dataSource = mSourceList.get(index);
            Log.d("ViewPage", String.format("index = %d, desc = %s", index, getContext().getString(dataSource.getResId())));
            mListener.onItemSelect(dataSource.getResId());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshPoints();
        if(mListener != null){
            mListener.onPageChange(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPageList.get(position));
            return mPageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    /**
     * 左右滑动容器
     */
    class MyViewPager extends ViewPager {

        public MyViewPager(Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if(getCurrentItem() != 0){
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onInterceptTouchEvent(ev);
        }
    }

    public interface OnItemListener {
        void onItemSelect(int resId);
        void onPageChange(int pageIndex);
    }

}
