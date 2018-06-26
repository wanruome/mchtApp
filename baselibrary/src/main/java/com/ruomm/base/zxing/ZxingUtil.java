/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月23日 上午11:24:26 
 */
package com.ruomm.base.zxing;

import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.base.zxing.ui.ZxingCreateDialog;
import com.ruomm.base.zxing.ui.ZxingPopupWindow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

public class ZxingUtil {
	/**
	 * 显示一个内容的二维码对话框，Dialog模式
	 * @param mFManager
	 * @param zxing_contentString
	 */
	public static void showZxingImageDialog(FragmentManager mFManager, String zxing_contentString) {
		if (null == mFManager || TextUtils.isEmpty(zxing_contentString)) {
			return;
		}
		ZxingCreateDialog zxingCreateDialog = new ZxingCreateDialog();
		Bundle data = new Bundle();
		data.putString(InjectUtil.getBeanKey(ZxingCreateDialog.class), zxing_contentString);
		zxingCreateDialog.setArguments(data);
		zxingCreateDialog.show(mFManager, InjectUtil.getBeanKey(ZxingCreateDialog.class));
	}
	/**
	 * 显示一个内容的二维码对话框，PopupWindow模式
	 * @param mContext
	 * @param v
	 * @param zxing_contentString
	 */
	public static void showZxingImagePopup(Context mContext, View v, String zxing_contentString) {
		ZxingPopupWindow zxingPopupWindow = new ZxingPopupWindow(mContext, zxing_contentString);
		zxingPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
}
