/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年11月18日 下午6:17:12 
 */
package com.ruomm.base.tools.listener;

/**
 * 播放器播放状态回调监听类
 * 
 * @author Ruby
 */
public interface MPlayListener {
	/**
	 * 播放失败
	 * 
	 * @param filePath
	 *            播放文件的路径
	 */
	void onMediaPlayStartError(String filePath);

	/**
	 * 播放停止
	 * 
	 * @param filePath
	 *            播放文件的路径
	 */
	void onMediaPlayStop(String filePath);
}
