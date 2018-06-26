/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年10月12日 上午11:45:40 
 */
package com.ruomm.base.view.rulerview.tools;

import com.ruomm.base.view.rulerview.RulerView;

public interface RulerChangerListener {
	public void onRulerValueChanger(RulerView mRulerView, float rulerValue, float oldValue);
}
