/**
 *	@copyright 长本网络-2015
 * 	@author wanruome
 * 	@create 2015年3月17日 上午10:25:06
 */
package com.ruomm.base.view.wheel.adaper;

public class ArrayWheelAdapter implements WheelAdapter {
	String[] stringArray;

	public ArrayWheelAdapter(String[] stringArray) {
		super();
		this.stringArray = stringArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == stringArray ? 0 : stringArray.length;
	}

	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		return stringArray[index];
	}

	@Override
	public String getCalWidhtString() {
		// TODO Auto-generated method stub
		return null == stringArray || stringArray.length == 0 ? "" : stringArray[getCount() - 1];
	}

}
