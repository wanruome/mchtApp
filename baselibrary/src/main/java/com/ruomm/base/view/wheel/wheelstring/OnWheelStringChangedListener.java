/**
 *	@copyright 视秀科技-2014 
 * 	@author wanruome  
 * 	@create 2014-11-24 下午3:12:35 
 */
package com.ruomm.base.view.wheel.wheelstring;

public interface OnWheelStringChangedListener {
	/**
	 * Callback method to be invoked when current item changed
	 * 
	 * @param wheel
	 *            the wheel view whose state has changed
	 * @param oldValue
	 *            the old value of current item
	 * @param newValue
	 *            the new value of current item
	 */
	void onChanged(WheelView wheel, int oldValue, int newValue);
}
