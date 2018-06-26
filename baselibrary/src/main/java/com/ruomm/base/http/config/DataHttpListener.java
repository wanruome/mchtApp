/**
 *	@copyright 盛炬支付-2017
 * 	@author wanruome
 * 	@create 2017年2月23日 下午2:21:37
 */
package com.ruomm.base.http.config;

public interface DataHttpListener {
	/**
	 * 请求回调
	 *
	 * @param resultObject
	 *            请求结果的自动解析好的对象
	 * @param resultBytes
	 *            请求结果的原始数据
	 * @param status
	 *            请求结果的状态
	 */
	public void httpCallBack(Object resultObject, byte[] resultBytes, int status);

	/**
	 * 请求回调过滤器，
	 *
	 * @param resultBytes
	 *            请求结果的原始数据
	 * @param status
	 *            请求结果的状态
	 * @return 返回true表示过滤器成功，不会执行httpCallBack，否则执行httpCallBack回调
	 */
	public boolean httpCallBackFilter(byte[] resultBytes, int status);
}
