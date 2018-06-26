/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年5月15日 上午9:14:45
 */
package com.ruomm.base.zxing.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ruomm.base.ioc.annotation.InjectEntity;
import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.ImageUtils;
import com.ruomm.base.zxing.encoding.EncodingHandler;
import com.ruomm.R;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.squareup.picasso.Picasso;

@InjectEntity(beanKey = "zxingcreatedialog")
public class ZxingCreateDialog extends DialogFragment {
	private View dialogView;
	private ImageView zxing_image;
	private ImageView zxing_imagelogo;
	private String zxing_contentString;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// android.R.style.Theme_Holo_Light_Dialog
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogStyle_floating_bgdark);

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		dialogView = inflater.inflate(R.layout.zxing_dialog, null);
		Bundle data = getArguments();
		if (null != data && data.containsKey(InjectUtil.getBeanKey(ZxingCreateDialog.class))) {
			zxing_contentString = data.getString(InjectUtil.getBeanKey(ZxingCreateDialog.class));
		}
		zxing_image = (ImageView) dialogView.findViewById(R.id.zxing_image);
		zxing_imagelogo = (ImageView) dialogView.findViewById(R.id.zxing_imagelogo);
		if (TextUtils.isEmpty(zxing_contentString)) {
			zxing_contentString = "http://www.ekangtong.com.cn";
		}
		createZxingImage();
		return dialogView;
	}

	private void createZxingImage() {
		try {

			if (!TextUtils.isEmpty(zxing_contentString)) {

				ErrorCorrectionLevel mErrorCorrectionLevel = ErrorCorrectionLevel.M;
				File fileqrCode = EncodingHandler.getQRcodeFile(mContext, zxing_contentString, mErrorCorrectionLevel);

				if (!ImageUtils.isImage(fileqrCode.getPath(), 10)) {
					// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（600*600）
					Bitmap qrCodeBitmap;

					qrCodeBitmap = EncodingHandler.createQRCode(zxing_contentString, 360, mErrorCorrectionLevel);
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
