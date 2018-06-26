/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月9日 下午1:15:08 
 */
package com.ruomm.base.view.tagtextviews.util;

public class TagTextViewDrawItem {
	/**
	 * public String textValue; public int imageValue; public int imageWidth;
	 */
	public int drawX;
	public int drawY;
	public String textValue;
	public int imageValue;
	public int imageWidth;
	public int drawWidth;

	@Override
	public String toString() {
		return "TextViewAutoLineItemValue [drawX=" + drawX + ", drawY=" + drawY + ", textValue=" + textValue
				+ ", imageValue=" + imageValue + ", imageWidth=" + imageWidth + "]";
	}

}
