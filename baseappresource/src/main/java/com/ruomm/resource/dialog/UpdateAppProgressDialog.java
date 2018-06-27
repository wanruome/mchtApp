/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月11日 下午5:28:33 
 */
package com.ruomm.resource.dialog;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.resource.R;

public class UpdateAppProgressDialog extends BaseDialogUserConfig {
	private final ProgressBar mProgressBar;

	public UpdateAppProgressDialog(Context mContext, BaseDialogClickListener listener) {
		super(mContext, R.layout.common_dialog_appversion_progressbar, R.style.dialogStyle_floating_bgdark);
		mProgressBar = (ProgressBar) findViewById(R.id.dialog_progressbar);
		setBaseDialogClick(listener);
		setAutoDisMisss(false);
		setCancelable(false);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * BaseConfig.Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		setListener(R.id.dialog_cancle);
		setListener(R.id.dialog_confirm);
	}

	public void setUpdateProgress(double progressValue) {
		int max = mProgressBar.getMax();
		int progress = (int) (progressValue * mProgressBar.getMax());
		if (progress < 0) {
			progress = 0;
		}
		else if (progress > max) {
			progress = max;

		}
		mProgressBar.setProgress(progress);
	}

	public UpdateAppProgressDialog(Context mContext, String title, BaseDialogClickListener listener) {

		this(mContext, listener);
		setText(R.id.dialog_title, title);
	}

	public void setDialogTitle(String title) {
		setText(R.id.dialog_title, title);
	}

	public void setDialogConfirmBtnText(String text) {
		setText(R.id.dialog_confirm, text);
	}

	public void setDialogCancleBtnText(String text) {
		setText(R.id.dialog_cancle, text);
	}
}
