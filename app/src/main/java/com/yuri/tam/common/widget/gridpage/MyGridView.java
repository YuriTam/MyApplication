package com.yuri.tam.common.widget.gridpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.List;

/**
 * 自定义网络布局
 *
 * modify by 谭忠扬-YuriTam
 */
public class MyGridView extends GridView implements AdapterView.OnItemClickListener {

    private Context mContext;
    private int mSpacing = 10;
    private GridViewAdapter mAdapter;
    private List<DataSource> mSourceList;
    private onItemListener mListener;

    public MyGridView(Context context) {
        this(context, null);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        //初始化参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.MODEL.equals("N3")){
            mSpacing = 7;
        }
        setHorizontalSpacing(mSpacing);
        setVerticalSpacing(mSpacing);
        setLayoutParams(params);
        //初始化适配器
        mAdapter = new GridViewAdapter();
        setAdapter(mAdapter);
        setOnItemClickListener(this);
    }

    /**
     * 添加数据
     *
     * @param sourceList
     */
    public void addSourceList(List<DataSource> sourceList){
        this.mSourceList = sourceList;
        mAdapter.notifyDataSetChanged();
    }

    public void setListener(onItemListener listener){
        this.mListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mListener != null){
            mListener.onItemSelect(position);
        }
    }

    /**
     * 适配器
     */
    class GridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mSourceList != null) return mSourceList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mSourceList != null) return mSourceList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridItemView itemView;
            if(convertView != null){
                itemView = (GridItemView)convertView;
            }else{
                itemView = new GridItemView(mContext);
            }
            DataSource source = mSourceList.get(position);
            itemView.setItemSource(source);
            return itemView;
        }
    }

    /**
     * 数据实体
     */
    public static class DataSource{

        private int resId;
        private Bitmap bitmap;

        public DataSource(int resId, Bitmap bitmap) {
            this.resId = resId;
            this.bitmap = bitmap;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    public interface onItemListener {
        void onItemSelect(int position);
    }
}
