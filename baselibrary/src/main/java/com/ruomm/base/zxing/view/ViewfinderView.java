/*
 * Copyright (C) 2008 ZXing authors
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

package com.ruomm.base.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.ruomm.base.zxing.camera.CameraManager;
import com.ruomm.R;
import com.google.zxing.ResultPoint;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 * �Զ����View������ʱ�м���ʾ��
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192, 128, 64 };
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;
	private final int borderSize;
	private final int cornerLenght;
	private final int cornerStrokeSize;
	private final int cornerColor;
	// private final int laserSize;
	// private final int laserPassColor;
	private final int laserSize;
	private int larseStep;
	private int larsePosition = 0;
	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	// private final int laserColor;
	private final int resultPointColor;
	// private final int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;
	private final Drawable lineDrawable;
	private final Rect lineRect;
	private boolean isShowScanRect = true;

	public void setShowScanRect(boolean isShowScanRect) {
		this.isShowScanRect = isShowScanRect;
	}

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.zxing_viewfinder_mask);
		resultColor = resources.getColor(R.color.zxing_result_view);
		frameColor = resources.getColor(R.color.zxing_viewfinder_frame);
		// laserColor = resources.getColor(R.color.zxing_viewfinder_laser);
		// laserPassColor = resources.getColor(R.color.zxing_viewfinder_laserPass);
		resultPointColor = resources.getColor(R.color.zxing_possible_result_points);
		laserSize = resources.getDimensionPixelSize(R.dimen.zxing_laser_size);
		larseStep = resources.getDimensionPixelSize(R.dimen.zxing_laser_step);
		if (larseStep <= 0) {
			float scale = context.getResources().getDisplayMetrics().density;
			larseStep = (int) (3 * scale + 0.5f);
		}
		borderSize = resources.getDimensionPixelSize(R.dimen.zxing_border_size);
		cornerLenght = resources.getDimensionPixelSize(R.dimen.zxing_corner_lenght);
		cornerStrokeSize = resources.getDimensionPixelSize(R.dimen.zxing_corner_strokesize);
		cornerColor = resources.getColor(R.color.zxing_corner_color);
		possibleResultPoints = new HashSet<ResultPoint>(5);
		lineDrawable = getResources().getDrawable(R.drawable.zxing_barcode_laser);
		lineRect = new Rect();
		setShowScanRect(true);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		if (isShowScanRect) {
			canvas.drawRect(0, 0, width, frame.top, paint);
			canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
			canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
			canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		}

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		}

		else if (isShowScanRect) {

			// Draw a two pixel solid black border inside the framing rect
			paint.setColor(frameColor);
			// 上边框
			canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + borderSize, paint);
			// 下边框
			canvas.drawRect(frame.left, frame.bottom + 1 - borderSize, frame.right + 1, frame.bottom + 1, paint);
			// 左边框
			canvas.drawRect(frame.left, frame.top + borderSize, frame.left + borderSize, frame.bottom + 1 - borderSize,
					paint);
			canvas.drawRect(frame.right + 1 - borderSize, frame.top + borderSize, frame.right + 1, frame.bottom + 1
					- borderSize, paint);

			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			// 扫描线绘制
			// int laserOffSet = laserSize / 2 + 1;
			// larsePosition = larsePosition + larseStep;
			// if (larsePosition > frame.height() - laserOffSet - borderSize) {
			// larsePosition = laserOffSet + borderSize;
			// }
			// else if (larsePosition < laserOffSet + borderSize) {
			// larsePosition = laserOffSet + borderSize;
			// }
			// int middle = frame.top + larsePosition;
			// int middleTop = middle - laserSize / 2;
			// int middleBottom = middleTop + laserSize;
			// // 扫描线绘制(颜色方法)
			// // paint.setColor(laserColor);
			// // paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			// // scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			// // canvas.drawRect(frame.left + borderSize, middleTop, frame.right - borderSize + 1,
			// // middleBottom, paint);
			// // 扫描线绘制(图像方法)
			// // lineRect.set(frame.left + borderSize, frame.top + borderSize, frame.right -
			// // borderSize + 1, middleBottom);
			// lineDrawable.setBounds(lineRect);
			// lineDrawable.draw(canvas);
			//
			// // 扫描线扫过的区域颜色
			// paint.setColor(laserPassColor);
			// canvas.drawRect(frame.left + borderSize, frame.top + borderSize, frame.right -
			// borderSize, middleTop, paint);
			// 绘制扫描线
			int laserOffSet = laserSize / 2;
			larsePosition = larsePosition + larseStep;
			if (larsePosition > frame.height() - laserOffSet) {
				larsePosition = laserOffSet + borderSize;
			}
			else if (larsePosition < laserOffSet + borderSize) {
				larsePosition = laserOffSet + borderSize;
			}
			int middle = frame.top + larsePosition;
			lineRect.set(frame.left + borderSize, frame.top + borderSize, frame.right - borderSize + 1, middle);
			lineDrawable.setBounds(lineRect);
			lineDrawable.draw(canvas);

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			}
			else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
				}
			}
			// 绘制4个角
			paint.setColor(cornerColor);

			// 左上角
			canvas.drawRect(frame.left, frame.top, frame.left + cornerLenght, frame.top + cornerStrokeSize, paint);

			canvas.drawRect(frame.left, frame.top, frame.left + cornerStrokeSize, frame.top + cornerLenght, paint);

			// 右上角

			canvas.drawRect(frame.right + 1 - cornerLenght, frame.top, frame.right + 1, frame.top + cornerStrokeSize,
					paint);

			canvas.drawRect(frame.right + 1 - cornerStrokeSize, frame.top, frame.right + 1, frame.top + cornerLenght,
					paint);

			// 左下角

			canvas.drawRect(frame.left, frame.bottom + 1 - cornerStrokeSize, frame.left + cornerLenght,
					frame.bottom + 1, paint);

			canvas.drawRect(frame.left, frame.bottom + 1 - cornerLenght, frame.left + cornerStrokeSize,
					frame.bottom + 1, paint);

			// 右下角

			canvas.drawRect(frame.right + 1 - cornerLenght, frame.bottom + 1 - cornerStrokeSize, frame.right + 1,
					frame.bottom + 1, paint);

			canvas.drawRect(frame.right + 1 - cornerStrokeSize, frame.bottom + 1 - cornerLenght, frame.right + 1,
					frame.bottom + 1, paint);

			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	public void recycleLineDrawable() {
		if (lineDrawable != null) {
			lineDrawable.setCallback(null);
		}
	}

}
