/**
 *	@copyright 视秀科技-2014 
 * 	@author wanruome  
 * 	@create 2014-11-26 下午1:09:40 
 */
package com.ruomm.base.view.wheel.adaper;

public class NumericWheelAdapter implements WheelAdapter {
	private int minValue;
	private int maxValue;
	private String format;

	public NumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return maxValue - minValue + 1;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getCount()) {
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public String getCalWidhtString() {
		// TODO Auto-generated method stub
		return null != format ? String.format(format, minValue) : Integer.toString(minValue);
	}
}
