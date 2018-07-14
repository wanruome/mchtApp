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
import com.zjsj.mchtapp.view.adapter.ListStringWheelAdapter;

import java.util.List;

public class ListStringWheelDialog extends BaseDialogUserConfig implements OnWheelStringChangedListener {
	public static final float Dialoag_WidthPercent = 0.83f;

	public WheelView wheel_string;
	Activity context;
	private ListStringWheelAdapter wheelAdapter;
	List<String> listDatas;
	private String wheelValue=null;


	public ListStringWheelDialog(Context mContext,List<String> listDatas, BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_stringwheel, R.style.dialogStyle_floating_bgdark);
		this.listDatas =listDatas;
		this.wheelValue=null;
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
		setListener(R.id.wheelbtn_clear);
		setListenerCancle(R.id.wheelbtn_cancle);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_string = (WheelView) findViewById(R.id.wheel_string);
		setAdapter();
	}
	public ListStringWheelDialog(Context mContext, List<String> listDatas, String wheelValue, BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_stringwheel, R.style.dialogStyle_floating_bgdark);
		this.listDatas =listDatas;
		this.wheelValue=wheelValue;
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
		setListener(R.id.wheelbtn_clear);
		setListenerCancle(R.id.wheelbtn_cancle);
		setText(R.id.dialog_title,"选择交易手机号");
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_string = (WheelView) findViewById(R.id.wheel_string);
		setAdapter();
	}



	private void setAdapter() {
		this.wheelValue=wheelValue;
		wheel_string.addChangingListener(this);
		wheelAdapter=new ListStringWheelAdapter(listDatas);
		wheel_string.setAdapter(wheelAdapter);
		int index=0;
		if(null!=wheelValue&&wheelValue.length()>0){
			index= listDatas.indexOf(wheelValue);
		}
		if(index<0)
		{
			index=0;
		}
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

	public String getDateValue() {
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
