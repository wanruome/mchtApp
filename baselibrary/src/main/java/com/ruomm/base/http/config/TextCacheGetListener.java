/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 下午7:04:15 
 */
package com.ruomm.base.http.config;

public interface TextCacheGetListener {
	// 获取cache字符串
	public String getCacheString(String key);

	// 获取缓存时间
	public long getCacheTime();
	//缓存是否有效
	public boolean isCacheOk();
    //设置缓存否有效
	public void setCacheOk(boolean isCacheOk);
	//是否使用httpCallBack返回数据，若使用则调用httpCallBack返回数据，否则使用httpCallBackCache返回数据
	public TextCacheGetListener setCacheExecuteToHttpCallBack(boolean isCacheExecuteToHttpCallBack);
	//是否使用httpCallBack返回数据，若使用则调用httpCallBack返回数据，否则使用httpCallBackCache返回数据
	public boolean isCacheExecuteToHttpCallBack();
	//设置cache有效时候是否继续执行网络请求
	public TextCacheGetListener setDoRealHttp(boolean isDoRealHttp);
	//cache有效时候是否继续执行网络请求
	public boolean isDoRealHttp();
	// 是否返回本地
	public void httpCallBackCache(Object resultObject, byte[] resultBytes, int status, long cacheTime);

}
