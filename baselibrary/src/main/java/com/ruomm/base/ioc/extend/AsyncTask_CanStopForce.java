package com.ruomm.base.ioc.extend;

import android.os.AsyncTask;

/**
 * AsyncTask加入停止函数和强制停止函数的重写类
 * 
 * @author Ruby
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class AsyncTask_CanStopForce<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	/**
	 * 是否停止
	 */
	private boolean isStopTask = false;
	/**
	 * 是否强制停止
	 */
	private boolean isForceStopTask = false;

	/**
	 * 是否停止， doInBackground判定时候调用此方法判定停止后 doInBackground立刻执行完毕
	 */
	public boolean isStopTask() {
		return isStopTask;
	}

	/**
	 * 是否强制停止， doInBackground判定时候调用此方法判定停止后 doInBackground立刻执行完毕
	 */
	public boolean isForceStopTask() {
		return isForceStopTask;
	}

	/**
	 * 设置停止
	 */
	public void stopTask() {
		this.isStopTask = true;
	}

	/**
	 * 设置是否停止
	 * 
	 * @param isStopTask
	 */
	public void stopTask(boolean isStopTask) {
		this.isStopTask = isStopTask;
	}

	/**
	 * 设置强制停止
	 */
	public void forceStopTask() {
		this.isForceStopTask = true;
	}

	/**
	 * 设置是否强制停止
	 * 
	 * @param isStopTask
	 */
	public void forceStopTask(boolean isForceStopTask) {
		this.isForceStopTask = isForceStopTask;
	}
}
