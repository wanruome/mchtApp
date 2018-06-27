/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年3月20日 下午2:50:40
 */
package com.ruomm.resource.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

public class DialogUtil {
	public static Dialog mLoadDialog;
	public static boolean isUseBindDialog = false;

	public static CommonDialogLoad createLoadingDialog(Context mContext, boolean cancelable) {
		CommonDialogLoad dialog = new CommonDialogLoad(mContext, cancelable);
		// dialog.setLoadingInfo(text);
		dialog.setCancelable(cancelable);
		return dialog;
	}

	public static void showLoading(Activity mActivity) {
		showDialogLoading(mActivity, "数据传送中，请稍后...");
		// showLoading(mActivity, "数据传送中，请稍后...", false);
	}

	public static void showLoading(Activity mActivity, String text) {
		showDialogLoading(mActivity, text);
		// showLoading(mActivity, text, false);
	}

	public static void showLoading(Activity mActivity, int textRes) {
		showDialogLoading(mActivity, textRes);
		// showLoading(mActivity, text, false);
	}

	public static void showProgress(Activity mActivity) {
		showDialogProgress(mActivity, "数据传送中，请稍后...");
		// showLoading(mActivity, "数据传送中，请稍后...", true);
	}

	public static void showProgress(Activity mActivity, String text) {
		showDialogProgress(mActivity, text);
		// showLoading(mActivity, text, true);
	}

	public static void showProgress(Activity mActivity, int textRes) {
		showDialogProgress(mActivity, textRes);
		// showLoading(mActivity, text, false);
	}

	private static void showDialogProgress(Activity mActivity, int textRes) {
		if (isUseBindDialog) {
			DialogLoadingListener dialogLoadingListener = null;
			try {
				dialogLoadingListener = (DialogLoadingListener) mActivity;
			}
			catch (Exception e) {
				dialogLoadingListener = null;
			}
			if (null != dialogLoadingListener) {
				dialogLoadingListener.showProgressDialog(textRes);
			}
			else {
				if (null != mLoadDialog) {
					mLoadDialog.dismiss();
				}
				CommonDialogLoad dialog = new CommonDialogLoad(mActivity, true);
				dialog.setLoadingInfo(textRes);
				dialog.show();
				mLoadDialog = dialog;
			}
		}
		else {
			if (null != mLoadDialog) {
				mLoadDialog.dismiss();
			}
			CommonDialogLoad dialog = new CommonDialogLoad(mActivity, true);
			dialog.setLoadingInfo(textRes);
			dialog.show();
			mLoadDialog = dialog;
		}
	}

	private static void showDialogLoading(Activity mActivity, int textRes) {

		if (isUseBindDialog) {
			DialogLoadingListener dialogLoadingListener = null;
			try {
				dialogLoadingListener = (DialogLoadingListener) mActivity;
			}
			catch (Exception e) {
				dialogLoadingListener = null;
			}
			if (null != dialogLoadingListener) {
				dialogLoadingListener.showLoading(textRes);
			}
			else {
				if (null != mLoadDialog) {
					mLoadDialog.dismiss();
				}
				CommonDialogLoad dialog = new CommonDialogLoad(mActivity, false);
				dialog.setLoadingInfo(textRes);
				dialog.show();
				mLoadDialog = dialog;
			}
		}
		else {
			if (null != mLoadDialog) {
				mLoadDialog.dismiss();
			}
			CommonDialogLoad dialog = new CommonDialogLoad(mActivity, false);
			dialog.setLoadingInfo(textRes);
			dialog.show();
			mLoadDialog = dialog;
		}
	}

	private static void showDialogProgress(Activity mActivity, String textDialog) {
		if (isUseBindDialog) {
			DialogLoadingListener dialogLoadingListener = null;
			try {
				dialogLoadingListener = (DialogLoadingListener) mActivity;
			}
			catch (Exception e) {
				dialogLoadingListener = null;
			}
			if (null != dialogLoadingListener) {
				dialogLoadingListener.showProgressDialog(textDialog);
			}
			else {
				if (null != mLoadDialog) {
					mLoadDialog.dismiss();
				}
				CommonDialogLoad dialog = new CommonDialogLoad(mActivity, true);
				dialog.setLoadingInfo(textDialog);
				dialog.show();
				mLoadDialog = dialog;
			}
		}
		else {
			if (null != mLoadDialog) {
				mLoadDialog.dismiss();
			}
			CommonDialogLoad dialog = new CommonDialogLoad(mActivity, true);
			dialog.setLoadingInfo(textDialog);
			dialog.show();
			mLoadDialog = dialog;
		}
	}

	private static void showDialogLoading(Activity mActivity, String textDialog) {

		if (isUseBindDialog) {
			DialogLoadingListener dialogLoadingListener = null;
			try {
				dialogLoadingListener = (DialogLoadingListener) mActivity;
			}
			catch (Exception e) {
				dialogLoadingListener = null;
			}
			if (null != dialogLoadingListener) {
				dialogLoadingListener.showLoading(textDialog);
			}
			else {
				if (null != mLoadDialog) {
					mLoadDialog.dismiss();
				}
				CommonDialogLoad dialog = new CommonDialogLoad(mActivity, false);
				dialog.setLoadingInfo(textDialog);
				dialog.show();
				mLoadDialog = dialog;
			}
		}
		else {
			if (null != mLoadDialog) {
				mLoadDialog.dismiss();
			}
			CommonDialogLoad dialog = new CommonDialogLoad(mActivity, false);
			dialog.setLoadingInfo(textDialog);
			dialog.show();
			mLoadDialog = dialog;
		}
	}

	public static void dismissDialogProgress(Activity mActivity) {
		if (isUseBindDialog) {
			DialogLoadingListener dialogLoadingListener = null;
			try {
				dialogLoadingListener = (DialogLoadingListener) mActivity;
			}
			catch (Exception e) {
				dialogLoadingListener = null;
			}
			if (null != dialogLoadingListener) {
				dialogLoadingListener.dismissProgressDialog();

			}
			else if (null != mLoadDialog && mLoadDialog.isShowing()) {
				mLoadDialog.dismiss();
			}
		}
		else {
			if (null != mLoadDialog && mLoadDialog.isShowing()) {
				mLoadDialog.dismiss();
			}
		}

	}

	public static void dismissDialogLoading(Activity mActivity) {
		if (isUseBindDialog) {
			DialogLoadingListener dialogLoadingListener = null;
			try {
				dialogLoadingListener = (DialogLoadingListener) mActivity;
			}
			catch (Exception e) {
				dialogLoadingListener = null;
			}
			if (null != dialogLoadingListener) {
				// dialogLoadingListener.dismissProgressDialog();
				dialogLoadingListener.dismissLoading();
			}
			else if (null != mLoadDialog && mLoadDialog.isShowing()) {
				mLoadDialog.dismiss();
			}
		}
		else {
			if (null != mLoadDialog && mLoadDialog.isShowing()) {
				mLoadDialog.dismiss();
			}
		}

	}

	// private static void showLoading(Activity mActivity, String text, boolean cancelable) {
	//
	// if (mActivity instanceof FragmentActivity) {
	// FragmentActivity mFragmentActivity = (FragmentActivity) mActivity;
	// CommonFragmentDialogLoad fragmentDialog = new CommonFragmentDialogLoad();
	// Bundle data = new Bundle();
	// if (TextUtils.isEmpty(text)) {
	// data.putString(InjectUtil.getBeanKey(CommonFragmentDialogLoad.class), "");
	// }
	// else {
	// data.putString(InjectUtil.getBeanKey(CommonFragmentDialogLoad.class), text);
	// }
	// data.putBoolean(InjectUtil.getBeanKey(CommonFragmentDialogLoad.class) + "_boolean",
	// cancelable);
	// fragmentDialog.setArguments(data);
	// fragmentDialog.setCancelable(cancelable);
	// // try {
	// fragmentDialog.show(FragmentTransaction, tag)
	// fragmentDialog.show(mFragmentActivity.getSupportFragmentManager(),
	// InjectUtil.getBeanKey(CommonFragmentDialogLoad.class));
	// // }
	// // catch (Exception e) {
	// // // TODO: handle exception
	// // }
	//
	// }
	// else {
	// if (null != mLoadDialog) {
	// mLoadDialog.dismiss();
	// }
	// CommonDialogLoad dialog = new CommonDialogLoad(mActivity, cancelable);
	// dialog.setLoadingInfo(text);
	// dialog.show();
	// mLoadDialog = dialog;
	// }
	//
	// }

	// public static void dismissDialog(Activity mActivity) {
	// if (mActivity instanceof FragmentActivity) {
	// FragmentActivity mFragmentActivity = (FragmentActivity) mActivity;
	// CommonFragmentDialogLoad fragmentDialog = (CommonFragmentDialogLoad) mFragmentActivity
	// .getSupportFragmentManager().findFragmentByTag(
	// InjectUtil.getBeanKey(CommonFragmentDialogLoad.class));
	//
	// if (null != fragmentDialog) {
	// // fragmentDialog.dismiss();
	// fragmentDialog.dismissAllowingStateLoss();
	// }
	// }
	// else {
	// if (null != mLoadDialog && mLoadDialog.isShowing()) {
	// mLoadDialog.dismiss();
	// }
	// }
	// }
}
