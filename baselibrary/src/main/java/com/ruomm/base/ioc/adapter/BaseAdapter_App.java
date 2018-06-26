/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月10日 下午2:07:39 
 */
package com.ruomm.base.ioc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * BaseAdpter的继承类
 * 
 * @author Ruby
 */
public abstract class BaseAdapter_App extends BaseAdapter {
	/**
	 * 显式声明Context
	 */
	protected final Context mContext;
	/**
	 * 显式声明LayoutInflater
	 */
	protected final LayoutInflater mInflater;

	/**
	 * 在构造函数里面显式声明变量复制
	 * 
	 * @param mContext
	 *            Context赋值；
	 */
	public BaseAdapter_App(Context mContext) {
		super();
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(this.mContext);
	}

}
