package com.ruomm.base.view.dialog;

import android.support.v4.app.DialogFragment;
import android.view.Gravity;

public class BaseDialogFragmentBottom extends DialogFragment {

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setGravity(getDefaultGravity());
	}

	public void setGravity(int gravity) {
		getDialog().getWindow().setGravity(getDefaultGravity());
	}

	private int getDefaultGravity() {
		return Gravity.BOTTOM;
	}
}
