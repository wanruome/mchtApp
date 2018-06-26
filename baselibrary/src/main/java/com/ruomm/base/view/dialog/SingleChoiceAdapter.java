/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月16日 下午5:46:48 
 */
package com.ruomm.base.view.dialog;

import java.util.List;

import com.ruomm.base.ioc.extend.StringValueOnUIShow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleChoiceAdapter<T> extends BaseAdapter {
	private final Context mContext;
	private final LayoutInflater listInflater;
	private final List<T> listDatas;
	private int currentIndex;
	private final int layoutID;
	private final int textID;
	private final int imageID;

	public SingleChoiceAdapter(Context listContext, List<T> listDatas, int layoutID, int textID, int imageID) {
		super();
		this.mContext = listContext;
		this.listInflater = LayoutInflater.from(this.mContext);
		this.listDatas = listDatas;
		this.layoutID = layoutID;
		this.textID = textID;
		this.imageID = imageID;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == listDatas ? 0 : listDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (null == convertView) {

			convertView = listInflater.inflate(layoutID, null);
			holderView = new HolderView();
			holderView.dialog_listitem_text = (TextView) convertView.findViewById(textID);
			holderView.dialog_listitem_image = (ImageView) convertView.findViewById(imageID);
			convertView.setTag(holderView);
		}
		holderView = (HolderView) convertView.getTag();
		StringValueOnUIShow item = null;
		try {
			item = (StringValueOnUIShow) listDatas.get(position);
		}
		catch (Exception e) {
			item = null;
		}
		String itemString = null;
		if (null != item) {
			itemString = item.getUIStringValue();
		}
		else {
			itemString = String.valueOf(listDatas.get(position));
		}
		if (null != holderView.dialog_listitem_image) {
			if (position == currentIndex) {
				holderView.dialog_listitem_image.setSelected(true);
			}
			else {
				holderView.dialog_listitem_image.setSelected(false);
			}
		}
		holderView.dialog_listitem_text.setText(itemString);
		return convertView;
	}

	class HolderView {
		TextView dialog_listitem_text;
		ImageView dialog_listitem_image;
	}
}
