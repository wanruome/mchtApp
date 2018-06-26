/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年12月24日 下午5:10:37
 */
package com.ruomm.base.ioc.extend;

/**
 * 事件驱动的辅助类，可以用于EventBus来区分同类型的不同来源和目标事件
 * 
 * @author Ruby
 */
public class BaseEvent {
	/**
	 * 事件来源
	 */
	public String source;
	/**
	 * 事件目标
	 */
	public String target;
}
