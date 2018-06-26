/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年3月20日 下午2:30:31
 */
package com.ruomm.base.view.upimg;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruomm.R;

public class UpImgDialog extends DialogFragment {
	private View dialogView;
	private TextView dialogloading_text;
	private String text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogStyle_floating);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		dialogView = inflater.inflate(R.layout.upimg_dialog_compress, null);
		Bundle data = getArguments();
		text = data.getString(UpImgDialog.class.getSimpleName());
		dialogloading_text = (TextView) dialogView.findViewById(R.id.upimg_dialog_text);
		setCancelable(false);
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
