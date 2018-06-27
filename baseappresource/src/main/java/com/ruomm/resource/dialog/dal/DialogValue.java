/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年4月19日 上午8:25:37 
 */
package com.ruomm.resource.dialog.dal;

public class DialogValue {
	public CharSequence title;
	public CharSequence contentString;
	public CharSequence btnCancle;
	public CharSequence btnConfim;

	public DialogValue(CharSequence contentString) {
		super();
		this.contentString = contentString;
	}

	public DialogValue(CharSequence title, CharSequence contentString) {
		super();
		this.title = title;
		this.contentString = contentString;
	}

	public DialogValue(CharSequence title, CharSequence contentString, CharSequence btnCancle, CharSequence btnConfim) {
		super();
		this.title = title;
		this.contentString = contentString;
		this.btnCancle = btnCancle;
		this.btnConfim = btnConfim;
	}

}
