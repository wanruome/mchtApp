/**
 *	@copyright 亿康通-2015  
 * 	@author wanruome  
 * 	@create 2014-11-18 上午11:49:35 
 */
package com.ruomm.base.tools.imagecore;

/**
 * Image尺寸类
 * 
 * @author Ruby
 */
public class ImageRealSize {
	/**
	 * 图片宽度
	 */
	public int width;
	/**
	 * 图片高度
	 */
	public int height;

	/**
	 * 是否为图片，标准为宽和高都至少1个像素
	 * 
	 * @return 是否图片
	 */
	public boolean isTrueImage() {
		if (width < 1 || height < 1) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * 判断图片是否符合最小尺寸
	 * 
	 * @param minSize
	 *            最小的高度和宽度尺寸
	 * @return 图片是否符合最小尺寸
	 */
	public boolean isTrueImage(int minSize) {
		return isTrueImage(minSize, minSize);
	}

	/**
	 * 判断图片是否符合最小尺寸
	 * 
	 * @param minWidth
	 *            最小的宽度
	 * @param minHeight
	 *            最小的高度
	 * @return 图片是否符合最小尺寸
	 */
	public boolean isTrueImage(int minWidth, int minHeight) {
		if (width < 1 || height < 1) {
			return false;
		}
		if (width < minWidth) {
			return false;
		}
		else if (height < minHeight) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * 判断图片是否符合最小像素面积
	 * 
	 * @param minSquare
	 *            图片的最小像素面积
	 * @return 图片是否符合最小像素面积
	 */
	public boolean isTrueSquareImage(int minSquare) {
		if (width < 1 || height < 1) {
			return false;
		}
		if (width * height > minSquare) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 获取图片面积
	 * 
	 * @return 图片的像素面积
	 */
	public int getImageSquare() {
		return width * height;
	}
}
