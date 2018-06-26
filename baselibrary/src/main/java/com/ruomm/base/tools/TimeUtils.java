package com.ruomm.base.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * 项目名称：工具类 类名称：TimeUtils 类描述： 时间常用操作类 创建人：王龙能 联系方式：563208883 http://www.wanglongneng.cn
 * 创建时间：2014-3-11 下午3:43:58 修改人：王龙能 修改时间：2014-3-11 下午3:43:58 修改备注：
 * 
 * @version
 */
public class TimeUtils {
	public static final long VALUE_DAYTimeMillis = 1000l * 3600l * 24l;
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat TIME_FORMAT_DATE_12 = new SimpleDateFormat("a hh:mm");
	public static final SimpleDateFormat TIME_FORMAT_DATE_24 = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat NO_SEC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 长的时间转换为字符串
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String formatTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * 时间长字符串，格式为DEFAULT_DATE_FORMAT
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String formatTime(long timeInMillis) {
		return formatTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	public static String format24ClockTime(long timeInMillis) {
		return formatTime(timeInMillis, TIME_FORMAT_DATE_24);
	}

	public static String format12ClockTime(long timeInMillis) {
		String timeString = formatTime(timeInMillis, TIME_FORMAT_DATE_12);
		if (timeString.contains("PM")) {
			return timeString.replace("PM", "下午");
		}
		else {
			return timeString.replace("AM", "上午");
		}
	}

	/**
	 * 时间解析类
	 * 
	 * @param datetime
	 * @return
	 */
	public static long parseTime(String datetime) {
		return parseTime(datetime, DEFAULT_DATE_FORMAT);
	}

	public static long parseTime(String datetime, SimpleDateFormat dateFormat) {

		try {
			Date dt2 = dateFormat.parse(datetime);
			return dt2.getTime();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			return 0;
		}

	}

	/**
	 * 获取一段时间内总计的毫秒
	 * 
	 * @return 总计的毫秒
	 */
	public static long getTimeMillisOfYear(long milliseconds) {
		int year = getValueOfYear(milliseconds);
		String tempString = String.format("%04d", year) + "-01-01 00:00:00";
		long valueTime = 0;
		try {
			valueTime = DEFAULT_DATE_FORMAT.parse(tempString).getTime() / 1000;
		}
		catch (Exception e) {
			valueTime = 0;
		}
		return valueTime * 1000;

	}

	//
	@SuppressWarnings("deprecation")
	public static long getTimeMillisOfMonth(long milliseconds) {
		Date dateTemp = new Date(milliseconds);
		dateTemp.setHours(0);
		dateTemp.setMinutes(0);
		dateTemp.setSeconds(0);
		dateTemp.setHours(0);
		dateTemp.setMinutes(0);
		dateTemp.setSeconds(0);
		dateTemp.setDate(1);
		long valueTime = dateTemp.getTime() / 1000;
		return valueTime * 1000;

	}

	//
	@SuppressWarnings("deprecation")
	public static long getTimeMillisOfDay(long milliseconds) {
		Date dateTemp = new Date(milliseconds);
		dateTemp.setHours(0);
		dateTemp.setMinutes(0);
		dateTemp.setSeconds(0);
		dateTemp.setHours(0);
		dateTemp.setMinutes(0);
		dateTemp.setSeconds(0);
		long valueTime = dateTemp.getTime() / 1000;
		return valueTime * 1000;
	}

	/**
	 * 获取特定时间的年月日信息
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static int getValueOfYear(long milliseconds) {
		return getValueOfYearByDate(new Date(milliseconds));
	}

	public static int getValueOfMonth(long milliseconds) {
		return getValueOfMonthByDate(new Date(milliseconds));
	}

	public static int getValueOfDay(long milliseconds) {
		return getValueOfDayByDate(new Date(milliseconds));
	}

	@SuppressWarnings("deprecation")
	public static int getValueOfYearByDate(Date date) {
		if (null == date) {
			return -1;
		}
		else {
			return date.getYear() + 1900;
		}
	}

	@SuppressWarnings("deprecation")
	public static int getValueOfMonthByDate(Date date) {
		if (null == date) {
			return -1;
		}
		else {
			return date.getMonth() + 1;
		}
	}

	@SuppressWarnings("deprecation")
	public static int getValueOfDayByDate(Date date) {
		if (null == date) {
			return -1;
		}
		else {
			return date.getDate();
		}
	}

	/**
	 * 获取当前时间（毫秒）
	 * 
	 * @return
	 */
	public static long getCurrentTime() {
		return new Date().getTime();
	}

	/**
	 * 获取当前时间以毫秒为单位，格式为DEFAULT_DATE_FORMAT
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return formatTime(getCurrentTime());
	}

	/**
	 * 获取当前时间（毫秒）
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return formatTime(getCurrentTime(), dateFormat);
	}

	/**
	 * 判断是否为合法的日期时间字符串
	 * 
	 * @param str_input
	 * @param str_input
	 * @return boolean;符合为true,不符合为false
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean isDate(String str_input, String rDateFormat) {
		if (!TextUtils.isEmpty(rDateFormat)) {
			SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
			formatter.setLenient(false);
			try {
				formatter.format(formatter.parse(str_input));
			}
			catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static int getNowYear() {
		Date date = new Date();
		int year = date.getYear() + 1900;
		return year;
	}
	/**
	 * 获取小时和分钟
	 * @param time
	 * @return
	 */
	public static String getHourAndMin(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		String min = calendar.get(Calendar.MINUTE) + "";

		if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
			hour = "0" + hour;
		}
		else {
			hour = "" + hour;
		}

		if (calendar.get(Calendar.MINUTE) < 10) {
			min = "0" + min;
		}
		else {
			min = "" + min;
		}
		return hour + ":" + min;
	}
	/**
	 * 依据生日获取周岁
	 * @param time
	 * @return
	 */
	public static int getAge(long time) {
		Date birthday = new Date(time);
		try {
			return getAge(birthday);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("出生时间大于当前时间!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;// 注意此处，如果不加1的话计算结果是错误的
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			}
			else {
				age--;
			}
		}
		return age;
	}
	/**
	 * 格式化星期显示
	 * @param day_of_week
	 * @return
	 */
	public static String getWeek_CnName(int day_of_week) {
		String[] weeksname = new String[] { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		return getWeek_CnName(day_of_week, weeksname);
	}

	public static String getWeek_CnName(int day_of_week, String[] weeksname) {
		if (day_of_week < 0 || day_of_week > 6) {
			return "未知";
		}
		else {
			return weeksname[day_of_week];
		}
	}
	//是否闰年
	public static Boolean isLeapyear(int year) {
		if (year % 4 == 0 && year % 100 != 0) {
			return true;
		}
		else if (year % 400 == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	//是否闰年
	public static Boolean isLeapyear() {
		return isLeapyear(getValueOfYearByDate(new Date()));
	}

	// 获取一个月有多少天
	public static int getDayNumberInMonth(int year, int month) {
		int daysnumberinmonth = 30;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			daysnumberinmonth = 31;
		}
		else if (month == 4 || month == 6 || month == 9 || month == 11) {
			daysnumberinmonth = 30;
		}
		else if (month == 2) {
			if (isLeapyear(year)) {
				daysnumberinmonth = 29;
			}
			else {
				daysnumberinmonth = 28;
			}
		}
		return daysnumberinmonth;
	}
}
