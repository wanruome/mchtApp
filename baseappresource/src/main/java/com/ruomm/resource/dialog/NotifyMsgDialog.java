/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年3月15日 上午9:32:26 
 */
package com.ruomm.resource.dialog;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.resource.R;

public class NotifyMsgDialog extends BaseDialogUserConfig {
	public NotifyMsgDialog(Context mContext) {
		super(mContext, R.layout.dialog_notifymsg, R.style.dialogStyle_floating_bgdark);
		setCancelable(false);
		setListener(R.id.dialog_cancle);
		setListener(R.id.dialog_confirm);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * BaseConfig.Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);

	}

	private NotifyMsgDialog(Context mContext, int layoutId, int dialogStyle) {
		super(mContext, layoutId, dialogStyle);

		// TODO Auto-generated constructor stub
	}

	public void setNotifyMsg(String contentString) {
		setText(R.id.dialog_content, contentString);
	}

	@Override
	public void setBaseDialogClick(BaseDialogClickListener listeners) {
		// TODO Auto-generated method stub
		super.setBaseDialogClick(listeners);

	}

}
