/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月12日 下午2:23:18 
 */
package com.ruomm.base.view.upimg;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ruomm.R;
import com.ruomm.base.tools.picasso.CropSquareTransformation;
import com.squareup.picasso.Picasso;

public class UpImgAdapterPublish extends BaseAdapter {
	private final Context mContext;
	private final LayoutInflater mInflater;

	public UpImgAdapterPublish(Context mContext) {
		super();
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		return UpImgHelper.getInstance().getSize() < UpImgHelper.getInstance().getTotalSize() ? UpImgHelper
				.getInstance().getSize() + 1 : UpImgHelper.getInstance().getTotalSize();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView = null;
		if (null == convertView) {
			holderView = new HolderView();
			convertView = mInflater.inflate(R.layout.upimg_publish_item, null);
			holderView.upimg_image = (ImageView) convertView.findViewById(R.id.upimg_image);
			convertView.setTag(holderView);

		}
		else {
			holderView = (HolderView) convertView.getTag();
		}
		if (position == UpImgHelper.getInstance().getSize()) {
			// holderView.upimg_image.setImageResource(R.drawable.upimg_addpic_unfocused);
			Picasso.with(mContext).load(R.drawable.upimg_addpic_unfocused).into(holderView.upimg_image);
		}
		else {
			File file = UpImgHelper.getInstance().getImageFile(UpImgHelper.getInstance().drr.get(position));
			Picasso.with(mContext).load(file)
					.transform(new CropSquareTransformation(UpImgHelper.SIZE_PICASSO_LOAD_SIZE, false))
					.into(holderView.upimg_image);
		}
		holderView.upimg_image.setTag(position);
		holderView.upimg_image.setOnClickListener(myOnClickListener);
		return convertView;
	}

	class HolderView {

		ImageView upimg_image;
	}

	private final OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int vID = v.getId();

			if (vID == R.id.upimg_image) {
				int index = (Integer) v.getTag();
				if (index == UpImgHelper.getInstance().getSize()) {
					TakeAndGetPictureDialog takeAndGetPictureDialog = new TakeAndGetPictureDialog(mContext);
					takeAndGetPictureDialog.show();
					// takeAndGetPictureDialog.showAtLocation(v, Gravity.CENTER, 0, 0);
				}
			}

		}
	};

}
