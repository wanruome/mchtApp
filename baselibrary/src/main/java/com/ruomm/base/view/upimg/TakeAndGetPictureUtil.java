/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年5月11日 上午9:19:29 
 */
package com.ruomm.base.view.upimg;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

public class TakeAndGetPictureUtil {
	public static void getPicture(Activity mActivity) {
		Intent intent = new Intent(mActivity, UpImageActivity.class);
		mActivity.startActivityForResult(intent, UpImgHelper.RequestCode_GET_PICTURES);
	}

	public static void getPicture(Fragment mFragment) {
		Intent intent = new Intent(mFragment.getContext(), UpImageActivity.class);
		mFragment.startActivityForResult(intent, UpImgHelper.RequestCode_GET_PICTURES);
	}

	public static void takePicture(Activity mActivity) {
		if (UpImgHelper.getInstance().getSize() >= UpImgHelper.getInstance().getTotalSize()) {
			Toast.makeText(mActivity, "最多上传" + UpImgHelper.getInstance().getTotalSize() + "图片", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String takePicImagePath = UpImgHelper.getInstance().getTakePicImagePath(true);

		if (TextUtils.isEmpty(takePicImagePath)) {
			Toast.makeText(mActivity, "没有SD卡", Toast.LENGTH_SHORT).show();
		}
		else {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(takePicImagePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

			mActivity.startActivityForResult(openCameraIntent, UpImgHelper.RequestCode_TAKE_PICTURE);

		}
	}

	public static void takePicture(Fragment mFragment) {
		Context mContext = mFragment.getContext();
		if (UpImgHelper.getInstance().getSize() >= UpImgHelper.getInstance().getTotalSize()) {
			Toast.makeText(mContext, "最多上传" + UpImgHelper.getInstance().getTotalSize() + "图片", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String takePicImagePath = UpImgHelper.getInstance().getTakePicImagePath(true);

		if (TextUtils.isEmpty(takePicImagePath)) {
			Toast.makeText(mContext, "没有SD卡", Toast.LENGTH_SHORT).show();
		}
		else {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(takePicImagePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			mFragment.startActivityForResult(openCameraIntent, UpImgHelper.RequestCode_TAKE_PICTURE);

		}
	}
}
