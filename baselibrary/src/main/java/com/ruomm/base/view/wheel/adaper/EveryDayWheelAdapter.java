/**
 *	@copyright 视秀科技-2014 
 * 	@author wanruome  
 * 	@create 2014-11-26 下午1:09:40 
 */
package com.ruomm.base.view.wheel.adaper;

public class EveryDayWheelAdapter implements WheelAdapter {
	private int minValue = 1;
	private int maxValue = 100;

	public EveryDayWheelAdapter(int minValue, int maxValue) {
		super();
		this.minValue = minValue;
		this.maxValue = maxValue;
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
			if (value > 0 && value < maxValue) {
				return " " + String.format("%02d", value);
			}
			else {
				return String.format("%03d", value);
			}
		}
		return null;
	}

	@Override
	public String getCalWidhtString() {
		// TODO Auto-generated method stub
		return String.format("%03d", maxValue);
	}

	public int getItemValue(int index) {
		int value = minValue + index;
		return value;
	}
}
