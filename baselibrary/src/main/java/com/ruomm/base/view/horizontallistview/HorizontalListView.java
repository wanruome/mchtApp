/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月24日 下午1:59:19 
 */
package com.ruomm.base.view.horizontallistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;

public class HorizontalListView extends HorizontalScrollView {
	private ListAdapter adapter;

	public HorizontalListView(Context context) {
		super(context);

	}

	public HorizontalListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public HorizontalListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public ListAdapter getAdapter() {
		// TODO Auto-generated method stub
		return this.adapter;
	}

	public void setAdapter(ListAdapter adapter) {
		this.adapter = adapter;

	}

	public View getSelectedView() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSelection(int position) {
		// TODO Auto-generated method stub

	}

}
