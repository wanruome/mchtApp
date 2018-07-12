/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年8月24日 上午10:49:40 
 */
package com.ruomm.base.service.downloadservice;

import java.io.Serializable;

import com.ruomm.base.ioc.annotation.NotProguard;

@NotProguard
public class DownLoadEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3012465807448998231L;
	public final static String  UpdateAppVersion_Action = "updateappversion_action";
	public final static String ACTION_DOWNLOADEVENT_DEFAULT_PROGRESS = "downloadevent_default_progress";
	public final static String ACTION_DOWNLOADEVENT_DEFAULT_DONE = "downloadevent_default_done";
	public final static int DownLoadStatus_None = 0;
	public final static int DownLoadStatus_OnDownLoading = 100;
	public final static int DownLoadStatus_Sucess = 200;
	public final static int DownLoadStatus_Fail = 400;
	public int downloadStatus = 0;
	public long bytesWritten;
	public long totalSize;
	public double valueProgress;
	public DownLoadValue mDownLoadValue;
	public Object tag;
}
