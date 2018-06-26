/**
 *	@copyright 亿康通 -2015 
 * 	@author liufangcai  
 * 	@create 2015年8月18日 下午4:17:36 
 */
package com.ruomm.base.view.viewpager;

import android.content.Context;
import android.util.AttributeSet;

public class GuideFixedIndicatorView extends FixedIndicatorView {
	int prePositionOffsetPixels = -1;
	private final Context mContext;
	private ViewPagerEndListener mListener;

	public GuideFixedIndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public GuideFixedIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public GuideFixedIndicatorView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void setViewPagerEndListener(ViewPagerEndListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
		int count = getAdapter().getCount();
		if (position == count - 1 && prePositionOffsetPixels == positionOffsetPixels) {
			if (mListener != null) {
				mListener.ViewPagerEnd();
			}
		}
		prePositionOffsetPixels = positionOffsetPixels;
	}
}
