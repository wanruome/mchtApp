package com.ruomm.base.zxing.encoding;

import java.io.File;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.FileUtils;

/**
 * @author Ryan Tang
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;

	public static File getQRcodeFile(Context mContext, String zxing_contentString) {
		return getQRcodeFile(mContext, zxing_contentString, null);
	}

	public static File getQRcodeFile(Context mContext, String zxing_contentString,
			ErrorCorrectionLevel mErrorCorrectionLevel) {
		if (null == mErrorCorrectionLevel) {
			File fileqrCode = FileUtils.createFileInContext(mContext, "cache" + File.separator + "zxing_"
					+ EncryptUtils.EncodingMD5(zxing_contentString) + ".png");
			return fileqrCode;
		}
		else {
			File fileqrCode = FileUtils.createFileInContext(
					mContext,
					"cache" + File.separator + "zxing" + String.valueOf(mErrorCorrectionLevel) + "_"
							+ EncryptUtils.EncodingMD5(zxing_contentString) + ".png");
			return fileqrCode;
		}
	}

	public static Bitmap createQRCode(String str, int widthAndHeight, ErrorCorrectionLevel errorCorrectionLevel)
			throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,
				hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
		return createQRCode(str, widthAndHeight, ErrorCorrectionLevel.M);
	}
}
