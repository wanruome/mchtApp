/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月28日 下午3:37:05 
 */
package com.ruomm.base.ioc.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 泛型BaseAdapter，使用List集合作为数据源
 * 
 * @author Ruby
 * @param <T>
 */
public abstract class BaseAdapter_T<T> extends BaseAdapter {
	/**
	 * 显式声明Context
	 */
	protected final Context mContext;
	/**
	 * 显式声明LayoutInflater
	 */
	protected LayoutInflater mInflater;
	/**
	 * 泛型数据集合
	 */
	protected List<T> listDatas;

	/**
	 * 在构造函数里面显式声明变量复制和设置数据源
	 * 
	 * @param mContext
	 */
	public BaseAdapter_T(Context mContext, List<T> listDatas) {
		super();
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.listDatas = listDatas;
	}

	/**
	 * 计算Item数量
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == listDatas ? 0 : listDatas.size();
	}

	/**
	 * 返回Item对应的Object对象
	 */
	@Override
	public Object getItem(int position) {
		int count = null == listDatas ? 0 : listDatas.size();
		if (position >= count) {
			return null;
		}
		else {
			return null == listDatas ? null : listDatas.get(position);
		}
	}

	/**
	 * 返回Item对应的ID
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 换数据源更新适配器数据并刷新UI
	 * 
	 * @param listDatas
	 */
	public void notifyDataSetChanged(List<T> listDatas) {
		this.listDatas = listDatas;
		super.notifyDataSetChanged();
	}

}
