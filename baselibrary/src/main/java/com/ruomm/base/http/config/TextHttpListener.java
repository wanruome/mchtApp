package com.ruomm.base.http.config;

public interface TextHttpListener {
	/**
	 * 请求回调
	 * 
	 * @param resultObject
	 *            请求结果的自动解析好的对象
	 * @param resultString
	 *            请求结果的原始数据
	 * @param status
	 *            请求结果的状态
	 */
	public void httpCallBack(Object resultObject, String resultString, int status);

	/**
	 * 请求回调过滤器，
	 * 
	 * @param resultString
	 *            请求结果的原始数据
	 * @param status
	 *            请求结果的状态
	 * @return 返回true表示过滤器成功，不会执行httpCallBack，否则执行httpCallBack回调
	 */
	public boolean httpCallBackFilter(String resultString, int status);
	
	// public void httpCallBackToast(Object resultObject, String resultString, int status);
	// public void httpCallBackFinish(int status);

}
