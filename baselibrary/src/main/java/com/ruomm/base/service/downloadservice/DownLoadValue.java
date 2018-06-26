/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年1月8日 下午12:49:48
 */
package com.ruomm.base.service.downloadservice;

import java.io.Serializable;

import com.ruomm.base.ioc.annotation.NotProguard;

@NotProguard
public class DownLoadValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5028578689271228116L;
	public String url;
	public String fileSucess;
	public String saveTag;
	public String tagProgress;
	public String tagEnd;
	private boolean isInterrupt;

	public boolean isSendProgressEvent;
	public boolean isSendDoneEvent;

	public DownLoadValue(String url, String fileSucess) {
		super();
		this.url = url;
		this.fileSucess = fileSucess;
	}

	public boolean isInterrupt() {
		return isInterrupt;
	}

	public void setInterrupt(boolean isInterrupt) {
		this.isInterrupt = isInterrupt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fileSucess == null ? 0 : fileSucess.hashCode());
		result = prime * result + (url == null ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DownLoadValue other = (DownLoadValue) obj;
		if (fileSucess == null) {
			if (other.fileSucess != null) {
				return false;
			}
		}
		else if (!fileSucess.equals(other.fileSucess)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		}
		else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	public String getCallTag() {
		return "DownLoadValue [url=" + url + ", fileSucess=" + fileSucess + "]";
	}

}
