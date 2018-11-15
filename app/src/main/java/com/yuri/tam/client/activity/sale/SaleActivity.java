package com.yuri.tam.client.activity.sale;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;

import butterknife.BindView;

/**
 * 安全信息
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年11月13日
 */
public class SaleActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标题栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_sale;
    }

    @Override
    protected void initView() {
        tvTitle.setText("安全信息");
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }
}
