/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年3月27日 下午8:17:09
 */
package com.ruomm.resource.dialog;

public interface DialogLoadingListener {
	public void showProgressDialog(int textRes);

	public void showProgressDialog(String textDialog);

	public void dismissProgressDialog();

	public void showLoading();

	public void showLoading(int textRes);

	public void showLoading(String textDialog);

	public void dismissLoading();
}
