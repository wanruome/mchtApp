/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2014-11-18 上午11:45:41 
 */
package com.ruomm.base.tools.imagecore;

/**
 * 计算ImageRealSize宽度高度比例不变情况下缩放后的尺寸类
 * 
 * @author Ruby
 */
public class ImageResize {
	/**
	 * 缩放后的宽度
	 */
	public int width;
	/**
	 * 缩放后的高度
	 */
	public int height;

	/**
	 * 缩放图片,小于新尺寸会自动把尺寸调整到新尺寸
	 * 
	 * @param imageRealSize
	 *            原始尺寸
	 * @param newsize
	 *            新的图片宽度和宽度最大值
	 */
	public ImageResize(ImageRealSize imageRealSize, int newsize) {
		this(imageRealSize, newsize, newsize, true);
	}

	/**
	 * 缩放图片,小于新尺寸会自动把尺寸调整到新尺寸
	 * 
	 * @param imageRealSize
	 *            原始尺寸
	 * @param newWidth
	 *            新的图片宽度最大值
	 * @param newHeight
	 *            新的图片高度最大值
	 */
	public ImageResize(ImageRealSize imageRealSize, int newWidth, int newHeight) {
		this(imageRealSize, newWidth, newHeight, true);
	}

	/**
	 * 缩放图片
	 * 
	 * @param imageRealSize
	 *            原始尺寸
	 * @param newWidth
	 *            新的图片宽度最大值
	 * @param newHeight
	 *            新的图片高度最大值
	 * @param isAutoMax
	 *            是否自动放大图片，自动放大则图片小于新尺寸会自动把尺寸调整到新尺寸
	 */
	public ImageResize(ImageRealSize imageRealSize, int newWidth, int newHeight, boolean isAutoMax) {
		super();
		if (null == imageRealSize || !imageRealSize.isTrueImage()) {
			this.width = 0;
			this.height = 0;
		}
		else if (newWidth <= 0 || newHeight <= 0) {
			this.width = imageRealSize.width;
			this.height = imageRealSize.height;
		}
		else {
			int x = imageRealSize.width;
			int y = imageRealSize.height;
			int newX = 0;
			int newY = 0;
			float realValue = x * 1.0f / (y * 1.0f);
			float desValue = newWidth * 1.0f / (newHeight * 1.0f);
			if (realValue >= desValue) {
				newX = newWidth;
				newY = newWidth * y / x;
				if (newY == 0) {
					newY = y;
				}
			}
			else {
				newY = newHeight;
				newX = newHeight * x / y;
				if (newX == 0) {
					newX = x;
				}
			}
			if (isAutoMax) {
				this.width = newX;
				this.height = newY;
			}
			else {
				if (x <= newWidth && y <= newHeight) {
					this.width = x;
					this.height = y;
				}
				else {
					this.width = newX;
					this.height = newY;
				}
			}
		}
	}

}
