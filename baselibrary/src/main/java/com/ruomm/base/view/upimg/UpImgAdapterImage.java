/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月12日 上午11:25:46 
 */
package com.ruomm.base.view.upimg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ruomm.R;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.StringUtils;
import com.squareup.picasso.Picasso;

public class UpImgAdapterImage extends BaseAdapter {

	private final Context mContext;
	private final List<ImageItem> datalist;
	private final LayoutInflater mInflater;
	private final int width;
	private final UpImgCallBackText upImgCallBackText;
	private final ArrayList<String> listImage;

	public UpImgAdapterImage(Context mContext, List<ImageItem> datalist, ArrayList<String> listImage,
			UpImgCallBackText upImgCallBackText) {
		super();
		this.mContext = mContext;
		this.datalist = datalist;
		this.listImage = listImage;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.upImgCallBackText = upImgCallBackText;
		width = DisplayUtil.getDispalyWidth(mContext);
		if (null != upImgCallBackText) {
			upImgCallBackText.onTextCallBcak(listImage.size());
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == datalist ? 0 : datalist.size();
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView = null;
		if (null == convertView) {
			holderView = new HolderView();
			convertView = mInflater.inflate(R.layout.upimg_image_item, null);
			holderView.upimg_image = (ImageView) convertView.findViewById(R.id.upimg_image);
			holderView.upimg_bgselect = (ImageView) convertView.findViewById(R.id.upimg_bgselect);
			// holderView.upimg_isselect = (ImageView)
			// convertView.findViewById(R.id.upimg_isselect);
			holderView.upimg_container = (RelativeLayout) convertView.findViewById(R.id.upimg_container);

			convertView.setTag(holderView);

		}
		else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.upimg_container.setLayoutParams(new LinearLayout.LayoutParams(width * 2 / 5, width / 3));
		holderView.upimg_container.requestLayout();
		ImageItem imageItem = datalist.get(position);
		String imagePath = imageItem.imagePath;
		// .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
		if (!TextUtils.isEmpty(imageItem.thumbnailPath)) {
			// .transform(new CropSquareTransformation(UpImgHelper.SIZE_PICASSO_LOAD, false))
			Picasso.with(mContext).load(new File(imageItem.thumbnailPath)).placeholder(R.drawable.upimg_default_nodata)
					.error(R.drawable.upimg_default_nodata).into(holderView.upimg_image);
		}
		else {
			Picasso.with(mContext).load(StringUtils.getPicassoUrl(imageItem.imagePath))
					.placeholder(R.drawable.upimg_default_nodata).error(R.drawable.upimg_default_nodata)
					.resize(UpImgHelper.SIZE_PICASSO_LOAD_WIDTH, UpImgHelper.SIZE_PICASSO_LOAD_WIDTH)
					.into(holderView.upimg_image);
		}
		if (listImage.contains(imagePath)) {
			holderView.upimg_bgselect.setSelected(true);

		}
		else {
			holderView.upimg_bgselect.setSelected(false);

		}
		holderView.upimg_bgselect.setTag(position);
		holderView.upimg_bgselect.setOnClickListener(myOnClickListener);
		return convertView;
	}

	class HolderView {

		ImageView upimg_image;
		// ImageView upimg_isselect;
		ImageView upimg_bgselect;
		RelativeLayout upimg_container;
	}

	private final OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.upimg_bgselect) {
				int index = (Integer) v.getTag();
				ImageItem imageItem = datalist.get(index);
				String path = imageItem.imagePath;
				if (listImage.contains(path)) {
					listImage.remove(path);
					v.setSelected(false);
					// notifyDataSetChanged();
				}
				else if (listImage.size() < UpImgHelper.getInstance().getTotalSize()) {
					listImage.add(path);
					v.setSelected(true);
					// notifyDataSetChanged();
				}
				else {
					Toast.makeText(mContext, "最多选择" + UpImgHelper.getInstance().getTotalSize() + "张",
							Toast.LENGTH_SHORT).show();
				}
				if (null != upImgCallBackText) {
					upImgCallBackText.onTextCallBcak(listImage.size());
				}

			}

		}
	};

}
