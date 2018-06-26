/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年4月19日 下午2:47:29 
 */
package com.ruomm.base.view.showpwdview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;

public class ShowPwdView extends Button {
	public static final long DefaultShowTime = 1000l;
	public EditText bindEditText;
	public long showTime = DefaultShowTime;
	private long timeDown = 0;
	private Runnable mShowPwdRunnable = null;

	public void setBindEditText(EditText bindEditText) {
		this.bindEditText = bindEditText;
	}

	public void setShowTime(long showTime) {
		if (showTime > 5000l) {
			this.showTime = DefaultShowTime;
		}
		else if (showTime < 0) {
			this.showTime = 0;
		}
		else {
			this.showTime = showTime;
		}
	}

	public ShowPwdView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ShowPwdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ShowPwdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int evAction = event.getAction();
		if (evAction == MotionEvent.ACTION_DOWN) {
			removeRunnableShowPwd();
			timeDown = System.currentTimeMillis();
			if (null != bindEditText) {

				bindEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
		}
		else if (evAction == MotionEvent.ACTION_MOVE) {

		}
		else {
			if (null == bindEditText) {

			}
			else if (showTime <= 0) {
				timeDown = System.currentTimeMillis();
				bindEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			else {
				long timeOffset = System.currentTimeMillis() - timeDown;
				if (timeOffset < showTime && timeOffset >= 0) {
					if (null == mShowPwdRunnable) {
						mShowPwdRunnable = new Runnable() {

							@Override
							public void run() {
								if (null != bindEditText) {

									bindEditText.setInputType(InputType.TYPE_CLASS_TEXT
											| InputType.TYPE_TEXT_VARIATION_PASSWORD);
								}

							}
						};
					}
					ShowPwdView.this.postDelayed(mShowPwdRunnable, showTime - timeOffset);
				}
				else {
					if (null != bindEditText) {
						timeDown = System.currentTimeMillis();
						bindEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
				}

			}

		}

		return super.onTouchEvent(event);
	}

	private void removeRunnableShowPwd() {
		if (null != mShowPwdRunnable) {
			removeCallbacks(mShowPwdRunnable);
			mShowPwdRunnable = null;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		removeRunnableShowPwd();
		if (null != this.bindEditText) {
			this.bindEditText = null;
		}
		super.onDetachedFromWindow();

	}
}
