package com.yuri.tam.client.fragment.left;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.utils.AppUtils;
import com.common.utils.ToastUtils;
import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.client.fragment.EditDialogFragment;
import com.yuri.tam.client.fragment.ProgressDialogFragment;
import com.yuri.tam.client.fragment.TextDialogFragment;
import com.yuri.tam.core.api.ApiRepository;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import java8.util.Optional;

/**
 * 左侧菜单
 *
 * @author 谭忠扬-YuriTam
 * @time 2017年11月29日
 */
public class LeftFragment extends BaseFragment implements LeftContract.View {

    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private EditDialogFragment mEditDialogFragment;
    private ProgressDialogFragment mDialogFragment;
    private LeftContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LeftPresenter(this, ApiRepository.getInstance());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_left, container, false);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        //设置当前版本
        tvVersion.setText(AppUtils.getAppVersionName(mContext));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart();
    }

    @OnClick({R.id.iv_left_logo, R.id.iv_location, R.id.ll_check_update, R.id.ll_setting_param})
    public void onClicked(View view) {
        if (UIUtils.isDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_left_logo:

                break;
            case R.id.iv_location:

                break;
            case R.id.ll_check_update:
                mPresenter.checkUpdate(getActivity());
                break;
            case R.id.ll_setting_param:
                mEditDialogFragment = new EditDialogFragment();
                mEditDialogFragment.setDialogType(EditDialogFragment.DIALOG_TYPE_WARNING);
                mEditDialogFragment.addTitle("请输入管理员密码");
                mEditDialogFragment.addEditText1(InputType.TYPE_CLASS_NUMBER, 8, null, true);
                mEditDialogFragment.setPositionText(getString(R.string.confirm));
                mEditDialogFragment.setNegativeText(getString(R.string.cancel));
                mEditDialogFragment.setDialogListener(new EditDialogFragment.OnDialogListener() {
                    @Override
                    public void onCancel() {
                        mEditDialogFragment.dismiss();
                    }

                    @Override
                    public void onConfirm(List<String> values) {
                        Iterator<String> iterator = values.iterator();
                        mPresenter.login2Setting(iterator.next());
                    }
                });
                mEditDialogFragment.show(getActivity().getSupportFragmentManager(), null);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(LeftContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showIsNewestVersion() {
        showToast("已经是最新版本");
    }

    @Override
    public void intent2Setting() {
        if (mEditDialogFragment != null && mEditDialogFragment.getDialog() != null){
            Dialog dialog = mEditDialogFragment.getDialog();
            if (dialog.isShowing()) mEditDialogFragment.dismiss();
        }
    }

    @Override
    public void showErr(String errMsg) {
        Optional.ofNullable(errMsg)
                .filter(s -> !TextUtils.isEmpty(s))
                .ifPresent(s -> ToastUtils.show(s));
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void setCheckUpdateIndicator(boolean active) {
        if (active) {
            if (mDialogFragment == null) {
                mDialogFragment = new ProgressDialogFragment();
            }
            mDialogFragment.setTipTitle(getString(R.string.check_version_update));
            mDialogFragment.setTip(getString(R.string.check_whether_has_new_version));
            mDialogFragment.setCancelable(false);
            mDialogFragment.show(getActivity().getSupportFragmentManager(), null);
        } else {
            if (mDialogFragment != null && mDialogFragment.getDialog() != null) {
                Dialog dialog = mDialogFragment.getDialog();
                if (dialog.isShowing()) {
                    mDialogFragment.dismiss();
                }
                mDialogFragment = null;
            }
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
