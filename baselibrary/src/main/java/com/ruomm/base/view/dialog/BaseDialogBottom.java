package com.ruomm.base.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

public class BaseDialogBottom extends Dialog {
	public BaseDialogBottom(Context context) {
		super(context);
		init(context);
	}

	protected BaseDialogBottom(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public BaseDialogBottom(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	private void init(Context context) {
		setGravity(getDefaultGravity());
	}

	public void setGravity(int gravity) {
		Window window = getWindow();
		window.setGravity(gravity);

	}

	private int getDefaultGravity() {
		return Gravity.BOTTOM;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}

}
