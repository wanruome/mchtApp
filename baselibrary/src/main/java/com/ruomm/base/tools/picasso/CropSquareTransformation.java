/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月15日 下午1:09:40 
 */
package com.ruomm.base.tools.picasso;

import android.content.Context;
import android.graphics.Bitmap;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.ImageUtils;
import com.squareup.picasso.Transformation;

public class CropSquareTransformation implements Transformation {
	private final int newWidth;
	private final int newHeight;
	private final boolean isAutoMax;

	public CropSquareTransformation(Context mContext, boolean isAutoMax) {
		this.newWidth = DisplayUtil.getDispalyWidth(mContext);
		this.newHeight = DisplayUtil.getDispalyHeightWithoutStatusBar(mContext);
		this.isAutoMax = isAutoMax;
	}

	public CropSquareTransformation(int scaleSize, boolean isAutoMax) {
		super();
		this.newWidth = scaleSize;
		this.newHeight = scaleSize;
		this.isAutoMax = isAutoMax;

	}

	public CropSquareTransformation(int newWidth, int newHeight, boolean isAutoMax) {
		super();
		this.newWidth = newWidth;
		this.newHeight = newHeight;
		this.isAutoMax = isAutoMax;
	}

	@Override
	public Bitmap transform(Bitmap source) {
		// int size = Math.min(source.getWidth(), source.getHeight());
		// int x = (source.getWidth() - size) / 2;
		// int y = (source.getHeight() - size) / 2;
		// Bitmap result = Bitmap.createBitmap(source, x, y, size, size);

		// Bitmap result = ImageUtils.scaleImageTo(source, scaleSize);
		// if (result != source) {
		// source.recycle();
		// }
		// return result;
		Bitmap result = ImageUtils.scaleImageFit(source, newWidth, newHeight, isAutoMax);
		if (result != source) {
			source.recycle();
		}
		return result;

	}

	@Override
	public String key() {
		return "square()";
	}
}
