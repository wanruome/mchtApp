/**
 *	@copyright 视秀科技-2014 
 * 	@author wanruome  
 * 	@create 2014-9-15 上午11:08:02 
 */
package com.ruomm.base.view.animview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class AnimFrameLayout extends FrameLayout {
	private final Context mContext;
	private final Scroller mScroller;
	private boolean isAnimScrolling = false;
	private AnimScrollListener mScrollListener;

	public AnimScrollListener getAnimScrollListener() {
		return mScrollListener;
	}

	public void setAnimScrollListener(AnimScrollListener mScrollListener) {
		this.mScrollListener = mScrollListener;
	}

	public AnimFrameLayout(Context context) {
		this(context, null);
	}

	public AnimFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mScroller = new Scroller(mContext);
	}

	public void startBounceAnimX(int startX, int endX, int durationtime) {
		isAnimScrolling = true;
		mScroller.startScroll(startX, 0, endX - startX, 0, durationtime);
		invalidate();

	}

	public void startBounceAnimY(int startY, int endY, int durationtime) {
		isAnimScrolling = true;
		mScroller.startScroll(0, startY, 0, endY - startY, durationtime);
		invalidate();
	}

	public void startBounceAnim(int startX, int startY, int endX, int endY, int durationtime) {
		isAnimScrolling = true;
		mScroller.startScroll(startX, startY, endX - startX, endY - startY, durationtime);
		invalidate();
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			if (null != mScrollListener) {
				mScrollListener.onAnimScroll(getScrollX(), getScrollY(), false);

			}
			postInvalidate();
		}
		if (mScroller.isFinished()) {
			if (null != mScrollListener) {
				mScrollListener.onAnimScroll(getScrollX(), getScrollY(), true);
			}
		}
	}

	public boolean isAnimScrolling() {
		return isAnimScrolling;
	}

}
