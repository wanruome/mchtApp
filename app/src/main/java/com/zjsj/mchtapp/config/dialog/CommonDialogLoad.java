/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年3月10日 上午11:42:43
 */
package com.zjsj.mchtapp.config.dialog;



import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zjsj.mchtapp.R;

public class CommonDialogLoad extends Dialog {
	private final TextView dialogloading_text;

	public CommonDialogLoad(Context context, boolean cancelable) {
		super(context, R.style.dialogStyle_floating_bgdark);
		if (cancelable) {
			setContentView(R.layout.common_dialog_progress);
		}
		else {
			setContentView(R.layout.common_dialog_load);
		}
		dialogloading_text = (TextView) findViewById(R.id.dialogloading_text);
		setCancelable(cancelable);
		// TODO Auto-generated constructor stub
	}

	public void setLoadingInfo(CharSequence text) {
		if (TextUtils.isEmpty(text)) {
			dialogloading_text.setText(null);
			dialogloading_text.setVisibility(View.GONE);
		}
		else {
			dialogloading_text.setText(text);
			dialogloading_text.setVisibility(View.VISIBLE);
		}
	}

	public void setLoadingInfo(int resid) {
		if (resid <= 0) {
			dialogloading_text.setText(null);
			dialogloading_text.setVisibility(View.GONE);
		}
		else {
			dialogloading_text.setText(resid);
			dialogloading_text.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (null != DialogUtil.mLoadDialog) {
			DialogUtil.mLoadDialog = null;
		}
	}

}
