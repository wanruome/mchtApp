/**
 *	@copyright 视秀科技-2014
 * 	@author wanruome
 * 	@create 2014-11-26 下午1:09:40
 */
package com.ruomm.base.view.wheel.adaper;


public class NumericAutoWheelAdapter implements WheelAdapter {
	private int minValue;
	private int maxValue;
	private int maxSize;

	public NumericAutoWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.maxSize = getCalWidhtString().length();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return maxValue - minValue + 1;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getCount()) {
			StringBuilder buf = new StringBuilder();
			int value = minValue + index;
			String temp = Integer.toString(value);
			int offset = maxSize - temp.length();
			for (int i = 0; i < offset; i++) {
				buf.append(" ");
			}
			buf.append(temp);
			return new String(buf);
		}
		return null;
	}

	@Override
	public String getCalWidhtString() {
		// TODO Auto-generated method stub
		return Integer.toString(maxValue);
	}
}
