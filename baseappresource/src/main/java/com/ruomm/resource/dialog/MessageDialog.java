/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年3月15日 上午9:32:26 
 */
package com.ruomm.resource.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.resource.R;
import com.ruomm.resource.dialog.dal.DialogValue;

public class MessageDialog extends BaseDialogUserConfig {

	public MessageDialog(Context mContext) {
		super(mContext, R.layout.dialog_message, R.style.dialogStyle_floating_bgdark);
		setCancelable(false);
		setListener(R.id.dialog_cancle);
		setListener(R.id.dialog_confirm);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * BaseConfig.Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		// LinearLayout layout=(LinearLayout) findViewById(R.id.dialog_container);
		// layout.set
		// setDialogLayoutParams(DisplayUtil.getDispalyWidth(mContext) * 3 / 4,
		// LayoutParams.WRAP_CONTENT);
	}

	public MessageDialog(Context mContext, boolean isCancelcale) {
		this(mContext);
		setCancelable(isCancelcale);

	}

	private MessageDialog(Context mContext, int layoutId, int dialogStyle) {
		super(mContext, layoutId, dialogStyle);

		// TODO Auto-generated constructor stub
	}

	public void setDialogValue(DialogValue mDialogValue) {

		if (null != mDialogValue) {
			setMessageContent(mDialogValue.title, mDialogValue.contentString);
			setMessageButton(mDialogValue.btnCancle, mDialogValue.btnConfim);
		}
		else {
			setMessageContent(null, null);
			setMessageButton(null, null);
		}
	}

	private void setMessageContent(CharSequence titleString, CharSequence contentString) {
		if (TextUtils.isEmpty(titleString)) {
			setText(R.id.dialog_title, "提示");
		}
		else {
			setText(R.id.dialog_title, titleString);
		}
		if (TextUtils.isEmpty(contentString)) {
			setText(R.id.dialog_content, contentString);
			findViewById(R.id.dialog_content).setVisibility(View.GONE);
		}
		else {
			setText(R.id.dialog_content, contentString);
			findViewById(R.id.dialog_content).setVisibility(View.VISIBLE);
		}
	}

	private void setMessageButton(CharSequence cancaleText, CharSequence confirmText) {
		if (TextUtils.isEmpty(cancaleText)) {
			setText(R.id.dialog_cancle, "取消");
		}
		else {
			setText(R.id.dialog_cancle, cancaleText);
		}
		if (TextUtils.isEmpty(confirmText)) {
			setText(R.id.dialog_confirm, "确定");
		}
		else {
			setText(R.id.dialog_confirm, confirmText);
		}

	}

	// public int getDispalyWidth(Context mContext) {
	// DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
	// int displaywidth = dm.widthPixels;
	// return displaywidth;
	// }
	//
	// public int getDispalyHeight(Context mContext) {
	// DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
	// int displayheight = dm.heightPixels;
	// return displayheight;
	// }

}
