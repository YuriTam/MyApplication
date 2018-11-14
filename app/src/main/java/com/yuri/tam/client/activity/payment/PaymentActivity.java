package com.yuri.tam.client.activity.payment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账单信息
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年11月13日
 */
public class PaymentActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //标题栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_payment;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(R.string.payment_title);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_fab})
    public void onClick(View view){
        if (UIUtils.isDoubleClick()) return;
        switch (view.getId()){
            case R.id.btn_fab:
                showToast("敬请期待...");
                break;
            default:
                break;
        }
    }

}
