/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月12日 上午10:23:23 
 */
package com.ruomm.base.view.upimg;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruomm.R;
import com.ruomm.base.tools.StringUtils;
import com.squareup.picasso.Picasso;

public class UpImgAdapterBucket extends BaseAdapter {
	private final Context mContext;
	private final OnClickListener onClickListener;
	private final List<ImageBucket> dataList;
	private final LayoutInflater mInflater;

	public UpImgAdapterBucket(Context mContext, List<ImageBucket> dataList, OnClickListener onClickListener) {
		super();
		this.mContext = mContext;
		this.onClickListener = onClickListener;
		this.dataList = dataList;
		this.mInflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == dataList ? 0 : dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (null == convertView) {
			holderView = new HolderView();
			convertView = mInflater.inflate(R.layout.upimg_bucket_item, null);
			holderView.upimg_image = (ImageView) convertView.findViewById(R.id.upimg_image);
			holderView.upimg_name = (TextView) convertView.findViewById(R.id.upimg_name);
			holderView.upimg_count = (TextView) convertView.findViewById(R.id.upimg_count);
			convertView.setTag(holderView);

		}
		else {
			holderView = (HolderView) convertView.getTag();
		}
		ImageBucket imageBucket = dataList.get(position);
		// String path = imageBucket.imageList.get(0).thumbnailPath;
		// if (TextUtils.isEmpty(path)) {
		// path = imageBucket.imageList.get(0).imagePath;
		// }
		// else {
		// Picasso.with(mContext).load(new File(path)).
		// .transform(new CropSquareTransformation(240)).into(holderView.upimg_image);
		// }

		// String imagePath = imageBucket.imageList.get(0).imagePath;
		if (!TextUtils.isEmpty(imageBucket.imageList.get(0).thumbnailPath)) {
			Picasso.with(mContext).load(StringUtils.getPicassoUrl(imageBucket.imageList.get(0).thumbnailPath))
					.placeholder(R.drawable.upimg_default_nodata).error(R.drawable.upimg_default_nodata)
					.into(holderView.upimg_image);
		}
		else {
			Picasso.with(mContext).load(StringUtils.getPicassoUrl(imageBucket.imageList.get(0).imagePath))
					.placeholder(R.drawable.upimg_default_nodata).error(R.drawable.upimg_default_nodata)
					.resize(UpImgHelper.SIZE_PICASSO_LOAD_WIDTH, UpImgHelper.SIZE_PICASSO_LOAD_HEIGHT)
					.into(holderView.upimg_image);
		}

		holderView.upimg_count.setText(imageBucket.count + "");
		holderView.upimg_name.setText(imageBucket.bucketName);
		holderView.upimg_image.setTag(position);
		holderView.upimg_image.setOnClickListener(onClickListener);

		return convertView;
	}

	class HolderView {

		ImageView upimg_image;
		TextView upimg_name;
		TextView upimg_count;
	}

}
