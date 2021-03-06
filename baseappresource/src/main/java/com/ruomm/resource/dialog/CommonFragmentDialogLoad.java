/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年3月20日 下午2:30:31
 */
package com.ruomm.resource.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.InjectEntity;
import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.resource.R;


@InjectEntity(beanKey = "CommonFragmentDialogLoad")
public class CommonFragmentDialogLoad extends DialogFragment {
	private View dialogView;
	private TextView dialogloading_text;
	private String text;
	private boolean isCancelable = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogStyle_floating_bgdark);

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Bundle data = getArguments();
		text = data.getString(InjectUtil.getBeanKey(CommonFragmentDialogLoad.class));
		isCancelable = data.getBoolean(InjectUtil.getBeanKey(CommonFragmentDialogLoad.class) + "_boolean", false);
		if (isCancelable) {
			dialogView = inflater.inflate(R.layout.common_dialog_progress, container);
		}
		else {
			dialogView = inflater.inflate(R.layout.common_dialog_load, container);
		}
		dialogloading_text = (TextView) dialogView.findViewById(R.id.dialogloading_text);
		setCancelable(isCancelable);
		setLoadingInfo();
		return dialogView;
	}

	private void setLoadingInfo() {
		if (TextUtils.isEmpty(text)) {
			dialogloading_text.setText(null);
			dialogloading_text.setVisibility(View.GONE);
		}
		else {
			dialogloading_text.setText(text);
			dialogloading_text.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
