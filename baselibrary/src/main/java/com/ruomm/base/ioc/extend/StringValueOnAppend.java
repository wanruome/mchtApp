/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年8月10日 下午5:18:21 
 */
package com.ruomm.base.ioc.extend;

/**
 * 对象集合按照功能转为字符串的接口类
 * <p>
 * StringUtil.appendString(List<V> lists, String appendString, String tag)
 * 方法拼接对象为字符串时候调用，实现此接口可以实现对象集合按照功能字符串的拼接
 * 
 * @author Ruby
 */
public interface StringValueOnAppend {
	public String getAppendStringValue(String tag);
}
