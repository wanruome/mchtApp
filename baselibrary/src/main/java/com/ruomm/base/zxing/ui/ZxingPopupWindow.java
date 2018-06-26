package com.ruomm.base.zxing.ui;

import java.io.File;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.ImageUtils;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.zxing.encoding.EncodingHandler;
import com.ruomm.R;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;

public class ZxingPopupWindow extends PopupWindow {
	// public static int POPUP_BG_COLOR = 0x00000000;
	private final View mview;
	private View autodismissView;
	private int cancel_id = -1;
	private boolean isAutodismiss = true;
	private String zxing_contentString;
	private final BaseDialogClickListener listener;
	private final Context mContext;
	private ImageView zxing_image;
	private ImageView zxing_imagelogo;

	private ZxingPopupWindow(Context mContext, int layoutId, BaseDialogClickListener listener) {
		super(mContext);
		this.mContext = mContext;
		this.listener = listener;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mview = inflater.inflate(layoutId, null);
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setContentView(mview);
		this.setFocusable(true);
		this.setBackground(0x00000000);

	}

	public ZxingPopupWindow(Context mContext, String zxing_contentString) {
		this(mContext, R.layout.zxing_popup, null);
		this.zxing_contentString = zxing_contentString;
		this.zxing_image = (ImageView) mview.findViewById(R.id.zxing_image);
		this.zxing_imagelogo = (ImageView) mview.findViewById(R.id.zxing_imagelogo);
		setAutoDisMisss(R.id.zxing_popupdismiss);
		createZxingImage();

	}

	public void setAutoDisMisss(boolean isAutodismiss) {
		this.isAutodismiss = isAutodismiss;
	}

	public void setAutoDisMisss(int viewID) {
		try {
			autodismissView = findViewById(viewID);
		}
		catch (Exception e) {
			autodismissView = null;
		}
		if (null != autodismissView) {
			mview.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int x = (int) event.getX();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						int top = autodismissView.getTop();
						int buttom = autodismissView.getBottom();
						int left = autodismissView.getLeft();
						int right = autodismissView.getRight();

						if (x < left || x > right || y < top || y > buttom) {
							ZxingPopupWindow.this.dismiss();
						}
					}
					return true;
				}
			});
		}
	}

	public void setText(int vID, CharSequence text) {
		try {
			View v = findViewById(vID);
			Method method = v.getClass().getMethod("setText", CharSequence.class);
			method.setAccessible(true);
			if (TextUtils.isEmpty(text)) {
				method.invoke(v, "");
			}
			else {
				method.invoke(v, text);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
	}

	public void setBackground(int bgcolor) {
		ColorDrawable dw = new ColorDrawable(bgcolor);
		this.setBackgroundDrawable(dw);
	}

	public void setListener(int viewid) {

		View view = null;
		try {
			view = findViewById(viewid);
		}
		catch (Exception e) {
			view = null;
		}
		if (view != null) {
			view.setOnClickListener(myOnClickListener);
		}

	}

	public void setListenerCancle(int viewid) {
		View view = null;
		try {
			view = findViewById(viewid);
		}
		catch (Exception e) {
			view = null;
		}
		if (view != null) {
			cancel_id = viewid;
			view.setOnClickListener(myOnClickListener);
		}
	}

	public View findViewById(int vID) {
		View v = mview.findViewById(vID);
		return v;
	}

	private final OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == cancel_id) {
				dismiss();
			}
			else {
				if (null != listener) {
					listener.onDialogItemClick(v, null);
				}
				if (isAutodismiss) {
					dismiss();
				}
			}

		}
	};

	private void createZxingImage() {
		// String contentString = JSON.toJSONString("");
		try {

			if (!TextUtils.isEmpty(zxing_contentString)) {
				ErrorCorrectionLevel mErrorCorrectionLevel = ErrorCorrectionLevel.M;
				File fileqrCode = EncodingHandler.getQRcodeFile(mContext, zxing_contentString, mErrorCorrectionLevel);

				if (!ImageUtils.isImage(fileqrCode.getPath(), 10)) {
					// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（600*600）
					Bitmap qrCodeBitmap;

					qrCodeBitmap = EncodingHandler.createQRCode(zxing_contentString, 360, mErrorCorrectionLevel);

					// ------------------添加logo部分开始------------------//
					// Bitmap logoBmpPre =
					// BitmapFactory.decodeResource(getActivity().getResources(),
					// R.drawable.ic_launcher);
					// Bitmap logoBmp = ImageUtils.scaleImg(logoBmpPre, 50, 50);
					//
					// Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(),
					// qrCodeBitmap.getHeight(),
					// qrCodeBitmap.getConfig());
					// Canvas canvas = new Canvas(bitmap);
					// // 二维码绘制
					// canvas.drawBitmap(qrCodeBitmap, 0, 0, null);
					// // logo绘制
					// canvas.drawBitmap(logoBmp, qrCodeBitmap.getWidth() / 2 - logoBmp.getWidth() /
					// 2,
					// qrCodeBitmap.getHeight() / 2 - logoBmp.getHeight() / 2, null);
					//
					// // qrImgImageView.setImageBitmap(qrCodeBitmap);
					// logoBmpPre.recycle();
					// logoBmp.recycle();
					// qrCodeBitmap.recycle();
					// ------------------添加logo部分结束------------------//
					// FileUtils.saveBitmap(filedir.getPath(), qrCodeBitmap, md5Temp);
					FileUtils.saveBitmap(qrCodeBitmap, fileqrCode, CompressFormat.PNG);
					qrCodeBitmap.recycle();
				}
				Picasso.with(mContext).load(fileqrCode).into(zxing_image);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
