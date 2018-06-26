/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月15日 上午9:49:22 
 */
package com.ruomm.base.view.upimg;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ruomm.R;

public class UpImgFragBucket extends Fragment {
	private View mView;
	AlbumHelper helper;
	List<ImageBucket> dataList;
	GridView upimg_gridview;
	UpImgAdapterBucket upimgAdapeterBucket;
	Context mContext;
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
		mView = inflater.inflate(R.layout.upimg_bucket_fragment, null);
		mContext = getActivity();
		upimg_gridview = (GridView) mView.findViewById(R.id.upimg_gridview);
		helper = AlbumHelper.getHelper();
		helper.init(mContext.getApplicationContext());
		initData();
		return mView;

	}

	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		dataList = helper.getImagesBucketList(false);
		upimgAdapeterBucket = new UpImgAdapterBucket(mContext, dataList, bucketOnClickListener);
		upimg_gridview.setAdapter(upimgAdapeterBucket);

	}

	private final OnClickListener bucketOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.upimg_image) {
				int index = (Integer) v.getTag();
				ImageBucket imageBucket = dataList.get(index);
				if (null != mUpImgCallBackActivity) {
					mUpImgCallBackActivity.onUpImgCallBack(imageBucket);
				}
				// Intent intent = new Intent(mContext, UpImgImageActivity.class);
				// intent.putExtra(ImageBucket.class.getSimpleName(), imageBucket);
				// startActivity(intent);
			}

		}
	};
}
