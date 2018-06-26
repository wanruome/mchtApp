/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月16日 下午3:17:02 
 */
package com.ruomm.base.ioc.extend;

/**
 * Thread可以停止的继承类
 * <p>
 * 在线程run的时候可以判定isStop方法来决定是否马上退出run方法安全结束线程
 * 
 * @author Ruby
 */

public class Thread_CanStop extends Thread {
	/**
	 * 是否线程停止
	 */
	protected boolean isStopTask = false;

	/**
	 * 是否线程停止
	 */
	public boolean isStopTask() {
		return isStopTask;
	}

	/**
	 * 设置线程停止
	 */
	public void stopTask() {
		this.isStopTask = true;
	}

	/**
	 * 设置线程是否停止
	 * 
	 * @param isStopTask
	 */
	public void stopTask(boolean isStopTask) {
		this.isStopTask = isStopTask;
	}
}
