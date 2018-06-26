/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月2日 上午9:35:47 
 */
package com.ruomm.base.view.dialog;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public abstract class BaseDialogUserConfig extends Dialog {
	protected Context mContext;
	private View autodismissView;
	protected BaseDialogClickListener listener;
	private int cancel_id = -1;
	private boolean isAutodismiss = true;
	private Object tag;

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public BaseDialogUserConfig(Context mContext, int layoutId, int dialogStyle) {
		super(mContext, dialogStyle);
		this.mContext = mContext;
		setContentView(layoutId);

	}

	public void setBaseDialogClick(BaseDialogClickListener listeners) {
		this.listener = listeners;
	}

	public void setAutoDisMisss(boolean isAutodismiss) {
		this.isAutodismiss = isAutodismiss;
	}

	public void setAutoDisMisss(int viewID) {
		try {
			autodismissView = findViewById(viewID);
		}
		catch (Exception e) {
			autodismissView = null;
		}
		if (null != autodismissView) {
			getWindow().getDecorView().setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int x = (int) event.getX();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						int top = autodismissView.getTop();
						int buttom = autodismissView.getBottom();
						int left = autodismissView.getLeft();
						int right = autodismissView.getRight();

						if (x < left || x > right || y < top || y > buttom) {
							dismiss();
						}
					}
					return true;
				}
			});
		}
	}

	public void setDialogLayoutParams(int width, int height) {

		getWindow().setLayout(width, height);
	}

	public void setGravity(int gravity) {
		Window window = getWindow();
		window.setGravity(gravity);

	}

	public void setText(int vID, CharSequence text) {
		try {
			View v = findViewById(vID);
			Method method = v.getClass().getMethod("setText", CharSequence.class);
			method.setAccessible(true);
			if (TextUtils.isEmpty(text)) {
				method.invoke(v, "");
			}
			else {
				method.invoke(v, text);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
	}

	public void setListener(int viewid) {

		View view = null;
		try {
			view = findViewById(viewid);
		}
		catch (Exception e) {
			view = null;
		}
		if (view != null) {
			view.setOnClickListener(dialogOnClickListener);
		}

	}

	public void setListenerCancle(int viewid) {
		View view = null;
		try {
			view = findViewById(viewid);
		}
		catch (Exception e) {
			view = null;
		}
		if (view != null) {
			cancel_id = viewid;
			view.setOnClickListener(dialogOnClickListener);
		}
	}

	private final View.OnClickListener dialogOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			onItemClick(v, v.getId());
			if (v.getId() == cancel_id) {
				dismiss();
			}
			else {
				if (null != listener) {
					listener.onDialogItemClick(v, tag);
				}
				if (isAutodismiss) {
					dismiss();
				}
			}

		}
	};

	protected void onItemClick(View v, int vID) {

	}
}
