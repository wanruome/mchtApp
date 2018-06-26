/**
 *	@copyright 婉若小雪-2015 
 * 	@author wanruome  
 * 	@create 2015年3月27日 下午6:31:05 
 */
package com.ruomm.base.view.wheel.wheelstring.adapter;

import com.ruomm.base.view.wheel.wheelstring.WheelStringAdapter;

public class StringWheelStringAdapter implements WheelStringAdapter {
	String[] stringArray;

	public StringWheelStringAdapter(String[] stringArray) {
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

}
