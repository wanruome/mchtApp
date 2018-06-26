/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月12日 上午11:02:59 
 */
package com.ruomm.base.view.upimg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.ImageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

public class UpImgHelper {
	public static final String TAG_IMAGEBUCKET = "tag_ImageBucket";
	public static final String TAG_SELECTED_IMAGES = "tag_selected_images";
	public static final String ERROE_TAKE_PICK = "手机没有SD卡，拍照失败！";
	public static final String PATH_CACHE_COMPRESS = "files" + File.separator + "cache_compress";
	public static final String PATH_TAKE_PICTURE = "picture";
	public static final int RequestCode_TAKE_PICTURE = 201;
	public static final int RequestCode_GET_PICTURES = 202;
	public static final int RequestCode_CROP_IMAGE = 203;
	public static final int SIZE_COMPRESS = 360;
	public static final int SIZE_CROP_WIDTH = 320;
	public static final int SIZE_CROP_HEIGHT = 320;
	public static final int SIZE_PICASSO_LOAD_SIZE = 160;
	public static final int SIZE_PICASSO_LOAD_WIDTH = 180;
	public static final int SIZE_PICASSO_LOAD_HEIGHT = 120;

	/**
	 * 变量声明
	 */
	private int totalCount = 9;
	public List<String> drr = new ArrayList<String>();
	private String path_compress;
	private String path_takepic;
	private int size_compress;
	private int size_crop_x;
	private int size_crop_y;
	public boolean isCropImg = false;
	private String takePicImagePath;
	private static UpImgHelper mUpImgHelper;
	private Object upTag;

	private UpImgHelper() {
		super();
	}

	public static UpImgHelper getInstance() {
		if (null == mUpImgHelper) {
			mUpImgHelper = new UpImgHelper();
		}
		return mUpImgHelper;
	}

	public void setTag(Object tag) {
		this.upTag = tag;
	}

	public Object getTag() {
		return this.upTag;
	}

	public void delCompressFiles() {
		FileUtils.delAllFile(path_compress);
	}

	public static void closeInstance() {
		if (null != mUpImgHelper) {
			mUpImgHelper.delCompressFiles();
			mUpImgHelper.drr.clear();
		}
		mUpImgHelper = null;
	}

	public UpImgHelper initialize(Context mContext, int countTotal, int compressSize) {
		return initialize(countTotal, compressSize,
				FileUtils.getPathContext(mContext, UpImgHelper.PATH_CACHE_COMPRESS),
				FileUtils.getPathExternalFile(mContext, UpImgHelper.PATH_TAKE_PICTURE));

		// initialize(countTotal, compressSize, FileUtils.getPathContext(mContext,
		// UpImgHelper.PATH_CACHE_COMPRESS),
		// FileUtils.getPathExternalStorage(UpImgHelper.PATH_TAKE_PICTURE));
	}

	public UpImgHelper initialize(int countTotal, int compressSize, String compress_Path, String takepic_Path) {
		this.upTag = null;
		if (null == drr) {
			drr = new ArrayList<String>();
		}
		else {
			drr.clear();
		}
		if (compressSize <= 0) {
			size_compress = SIZE_COMPRESS;
		}
		else {
			size_compress = compressSize;
		}
		path_compress = compress_Path;
		path_takepic = takepic_Path;
		FileUtils.createDir(path_compress);
		if (null == FileUtils.createDir(path_takepic)) {
			path_takepic = null;
		}
		// FileUtils.delAllFile(compress_Path);
		totalCount = countTotal;
		isCropImg = false;
		return this;
	}

	public UpImgHelper initialize(Context mContext, boolean isCrop, int cropsize) {
		return initialize(isCrop, cropsize, FileUtils.getPathContext(mContext, UpImgHelper.PATH_CACHE_COMPRESS),
				FileUtils.getPathExternalFile(mContext, UpImgHelper.PATH_TAKE_PICTURE));

	}

	public UpImgHelper initialize(boolean isCrop, int cropsize, String compress_Path, String takepic_Path) {
		this.upTag = null;
		if (null == drr) {
			drr = new ArrayList<String>();
		}
		else {
			drr.clear();
		}
		if (isCrop) {
			if (cropsize <= 0) {
				size_crop_x = SIZE_CROP_WIDTH;
				size_crop_y = SIZE_CROP_HEIGHT;
			}
			else {
				size_crop_x = cropsize;
				size_crop_y = cropsize;
			}
		}
		else {
			if (cropsize <= 0) {
				size_compress = SIZE_COMPRESS;

			}
			else {
				size_compress = cropsize;
			}
		}
		path_compress = compress_Path;
		path_takepic = takepic_Path;
		FileUtils.createDir(path_compress);
		// FileUtils.delAllFile(path_compress);
		if (null == FileUtils.createDir(path_takepic)) {
			path_takepic = null;
		}
		totalCount = 1;
		isCropImg = isCrop;
		return this;

	}

	public String getTakePicImagePath(boolean isNewTakePhoto) {
		if (isNewTakePhoto) {
			if (TextUtils.isEmpty(path_takepic)) {
				this.takePicImagePath = null;
				return null;
			}
			else {
				this.takePicImagePath = path_takepic + File.separator + FileUtils.getFilenameByTime("img_", ".jpg");
				return takePicImagePath;
			}
		}
		else {
			return this.takePicImagePath;
		}
	}

	public void compressListImage(ArrayList<String> listImgdrrs) {
		drr.clear();
		if (TextUtils.isEmpty(path_compress) || isCropImg) {
			for (String pathImg : listImgdrrs) {
				if (pathImg.startsWith("http")) {
					drr.add(pathImg);
					continue;
				}
				if (ImageUtils.isImage(pathImg, 6)) {
					drr.add(pathImg);
				}
			}
			return;
		}
		for (String pathImg : listImgdrrs) {
			if (pathImg.startsWith("http")) {
				drr.add(pathImg);
				continue;
			}
			String pathScale = path_compress + File.separator + EncryptUtils.EncodingMD5(pathImg) + ".jpg";
			if (ImageUtils.isImage(pathScale, 6)) {
				drr.add(pathImg);
				continue;
			}
			boolean flag = ImageUtils.scaleImageFile(pathImg, pathScale, size_compress, false);
			if (flag) {
				drr.add(pathImg);
			}
		}

	}

	public void addImage(String pathImg) {
		if (drr.contains(pathImg)) {
			return;
		}
		if (TextUtils.isEmpty(path_compress) || isCropImg) {
			if (ImageUtils.isImage(pathImg, 6)) {
				drr.add(pathImg);
			}
			return;
		}
		String pathScale = path_compress + File.separator + EncryptUtils.EncodingMD5(pathImg) + ".jpg";
		if (ImageUtils.isImage(pathScale, 6)) {
			drr.add(pathImg);
			return;
		}
		boolean flag = ImageUtils.scaleImageFile(pathImg, pathScale, size_compress, false);
		if (flag) {
			drr.add(pathImg);
		}
	}

	public String getImagePath(String imagePath) {
		if (TextUtils.isEmpty(path_compress)) {
			return new File(imagePath).getPath();
		}
		File file = new File(path_compress + File.separator + EncryptUtils.EncodingMD5(imagePath) + ".jpg");
		if (null == file || !file.exists()) {
			file = null;
			return new File(imagePath).getPath();
		}
		else {
			return file.getPath();
		}
	}

	public String getImageCompressPath(String imagePath) {
		File file = new File(path_compress + File.separator + EncryptUtils.EncodingMD5(imagePath) + ".jpg");
		if (null == file || !file.exists()) {
			file = null;
		}
		if (null == file) {
			return null;
		}
		return file.getPath();
	}

	public File getImageFile(String imagePath) {
		if (TextUtils.isEmpty(path_compress)) {
			return new File(imagePath);
		}
		File file = new File(path_compress + File.separator + EncryptUtils.EncodingMD5(imagePath) + ".jpg");
		if (null == file || !file.exists()) {
			file = null;
			return new File(imagePath);
		}
		else {
			return file;
		}
	}

	public File getImageCompressFile(String imagePath) {
		File file = new File(path_compress + File.separator + EncryptUtils.EncodingMD5(imagePath) + ".jpg");
		if (null == file || !file.exists()) {
			file = null;
		}
		return file;
	}

	public void removeImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		if (drr.contains(path)) {
			drr.remove(path);
		}

	}

	public boolean isHasImage(String path) {
		return drr.contains(path);
	}

	public boolean isHasTakepicPath() {
		return !TextUtils.isEmpty(path_takepic);
	}

	public int getSize() {
		return null == drr ? 0 : drr.size();
	}

	public int getTotalSize() {
		return totalCount;
	}

	public boolean onUpImgActivityResult(Fragment mFragment, int requestCode, int resultCode, Intent data) {
		if (requestCode == UpImgHelper.RequestCode_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {

			if (!ImageUtils.isImage(this.takePicImagePath)) {
				this.takePicImagePath = null;
			}
			else {
				addImage(this.takePicImagePath);
				this.takePicImagePath = null;
			}
			if (isCropImg && getSize() == 1) {
				cropUpImage(mFragment);
				return false;
			}
			else {
				return true;
			}

		}
		else if (requestCode == UpImgHelper.RequestCode_GET_PICTURES && resultCode == Activity.RESULT_OK) {
			// cropUpImage(mContext);
			if (isCropImg && getSize() == 1) {
				cropUpImage(mFragment);
				return false;
			}
			else {
				return true;
			}
		}
		else if (requestCode == UpImgHelper.RequestCode_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");
			saveCropUpImage(bitmap);
			if (null != bitmap) {
				bitmap.recycle();
			}
			return true;
		}
		return false;
	}

	public boolean onUpImgActivityResult(Context mContext, int requestCode, int resultCode, Intent data) {
		if (requestCode == UpImgHelper.RequestCode_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
			// Bundle bundle = data.getExtras();
			// Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			//
			// if (null == bitmap) {
			// Toast.makeText(mContext, ERROE_TAKE_PICK, Toast.LENGTH_LONG).show();
			// return;
			// }
			// File file = FileUtils.createFile(path_takepic + File.separator
			// + FileUtils.getFilenameByTime("img_", ".jpg"));
			// if (null == file) {
			// Toast.makeText(mContext, ERROE_TAKE_PICK, Toast.LENGTH_LONG).show();
			// return;
			// }
			//
			// boolean flag = FileUtils.saveBitmap(bitmap, file);
			// bitmap.recycle();
			// if (flag) {
			// addImage(file.getPath());
			// }

			if (!ImageUtils.isImage(this.takePicImagePath)) {
				this.takePicImagePath = null;
			}
			else {
				addImage(this.takePicImagePath);
				this.takePicImagePath = null;
			}
			if (isCropImg && getSize() == 1) {
				cropUpImage(mContext);
				return false;
			}
			else {
				return true;
			}

		}
		else if (requestCode == UpImgHelper.RequestCode_GET_PICTURES && resultCode == Activity.RESULT_OK) {
			// cropUpImage(mContext);
			if (isCropImg && getSize() == 1) {
				cropUpImage(mContext);
				return false;
			}
			else {
				return true;
			}
		}
		else if (requestCode == UpImgHelper.RequestCode_CROP_IMAGE && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");
			saveCropUpImage(bitmap);
			if (null != bitmap) {
				bitmap.recycle();
			}
			return true;
		}
		return false;
	}

	private void saveCropUpImage(Bitmap mBitmap) {
		for (String pathImg : drr) {
			String pathCrop = path_compress + File.separator + EncryptUtils.EncodingMD5(pathImg) + ".jpg";
			FileUtils.saveBitmap(mBitmap, new File(pathCrop));
			break;
		}
	}

	private void cropUpImage(Context mContext) {
		if (!isCropImg || getSize() != 1) {
			return;
		}
		else {
			String imagePath = drr.get(0);
			File mFile = new File(imagePath);
			final Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(mFile), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", size_crop_x);
			intent.putExtra("outputY", size_crop_y);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);
			((Activity) mContext).startActivityForResult(intent, RequestCode_CROP_IMAGE);
		}
	}

	private void cropUpImage(Fragment mFragment) {
		if (!isCropImg || getSize() != 1) {
			return;
		}
		else {
			String imagePath = drr.get(0);
			File mFile = new File(imagePath);
			final Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(mFile), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", size_crop_x);
			intent.putExtra("outputY", size_crop_y);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);
			mFragment.startActivityForResult(intent, RequestCode_CROP_IMAGE);
		}
	}
}
