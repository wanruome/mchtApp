/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月12日 下午4:05:52 
 */
package com.ruomm.base.view.upimg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dialog.BaseDialogUserConfig;
import com.ruomm.R;

public class TakeAndGetPictureDialog extends BaseDialogUserConfig {
	Fragment mFragment;

	@SuppressLint("InflateParams")
	public TakeAndGetPictureDialog(Context mContext) {
		super(mContext, R.layout.upimg_popupwindows, R.style.dialogStyle_floating_bgdark);
		setDialogLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setGravity(Gravity.BOTTOM);
		setBaseDialogClick(baseDialogClickListener);
		setListener(R.id.upimg_popupwindows_camera);
		setListener(R.id.upimg_popupwindows_photo);
		setListenerCancle(R.id.upimg_popupwindows_cancel);

	}

	public TakeAndGetPictureDialog(Fragment mFragment) {
		super(mFragment.getActivity(), R.layout.upimg_popupwindows, R.style.dialogStyle_floating_bgdark);
		this.mFragment = mFragment;
		setDialogLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setGravity(Gravity.BOTTOM);
		setBaseDialogClick(baseDialogClickListener);

		setListener(R.id.upimg_popupwindows_camera);
		setListener(R.id.upimg_popupwindows_photo);
		setListenerCancle(R.id.upimg_popupwindows_cancel);

	}

	private final BaseDialogClickListener baseDialogClickListener = new BaseDialogClickListener() {

		@Override
		public void onDialogItemClick(View v, Object tagSub) {
			int vID = v.getId();
			if (vID == R.id.upimg_popupwindows_camera) {
				photo();
				// dismiss();
			}
			else if (vID == R.id.upimg_popupwindows_photo) {
				if (null == mFragment) {
					TakeAndGetPictureUtil.getPicture((Activity) mContext);

				}
				else {
					TakeAndGetPictureUtil.getPicture(mFragment);

				}
				// Intent intent = new Intent(mContext, UpImageActivity.class);
				// // mContext.startActivity(intent);
				// if (null == mFragment) {
				// ((Activity) mContext).startActivityForResult(intent,
				// UpImgHelper.RequestCode_GET_PICTURES);
				// // dismiss();
				// }
				// else {
				// mFragment.startActivityForResult(intent, UpImgHelper.RequestCode_GET_PICTURES);
				// }
			}

		}
	};

	private void photo() {
		if (null == mFragment) {
			TakeAndGetPictureUtil.takePicture((Activity) mContext);
		}
		else {
			TakeAndGetPictureUtil.takePicture(mFragment);
		}
		// if (UpImgHelper.getInstance().getSize() >= UpImgHelper.getInstance().getTotalSize()) {
		// Toast.makeText(mContext, "最多上传" + UpImgHelper.getInstance().getTotalSize() + "图片",
		// Toast.LENGTH_SHORT)
		// .show();
		// return;
		// }
		// String takePicImagePath = UpImgHelper.getInstance().getTakePicImagePath(true);
		//
		// if (TextUtils.isEmpty(takePicImagePath)) {
		// Toast.makeText(mContext, "没有SD卡", Toast.LENGTH_SHORT).show();
		// }
		// else {
		// Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// File file = new File(takePicImagePath);
		// if (!file.getParentFile().exists()) {
		// file.getParentFile().mkdirs();
		// }
		//
		// Uri imageUri = Uri.fromFile(file);
		// openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		// if (null == mFragment) {
		// ((Activity) mContext).startActivityForResult(openCameraIntent,
		// UpImgHelper.RequestCode_TAKE_PICTURE);
		// }
		// else {
		// mFragment.startActivityForResult(openCameraIntent, UpImgHelper.RequestCode_TAKE_PICTURE);
		// }
		//
		// }
	}

}
