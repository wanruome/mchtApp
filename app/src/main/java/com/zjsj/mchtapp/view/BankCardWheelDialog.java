package com.zjsj.mchtapp.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.base.view.wheel.wheelstring.OnWheelStringChangedListener;
import com.ruomm.base.view.wheel.wheelstring.WheelView;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.R;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.view.adapter.BankCardWheelAdapter;

import java.util.List;

public class BankCardWheelDialog extends BaseDialogUserConfig implements OnWheelStringChangedListener {
	public static final float Dialoag_WidthPercent = 0.83f;

	public WheelView wheel_string;
	Activity context;
	private BankCardWheelAdapter wheelAdapter;
	List<RepaymentBankCard> listDatas;
	private RepaymentBankCard wheelValue=null;
	private RepaymentBankCard wheelValueInt=null;


	public BankCardWheelDialog(Context mContext, List<RepaymentBankCard> listDatas, BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_stringwheel, R.style.dialogStyle_floating_bgdark);
		this.listDatas =listDatas;
		this.wheelValueInt=null;
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
//		setListener(R.id.wheelbtn_clear);
		setListenerCancle(R.id.wheelbtn_cancle);
		setText(R.id.dialog_title,"选择交易银行卡");
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_string = (WheelView) findViewById(R.id.wheel_string);
		setAdapter();
	}
	public BankCardWheelDialog(Context mContext, List<RepaymentBankCard> listDatas,RepaymentBankCard wheelValueInt, BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_stringwheel, R.style.dialogStyle_floating_bgdark);
		this.listDatas =listDatas;
		this.wheelValueInt=wheelValueInt;
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
//		setListener(R.id.wheelbtn_clear);
		setListenerCancle(R.id.wheelbtn_cancle);
		setText(R.id.dialog_title,"选择交易银行卡");
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_string = (WheelView) findViewById(R.id.wheel_string);
		setAdapter();
	}



	private void setAdapter() {

		wheel_string.addChangingListener(this);
		wheelAdapter=new BankCardWheelAdapter(listDatas);
		wheel_string.setAdapter(wheelAdapter);
		int index=0;
		if(null!=wheelValueInt){
			index= listDatas.indexOf(wheelValueInt);
		}
		if(index<0){
			index=0;
		}
//		for(int i=0;i<listDatas.size();i++)
//		{
//			if(wheelValue.cardIndex.equals(listDatas.get(i).cardIndex))
//			{
//				index=i;
//				break;
//			}
//		}

		wheel_string.setCurrentItem(index);

	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == wheel_string) {
			int index=wheel_string.getCurrentItem();
			MLog.i(index);
			wheelValue= listDatas.get(index);
		}


	}

	public RepaymentBankCard getDateValue() {
		return  wheelValue;
	}

	@Override
	protected void onItemClick(View v, int vID) {
		// TODO Auto-generated method stub
		super.onItemClick(v, vID);
		if (vID == R.id.wheelbtn_over) {
			v.setTag(wheelValue);
		}
	}
}
