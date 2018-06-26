/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月15日 上午9:53:34 
 */
package com.ruomm.base.view.upimg;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.ruomm.R;

public class UpImgFragImage extends Fragment {
	private View mView;
	GridView upimg_gridview;
	Button upimg_btn;
	ImageBucket mImageBucket;
	UpImgAdapterImage upimgAdapterImage;
	Context mContext;
	ArrayList<String> listImage;
	UpImgCallBackActivity mUpImgCallBackActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			mUpImgCallBackActivity = (UpImgCallBackActivity) getActivity();
		}
		catch (Exception e) {
			mUpImgCallBackActivity = null;
		}

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.upimg_image_fragment, null);
		mContext = getActivity();
		listImage = getArguments().getStringArrayList(UpImgHelper.TAG_SELECTED_IMAGES);
		upimg_gridview = (GridView) mView.findViewById(R.id.upimg_gridview);
		upimg_btn = (Button) mView.findViewById(R.id.upimg_btn);
		upimg_btn.setOnClickListener(myOnClickListener);
		mImageBucket = (ImageBucket) getArguments().getSerializable(UpImgHelper.TAG_IMAGEBUCKET);
		upimgAdapterImage = new UpImgAdapterImage(mContext, mImageBucket.imageList, listImage, upImgCallBackText);
		upimg_gridview.setAdapter(upimgAdapterImage);
		return mView;
	}

	private final UpImgCallBackText upImgCallBackText = new UpImgCallBackText() {

		@Override
		public void onTextCallBcak(int count) {

			upimg_btn.setText("完成(" + String.format("%02d", count) + "/"
					+ String.format("%02d", UpImgHelper.getInstance().getTotalSize()) + ")");

		}
	};

	private final OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null != mUpImgCallBackActivity) {
				mUpImgCallBackActivity.onUpImgCallBackFinish();
			}
		}
	};
}
