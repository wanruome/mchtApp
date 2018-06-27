/**
 *	@copyright 亿康通 -2015 
 * 	@author liufangcai  
 * 	@create 2015年7月16日 上午10:13:21 
 */
package com.ruomm.base.view.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ruomm.R;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.baseconfig.BaseConfig;

public class SingleChoiceDialogBase<T> extends BaseDialogUserConfig {
	/**
	 * BaseDialog属性设置
	 */
	private boolean isSameCallBack = false;
	private ListView dialog_list;
	private final List<T> listDatas;
	private SingleChoiceAdapter<T> mAdapter;
	private Object preValue;
	private int preIndex = -1;
	private int currentIndex = -1;
	private Object currentValue;
	private final SingleChoiceDialogListener singleChoiceDialogListener;
	private int submit_ID = -1;

	public SingleChoiceDialogBase(Context context, int layoutResID, List<T> data,
			SingleChoiceDialogListener singleChoiceDialogListener) {
		super(context, layoutResID, R.style.dialogStyle_floating_bgdark);
		setDialogLayoutParams((int) (DisplayUtil.getDispalyWidth(context) * BaseConfig.Dialoag_WidthPercent),
				LayoutParams.WRAP_CONTENT);
		this.listDatas = data;
		this.singleChoiceDialogListener = singleChoiceDialogListener;
		setBaseDialogClick(baseDialogClickListener);
	}

	/**
	 * @param listID
	 *            listViewID
	 * @param itemLayout
	 *            item 布局文件ID
	 * @param textID
	 *            Item的文本显示
	 * @param imageID
	 *            Item的图片显示
	 */
	public void setListViewLayout(int listID, int itemLayout, int textID, int imageID) {

		dialog_list = (ListView) findViewById(listID);
		mAdapter = new SingleChoiceAdapter<T>(getContext(), listDatas, itemLayout, textID, imageID);
		mAdapter.setCurrentIndex(currentIndex);
		if (null != dialog_list) {
			dialog_list.setAdapter(mAdapter);
			dialog_list.setOnItemClickListener(listItemClickListener);
		}

	}

	public void setSameCallBack(boolean isSameCallBack) {
		this.isSameCallBack = isSameCallBack;
	}

	public void setListenerSubmit(int viewid) {
		setListener(viewid);
		submit_ID = viewid;

	}

	public void setCurrentIndex(int current) {
		if (null != listDatas && current >= 0 && current < listDatas.size()) {
			preIndex = current;
			preValue = listDatas.get(preIndex);
		}
		else {
			preIndex = -1;
			preValue = null;
		}
		currentIndex = preIndex;
		currentValue = preValue;
		if (null != mAdapter) {
			mAdapter.setCurrentIndex(currentIndex);
			mAdapter.notifyDataSetChanged();
		}
	}

	public void setCurrentValue(T value) {
		if (null == value || null == listDatas) {
			preIndex = -1;
			preValue = null;
		}
		else {
			int index = listDatas.indexOf(value);
			if (index < 0) {
				preIndex = -1;
				preValue = null;
			}
			else {
				preIndex = index;
				preValue = listDatas.get(index);
			}
		}
		currentIndex = preIndex;
		currentValue = preValue;
		if (null != mAdapter) {
			mAdapter.setCurrentIndex(currentIndex);
			mAdapter.notifyDataSetChanged();
		}
	}

	private final BaseDialogClickListener baseDialogClickListener = new BaseDialogClickListener() {

		@Override
		public void onDialogItemClick(View v, Object tagSub) {
			int vID = v.getId();
			if (vID == submit_ID) {
				doCallBackEvent(v, false);
			}

		}
	};

	private final OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			currentIndex = position;
			currentValue = listDatas.get(position);
			mAdapter.setCurrentIndex(currentIndex);
			mAdapter.notifyDataSetChanged();
			if (submit_ID <= 0) {
				doCallBackEvent(view, true);
			}

		}
	};

	private void doCallBackEvent(View v, boolean isDismiss) {
		if (currentIndex < 0) {
			return;
		}
		if (isSameCallBack) {
			if (null != singleChoiceDialogListener) {
				singleChoiceDialogListener.onItemChoice(v, currentIndex, currentValue);
			}

		}
		else {
			if (currentIndex != preIndex) {
				if (null != singleChoiceDialogListener) {
					singleChoiceDialogListener.onItemChoice(v, currentIndex, currentValue);
				}
			}
		}
		if (isDismiss) {
			dismiss();
		}

	}

	public void setListData(ArrayList<T> data) {
		listDatas.clear();
		if (data != null) {
			listDatas.addAll(data);
		}
		mAdapter.notifyDataSetChanged();
		setCurrentIndex(0);
		// setSameCallBack(true);
	}

}
