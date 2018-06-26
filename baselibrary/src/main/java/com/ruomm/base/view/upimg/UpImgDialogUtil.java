/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年3月20日 下午2:50:40
 */
package com.ruomm.base.view.upimg;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

public class UpImgDialogUtil {
	public static void showUpImgDialog(FragmentManager mFManager) {
		showUpImgDialog(mFManager, "压缩图片中，请稍后");

	}

	public static void showUpImgDialog(FragmentManager mFManager, String text) {
		UpImgDialog dialogLoadingFragment = new UpImgDialog();
		Bundle data = new Bundle();
		if (TextUtils.isEmpty(text)) {

			data.putString(UpImgDialog.class.getSimpleName(), "");
		}
		else {
			data.putString(UpImgDialog.class.getSimpleName(), text);
		}
		dialogLoadingFragment.setArguments(data);
		dialogLoadingFragment.show(mFManager, UpImgDialog.class.getSimpleName());

	}

	public static void dismissUpImgDialog(FragmentManager mFManager) {
		try {
			UpImgDialog dialogLoadingFragment = (UpImgDialog) mFManager.findFragmentByTag(UpImgDialog.class
					.getSimpleName());
			if (null != dialogLoadingFragment) {
				dialogLoadingFragment.dismiss();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}
}
