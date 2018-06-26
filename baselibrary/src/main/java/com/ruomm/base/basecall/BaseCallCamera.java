/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年2月5日 上午9:20:17 
 */
package com.ruomm.base.basecall;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * 调用系统相机辅助类
 * 
 * @author Ruby
 */
public class BaseCallCamera {

	private final Context mContext;
	/**
	 * 调用系统相机拍照时候相片存储的位置
	 */
	private File file = null;
	/**
	 * 需要调用相机的Activity
	 */
	private final Activity mActivity;
	/**
	 * 需要调用相机的Fragment
	 */
	private final Fragment mFragment;
	/**
	 * 调用相机的requestCode值，成功的resultCode为:Activity.RESULT_OK
	 */
	private final int requestCode;

	/**
	 * @param mActivity
	 *            调用相机的Activity；
	 * @param file
	 *            调用相机拍照的存储文件；
	 * @param requestCode
	 *            调用相机的RequestCode；
	 */
	public BaseCallCamera(Activity mActivity, File file, int requestCode) {
		super();

		this.mActivity = mActivity;
		this.mFragment = null;
		this.mContext = mActivity;
		this.requestCode = requestCode;
		this.file = file;
	}

	/**
	 * @param mFragment
	 *            调用相机的Fragment；
	 * @param file
	 *            调用相机拍照的存储文件；
	 * @param requestCode
	 *            调用相机的RequestCode；
	 */
	public BaseCallCamera(Fragment mFragment, File file, int requestCode) {
		this.mActivity = null;
		this.mFragment = mFragment;
		this.mContext = mFragment.getContext();
		this.requestCode = requestCode;
		this.file = file;
	}

	/**
	 * 开始调用系统相机拍照，依据调用环境启动相机
	 * <p>
	 * onActivityResult返回值返回到相应的Activity或者Fragment里
	 * <p>
	 * 成功返回的resultCode为：Activity. RESULT_OK
	 */
	public void startTakePhoto() {
		if (null != file) {
			if (null != mActivity) {
				Intent cIntent = getTakePickIntent();
				mActivity.startActivityForResult(cIntent, requestCode);
			}
			else if (null != mFragment) {
				Intent cIntent = getTakePickIntent();
				mFragment.startActivityForResult(cIntent, requestCode);
			}
			else {
				Toast.makeText(mContext, "没有调用相机环境，启动相机拍照失败！", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(mContext, "没有指定存储位置！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * @return 相机拍照的照片文件存储路径；
	 */
	public String getCameraStoryPath() {
		if (null == file) {
			return null;
		}
		else {
			return file.getPath();
		}
	}

	/**
	 * 构造一个启动相机的Intent，此Intent传入一个以文件构造而成的Uri，方便Activity或者Fragment调用系统相机
	 * 
	 * @return 系统相机拍照启动的Intent；
	 */
	private Intent getTakePickIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		return intent;
	}
}
