/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年2月4日 上午10:47:55
 */
package com.ruomm.base.tools;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * android的Asset文件夹文件工具类
 * @author Ruby
 *
 */
public class AssetsUtil {
	/**
	 * 依据Asset文件夹里的文件名称获取文件路径
	 * @param filename
	 * @return
	 */
	public static String getPath(String filename) {
		String path = "file:///android_asset/" + filename;
		return path;
	}
	/**
	 * 获取Asset文件夹里的一个文件的输入流
	 * @param mContext
	 * @param fileName
	 * @return 输入流
	 */
	public static InputStream getInputStream(Context mContext, String fileName) {
		try {

			InputStream is = mContext.getAssets().open(fileName);
			return is;
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取Asset文件夹里的一个文件的文本内容
	 * @param mContext
	 * @param fileName
	 * @return 文本内容
	 */
	public static String getString(Context mContext, String fileName) {
		try {

			InputStream is = mContext.getAssets().open(fileName);
			String string = FileUtils.readInputStream(is);
			return string;
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取Asset文件夹里的一个文件的图片Bitmap
	 * @param mContext
	 * @param fileName
	 * @return 图片Bitmap
	 */
	public static Bitmap getImage(Context mContext, String fileName) {
		Bitmap image = null;
		try {
			InputStream is = mContext.getAssets().open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (Exception e) {
			image = null;
		}
		return image;
	}
}
