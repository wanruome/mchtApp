/**
 *	@copyright 视秀科技-2014
 * 	@author wanruome
 * 	@create 2014-12-10 下午2:38:37
 */
package com.ruomm.base.view.percentview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class LinearLayout_PercentHeight extends LinearLayout {
	private float percentValue = 0.36f;
	private final int paddingLeft;
	private final int paddingRight;
	private final int paddingTop;
	private final int paddingButtom;
	private final int WIDTH_DISPLAY;
	private static final int[] ATTRS = new int[] { android.R.attr.paddingLeft, android.R.attr.paddingRight,
			android.R.attr.paddingTop, android.R.attr.paddingBottom, android.R.attr.layout_width };

	public LinearLayout_PercentHeight(Context context) {
		super(context);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		WIDTH_DISPLAY = dm.widthPixels;
		paddingLeft = 0;
		paddingRight = 0;
		paddingTop = 0;
		paddingButtom = 0;
	}

	public LinearLayout_PercentHeight(Context context, AttributeSet attrs) {
		super(context, attrs);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		WIDTH_DISPLAY = dm.widthPixels;
		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
		paddingLeft = a.getDimensionPixelSize(0, 0);
		paddingRight = a.getDimensionPixelSize(1, 0);
		paddingTop = a.getDimensionPixelSize(2, 0);
		paddingButtom = a.getDimensionPixelSize(3, 0);
		a.recycle();
	}

	@SuppressLint("NewApi")
	public LinearLayout_PercentHeight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		WIDTH_DISPLAY = dm.widthPixels;
		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
		paddingLeft = a.getDimensionPixelSize(0, 0);
		paddingRight = a.getDimensionPixelSize(1, 0);
		paddingTop = a.getDimensionPixelSize(2, 0);
		paddingButtom = a.getDimensionPixelSize(3, 0);
		a.recycle();
	}

	public void setHeightPercent(float percent_value) {
		this.percentValue = percent_value;
		int width = this.getLayoutParams().width;
		int size_width = 0;
		if (width > 0) {
			size_width = width;
		}
		else {
			size_width = WIDTH_DISPLAY;
		}
		int height = (int) ((size_width - paddingLeft - paddingRight) * percentValue) + paddingTop + paddingButtom;
		LayoutParams layoutParams = new LayoutParams(width, height);
		this.setLayoutParams(layoutParams);
		this.requestLayout();
	}

}
