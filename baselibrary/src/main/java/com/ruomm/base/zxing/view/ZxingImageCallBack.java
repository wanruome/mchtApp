/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月25日 下午4:46:40 
 */
package com.ruomm.base.zxing.view;

import android.graphics.Bitmap;

import com.google.zxing.Result;

public interface ZxingImageCallBack {
	public void hanleDecodeGallery(Result result, Bitmap barcode);

	public void hanleDecodeGalleryError(String imagePath);

}
