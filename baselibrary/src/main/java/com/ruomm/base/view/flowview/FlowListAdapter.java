/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月2日 下午5:34:46 
 */
package com.ruomm.base.view.flowview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FlowListAdapter<T> extends BaseAdapter {
	FlowAdapter<T> flowAdapter;
	int indexAdapter;

	public FlowListAdapter(FlowAdapter<T> flowAdapter, int indexAdapter) {
		this.flowAdapter = flowAdapter;
		this.indexAdapter = indexAdapter;
	}

	@Override
	public int getCount() {
		if (null == flowAdapter) {
			return 0;
		}

		else {
			return flowAdapter.listFlowListViewHeights.get(indexAdapter).listHeights.size();
		}
	}

	@Override
	public int getViewTypeCount() {
		if (null == flowAdapter) {
			return 1;
		}
		else {
			return flowAdapter.getViewTypeCount();
		}

	}

	@Override
	public int getItemViewType(int position) {

		// TODO Auto-generated method stub
		if (null == flowAdapter) {
			return 0;
		}
		else {
			int itemIndex = flowAdapter.listFlowListViewHeights.get(indexAdapter).listIndexs.get(position);
			return flowAdapter.getItemViewType(itemIndex);
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		int index = flowAdapter.listFlowListViewHeights.get(indexAdapter).listIndexs.get(position);
		return flowAdapter.listDatas.get(index);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		int index = flowAdapter.listFlowListViewHeights.get(indexAdapter).listIndexs.get(position);
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int itemIndex = flowAdapter.listFlowListViewHeights.get(indexAdapter).listIndexs.get(position);
		return flowAdapter.getView(itemIndex, convertView, parent);

	}
}
