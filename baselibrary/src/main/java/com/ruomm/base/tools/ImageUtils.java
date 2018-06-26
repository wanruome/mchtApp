package com.ruomm.base.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.ruomm.base.tools.imagecore.ImageRealSize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

@SuppressLint("NewApi")
public class ImageUtils {
	/**
	 * 图片等比例缩放
	 *
	 * @param org
	 *            源Bitmap
	 * @param newSize
	 *            新宽度和高度
	 * @param newWidth
	 *            新宽度
	 * @param newHeight
	 *            新高度
	 * @param isAutoMax
	 *            小图是否自动缩放为大图
	 * @return
	 */
	public static Bitmap scaleImageFit(Bitmap org, int newSize) {
		return scaleImageFit(org, newSize, newSize, true);
	}

	public static Bitmap scaleImageFit(Bitmap org, int newSize, boolean isAutoMax) {
		return scaleImageFit(org, newSize, newSize, isAutoMax);
	}

	public static Bitmap scaleImageFit(Bitmap org, int newWidth, int newHeight, boolean isAutoMax) {
		if (null == org || newWidth <= 0 || newHeight <= 0 || org.getWidth() <= 0 || org.getHeight() <= 0) {
			return org;
		}
		int x = org.getWidth();
		int y = org.getHeight();
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
			return scaleImageTo(org, newX, newY);
		}
		else {
			if (x <= newWidth && y <= newHeight) {
				return org;
			}
			else {
				return scaleImageTo(org, newX, newY);
			}
		}
	}

	/**
	 * 图片非等比例缩放
	 *
	 * @param org
	 *            源Bitmap
	 * @param newWidth
	 *            新宽度
	 * @param newHeight
	 *            新高度
	 * @return
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
		return scaleImageByMaxtrix(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
	}

	/**
	 * @param org
	 *            源图像
	 * @param scaleWidth
	 *            宽度Maxtix比例
	 * @param scaleHeight
	 *            高度Maxtix比例
	 * @return
	 */
	private static Bitmap scaleImageByMaxtrix(Bitmap org, float scaleWidth, float scaleHeight) {
		if (org == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
		if (org != bitmap) {
			org.recycle();
		}
		return bitmap;
	}

	/**
	 * 图片文件等比例缩放
	 *
	 * @param srcPath
	 *            源图片文件
	 * @param desPath
	 *            目标图片文件
	 * @param newSize
	 *            新宽度和高度
	 * @param newWidth
	 *            新宽度
	 * @param newHeight
	 *            新高度
	 * @param isAutoMax
	 *            小图是否自动缩放为大图
	 * @return
	 */

	public static boolean scaleImageFile(String srcPath, String desPath, int newSize) {
		return scaleImageFile(srcPath, desPath, newSize, newSize, true);
	}

	public static boolean scaleImageFile(String srcPath, String desPath, int newSize, boolean isAutoMax) {
		return scaleImageFile(srcPath, desPath, newSize, newSize, isAutoMax);
	}

	public static boolean scaleImageFile(String srcPath, String desPath, int rewidth, int reheight, boolean isAutoMax) {
		Bitmap bitmap = getImageFit(srcPath, rewidth, reheight, isAutoMax);
		if (null == bitmap) {
			return false;
		}
		boolean flag = FileUtils.saveBitmap(bitmap, desPath);
		if (null != bitmap) {
			bitmap.recycle();
		}
		return flag;
	}

	/**
	 * 从文件获取非等比例图像
	 *
	 * @param srcPath
	 *            源图片文件
	 * @param rewidth
	 *            新宽度
	 * @param reheight
	 *            新高度
	 * @return
	 */
	public static Bitmap getImage(String srcPath, int rewidth, int reheight) {
		Bitmap bitmap = getImageScale(srcPath, rewidth, reheight);
		if (null == bitmap) {
			return null;
		}
		return scaleImageTo(bitmap, rewidth, reheight);
	}

	/**
	 * 从文件获取等比例图像
	 *
	 * @param srcPath
	 *            源图片文件
	 * @param newSize
	 *            新宽度和高度
	 * @param newWidth
	 *            新宽度
	 * @param newHeight
	 *            新高度
	 * @param isAutoMax
	 *            小图是否自动缩放为大图
	 * @return
	 */
	public static Bitmap getImageFit(String srcPath, int newSize) {
		return getImageFit(srcPath, newSize, newSize, true);
	}

	public static Bitmap getImageFit(String srcPath, int newSize, boolean isAutoMax) {
		return getImageFit(srcPath, newSize, newSize, isAutoMax);
	}

	public static Bitmap getImageFit(String srcPath, int rewidth, int reheight, boolean isAutoMax) {
		Bitmap bmsource = getImageScale(srcPath, rewidth, reheight);
		if (null == bmsource) {
			return null;
		}
		Bitmap bm = scaleImageFit(bmsource, rewidth, reheight, isAutoMax);
		if (bmsource != bm) {
			bmsource.recycle();
		}
		return bm;
	}

	/**
	 * 简单获取图片 <br/>
	 * <ul>
	 * <li>获取的结果不是准确精度的图片，可能会大于或小于新宽度和新高度</li>
	 * <li>要获取准确精度图片 ，需要调用getImageFit或者getImage方法</li>
	 * </ul>
	 *
	 * @param srcPath
	 *            源图片文件
	 * @param rewidth
	 *            新宽度
	 * @param reheight
	 *            新高度
	 * @return
	 */

	private static Bitmap getImageScale(String srcPath, int rewidth, int reheight) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(srcPath, options);
			int x = options.outWidth;
			int y = options.outHeight;
			if (x <= 0 || y <= 0) {
				return null;
			}
			int scale = 1;
			if (x <= reheight && y <= reheight) {
				scale = 1;
			}
			else {
				float realValue = x * 1.0f / (y * 1.0f);
				float desValue = rewidth * 1.0f / (reheight * 1.0f);
				if (realValue >= desValue) {
					scale = x / rewidth;
				}
				else {
					scale = y / reheight;
				}
			}
			options.inJustDecodeBounds = false;
			options.inSampleSize = scale;
			return BitmapFactory.decodeFile(srcPath, options);

		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 图片压缩
	 *
	 * @param image
	 *            源图片
	 * @param kbValue
	 *            压缩目标大小
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		return compressImage(image, 300);
	}

	public static Bitmap compressImage(Bitmap image, int kbValue) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kbValue) { // 循环判断如果压缩后图片是否大于kbValue（kb）,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
			if (options < 15) {
				break;
			}
		}
		if (image != null) {
			image.recycle();
			image = null;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 获取圆角图片
	 *
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 从assets目录中获取图片资源
	 *
	 * @param mContext
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context mContext, String fileName) {
		Bitmap image = null;
		AssetManager am = mContext.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		}
		catch (IOException e) {
			image = null;
		}
		return image;
	}

	/**
	 * 判定是否为图像文件
	 *
	 * @param path
	 *            图像文件路径
	 * @param minSize
	 *            最小图像文件大小
	 * @return
	 */
	public static boolean isImage(String path) {
		return isImage(path, 1);

	}

	public static boolean isImage(String path, int minSize) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		try {
			File file = new File(path);
			if (!file.exists()) {
				file = null;
				return false;
			}
			file = null;
			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inJustDecodeBounds = true;

			BitmapFactory.decodeFile(path, options);
			int calsize = minSize;
			if (minSize < 1) {
				calsize = 1;
			}
			else {
				calsize = minSize;
			}
			if (options.outHeight >= calsize && options.outWidth >= calsize) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {

			return false;
		}

	}

	/**
	 * 获取图像真实尺寸
	 *
	 * @param path
	 *            图像文件路径
	 * @return
	 */

	public static ImageRealSize calImageSize(String path) {
		ImageRealSize imageRealSize = new ImageRealSize();
		if (TextUtils.isEmpty(path)) {
			return imageRealSize;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);
		if (options.outHeight > 0 && options.outWidth > 0) {
			imageRealSize.width = options.outWidth;
			imageRealSize.height = options.outHeight;
		}
		return imageRealSize;

	}

	/**
	 * 保存小图片
	 *
	 * @param srcPath
	 *            源图片文件
	 * @param desDirPath
	 *            目标图片文件夹
	 * @param width
	 *            目标宽度
	 * @param height
	 *            目标高度
	 * @return
	 */

	public static String SavethumbnailImages(String srcPath, String desDirPath, int width, int height) {

		File file = FileUtils.createDir(desDirPath);
		if (null == file) {
			return "";
		}
		String filePath = file.getPath() + File.separator + FileUtils.getFileNameWithoutExtension(srcPath) + "_s"
				+ ".jpg";

		Bitmap bm = getImage(srcPath, width, height);
		if (FileUtils.saveBitmap(bm, filePath)) {
			return filePath;
		}
		else {
			return "";
		}
	}

	public static String SavethumbnailImagesFit(String srcPath, String desDirPath, int newSize) {
		File file = FileUtils.createDir(desDirPath);
		if (null == file) {
			return "";
		}
		String filePath = file.getPath() + File.separator + FileUtils.getFileNameWithoutExtension(srcPath) + "_s"
				+ ".jpg";

		Bitmap bm = getImageFit(srcPath, newSize, true);
		if (FileUtils.saveBitmap(bm, filePath)) {
			if (null != bm) {
				bm.recycle();
			}
			return filePath;
		}
		else {
			if (null != bm) {
				bm.recycle();
			}
			return "";
		}
	}

	public static String SavethumbnailImagesFit(String srcPath, String desDirPath, int newWidth, int newHeight) {
		File file = FileUtils.createDir(desDirPath);
		if (null == file) {
			return "";
		}
		String filePath = file.getPath() + File.separator + FileUtils.getFileNameWithoutExtension(srcPath) + "_s"
				+ ".jpg";

		Bitmap bm = getImageFit(srcPath, newWidth, newWidth, true);
		if (FileUtils.saveBitmap(bm, filePath)) {
			if (null != bm) {
				bm.recycle();
			}
			return filePath;
		}
		else {
			if (null != bm) {
				bm.recycle();
			}
			return "";
		}
	}
}
