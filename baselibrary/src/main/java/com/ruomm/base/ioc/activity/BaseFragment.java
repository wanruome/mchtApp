package com.ruomm.base.ioc.activity;

import com.ruomm.base.ioc.intf.BaseEntryCallBackListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

public abstract class BaseFragment extends Fragment {
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

	// protected DialogLoadingListener dialogLoadingListener;

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

}
