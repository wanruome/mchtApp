package com.ruomm.resource.dialog;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.TimeUtils;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.base.view.wheel.WheelView;
import com.ruomm.base.view.wheel.adaper.NumericWheelAdapter;
import com.ruomm.base.view.wheel.listener.OnWheelChangedListener;
import com.ruomm.resource.R;

public class DayWheelDialog extends BaseDialogUserConfig implements OnWheelChangedListener {
	public static final float Dialoag_WidthPercent = 0.83f;
	public static final String ISBirthday = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
	private final WheelView wheel_year, wheel_month, wheel_day;
	Activity context;
	private NumericWheelAdapter yearadapter;
	private NumericWheelAdapter monthadapter;
	private NumericWheelAdapter dayadapter;
	// private String birthday;
	private final int syear = 2000;
	private final int eyear = 2050;
	private int year = 1990;
	private int month = 9;
	private int day = 15;
	private int daysofmonth = 30;

	public DayWheelDialog(Context mContext, BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_daywheel, R.style.dialogStyle_floating_bgdark);
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
		setListener(R.id.wheelbtn_clear);
		setListenerCancle(R.id.wheelbtn_cancle);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_year = (WheelView) findViewById(R.id.wheel_year);
		wheel_month = (WheelView) findViewById(R.id.wheel_month);
		wheel_day = (WheelView) findViewById(R.id.wheel_day);

	}

	public void setDateValue(long milliseconds) {
		Date dateValue = new Date(milliseconds);
		year = TimeUtils.getValueOfYearByDate(dateValue);
		month = TimeUtils.getValueOfMonthByDate(dateValue);
		day = TimeUtils.getValueOfDayByDate(dateValue);
		setAdapter();
	}

	public void setDateValue(String value) {
		Date dateValue = null;
		if (TextUtils.isEmpty(value)) {
			dateValue = new Date();
		}
		else {
			try {
				dateValue = TimeUtils.DATE_FORMAT_DATE.parse(value);

			}
			catch (Exception e) {
				dateValue = null;
			}
		}
		if (null == dateValue) {
			dateValue = new Date();
		}
		year = TimeUtils.getValueOfYearByDate(dateValue);
		month = TimeUtils.getValueOfMonthByDate(dateValue);
		day = TimeUtils.getValueOfDayByDate(dateValue);
		setAdapter();
	}

	private void setAdapter() {
		if (year < syear) {
			year = syear;
		}
		else if (year > eyear) {
			year = eyear;
		}
		if (month < 1) {
			month = 1;
		}
		else if (month > 12) {
			month = 12;
		}
		daysofmonth = TimeUtils.getDayNumberInMonth(year, month);
		if (day > daysofmonth) {
			day = daysofmonth;
		}
		else if (day < 1) {
			day = 1;
		}
		// if (year > eyear || year < syear || month < 1 || month > 12) {
		// Date date = new Date();
		// year = TimeUtils.getValueYear(date);
		// month = TimeUtils.getValueMonth(date);
		// daysofmonth = TimeUtils.getDayNumberInMonth(year, month);
		// if (day > daysofmonth || day < 1) {
		// day = TimeUtils.getValueDay(date);
		// }
		// }
		// else {
		// daysofmonth = TimeUtils.getDayNumberInMonth(year, month);
		// if (day > daysofmonth) {
		// day = daysofmonth;
		// }
		// else if (day < 1) {
		// day = 1;
		// }
		// }
		yearadapter = new NumericWheelAdapter(syear, eyear, "%04d 年");
		monthadapter = new NumericWheelAdapter(1, 12, "%02d 月");
		dayadapter = new NumericWheelAdapter(1, daysofmonth, "%02d 日");
		wheel_year.setAdapter(yearadapter);
		wheel_year.setCurrentItem(year - syear);
		wheel_month.setAdapter(monthadapter);
		wheel_month.setCurrentItem(month - 1);
		wheel_day.setAdapter(dayadapter);
		wheel_day.setCurrentItem(day - 1);
		wheel_year.addChangingListener(this);
		wheel_month.addChangingListener(this);
		wheel_day.addChangingListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == wheel_year) {
			year = wheel.getCurrentItem() + syear;
			int daystemp = TimeUtils.getDayNumberInMonth(year, month);
			if (daysofmonth != daystemp) {
				daysofmonth = daystemp;
				dayadapter = new NumericWheelAdapter(1, daysofmonth, "%02d 日");
				wheel_day.setAdapter(dayadapter);
				if (day > daysofmonth) {
					day = daysofmonth;
					wheel_day.setCurrentItem(day - 1);
				}

			}
		}
		if (wheel == wheel_month) {
			month = wheel.getCurrentItem() + 1;
			int daystemp = TimeUtils.getDayNumberInMonth(year, month);
			if (daysofmonth != daystemp) {
				daysofmonth = daystemp;
				dayadapter = new NumericWheelAdapter(1, daysofmonth, "%02d 日");
				wheel_day.setAdapter(dayadapter);
				if (day > daysofmonth) {
					day = daysofmonth;
					wheel_day.setCurrentItem(day - 1);
				}

			}
		}
		if (wheel == wheel_day) {
			day = wheel.getCurrentItem() + 1;
		}
	}

	public String getDateValue() {
		return String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
	}

	@Override
	protected void onItemClick(View v, int vID) {
		// TODO Auto-generated method stub
		super.onItemClick(v, vID);
		if (vID == R.id.wheelbtn_over) {
			v.setTag(getDateValue());
		}
		else if (vID == R.id.wheelbtn_clear) {
			v.setTag("");
		}
	}
}
