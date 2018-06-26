/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年10月31日 上午11:52:46 
 */
package com.ruomm.base.view.floatview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ruomm.R;

public class FloatView extends FrameLayout {
	private static float MinPercent = -0.8f;
	private int centerX = 0;
	private int centerY = 0;
	private int floatWidth = 0;
	private int floatHeight = 0;
	private int containerWidth = 0;
	private int containerHeight = 0;
	private View containerView;
	private View realView;
	private int value_Left = 0;
	private int value_Right = 0;
	private int value_Top = 0;
	private int value_Bottom = 0;
	private float percent_width = 0;
	private float percent_height = 0;
	private final Context mContext;
	private float downX;
	private float downY;
	private int downScrollX;
	private int downScrollY;
	private boolean isFloatEnable;

	public FloatView(Context context) {
		super(context);
		this.mContext = context;
	}

	public FloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initAttrs(context, attrs);
	}

	public FloatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initAttrs(context, attrs);

	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatView);
		percent_width = a.getFloat(R.styleable.FloatView_floatview_widthPercent, percent_width);
		percent_height = a.getFloat(R.styleable.FloatView_floatview_heightPercent, percent_height);
		if (percent_width < MinPercent) {
			percent_width = MinPercent;

		}
		if (percent_height < MinPercent) {
			percent_height = MinPercent;
		}
		isFloatEnable = a.getBoolean(R.styleable.FloatView_floatview_isEnableFloat, true);
		a.recycle();
	}

	public void addFloatView(int containerResID, int realViewID) {
		if (null != containerView) {
			removeView(containerView);
			containerView = null;
			realView = null;
			centerX = 0;
			centerY = 0;
			floatHeight = 0;
			floatWidth = 0;
		}
		if (containerResID > 0) {
			containerView = LayoutInflater.from(this.mContext).inflate(containerResID, null);
			containerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			addView(containerView);
			realView = findViewById(realViewID);
			if (null != realView) {
				realView.setOnTouchListener(onTouchListener);
			}
		}
	}

	public View getFloatClickView() {
		return realView;
	}

	public int getFloatClickViewID() {
		if (null != realView) {
			return realView.getId();
		}
		else {
			return 0;
		}
	}

	public void setFloatEnable(boolean isFloatEnable) {
		this.isFloatEnable = isFloatEnable;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (w > 0 && h > 0) {
			containerWidth = w;
			containerHeight = h;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	private final OnTouchListener onTouchListener = new OnTouchListener() {
		boolean isClick;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (isFloatEnable) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (centerX == 0 && centerY == 0 && floatHeight == 0 && floatWidth == 0) {
							floatWidth = v.getRight() - v.getLeft();
							floatHeight = v.getBottom() - v.getTop();
							centerX = (v.getLeft() + v.getRight()) / 2;
							centerY = (v.getTop() + v.getBottom()) / 2;
							calFloatAttr();
						}
						v.setSelected(true);
						isClick = false;
						downX = event.getRawX();
						downY = event.getRawY();
						downScrollX = containerView.getScrollX();
						downScrollY = containerView.getScrollY();

						break;
					case MotionEvent.ACTION_MOVE:
						isClick = true;
						// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
						int offSetX = (int) (downX - event.getRawX());
						int offSetY = (int) (downY - event.getRawY());
						updateFloatUI(downScrollX + offSetX, downScrollY + offSetY);
						return true;
					case MotionEvent.ACTION_UP:
						v.setSelected(false);
						return isClick;

					default:
						break;
				}
				return false;
			}
			else {
				return false;
			}
		}
	};

	private void calFloatAttr() {
		value_Left = 0 + centerX - floatWidth / 2;
		value_Right = value_Left - containerWidth + floatWidth;
		value_Top = 0 + centerY - floatHeight / 2;
		value_Bottom = value_Top - containerHeight + floatHeight;
		value_Left = (int) (value_Left - floatWidth * percent_width);
		value_Right = (int) (value_Right + floatWidth * percent_width);
		value_Top = (int) (value_Top - floatHeight * percent_height / 2);
		value_Bottom = (int) (value_Bottom + floatHeight * percent_height / 2);

	}

	private void updateFloatUI(int scrollX, int scrollY) {
		int realScrollX = scrollX;
		int realScrollY = scrollY;
		if (scrollX > value_Left) {
			realScrollX = value_Left;
		}
		else if (scrollX < value_Right) {
			realScrollX = value_Right;
		}
		if (scrollY > value_Top) {
			realScrollY = value_Top;
		}
		else if (scrollY < value_Bottom) {
			realScrollY = value_Bottom;
		}
		containerView.scrollTo(realScrollX, realScrollY);

	}
}
