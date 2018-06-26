/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruomm.base.view.camera;

import java.util.List;
import java.util.regex.Pattern;

import com.ruomm.base.tools.CameraUtil;
import com.ruomm.base.tools.DisplayUtil;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;

final class CameraConfigurationManager {

	private static final String TAG = CameraConfigurationManager.class.getSimpleName();

	private static final int TEN_DESIRED_ZOOM = 27;
	private static final int DESIRED_SHARPNESS = 30;

	private static final Pattern COMMA_PATTERN = Pattern.compile(",");

	private final Context context;
	private final Point screenResolution;
	private Point cameraResolution;
	private int previewFormat;
	// private String previewFormatString;
	private final int SDK_INT;
	private Size picSize;

	CameraConfigurationManager(Context context) {
		this.context = context;
		int sdkInt;
		try {
			sdkInt = Build.VERSION.SDK_INT;
		}
		catch (NumberFormatException nfe) {
			// Just to be safe
			sdkInt = 10000;
		}
		this.SDK_INT = sdkInt;
		screenResolution = new Point(DisplayUtil.getDispalyWidth(context), DisplayUtil.getDispalyHeight(context));

	}

	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		List<Size> listSizePic = parameters.getSupportedPictureSizes();
		List<Size> listSizePreview = parameters.getSupportedPreviewSizes();
		Size sizePre = CameraUtil.getBestSize(context, listSizePreview);
		picSize = CameraUtil.getBestSize(context, listSizePic);
		previewFormat = parameters.getPreviewFormat();
		if (null == sizePre) {
			// String previewFormatString = parameters.get("preview-format");
			Point screenResolutionForCamera = new Point();
			// preview size is always something like 480*320, other 320*480
			if (screenResolution.x < screenResolution.y) {
				screenResolutionForCamera.x = screenResolution.y;
				screenResolutionForCamera.y = screenResolution.x;
			}
			else {
				screenResolutionForCamera.x = screenResolution.x;

				screenResolutionForCamera.y = screenResolution.y;
			}
			cameraResolution = getCameraResolution(parameters, screenResolutionForCamera);
		}
		else {
			// for (Size size : listSizePic) {
			// MLog.i("支持", size.width + ":" + size.height);
			//
			// }
			// for (Size size : listSizePreview) {
			// MLog.i("预览", size.width + ":" + size.height);
			//
			// }
			// MLog.i("最佳的支持", sizePre.width + ":" + sizePre.height);
			// MLog.i("最佳的预览", picSize.width + ":" + picSize.height);
			// MLog.i("计算时间", System.currentTimeMillis() - time);
			cameraResolution = new Point();
			cameraResolution.x = sizePre.width;
			cameraResolution.y = sizePre.height;

		}

	}

	/**
	 * Sets the camera up to take preview images which are used for both preview and decoding. We
	 * detect the preview format here so that buildLuminanceSource() can build an appropriate
	 * LuminanceSource subclass. In the future we may want to force YUV420SP as it's the smallest,
	 * and the planar Y can be used for barcode scanning without a copy in some cases.
	 */

	void setDesiredCameraParameters(Camera camera) {

		Camera.Parameters parameters = camera.getParameters();
		// Log.d(TAG, "Setting preview size: " + cameraResolution);
		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		if (null != picSize) {
			parameters.setPictureSize(picSize.width, picSize.height);
		}
		setFlash(parameters);
		setZoom(parameters);
		// setSharpness(parameters);
		// modify here
		// 设置竖屏 camera.setDisplayOrientation(90);
		if (SDK_INT >= 8) {
			camera.setDisplayOrientation(90);
		}
		else {
			if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				parameters.set("orientation", "portrait");
				parameters.set("rotation", 90);
			}
			if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				parameters.set("orientation", "landscape");
				parameters.set("rotation", 90);
			}
		}
		camera.setParameters(parameters);

	}

	Point getCameraResolution() {
		return cameraResolution;
	}

	Point getScreenResolution() {
		return screenResolution;
	}

	int getPreviewFormat() {
		return previewFormat;
	}

	// String getPreviewFormatString() {
	// return previewFormatString;
	// }

	private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {

		String previewSizeValueString = parameters.get("preview-size-values");
		// saw this on Xperia
		if (previewSizeValueString == null) {
			previewSizeValueString = parameters.get("preview-size-value");
		}

		Point cameraResolution = null;

		if (previewSizeValueString != null) {
			// Log.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
			cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
		}

		if (cameraResolution == null) {
			// Ensure that the camera resolution is a multiple of 8, as the
			// screen may not be.
			cameraResolution = new Point(screenResolution.x >> 3 << 3, screenResolution.y >> 3 << 3);
			// cameraResolution = new Point(screenResolution.x,
			// screenResolution.y);
		}

		return cameraResolution;
	}

	//
	private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
		int bestX = 0;
		int bestY = 0;
		int diff = Integer.MAX_VALUE;
		for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {

			previewSize = previewSize.trim();
			int dimPosition = previewSize.indexOf('x');
			if (dimPosition < 0) {
				// Log.w(TAG, "Bad preview-size: " + previewSize);
				continue;
			}

			int newX;
			int newY;
			try {
				newX = Integer.parseInt(previewSize.substring(0, dimPosition));
				newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
			}
			catch (NumberFormatException nfe) {
				// Log.w(TAG, "Bad preview-size: " + previewSize);
				continue;
			}

			int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
			if (newDiff == 0) {
				bestX = newX;
				bestY = newY;
				break;
			}
			else if (newDiff < diff) {
				bestX = newX;
				bestY = newY;
				diff = newDiff;
			}

		}

		if (bestX > 0 && bestY > 0) {
			return new Point(bestX, bestY);
		}
		return null;
	}

	private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
		int tenBestValue = 0;
		for (String stringValue : COMMA_PATTERN.split(stringValues)) {
			stringValue = stringValue.trim();
			double value;
			try {
				value = Double.parseDouble(stringValue);
			}
			catch (NumberFormatException nfe) {
				return tenDesiredZoom;
			}
			int tenValue = (int) (10.0 * value);
			if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom - tenBestValue)) {
				tenBestValue = tenValue;
			}
		}
		return tenBestValue;
	}

	private void setFlash(Camera.Parameters parameters) {
		if (Build.MODEL.contains("Behold II") && SDK_INT == 3) { // 3
			parameters.set("flash-value", 1);
		}
		else {
			parameters.set("flash-value", 2);
		}
		parameters.set("flash-mode", "off");
	}

	private void setZoom(Camera.Parameters parameters) {

		String zoomSupportedString = parameters.get("zoom-supported");
		if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
			return;
		}

		int tenDesiredZoom = TEN_DESIRED_ZOOM;

		String maxZoomString = parameters.get("max-zoom");
		if (maxZoomString != null) {
			try {
				int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			}
			catch (NumberFormatException nfe) {
				// Log.w(TAG, "Bad max-zoom: " + maxZoomString);
			}
		}

		String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
		if (takingPictureZoomMaxString != null) {
			try {
				int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
				if (tenDesiredZoom > tenMaxZoom) {
					tenDesiredZoom = tenMaxZoom;
				}
			}
			catch (NumberFormatException nfe) {
				// Log.w(TAG, "Bad taking-picture-zoom-max: " + takingPictureZoomMaxString);
			}
		}

		String motZoomValuesString = parameters.get("mot-zoom-values");
		if (motZoomValuesString != null) {
			tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
		}

		String motZoomStepString = parameters.get("mot-zoom-step");
		if (motZoomStepString != null) {
			try {
				double motZoomStep = Double.parseDouble(motZoomStepString.trim());
				int tenZoomStep = (int) (10.0 * motZoomStep);
				if (tenZoomStep > 1) {
					tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
				}
			}
			catch (NumberFormatException nfe) {
				// continue
			}
		}

		// Set zoom. This helps encourage the user to pull back.
		// Some devices like the Behold have a zoom parameter
		if (maxZoomString != null || motZoomValuesString != null) {
			parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
		}

		// Most devices, like the Hero, appear to expose this zoom parameter.
		// It takes on values like "27" which appears to mean 2.7x zoom
		if (takingPictureZoomMaxString != null) {
			parameters.set("taking-picture-zoom", tenDesiredZoom);
		}
	}

	public static int getDesiredSharpness() {
		return DESIRED_SHARPNESS;
	}

}
