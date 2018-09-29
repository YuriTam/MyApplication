package com.yuri.tam.client.fragment.left;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.utils.AppUtils;
import com.common.utils.UIUtils;
import com.yuri.tam.R;
import com.yuri.tam.base.BaseFragment;
import com.yuri.tam.client.activity.setting.SettingActivity;
import com.yuri.tam.client.fragment.EditDialogFragment;
import com.yuri.tam.client.fragment.ProgressDialogFragment;
import com.yuri.tam.core.api.ApiRepository;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
                mEditDialogFragment.addTitle(getString(R.string.pls_input_admin_password));
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
        showToast(getString(R.string.already_new_version));
    }

    @Override
    public void intent2Setting() {
        if (mEditDialogFragment != null && mEditDialogFragment.getDialog() != null){
            Dialog dialog = mEditDialogFragment.getDialog();
            if (dialog.isShowing()) mEditDialogFragment.dismiss();
        }
        intent2Activity(SettingActivity.class);
    }

    @Override
    public void showErr(String errMsg) {
        showToast(errMsg);
    }

    @Override
    public void showEmpty() {
        showToast(getString(R.string.input_is_empty));
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
    public void showException(Throwable e) {
        showExceptionTips(e);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
