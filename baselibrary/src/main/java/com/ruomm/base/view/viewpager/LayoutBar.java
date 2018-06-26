package com.ruomm.base.view.viewpager;

import com.ruomm.base.tools.DisplayUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 通过xml来设置 滑动块
 */
public class LayoutBar implements ScrollBar {
	protected Context context;
	protected int layoutId;
	protected View view;
	protected int height;
	protected int width;
	protected Gravity gravity;
	protected int paddingHorizontal;

	public LayoutBar(Context context, int layoutId) {
		this(context, layoutId, Gravity.BOTTOM);
	}

	public LayoutBar(Context context, int layoutId, Gravity gravity) {
		super();
		this.context = context;
		this.layoutId = layoutId;
		this.view = LayoutInflater.from(context).inflate(layoutId, new LinearLayout(context), false);
		this.height = view.getLayoutParams().height;
		this.width = view.getLayoutParams().width;
		this.gravity = gravity;
		this.paddingHorizontal = 0;
	}

	public LayoutBar setPaddingHorizontal(int dip) {
		this.paddingHorizontal = DisplayUtil.dipTopx(context, dip);
		if (this.paddingHorizontal < 0) {
			this.paddingHorizontal = 0;
		}
		return this;
	}

	@Override
	public int getHeight(int tabHeight) {
		if (height <= 0) {
			return tabHeight;
		}
		return height;
	}

	@Override
	public int getWidth(int tabWidth) {
		if (width <= 0) {
			return tabWidth - 2 * this.paddingHorizontal;
		}
		return width;
	}

	@Override
	public View getSlideView() {
		return view;
	}

	@Override
	public Gravity getGravity() {
		return gravity;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

}
