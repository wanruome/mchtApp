package com.zjsj.mchtapp.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.base.view.wheel.listener.OnWheelChangedListener;
import com.ruomm.base.view.wheel.wheelstring.OnWheelStringChangedListener;
import com.ruomm.base.view.wheel.wheelstring.WheelView;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.R;
import com.zjsj.mchtapp.dal.response.RepayMentArea;
import com.zjsj.mchtapp.view.adapter.AreaCityAdapter;
import com.zjsj.mchtapp.view.adapter.AreaProvinceAdapter;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class AreaWheelDialog extends BaseDialogUserConfig implements OnWheelStringChangedListener {
	public static final float Dialoag_WidthPercent = 0.83f;

	public WheelView wheel_province, wheel_city;
	Activity context;
	private AreaCityAdapter areaCityAdapter;
	private AreaProvinceAdapter areaProvinceAdapter;
	List<RepayMentArea> cityData;
	private String city=null;
	private String province=null;


	public AreaWheelDialog(Context mContext, List<RepayMentArea> cityData,BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_areawheel, R.style.dialogStyle_floating_bgdark);
		this.cityData=cityData;
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
		setListenerCancle(R.id.wheelbtn_cancle);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_city = (WheelView) findViewById(R.id.wheel_city);
		wheel_province = (WheelView) findViewById(R.id.wheel_province);
		setAdapter(null,null);
	}
	public AreaWheelDialog(Context mContext, List<RepayMentArea> cityData,String province,String city,BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_areawheel, R.style.dialogStyle_floating_bgdark);
		this.cityData=cityData;
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
		setListenerCancle(R.id.wheelbtn_cancle);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_city = (WheelView) findViewById(R.id.wheel_city);
		wheel_province = (WheelView) findViewById(R.id.wheel_province);
		setAdapter(province,city);
	}


	private void setAdapter(String province,String city) {
		wheel_province.addChangingListener(this);
		wheel_city.addChangingListener(this);
		areaProvinceAdapter=new AreaProvinceAdapter(cityData);
		wheel_province.setAdapter(areaProvinceAdapter);
		wheel_province.setCurrentItem(areaProvinceAdapter.getIndexByName(province));
		int index=wheel_province.getCurrentItem();
		areaCityAdapter=new AreaCityAdapter(cityData.get(index));
		wheel_city.setAdapter(areaCityAdapter);
		wheel_city.setCurrentItem(areaCityAdapter.getIndexByName(city));

	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == wheel_province) {
			int index=wheel_province.getCurrentItem();
			MLog.i(index);
			areaCityAdapter=new AreaCityAdapter(cityData.get(index));
			wheel_city.setAdapter(areaCityAdapter);
			province=areaProvinceAdapter.getItem(index);
		}
		else if(wheel==wheel_city)
		{
			int index=wheel_city.getCurrentItem();
			city=areaCityAdapter.getItem(index);
		}

	}

	public String getDateValue() {
		if(TextUtils.isEmpty(province)||TextUtils.isEmpty(city)){
			return "";
		}
		else {
			return province+"-"+city;
		}
	}

	@Override
	protected void onItemClick(View v, int vID) {
		// TODO Auto-generated method stub
		super.onItemClick(v, vID);
		if (vID == R.id.wheelbtn_over) {
			v.setTag(getDateValue());
		}
	}
}
