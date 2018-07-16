package com.ruomm.resource.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TimeUtils;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.base.view.wheel.WheelView;
import com.ruomm.base.view.wheel.adaper.NumericWheelAdapter;
import com.ruomm.base.view.wheel.listener.OnWheelChangedListener;
import com.ruomm.resource.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayStartEndWheelDialog extends BaseDialogUserConfig implements OnWheelChangedListener {
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyyMMdd");
	public static final float Dialoag_WidthPercent = 0.83f;
	public static final String ISBirthday = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
	private final WheelView wheel_end_year, wheel_end_month, wheel_end_day;
	Activity context;
	private NumericWheelAdapter year_end_adapter;
	private NumericWheelAdapter month_end_adapter;
	private NumericWheelAdapter day_end_adapter;
	// private String birthday;
	private final int syear = 2000;
	private final int eyear = 2050;
	private int year_end = 1990;
	private int month_end = 9;
	private int day_end = 15;
	private int daysofmonth_end = 30;

	private final WheelView wheel_start_year, wheel_start_month, wheel_start_day;
	//开始时间
	private NumericWheelAdapter year_start_adapter;
	private NumericWheelAdapter month_start_adapter;
	private NumericWheelAdapter day_start_adapter;
	// private String birthday;
	private int year_start = 1990;
	private int month_start = 9;
	private int day_start = 15;
	private int daysofmonth_start = 30;
	private TextView wheel_text_select;
	private TextView wheel_start_lable;
	private TextView wheel_end_lable;
	private LinearLayout wheel_end_container;
	private boolean isStartEndSameDay=false;

	public DayStartEndWheelDialog(Context mContext, BaseDialogClickListener listener) {
		super(mContext, R.layout.dialog_daywheel_startandend, R.style.dialogStyle_floating_bgdark);
		setBaseDialogClick(listener);
		setListener(R.id.wheelbtn_over);
		setListener(R.id.wheelbtn_clear);
		setListenerCancle(R.id.wheelbtn_cancle);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(mContext) * Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		wheel_text_select=(TextView) findViewById(R.id.wheel_text_select);
		wheel_start_lable=(TextView) findViewById(R.id.wheel_start_lable);
		wheel_end_lable=(TextView) findViewById(R.id.wheel_end_lable);
		wheel_end_container=(LinearLayout) findViewById(R.id.wheel_end_container);
		wheel_start_year = (WheelView) findViewById(R.id.wheel_start_year);
		wheel_start_month = (WheelView) findViewById(R.id.wheel_start_month);
		wheel_start_day = (WheelView) findViewById(R.id.wheel_start_day);
		wheel_end_year = (WheelView) findViewById(R.id.wheel_end_year);
		wheel_end_month = (WheelView) findViewById(R.id.wheel_end_month);
		wheel_end_day = (WheelView) findViewById(R.id.wheel_end_day);
		setWheelDaySelect();
	}
	private void setWheelDaySelect()
	{
		wheel_text_select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isStartEndSameDay=!isStartEndSameDay;
                updateUiMode();
			}
		});
	}
	private void updateUiMode()
    {
        if(isStartEndSameDay)
        {
            wheel_end_container.setVisibility(View.GONE);
            wheel_end_lable.setVisibility(View.GONE);
            wheel_start_lable.setText("选择日期");
            wheel_text_select.setText("显示结束日期(不同天)");
        }
        else{
            wheel_end_container.setVisibility(View.VISIBLE);
            wheel_end_lable.setVisibility(View.VISIBLE);
            wheel_start_lable.setText("开始日期");
            wheel_text_select.setText("隐藏结束日期(同一天)");
        }
    }
    public void setDateValue(String value){
	    if(StringUtils.isEmpty(value)||value.length()<8)
        {
			setDateValueForStart(System.currentTimeMillis());
			setDateValueForEnd(System.currentTimeMillis());
			isStartEndSameDay=false;
			updateUiMode();
            return;
        }
        String[] values=value.split("-");
	    if(null==values||values.length<=0)
        {
			setDateValueForStart(System.currentTimeMillis());
			setDateValueForEnd(System.currentTimeMillis());
            isStartEndSameDay=false;
            updateUiMode();
            return;
        }
        else if(values.length==1)
        {
            setDateValueForStart(values[0]);
			setDateValueForEnd(values[0]);
            isStartEndSameDay=true;
            updateUiMode();
            return;
        }
        else{
            setDateValueForStart(values[0]);
            setDateValueForEnd(values[1]);
            isStartEndSameDay=false;
            updateUiMode();
            return;
        }


    }
	public void setDateValueForStart(long milliseconds) {
		Date dateValue = new Date(milliseconds);
		year_start = TimeUtils.getValueOfYearByDate(dateValue);
		month_start = TimeUtils.getValueOfMonthByDate(dateValue);
		day_start = TimeUtils.getValueOfDayByDate(dateValue);
		setAdapterForStart();
	}

	public void setDateValueForStart(String value) {
		Date dateValue = null;
		if (TextUtils.isEmpty(value)) {
			dateValue = new Date();
		}
		else {
			try {
				dateValue = DATE_FORMAT_DATE.parse(value);

			}
			catch (Exception e) {
				dateValue = null;
			}
		}
		if (null == dateValue) {
			dateValue = new Date();
		}
		year_start = TimeUtils.getValueOfYearByDate(dateValue);
		month_start = TimeUtils.getValueOfMonthByDate(dateValue);
		day_start = TimeUtils.getValueOfDayByDate(dateValue);
		setAdapterForStart();
	}

	private void setAdapterForStart() {
		if (year_start < syear) {
			year_start = syear;
		}
		else if (year_start > eyear) {
			year_start = eyear;
		}
		if (month_start < 1) {
			month_start = 1;
		}
		else if (month_start > 12) {
			month_start = 12;
		}
		daysofmonth_start = TimeUtils.getDayNumberInMonth(year_start, month_start);
		if (day_start > daysofmonth_start) {
			day_start = daysofmonth_start;
		}
		else if (day_start < 1) {
			day_start = 1;
		}
		// if (year_start > eyear || year_start < syear || month_start < 1 || month_start > 12) {
		// Date date = new Date();
		// year_start = TimeUtils.getValueYear(date);
		// month_start = TimeUtils.getValueMonth(date);
		// daysofmonth_start = TimeUtils.getDayNumberInMonth(year_start, month_start);
		// if (day_start > daysofmonth_start || day_start < 1) {
		// day_start = TimeUtils.getValueDay(date);
		// }
		// }
		// else {
		// daysofmonth_start = TimeUtils.getDayNumberInMonth(year_start, month_start);
		// if (day_start > daysofmonth_start) {
		// day_start = daysofmonth_start;
		// }
		// else if (day_start < 1) {
		// day_start = 1;
		// }
		// }
		year_start_adapter = new NumericWheelAdapter(syear, eyear, "%04d 年");
		month_start_adapter = new NumericWheelAdapter(1, 12, "%02d 月");
		day_start_adapter = new NumericWheelAdapter(1, daysofmonth_start, "%02d 日");
		wheel_start_year.setAdapter(year_start_adapter);
		wheel_start_year.setCurrentItem(year_start - syear);
		wheel_start_month.setAdapter(month_start_adapter);
		wheel_start_month.setCurrentItem(month_start - 1);
		wheel_start_day.setAdapter(day_start_adapter);
		wheel_start_day.setCurrentItem(day_start - 1);
		wheel_start_year.addChangingListener(this);
		wheel_start_month.addChangingListener(this);
		wheel_start_day.addChangingListener(this);
	}

	public void setDateValueForEnd(long milliseconds) {
		Date dateValue = new Date(milliseconds);
		year_end = TimeUtils.getValueOfYearByDate(dateValue);
		month_end = TimeUtils.getValueOfMonthByDate(dateValue);
		day_end = TimeUtils.getValueOfDayByDate(dateValue);
		setAdapterForEnd();
	}

	public void setDateValueForEnd(String value) {
		Date dateValue = null;
		if (TextUtils.isEmpty(value)) {
			dateValue = new Date();
		}
		else {
			try {
				dateValue = DATE_FORMAT_DATE.parse(value);

			}
			catch (Exception e) {
				dateValue = null;
			}
		}
		if (null == dateValue) {
			dateValue = new Date();
		}
		year_end = TimeUtils.getValueOfYearByDate(dateValue);
		month_end = TimeUtils.getValueOfMonthByDate(dateValue);
		day_end = TimeUtils.getValueOfDayByDate(dateValue);
		setAdapterForEnd();
	}

	private void setAdapterForEnd() {
		if (year_end < syear) {
			year_end = syear;
		}
		else if (year_end > eyear) {
			year_end = eyear;
		}
		if (month_end < 1) {
			month_end = 1;
		}
		else if (month_end > 12) {
			month_end = 12;
		}
		daysofmonth_end = TimeUtils.getDayNumberInMonth(year_end, month_end);
		if (day_end > daysofmonth_end) {
			day_end = daysofmonth_end;
		}
		else if (day_end < 1) {
			day_end = 1;
		}
		year_end_adapter = new NumericWheelAdapter(syear, eyear, "%04d 年");
		month_end_adapter = new NumericWheelAdapter(1, 12, "%02d 月");
		day_end_adapter = new NumericWheelAdapter(1, daysofmonth_end, "%02d 日");
		wheel_end_year.setAdapter(year_end_adapter);
		wheel_end_year.setCurrentItem(year_end - syear);
		wheel_end_month.setAdapter(month_end_adapter);
		wheel_end_month.setCurrentItem(month_end - 1);
		wheel_end_day.setAdapter(day_end_adapter);
		wheel_end_day.setCurrentItem(day_end - 1);
		wheel_end_year.addChangingListener(this);
		wheel_end_month.addChangingListener(this);
		wheel_end_day.addChangingListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == wheel_end_year) {
			year_end = wheel.getCurrentItem() + syear;
			int daystemp = TimeUtils.getDayNumberInMonth(year_end, month_end);
			if (daysofmonth_end != daystemp) {
				daysofmonth_end = daystemp;
				day_end_adapter = new NumericWheelAdapter(1, daysofmonth_end, "%02d 日");
				wheel_end_day.setAdapter(day_end_adapter);
				if (day_end > daysofmonth_end) {
					day_end = daysofmonth_end;
					wheel_end_day.setCurrentItem(day_end - 1);
				}

			}
		}
		if (wheel == wheel_end_month) {
			month_end = wheel.getCurrentItem() + 1;
			int daystemp = TimeUtils.getDayNumberInMonth(year_end, month_end);
			if (daysofmonth_end != daystemp) {
				daysofmonth_end = daystemp;
				day_end_adapter = new NumericWheelAdapter(1, daysofmonth_end, "%02d 日");
				wheel_end_day.setAdapter(day_end_adapter);
				if (day_end > daysofmonth_end) {
					day_end = daysofmonth_end;
					wheel_end_day.setCurrentItem(day_end - 1);
				}

			}
		}
		if (wheel == wheel_end_day) {
			day_end = wheel.getCurrentItem() + 1;
		}
		if (wheel == wheel_start_year) {
			year_start = wheel.getCurrentItem() + syear;
			int daystemp = TimeUtils.getDayNumberInMonth(year_start, month_start);
			if (daysofmonth_start != daystemp) {
				daysofmonth_start = daystemp;
				day_start_adapter = new NumericWheelAdapter(1, daysofmonth_start, "%02d 日");
				wheel_start_day.setAdapter(day_start_adapter);
				if (day_start > daysofmonth_start) {
					day_start = daysofmonth_start;
					wheel_start_day.setCurrentItem(day_start - 1);
				}

			}
		}
		if (wheel == wheel_start_month) {
			month_start = wheel.getCurrentItem() + 1;
			int daystemp = TimeUtils.getDayNumberInMonth(year_start, month_start);
			if (daysofmonth_start != daystemp) {
				daysofmonth_start = daystemp;
				day_start_adapter = new NumericWheelAdapter(1, daysofmonth_start, "%02d 日");
				wheel_start_day.setAdapter(day_start_adapter);
				if (day_start > daysofmonth_start) {
					day_start = daysofmonth_start;
					wheel_start_day.setCurrentItem(day_start - 1);
				}

			}
		}
		if (wheel == wheel_start_day) {
			day_start = wheel.getCurrentItem() + 1;
		}
	}

	public String getDateValue() {
		if(isStartEndSameDay)
		{
			return String.format("%04d", year_start) + String.format("%02d", month_start) + String.format("%02d", day_start);
		}
		else{
			return String.format("%04d", year_start) + String.format("%02d", month_start) + String.format("%02d", day_start)
					+"-"
					+String.format("%04d", year_end) + String.format("%02d", month_end) + String.format("%02d", day_end);
		}

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
