/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月25日 上午11:45:06 
 */
package com.ruomm.base.zxing.decoding;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class DecodeBitmap {

	public static Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {

			return null;

		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0) {
			sampleSize = 1;
		}
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		// Bitmap scanBitmap = ImageUtils.getImage(path, 200);
		if (null == scanBitmap) {
			return null;
		}
		Result decoderResult = null;
		try {
			RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
			BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
			QRCodeReader reader = new QRCodeReader();
			decoderResult = reader.decode(bitmap1, hints);

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			decoderResult = null;
		}
		if (null != scanBitmap) {
			scanBitmap.recycle();
		}
		return decoderResult;

	}

}
