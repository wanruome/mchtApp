package com.ruomm.base.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class DisplayUtil {
	/**
	 * 获取屏幕密度（1英寸的像素个数/160即为屏幕密度）
	 * 
	 * @param mContext
	 * @return
	 */
	public static float getDensity(Context mContext) {

		final float scale = mContext.getResources().getDisplayMetrics().density;
		return scale;
	}

	/**
	 * 获取屏幕像素密度（1英寸有多少像素）
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getDensityDpi(Context mContext) {
		final int densityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
		return densityDpi;
	}

	/**
	 * dip转换成px
	 * 
	 * @param mContext
	 * @param dipValue
	 * @return
	 */
	public static int dipTopx(Context mContext, float dipValue) {
		float scale = getDensity(mContext);
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换成dip
	 * 
	 * @param mContext
	 * @param pxValue
	 * @return
	 */

	public static int pxTodip(Context mContext, float pxValue) {
		float scale = getDensity(mContext);
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 像素转换成英寸
	 * 
	 * @param mContext
	 * @param pxValue
	 * @return
	 */
	public static float pxToInch(Context mContext, float pxValue) {
		int densityDpi = getDensityDpi(mContext);
		float inchvalue = pxValue * 1.0f / densityDpi;
		return inchvalue;
	}

	/**
	 * 像素转换成厘米
	 * 
	 * @param mContext
	 * @param pxValue
	 * @return
	 */
	public static float InchToCM(float inchValue) {
		return inchValue * 2.54f;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param mContext
	 */
	public static int getStatusBarHeight(Context mContext) {
		// WMS(依赖于窗口管理服务的回调的获取方法),此方法不推荐
		/**
		 * Rect frame = new Rect();
		 * ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		 * return frame.top;
		 */
		// findViewById方法(依赖于窗口管理服务的回调的获取方法),此方法不推荐
		/**
		 * return ((Activity) context).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		 */

		int result = 0;
		int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = mContext.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 获取屏幕的相对宽和高像素，转屏变动，横屏宽<高，竖屏宽<高
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getDispalyWidth(Context mContext) {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		int displaywidth = dm.widthPixels;
		return displaywidth;
	}

	public static int getDispalyHeight(Context mContext) {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		int displayheight = dm.heightPixels;
		return displayheight;
	}

	public static int getDispalyWidthByPercent(Context mContext, float percentValue) {
		return (int) (getDispalyWidth(mContext) * percentValue);
	}

	public static int getDispalyHeight(Context mContext, float percentValue) {
		return (int) (getDispalyHeight(mContext) * percentValue);
	}

	/**
	 * 获取屏幕去除状态条的相对高度像素，转屏变动，横屏宽<高，竖屏宽<高
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getDispalyHeightWithoutStatusBar(Context mContext) {
		return getDispalyHeight(mContext) - getStatusBarHeight(mContext);
	}

	/**
	 * 获取屏幕的绝对宽和高像素，转屏不变动，宽<高；
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getDispalyAbsWidth(Context mContext) {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		int displaywidth = dm.widthPixels;
		int displayheight = dm.heightPixels;
		return displaywidth < displayheight ? displaywidth : displayheight;
	}

	public static int getDispalyAbsHeight(Context mContext) {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		int displaywidth = dm.widthPixels;
		int displayheight = dm.heightPixels;
		return displaywidth < displayheight ? displayheight : displaywidth;
	}

	public static int getDispalyAbsWidthByPercent(Context mContext, float percentValue) {
		return (int) (getDispalyAbsWidth(mContext) * percentValue);
	}

	public static int getDispalyAbsHeightByPercent(Context mContext, float percentValue) {
		return (int) (getDispalyAbsHeight(mContext) * percentValue);
	}

	/**
	 * 获取ContentView
	 * 
	 * @param mActivity
	 * @return
	 */
	public static View getContentView(Activity mActivity) {
		View view = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		return view;
	}

	public static void closeSoftInputView(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (context.getCurrentFocus() != null) {
			// inputMethodManager.hideSoftInputFromInputMethod(context.getCurrentFocus().getWindowToken(),
			// InputMethodManager.HIDE_NOT_ALWAYS);
			inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
		}
	}

	public static void switchSoftInputView(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive()) {
			inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@SuppressLint("InlinedApi")
	public static void enableBarTint(Activity mActivity, int colorRes) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = mActivity.getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			// winParams.flags &= ~bits;
			win.setAttributes(winParams);
			SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(colorRes);
		}
	}

	public static int getTextWidth(String textMax, float fontSize) {
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(fontSize);
		float maxWidth = textPaint.measureText(textMax);
		return (int) (maxWidth + 0.5);
	}

}
