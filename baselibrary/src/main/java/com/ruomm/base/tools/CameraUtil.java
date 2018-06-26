/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年5月13日 下午2:20:44 
 */
package com.ruomm.base.tools;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;

/**
 * Camera工具类
 * 
 * @author Ruby
 */
public class CameraUtil {
	// 是否支持后置摄像头
	public static boolean isSupportBack(Context mContext) {
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
		if (SDK_INT >= 9) {
			if (getCameraIDBack() < 0) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return checkCameraHardware(mContext);
		}
	}

	// 是否支持前置摄像头
	public static boolean isSupportFront(Context mContext) {
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
		if (SDK_INT >= 9) {
			if (getCameraIDFront() < 0) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}

	// 获取后置摄像头ID，无法获取返回-1
	@SuppressLint("NewApi")
	public static int getCameraIDBack() {
		int defaultId = -1;
		// Find the total number of cameras available
		int mNumberOfCameras = Camera.getNumberOfCameras();

		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < mNumberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultId = i;
				break;
			}
		}
		return defaultId;
	}

	// 获取前置摄像头ID，无法获取返回-1
	@SuppressLint("NewApi")
	public static int getCameraIDFront() {
		int defaultId = -1;

		int mNumberOfCameras = Camera.getNumberOfCameras();
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < mNumberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
				defaultId = i;
				break;
			}
		}
		return defaultId;
	}

	// 检测摄像头硬件
	public static boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		}
		else {
			// no camera on this device
			return false;
		}
	}
	/**
	 * 依据屏幕尺寸，获取摄像头支持的最佳分辨率
	 * @param mContext
	 * @param listSize
	 * @return
	 */
	public static Size getBestSize(Context mContext, List<Size> listSize) {
		if (null == listSize || listSize.size() == 0) {
			return null;
		}
		int dX = DisplayUtil.getDispalyAbsWidth(mContext);
		int dY = DisplayUtil.getDispalyAbsHeight(mContext);
		float dPercent = dY * 1.0f / (dX * 1.0f);
		int dValue = getPercentValue(dPercent);

		ArrayList<Integer> listSizeValue = new ArrayList<Integer>();
		for (Size size : listSize) {
			int value = getCoeffBySize(size) + getCoeffByPercent(size, dValue);
			listSizeValue.add(value);
		}
		int dataSize = listSizeValue.size();
		int index = 0;
		int tempValue = listSizeValue.get(0);
		for (int i = 0; i < dataSize; i++) {
			int realValue = listSizeValue.get(i);
			if (Math.abs(realValue) < Math.abs(tempValue)) {
				index = i;
				tempValue = realValue;
			}
		}
		return listSize.get(index);
	}

	private static int getCoeffBySize(Size size) {
		int modeValue = 0;
		int sizeWidth = size.width;
		if (sizeWidth > 2700) {
			modeValue = 20;
		}
		else if (sizeWidth > 1920) {
			modeValue = 10;
		}
		else if (sizeWidth >= 960) {
			modeValue = 0;
		}
		else if (sizeWidth >= 800) {
			modeValue = -10;
		}
		else {
			modeValue = -20;
		}
		return modeValue;

	}

	private static int getCoeffByPercent(Size size, int dValue) {
		float sizePercent = size.width * 1.0f / (size.height * 1.0f);
		int sizeValue = getPercentValue(sizePercent);
		return Math.abs(sizeValue - dValue);
	}

	private static int getPercentValue(float modePercent) {
		float[] modeArrays = new float[] { 1.0f, 1.333f, 1.50f, 1.667f, 1.778f };
		int modeValue = 0;
		int modeSize = modeArrays.length;
		for (int i = 0; i < modeSize; i++) {
			if (i == modeSize - 1) {
				modeValue = i;
				break;
			}
			if (modePercent < (modeArrays[i] + modeArrays[i + 1]) / 2) {
				modeValue = i;
				break;
			}
		}
		return modeValue;
	}
}
