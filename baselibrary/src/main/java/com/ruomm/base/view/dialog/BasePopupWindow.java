package com.ruomm.base.view.dialog;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {
	// public static int POPUP_BG_COLOR = 0x00000000;
	protected Context popContext;
	private final View mview;
	private BaseDialogClickListener listener;
	private View autodismissView;
	private int cancel_id = -1;
	private boolean isAutodismiss = true;
	private Object tag;

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public BasePopupWindow(Context mContext, int layoutId, BaseDialogClickListener listener) {
		super(mContext);
		this.popContext = mContext;
		this.listener = listener;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mview = inflater.inflate(layoutId, null);
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setContentView(mview);
		this.setFocusable(true);
		this.setBackground(0x00000000);

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
			mview.setOnTouchListener(new OnTouchListener() {

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

	public void setBackground(int bgcolor) {
		ColorDrawable dw = new ColorDrawable(bgcolor);
		this.setBackgroundDrawable(dw);
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

	public View findViewById(int vID) {
		View v = mview.findViewById(vID);
		return v;
	}

	private final OnClickListener dialogOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
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
}
