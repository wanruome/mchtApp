/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月15日 上午9:59:53 
 */
package com.ruomm.base.view.upimg;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.baseconfig.KeyBackUtil;
import com.ruomm.R;

@SuppressLint("NewApi")
public class UpImageActivity extends FragmentActivity implements UpImgCallBackActivity, MenuTopListener {
	FragmentManager mFManager;
	Context mContext;
	MenuTopView mymenutop;
	FrameLayout upimg_container_compress;
	ArrayList<String> listImage = new ArrayList<String>();
	private long time_backclik = 0;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int msgID = msg.what;
			if (msgID == UpImgHelper.RequestCode_GET_PICTURES) {
				UpImgDialogUtil.dismissUpImgDialog(mFManager);
				setResult(RESULT_OK);
				finish();
			}
		}

	};

	@Override
	public void onMenuTopClick(View v, int vID) {
		if (KeyBackUtil.isTrueBackFragment(time_backclik)) {
			time_backclik = System.currentTimeMillis();
			Fragment mFragment = mFManager.findFragmentById(R.id.upimg_container);
			if (null != mFragment && mFragment instanceof UpImgFragImage) {
				showBucketFragment();
			}
			else {
				finish();
			}
		}

	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.upimg_act);
		DisplayUtil.enableBarTint(this, R.color.menutop_background_color);
		initOnCreate();
		showBucketFragment();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	private void initOnCreate() {

		mContext = this;
		mymenutop = (MenuTopView) findViewById(R.id.mymenutop);
		upimg_container_compress = (FrameLayout) findViewById(R.id.upimg_container_compress);
		mFManager = getSupportFragmentManager();
		for (String tempDrr : UpImgHelper.getInstance().drr) {
			listImage.add(tempDrr);
		}

	}

	private void showBucketFragment() {
		mymenutop.setCenterText("相册列表");
		Fragment mFragment = new UpImgFragBucket();
		mFManager.beginTransaction().replace(R.id.upimg_container, mFragment).commit();

	}

	private void showImageFragment(ImageBucket imageBucket) {
		mymenutop.setCenterText("选择相片");
		Fragment mFragment = new UpImgFragImage();
		Bundle data = new Bundle();
		data.putSerializable(UpImgHelper.TAG_IMAGEBUCKET, imageBucket);
		data.putStringArrayList(UpImgHelper.TAG_SELECTED_IMAGES, listImage);
		mFragment.setArguments(data);
		mFManager.beginTransaction().replace(R.id.upimg_container, mFragment).commit();
	}

	@Override
	public void onUpImgCallBack(ImageBucket imageBucket) {
		showImageFragment(imageBucket);

	}

	@Override
	public void onUpImgCallBackFinish() {
		UpImgDialogUtil.showUpImgDialog(mFManager);
		new Thread() {
			@Override
			public void run() {
				UpImgHelper.getInstance().compressListImage(listImage);
				SystemClock.sleep(100);
				mHandler.sendEmptyMessage(UpImgHelper.RequestCode_GET_PICTURES);

			};
		}.start();

	}

}
