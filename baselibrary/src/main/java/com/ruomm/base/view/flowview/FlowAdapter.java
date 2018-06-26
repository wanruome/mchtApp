/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月2日 下午5:11:05 
 */
package com.ruomm.base.view.flowview;

import java.util.ArrayList;
import java.util.List;

import com.ruomm.base.tools.DisplayUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FlowAdapter<T> {
	protected int column_offset = 8;
	protected List<T> listDatas;
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected FlowGridView flowGridView;
	ArrayList<FlowListAdapter<T>> listFlowAdapters = new ArrayList<FlowListAdapter<T>>();

	protected ArrayList<FlowListViewItem> listFlowListViewHeights = new ArrayList<FlowListViewItem>();

	public FlowAdapter(Context mContext, List<T> listDatas) {
		super();
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.listDatas = listDatas;
		this.column_offset = DisplayUtil.dipTopx(mContext, 2);
	}

	public FlowGridView getFlowGridView() {
		return flowGridView;
	}

	public void setFlowGridView(FlowGridView flowGridView) {
		this.flowGridView = flowGridView;
		if (null != this.flowGridView) {
			this.column_offset = this.flowGridView.getColumnOffset();
		}
	}

	public void sortListDatas() {

		listFlowListViewHeights.clear();
		listFlowAdapters.clear();
		if (null == flowGridView) {
			return;
		}
		int countColumn = flowGridView.getCountColumn();
		for (int i = 0; i < countColumn; i++) {

			listFlowListViewHeights.add(new FlowListViewItem());
		}
		if (null == listDatas) {
			return;
		}
		for (int i = 0; i < listDatas.size(); i++) {
			int index = getMinIndex();
			int itemHeight = getItemHeight(i);
			FlowListViewItem flowListItem = listFlowListViewHeights.get(index);
			flowListItem.totalHeight = flowListItem.totalHeight + itemHeight;
			flowListItem.listHeights.add(itemHeight);
			flowListItem.listIndexs.add(i);
		}

		for (int i = 0; i < countColumn; i++) {
			listFlowAdapters.add(new FlowListAdapter<T>(this, i));
		}

	}

	private int getMinIndex() {
		int minHeight = listFlowListViewHeights.get(0).totalHeight;
		int index = 0;
		for (int i = 0; i < listFlowListViewHeights.size(); i++) {
			if (listFlowListViewHeights.get(i).totalHeight + column_offset < minHeight) {
				index = i;
				minHeight = listFlowListViewHeights.get(i).totalHeight;
			}
		}
		return index;
	}

	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		listFlowListViewHeights.clear();
		int countColumn = flowGridView.getCountColumn();
		for (int i = 0; i < countColumn; i++) {
			listFlowListViewHeights.add(new FlowListViewItem());
		}
		if (null == listDatas) {
			return;
		}
		for (int i = 0; i < listDatas.size(); i++) {
			int index = getMinIndex();
			int itemHeight = getItemHeight(i);
			FlowListViewItem flowListItem = listFlowListViewHeights.get(index);
			flowListItem.totalHeight = flowListItem.totalHeight + itemHeight;
			flowListItem.listHeights.add(itemHeight);
			flowListItem.listIndexs.add(i);
		}
		flowGridView.notifyDataSetChanged(this);
		for (FlowListAdapter<T> mAdapter : listFlowAdapters) {
			mAdapter.notifyDataSetChanged();
		}

	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	public int getItemViewType(int index) {
		return 0;
	}

	public abstract View getView(int index, View convertView, ViewGroup parent);

	public abstract int getItemHeight(int index);

	public int getViewHeight(int index) {
		if (null == flowGridView) {
			return getItemHeight(index);
		}
		else {
			return getItemHeight(index) - flowGridView.getDividerHeight();
		}
	}
}
