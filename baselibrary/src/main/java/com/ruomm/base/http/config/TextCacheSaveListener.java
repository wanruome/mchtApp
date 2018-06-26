/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 下午7:02:12 
 */
package com.ruomm.base.http.config;

public interface TextCacheSaveListener {
	public void saveResulit(String key, Object cacheObject, String cacheString);
}
