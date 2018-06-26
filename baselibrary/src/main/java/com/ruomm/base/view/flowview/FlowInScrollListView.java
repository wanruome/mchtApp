package com.ruomm.base.view.flowview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class FlowInScrollListView extends ListView {
	public FlowInScrollListView(Context context) {
		super(context);
	}

	public FlowInScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowInScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (ev.getAction() == MotionEvent.ACTION_MOVE) {
	// return true;
	// }
	// return super.dispatchTouchEvent(ev);
	// }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
