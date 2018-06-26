/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年8月7日 下午3:17:59 
 */
package com.ruomm.base.ioc.extend;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.ruomm.R;

/**
 * 动态排版listview,效果是每列显示的Item的数量不固定，Item的宽度适配
 * 
 * @author Ruby
 * @param <T>
 */
public abstract class DynamicAdapter<T> extends BaseAdapter {
	/**
	 * 显式声明Context，方便调用
	 */
	protected Context mContext;
	/**
	 * 显式声明mInflater，方便调用
	 */
	protected LayoutInflater mInflater;
	/**
	 * 泛型数据源
	 */
	protected ArrayList<T> listDatas;
	/**
	 * Item的宽度集合
	 */
	protected List<Integer> listWidths = new ArrayList<Integer>();
	/**
	 * 每列Item的数量集合
	 */
	protected ArrayList<Integer> listCounts = new ArrayList<Integer>();
	/**
	 * 整体ListView的显示宽度
	 */
	protected int viewWidth;

	/**
	 * 构造方法
	 * 
	 * @param mContext
	 * @param viewWidth
	 *            ListView的显示宽度
	 * @param listDatas
	 *            ListView的数据源
	 */
	public DynamicAdapter(Context mContext, int viewWidth, ArrayList<T> listDatas) {
		super();
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.listDatas = listDatas;
		this.viewWidth = viewWidth;
		setRealDatas();

	}

	/**
	 * 计算每列的数据源
	 */
	public void setRealDatas() {
		ArrayList<Integer> listIndex = new ArrayList<Integer>();
		listWidths.clear();
		if (null != this.listDatas) {

			for (int i = 0; i < listDatas.size(); i++) {
				listWidths.add(calDynamicItemWidht(i));
			}
			int tempWidth = 0;
			for (int i = 0; i < listWidths.size(); i++) {

				int currentWidth = listWidths.get(i);
				int nextWidth = 0;
				if (i + 1 < listWidths.size()) {
					nextWidth = listWidths.get(i + 1);
				}
				if (currentWidth >= viewWidth) {
					addIndex(listIndex, i);
					tempWidth = 0;
				}
				else {
					if (tempWidth + currentWidth > viewWidth) {
						addIndex(listIndex, i - 1);
						tempWidth = currentWidth;
					}
					else if (tempWidth + currentWidth == viewWidth) {
						addIndex(listIndex, i);
						tempWidth = 0;
					}
					else {
						tempWidth = tempWidth + currentWidth;
						if (tempWidth + nextWidth > viewWidth) {
							addIndex(listIndex, i);
							tempWidth = 0;
						}
					}

				}

			}

		}
		setListRealDatas(listIndex);
	}

	/**
	 * 添加一个listview的显示列
	 * 
	 * @param listIndex
	 * @param i
	 */
	private void addIndex(ArrayList<Integer> listIndex, int i) {
		if (!listIndex.contains(i)) {
			listIndex.add(i);
		}
	}

	/**
	 * 计算显示列的Item数量
	 * 
	 * @param listIndex
	 */
	private void setListRealDatas(ArrayList<Integer> listIndex) {
		if (null == this.listDatas || this.listDatas.size() == 0) {
			listCounts.clear();
		}
		else {
			listCounts.clear();
			if (listIndex.size() == 0) {
				listCounts.add(listDatas.size());
			}
			else {
				int count = 0;
				for (int i = 0; i < listDatas.size(); i++) {
					count = count + 1;

					if (listIndex.contains(i)) {
						listCounts.add(count);
						count = 0;
					}
				}
				if (count > 0) {
					listCounts.add(count);
				}

			}
		}
	}

	/**
	 * 更新数据源
	 */
	@Override
	public void notifyDataSetChanged() {
		setRealDatas();
		super.notifyDataSetChanged();
	}

	/**
	 * 获取ListView的显示列个数
	 */
	@Override
	public int getCount() {
		return listCounts.size();
	}

	/**
	 * 获取每列开始显示的数据在数据源中的索引
	 * 
	 * @param position
	 * @return
	 */
	private int getCountStartIndex(int position) {
		int index = 0;
		for (int i = 0; i < position; i++) {
			index = index + listCounts.get(i);
		}
		return index;
	}

	/**
	 * 计算Item宽度，需要实现此方法
	 * 
	 * @param index
	 * @return
	 */
	public abstract int calDynamicItemWidht(int index);

	/**
	 * 获取item宽度
	 * 
	 * @param index
	 * @return
	 */
	public final int getDynamicItemWidth(int index) {
		return listWidths.get(index);
	}

	/**
	 * 获取Item显示的View，需要实现此方法
	 * 
	 * @param index
	 * @return
	 */
	public abstract View getDynamicItemView(int index);

	/**
	 * 因为ListView每列不是一个对象所以返回的是这个列的对象集合
	 */
	@Override
	public Object getItem(int position) {
		int startIndex = getCountStartIndex(position);
		int endIndex = listCounts.get(position) + startIndex;

		return new ArrayList<T>(listDatas.subList(startIndex, endIndex));

	}

	/**
	 * ListView每列的ID
	 */
	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	/**
	 * 依据ListView每列的位置计算每列真实显示的View，使用LinearLayout包装真实的ItemView
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.common_dynamicadapter_item, null);
			holderView = new HolderView();

			holderView.dynamic_linearlayout = (LinearLayout) convertView.findViewById(R.id.dynamic_linearlayout);
			convertView.setTag(holderView);
		}
		else {

			holderView = (HolderView) convertView.getTag();
		}
		int startIndex = getCountStartIndex(position);
		int endIndex = listCounts.get(position) + startIndex;
		holderView.dynamic_linearlayout.removeAllViews();
		for (int i = startIndex; i < endIndex; i++) {
			View view = getDynamicItemView(i);
			if (null != view) {
				// view.setLayoutParams(new AbsListView.LayoutParams(getDynamicItemWidth(i),
				// AbsListView.LayoutParams.MATCH_PARENT));
				view.setLayoutParams(new LinearLayout.LayoutParams(getDynamicItemWidth(i),
						LinearLayout.LayoutParams.MATCH_PARENT));
				view.requestLayout();

				holderView.dynamic_linearlayout.addView(view);
			}
		}
		holderView.dynamic_linearlayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		holderView.dynamic_linearlayout.requestLayout();
		return convertView;
	}

	/**
	 * 内部类封装
	 * 
	 * @author Ruby
	 */
	class HolderView {
		LinearLayout dynamic_linearlayout;
	}
}
