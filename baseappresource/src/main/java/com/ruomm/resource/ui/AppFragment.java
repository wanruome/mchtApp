package com.ruomm.resource.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.ruomm.base.ioc.activity.BaseFragment;
import com.ruomm.base.ioc.intf.BaseEntryCallBackListener;
import com.ruomm.resource.dialog.DialogLoadingListener;

public class AppFragment extends Fragment implements DialogLoadingListener{
    /**
     * 显式声明需要调用的Context
     */
    protected Context mContext;
    /**
     * 显式声明需要调用的Bundle
     */
    protected Bundle mBundle;
    /**
     * 显式声明需要调用的FragmentManager类
     */
    protected FragmentManager mFManager_Child;
    /**
     * 显式声明需要onCreateView加载的View
     */
    protected View mView;
    /**
     * 和Activity交互接口类，Actvity需要实现BaseEntryCallBackListener才可以使用
     */
    protected BaseEntryCallBackListener callBackListener;

    private DialogLoadingListener frgDialogLoadingListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mFManager_Child = getChildFragmentManager();
        mBundle = getArguments();
        try {
            callBackListener = (BaseEntryCallBackListener) mContext;
        }
        catch (Exception e) {
            callBackListener = null;
        }
        try {
            frgDialogLoadingListener = (DialogLoadingListener) mContext;
        }
        catch (Exception e) {
            frgDialogLoadingListener = null;
        }
    }

    /**
     * 回调主界面的接口
     *
     * @param tag
     *            区分不同回调的标识；
     * @param entrys
     *            回调的参数；
     */
    protected void callBackActivity(String tag, Object... entrys) {
        if (null != callBackListener) {
            callBackListener.onTagEntryCallBack(tag, entrys);
        }
    }

    @Override
    public void showProgressDialog(int textRes) {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.showProgressDialog(textRes);
        }
    }

    @Override
    public void showProgressDialog(String textDialog) {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.showProgressDialog(textDialog);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void dismissProgressDialog() {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.dismissProgressDialog();
        }
    }

    @Override
    public void showLoading() {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.showLoading();
        }

    }

    @Override
    public void showLoading(int textRes) {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.showLoading(textRes);
        }

    }

    @Override
    public void showLoading(String text) {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.showLoading(text);
        }

    }

    @Override
    public void dismissLoading() {
        if (null != frgDialogLoadingListener) {
            frgDialogLoadingListener.dismissLoading();
        }

    }
}
