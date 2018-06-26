/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年12月24日 上午9:55:27
 */
package com.ruomm.base.ioc.intf;

/**
 * ExpandListiew的Item点击和长按点击事件回调接口
 * 
 * @author Ruby
 */
public interface BaseListSubViewListener {
	/**
	 * 点击事件回调
	 * 
	 * @param parentIndex
	 *            ItemView所在父ItemView的索引
	 * @param index
	 *            ItemView的索引
	 * @param tag
	 *            ItemView对应的数据对象
	 */
	public void onListSubViewClick(int parentIndex, int index, Object tag);

	/**
	 * 长按点击事件回调
	 * 
	 * @param parentIndex
	 *            ItemView所在父ItemView的索引
	 * @param index
	 *            ItemView的索引
	 * @param tag
	 *            ItemView对应的数据对象
	 * @return 长按事件的返回值，true则消耗此长按事件，false不消耗此长按事件
	 */
	public boolean onListSubViewLongClick(int parentIndex, int index, Object tag);
}
