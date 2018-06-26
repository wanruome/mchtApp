/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年11月11日 下午2:23:17 
 */
package com.ruomm.base.view.repeatview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.ruomm.R;

public class RepeatView extends View {
	private static final int[] ATTRS = new int[] { android.R.attr.padding, android.R.attr.paddingLeft,
			android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom };
	private int paddingLeft = 0;
	private int paddingRight = 0;
	private int paddingTop = 0;
	private int paddingButtom = 0;
	private int repeatView_orientation = 0;
	private int repeatView_image = 0;
	private Context mContext;
	private Paint mPaint;
	private Drawable mDrawable;
	private int mDrawableWidth;
	private int mDrawableHeight;

	// private Rect mDrawableRect;

	public RepeatView(Context context) {
		super(context);
		initAttr(context, null, 0);

	}

	public RepeatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttr(context, attrs, 0);
	}

	public RepeatView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttr(context, attrs, defStyleAttr);
	}

	private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		// mDrawableRect = new Rect();
		if (null != attrs) {
			TypedArray aPadding = context.obtainStyledAttributes(attrs, ATTRS);
			int padding = aPadding.getDimensionPixelSize(0, 0);
			paddingLeft = aPadding.getDimensionPixelSize(1, padding);
			paddingTop = aPadding.getDimensionPixelSize(2, padding);
			paddingRight = aPadding.getDimensionPixelSize(3, padding);
			paddingButtom = aPadding.getDimensionPixelSize(4, padding);
			aPadding.recycle();
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RepeatView);
			int repeatView_imageRes = a.getResourceId(R.styleable.RepeatView_RepeatView_image, 0);
			repeatView_orientation = a.getInteger(R.styleable.RepeatView_RepeatView_orientation, 0);
			a.recycle();
			setRepeatImage(repeatView_imageRes);
		}

	}

	public void setRepeatImage(int repeatView_imageRes) {

		this.repeatView_image = repeatView_imageRes;
		if (this.repeatView_image > 0) {
			mDrawable = mContext.getResources().getDrawable(repeatView_image);
			mDrawableWidth = mDrawable.getIntrinsicWidth();
			mDrawableHeight = mDrawable.getIntrinsicHeight();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		if (canvasWidth <= 0 || canvasHeight <= 0 || null == mDrawable) {
			return;
		}
		if (repeatView_orientation == 0 && mDrawableWidth <= 0) {
			return;
		}
		else if (mDrawableHeight <= 0) {
			return;
		}
		canvas.save();
		canvas.clipRect(paddingLeft, paddingTop, canvasWidth - paddingRight, canvasHeight - paddingButtom);
		if (repeatView_orientation == 0) {
			int middle = (canvasHeight + paddingTop - paddingButtom) / 2;
			int top = middle - mDrawableHeight / 2;
			int bottom = top + mDrawableHeight;
			for (int i = 0;; i++) {
				int left = mDrawableWidth * i + paddingLeft;
				int right = left + mDrawableWidth;
				mDrawable.setBounds(left, top, right, bottom);
				mDrawable.draw(canvas);
				if (right >= canvasWidth - paddingRight) {
					break;
				}
			}
		}
		else {

			int middle = (canvasWidth + paddingLeft - paddingRight) / 2;
			int left = middle - mDrawableWidth / 2;
			int right = left + mDrawableWidth;
			for (int i = 0;; i++) {
				int top = mDrawableHeight * i + paddingTop;
				int bottom = top + mDrawableHeight;
				mDrawable.setBounds(left, top, right, bottom);
				mDrawable.draw(canvas);
				if (bottom > canvasHeight - paddingButtom) {
					break;
				}
			}

		}
		canvas.restore();
	}
}
