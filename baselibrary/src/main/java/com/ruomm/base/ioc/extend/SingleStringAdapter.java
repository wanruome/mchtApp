/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月16日 下午5:46:48 
 */
package com.ruomm.base.ioc.extend;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * listView单选的适配器
 * <p>
 * 请为图片资源设置selected状态图片，在没有ImageView的时候则不显示图标 ，在没有TextView的时候不显示文字
 * <p>
 * 增加可以状态选择的适配器 <br/>
 * 对象的数据显示需要实现UIStringValue接口或重写toString()方法才能正确显示 <br/>
 * 对象的状态选择需要实现UISelectValue接口才能使用状态的数据<br/>
 * 
 * @author Ruby
 * @param <T>
 */
public class SingleStringAdapter<T> extends BaseAdapter {
	/**
	 * Adapter对应的控件(ListView,GridView等)所在的Context
	 */
	private final Context mContext;
	/**
	 * LayoutInflater布局填充器
	 */
	private final LayoutInflater listInflater;
	/**
	 * 数据源
	 */
	private final List<T> listDatas;
	/**
	 * 当前选中的Item的索引号
	 */
	private int currentIndex;
	/**
	 * Item布局的layout资源文件ID
	 */
	private final int layoutID;
	/**
	 * Item的文字UI显示需要的TextVIew的ID
	 */
	private final int textID;
	/**
	 * Item的图片UI显示需要的ImageView的ID
	 */
	private final int imageID;
	/**
	 * 是否显示图片
	 */
	private boolean isShowImgIcon;

	/**
	 * 构造方法
	 * 
	 * @param listContext
	 *            对象View的Context
	 * @param listDatas
	 *            数据源
	 * @param layoutID
	 *            Item的文字UI显示需要的TextVIew的ID
	 * @param textID
	 *            Item的图片UI显示需要的ImageView的ID
	 */
	public SingleStringAdapter(Context listContext, List<T> listDatas, int layoutID, int textID) {
		super();
		this.mContext = listContext;
		this.listInflater = LayoutInflater.from(listContext);
		this.listDatas = listDatas;
		this.layoutID = layoutID;
		this.textID = textID;
		this.imageID = 0;
		this.isShowImgIcon = false;

	}

	/**
	 * 构造方法
	 * 
	 * @param listContext
	 *            对象View的Context
	 * @param listDatas
	 *            数据源
	 * @param layoutID
	 *            Item布局的layout资源文件ID
	 * @param textID
	 *            Item的文字UI显示示需要的TextView的ID
	 * @param imageID
	 *            Item的图片UI显示需要的ImageView的ID
	 */
	public SingleStringAdapter(Context listContext, List<T> listDatas, int layoutID, int textID, int imageID) {
		super();
		this.mContext = listContext;
		this.listInflater = LayoutInflater.from(listContext);
		this.listDatas = listDatas;
		this.layoutID = layoutID;
		this.textID = textID;
		this.imageID = imageID;
		this.isShowImgIcon = true;
	}

	/**
	 * 设置是否显示图片
	 * 
	 * @param isShowImgIcon
	 */
	public void setShowImgIcon(boolean isShowImgIcon) {
		this.isShowImgIcon = isShowImgIcon;
	}

	/**
	 * 设置当前显示的Item
	 * 
	 * @param currentIndex
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * 获取总条数
	 * 
	 * @return
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == listDatas ? 0 : listDatas.size();
	}

	/**
	 * 获取Item对应的数据
	 * 
	 * @param position
	 * @return
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDatas.get(position);
	}

	/**
	 * 获取Item对应的ID
	 * 
	 * @param position
	 * @return
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 开始布局适配
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (null == convertView) {

			convertView = listInflater.inflate(layoutID, null);
			holderView = new HolderView();
			holderView.dialog_listitem_text = (TextView) convertView.findViewById(textID);
			if (imageID > 0) {
				holderView.dialog_listitem_image = (ImageView) convertView.findViewById(imageID);
			}
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
		StringValueOnUISelect selectItem = null;
		try {
			selectItem = (StringValueOnUISelect) listDatas.get(position);

		}
		catch (Exception e) {
			selectItem = null;
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
			if (isShowImgIcon) {
				holderView.dialog_listitem_image.setVisibility(View.VISIBLE);
			}
			else {
				holderView.dialog_listitem_image.setVisibility(View.INVISIBLE);
			}
			if (null != selectItem) {
				holderView.dialog_listitem_image.setSelected(selectItem.getUISelectValue());
			}
		}
		if (null != holderView.dialog_listitem_text) {
			if (position == currentIndex) {
				holderView.dialog_listitem_text.setSelected(true);
			}
			else {
				holderView.dialog_listitem_text.setSelected(false);
			}
			holderView.dialog_listitem_text.setText(itemString);
			if (null != selectItem) {
				holderView.dialog_listitem_text.setSelected(selectItem.getUISelectValue());
			}
		}

		return convertView;
	}

	/**
	 * Item的View资源
	 * 
	 * @author Ruby
	 */
	class HolderView {
		TextView dialog_listitem_text;
		ImageView dialog_listitem_image;
	}
}
