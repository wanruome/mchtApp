/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月29日 下午6:00:29 
 */
package com.ruomm.base.ioc.extend;

/**
 * 对象是否选中的接口类，可以依照此来设置View的状态
 * 
 * @author Ruby
 */
public interface StringValueOnUISelect {
	/**
	 * 依照此来设置UI上View的Selceted状态
	 * 
	 * @return
	 */
	boolean getUISelectValue();
}
