/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月2日 下午4:36:52 
 */
package com.ruomm.base.view.flowview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ruomm.R;
import com.ruomm.base.tools.DisplayUtil;

public class FlowGridView extends LinearLayout {
	protected int column_offset = 0;
	protected int count_column = 3;
	protected int dividerHeight = 0;
	protected int dividerColor = 0x32000000;
	protected ArrayList<ListView> listViews = new ArrayList<ListView>();
	protected ArrayList<View> dividerViews = new ArrayList<View>();

	public int getCountColumn() {
		return count_column;
	}

	public int getDividerHeight() {
		return dividerHeight;
	}

	public ArrayList<ListView> getListViews() {
		return listViews;
	}

	public ArrayList<View> getDividerViews() {
		return dividerViews;
	}

	public int getColumnOffset() {
		return this.column_offset;
	}

	public FlowGridView(Context context) {
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		initPublic(context);
	}

	public FlowGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowGridView);
		dividerColor = a.getColor(R.styleable.FlowGridView_flow_dividerColor, dividerColor);
		count_column = a.getInt(R.styleable.FlowGridView_flow_column_count, count_column);
		dividerHeight = a.getDimensionPixelSize(R.styleable.FlowGridView_flow_dividerHeight, dividerHeight);
		column_offset = a.getDimensionPixelSize(R.styleable.FlowGridView_flow_column_offset, column_offset);
		a.recycle();
		initPublic(context);
	}

	@SuppressLint("NewApi")
	public FlowGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOrientation(LinearLayout.HORIZONTAL);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowGridView);
		dividerColor = a.getColor(R.styleable.FlowGridView_flow_dividerColor, dividerColor);
		count_column = a.getInt(R.styleable.FlowGridView_flow_column_count, count_column);
		dividerHeight = a.getDimensionPixelSize(R.styleable.FlowGridView_flow_dividerHeight, dividerHeight);
		if (dividerHeight < 0) {
			dividerHeight = 0;
		}
		column_offset = a.getDimensionPixelSize(R.styleable.FlowGridView_flow_column_offset, column_offset);
		a.recycle();
		initPublic(context);
	}

	private void initPublic(Context context) {
		if (column_offset <= 0) {
			column_offset = DisplayUtil.dipTopx(context, 2);
		}
		listViews.clear();
		for (int i = 0; i < count_column; i++) {
			ListView listView = new FlowInScrollListView(context);
			LayoutParams listLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
			listView.setLayoutParams(listLayoutParams);
			listView.setCacheColorHint(Color.TRANSPARENT);
			listView.setDivider(new ColorDrawable(dividerColor));
			listView.setDividerHeight(dividerHeight);
			listView.setSelector(R.color.AllTransparent);
			addView(listView);
			listView.setFocusable(false);
			listViews.add(listView);
			if (dividerHeight > 0) {
				View viewHeader = new View(context);
				AbsListView.LayoutParams headerLayoutParams = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, dividerHeight);
				viewHeader.setLayoutParams(headerLayoutParams);
				viewHeader.setBackgroundColor(dividerColor);
				listView.addHeaderView(viewHeader);
				View viewFooter = new View(context);
				AbsListView.LayoutParams footerLayoutParams = new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, dividerHeight);
				viewFooter.setLayoutParams(footerLayoutParams);
				viewHeader.setBackgroundColor(dividerColor);
				listView.addFooterView(viewFooter);
			}
			if (i < count_column - 1 && dividerHeight > 0) {
				View view = new View(context);
				LayoutParams viewLayoutParams = new LayoutParams(dividerHeight, dividerHeight);
				view.setLayoutParams(viewLayoutParams);
				view.setBackgroundColor(dividerColor);
				addView(view);
				dividerViews.add(view);
			}
		}

	}

	public <T> void setAdapter(FlowAdapter<T> adapter) {

		adapter.setFlowGridView(this);
		adapter.sortListDatas();
		// adapter.setColums(count_column);
		// adapter.sortListDatas();
		// adapter.setDividerHeight(dividerHeight);
		if (dividerViews.size() > 0) {
			for (int i = 0; i < count_column - 1; i++) {
				int size1 = adapter.listFlowListViewHeights.get(i).totalHeight;
				int size2 = adapter.listFlowListViewHeights.get(i + 1).totalHeight;
				int size = size1 > size2 ? size1 : size2;
				size = size + 2 * dividerHeight;
				View view = dividerViews.get(i);
				LayoutParams viewLayoutParams = new LayoutParams(dividerHeight, size);
				view.setLayoutParams(viewLayoutParams);
				view.requestLayout();
			}
		}
		for (int i = 0; i < count_column; i++) {
			ListView listview = listViews.get(i);

			listview.setAdapter(adapter.listFlowAdapters.get(i));
		}

	}

	public <T> void notifyDataSetChanged(FlowAdapter<T> adapter) {
		if (dividerViews.size() > 0) {
			for (int i = 0; i < count_column - 1; i++) {
				int size1 = adapter.listFlowListViewHeights.get(i).totalHeight;
				int size2 = adapter.listFlowListViewHeights.get(i + 1).totalHeight;
				int size = size1 > size2 ? size1 : size2;
				size = size + 2 * dividerHeight;
				View view = dividerViews.get(i);
				LayoutParams viewLayoutParams = new LayoutParams(dividerHeight, size);
				view.setLayoutParams(viewLayoutParams);
				view.requestLayout();
			}
		}
	}

	public void setOnItemClickListener(OnItemClickListener itemClickListener) {
		for (ListView listView : listViews) {
			listView.setOnItemClickListener(itemClickListener);
		}
	}

}
