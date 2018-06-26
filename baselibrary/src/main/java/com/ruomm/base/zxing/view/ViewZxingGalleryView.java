/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月25日 下午2:07:31 
 */
package com.ruomm.base.zxing.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.picasso.CropSquareTransformation;
import com.ruomm.base.zxing.decoding.DecodeBitmap;
import com.ruomm.R;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class ViewZxingGalleryView extends LinearLayout {
	private boolean isDecodingFail = false;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int vID = msg.what;

			if (vID == R.id.decode_succeeded) {
				setDecodingStatusSuccess();
				Result result = (Result) msg.obj;
				if (null != zxingImageCallBack) {
					zxingImageCallBack.hanleDecodeGallery(result, null);
				}
			}
			else if (vID == R.id.decode_failed) {
				setDecodingStatusError();
				isDecodingFail = true;
			}
		};

	};

	Context mContext;
	View mView;
	LinearLayout zxing_gallery_container;
	ImageView zxing_gallery_image;
	LinearLayout zxing_gallery_errordecode;
	LinearLayout zxing_gallery_decoding;
	ZxingImageCallBack zxingImageCallBack;
	private String decodeImagePath;
	private final int displayWidth;

	public ViewZxingGalleryView(Context context) {
		this(context, null);
	}

	public ViewZxingGalleryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public void setZxingImageCallBack(ZxingImageCallBack zxingImageCallBack) {
		this.zxingImageCallBack = zxingImageCallBack;
	}

	@SuppressLint("NewApi")
	public ViewZxingGalleryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		this.displayWidth = DisplayUtil.getDispalyAbsWidth(this.mContext);
		// LayoutInflater inflater = (LayoutInflater)
		// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutInflater inflater = LayoutInflater.from(context);
		mView = inflater.inflate(R.layout.zxing_gallery, null);
		mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mView);
		zxing_gallery_container = (LinearLayout) mView.findViewById(R.id.zxing_gallery_container);
		zxing_gallery_decoding = (LinearLayout) mView.findViewById(R.id.zxing_gallery_decoding);
		zxing_gallery_errordecode = (LinearLayout) mView.findViewById(R.id.zxing_gallery_errordecode);
		zxing_gallery_image = (ImageView) mView.findViewById(R.id.zxing_gallery_image);
		zxing_gallery_container.setOnClickListener(myOnClickListener);
		setDecodingStatusPreview();

	}

	public void startDecoding(final String imagePath) {
		this.decodeImagePath = imagePath;
		isDecodingFail = false;
		Picasso.with(mContext).load(StringUtils.getPicassoUrl(imagePath))
				.transform(new CropSquareTransformation(this.displayWidth / 2, false)).into(zxing_gallery_image);
		setDecodingStatusDecoding();

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		//
		// Result result = DecodeBitmap.scanningImage(imagePath);
		//
		// if (null == result) {
		// SystemClock.sleep(1500);
		// mHandler.sendEmptyMessage(R.id.decode_failed);
		// }
		// else {
		// Message msg = new Message();
		// msg.what = R.id.decode_succeeded;
		// msg.obj = result;
		// mHandler.sendMessage(msg);
		// mHandler.sendMessage(msg);
		// }
		//
		// }
		// }).start();
		Result result = DecodeBitmap.scanningImage(imagePath);
		if (null == result) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					SystemClock.sleep(1500);
					mHandler.sendEmptyMessage(R.id.decode_failed);
				}
			}).start();
		}
		else {
			setDecodingStatusSuccess();
			if (null != zxingImageCallBack) {
				zxingImageCallBack.hanleDecodeGallery(result, null);
			}
		}

	}

	public void setDecodingStatusPreview() {
		zxing_gallery_container.setVisibility(View.GONE);
		zxing_gallery_errordecode.setVisibility(View.GONE);
		zxing_gallery_decoding.setVisibility(View.GONE);
	}

	public void setDecodingStatusDecoding() {
		zxing_gallery_container.setVisibility(View.VISIBLE);
		zxing_gallery_errordecode.setVisibility(View.GONE);
		zxing_gallery_decoding.setVisibility(View.VISIBLE);
	}

	public void setDecodingStatusError() {
		zxing_gallery_container.setVisibility(View.VISIBLE);
		zxing_gallery_errordecode.setVisibility(View.VISIBLE);
		zxing_gallery_decoding.setVisibility(View.GONE);
	}

	public void setDecodingStatusSuccess() {
		zxing_gallery_container.setVisibility(View.VISIBLE);
		zxing_gallery_errordecode.setVisibility(View.GONE);
		zxing_gallery_decoding.setVisibility(View.GONE);
	}

	private final OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int vID = v.getId();
			if (vID == R.id.zxing_gallery_container) {
				if (isDecodingFail) {
					setDecodingStatusPreview();
					if (null != zxingImageCallBack) {
						zxingImageCallBack.hanleDecodeGalleryError(decodeImagePath);
					}
				}
			}

		}
	};
}
