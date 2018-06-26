/**
 *	@copyright 婉若小雪-2015 
 * 	@author wanruome  
 * 	@create 2015年3月30日 下午5:53:34 
 */
package com.ruomm.base.ioc.intf;

/**
 * 通用回调接口，可以用户Activity和Fragment交互，在BaseFragment里面集成好了，需要使用则Activity需要实现此接口
 * 
 * @author Ruby
 */
public interface BaseEntryCallBackListener {
	public void onTagEntryCallBack(String tag, Object... entrys);
}
