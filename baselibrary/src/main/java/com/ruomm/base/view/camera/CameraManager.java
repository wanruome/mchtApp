/**
 * Camera管理类
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年5月12日 上午10:05:29 
 */
package com.ruomm.base.view.camera;

import com.ruomm.base.tools.CameraUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.view.SurfaceHolder;

public final class CameraManager {
	private static CameraManager cameraManager;
	private final Context context;
	private Camera camera;
	private final boolean isAutoFocus;
	private boolean initialized = false;
	private boolean previewing = false;
	private final CameraConfigurationManager configManager;
	private final int SDK_INT;

	private CameraManager(Context context, boolean isAutoFocus) {
		super();
		this.context = context;
		this.configManager = new CameraConfigurationManager(context);

		SDK_INT = android.os.Build.VERSION.SDK_INT;
		if (SDK_INT >= 14) {
			this.isAutoFocus = isAutoFocus;
		}
		else {
			this.isAutoFocus = false;
		}
	}
	/**
	 * 初始化摄像头，默认自动对焦
	 * @param context
	 */
	public static void init(Context context) {
		if (cameraManager == null) {
			cameraManager = new CameraManager(context, true);
		}
	}
	/**
	 * 初始化摄像头
	 * @param context
	 * @param isAutoFucus 是否自动对焦
	 */
	public static void init(Context context, boolean isAutoFucus) {
		if (cameraManager == null) {
			cameraManager = new CameraManager(context, isAutoFucus);

		}
	}
	/**
	 * 获取摄像头管理对象
	 * @return
	 */
	public static CameraManager get() {
		return cameraManager;
	}
	/**
	 * 打开摄像头
	 * @param holder SurfalceHolder
	 * @param isBackCamera 是否后置摄像头
	 */
	public void openDriver(SurfaceHolder holder, boolean isBackCamera) {
		if (SDK_INT >= 9) {
			OpenDriverNew(holder, isBackCamera);
		}
		else {
			openDriverOld(holder);
		}
	}

	private void openDriverOld(SurfaceHolder holder) {
		try {
			if (camera == null) {
				camera = Camera.open();

				camera.setPreviewDisplay(holder);

				if (!initialized) {
					initialized = true;
					configManager.initFromCameraParameters(camera);
				}
				configManager.setDesiredCameraParameters(camera);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			camera = null;
			initialized = false;
		}
	}

	@SuppressLint("NewApi")
	private void OpenDriverNew(SurfaceHolder holder, boolean isBackCamera) {
		try {
			if (camera == null) {
				if (isBackCamera) {
					int id = CameraUtil.getCameraIDBack();
					if (id < 0) {
						return;
					}
					camera = Camera.open(id);
				}
				else {
					int id = CameraUtil.getCameraIDFront();
					if (id < 0) {
						return;
					}
					camera = Camera.open(id);
				}

				camera.setPreviewDisplay(holder);

				if (!initialized) {
					initialized = true;
					configManager.initFromCameraParameters(camera);
				}
				configManager.setDesiredCameraParameters(camera);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			camera = null;
			initialized = false;
		}
	}
	/**
	 * 关闭摄像头
	 */
	public void closeDriver() {
		stopPreview();
		initialized = false;
		if (camera != null) {
			lightOff();
			camera.release();
			camera = null;
		}
	}
	/**
	 * 拍照
	 * @param shutter 时间回调
	 * @param jpeg 拍照回调
	 */
	public void takePicture(ShutterCallback shutter, PictureCallback jpeg) {
		if (null != camera && previewing) {
			try {
				previewing = false;
				camera.takePicture(shutter, null, jpeg);

			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	/**
	 * 预览模式开启
	 */
	public void startPreview() {
		if (camera != null && !previewing) {
			setAutoFocus();
			camera.startPreview();
			if (isAutoFocus) {
				camera.cancelAutoFocus();
			}
			else {
			}
			previewing = true;
		}
	}
	/**
	 * 设置自动对焦
	 */
	private void setAutoFocus() {
		if (null != camera) {
			try {
				Parameters parameters = camera.getParameters();
				if (isAutoFocus) {
					parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
				}
				else {
					parameters.setFocusMode(Parameters.FLASH_MODE_ON);
				}

				camera.setParameters(parameters);
			}
			catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/**
	 * Tells the camera to stop drawing preview frames.
	 */
	public void stopPreview() {
		if (camera != null && previewing) {
			camera.autoFocus(null);
			camera.stopPreview();
			previewing = false;
		}
	}

	public Context getContext() {
		return context;
	}

	// 闪光灯的功能开启
	public void lightOpen() {
		if (camera != null) {
			Parameters parameter = camera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameter);
		}
	}

	// 闪光灯的功能关闭
	public void lightOff() {
		if (camera != null) {
			Parameters parameter = camera.getParameters();
			parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameter);
		}
	}

}
