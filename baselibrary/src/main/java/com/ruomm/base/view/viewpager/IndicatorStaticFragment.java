/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年10月13日 下午1:55:09 
 */
package com.ruomm.base.view.viewpager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.ruomm.base.ioc.annotation.InjectFragment;
import com.ruomm.base.view.viewpager.Indicator.IndicatorAdapter;
import com.ruomm.base.view.viewpager.Indicator.OnItemSelectedListener;
import com.ruomm.base.view.viewpager.Indicator.OnTransitionListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

public class IndicatorStaticFragment {
	private final Indicator indicatorView;
	private IndicatorStaticFragmentAdapter adapter;
	private OnIndicatorFragmentChangeListener onIndicatorFragmentChangeListener;

	public IndicatorStaticFragment(Indicator indicatorView) {
		super();
		this.indicatorView = indicatorView;
		indicatorView.setOnItemSelectListener(onItemSelectedListener);
	}

	private final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(View selectItemView, int select, int preSelect) {
			if (null != adapter) {
				adapter.setSelectItem(select);
			}
			if (null != onIndicatorFragmentChangeListener) {
				onIndicatorFragmentChangeListener.onIndicatorFragmentChange(preSelect, select);
			}
		}
	};

	public void setAdapter(IndicatorStaticFragmentAdapter adapter) {
		this.adapter = adapter;
		if (null != this.adapter) {
			indicatorView.setAdapter(adapter.getIndicatorAdapter());
			this.adapter.setSelectItem(getCurrentItem());
		}
	}

	public IndicatorStaticFragmentAdapter getAdapter() {
		return this.adapter;
	}

	public Indicator getIndicatorView() {
		return indicatorView;
	}

	/**
	 * 设置indicatorView上滑动变化的转换监听，tab在切换过程中会调用此监听。
	 * 
	 * @param onTransitionListener
	 */
	public void setIndicatorOnTransitionListener(OnTransitionListener onTransitionListener) {
		indicatorView.setOnTransitionListener(onTransitionListener);
	}

	/**
	 * 设置indicatorView的滑动块样式
	 * 
	 * @param scrollBar
	 */
	public void setIndicatorScrollBar(ScrollBar scrollBar) {
		indicatorView.setScrollBar(scrollBar);
	}

	/**
	 * 获取上一次选中的索引
	 * 
	 * @return
	 */
	public int getPreSelectItem() {
		return indicatorView.getPreSelectItem();
	}

	/**
	 * 获取当前选中的索引
	 * 
	 * @return
	 */
	public int getCurrentItem() {
		return indicatorView.getCurrentItem();
	}

	/**
	 * 通知适配器数据变化，重新加载页面
	 */
	public void notifyDataSetChanged() {
		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged(indicatorView.getCurrentItem());
		}
	}

	public OnIndicatorFragmentChangeListener getOnIndicatorFragmentChangeListener() {
		return onIndicatorFragmentChangeListener;
	}

	public void setOnIndicatorFragmentChangeListener(OnIndicatorFragmentChangeListener onIndicatorFragmentChangeListener) {
		this.onIndicatorFragmentChangeListener = onIndicatorFragmentChangeListener;
	}

	public static interface OnIndicatorFragmentChangeListener {
		/**
		 * 注意 preItem 可能为 -1。表示之前没有选中过,每次adapter.notifyDataSetChanged也会将preItem 设置为-1；
		 * 
		 * @param preItem
		 * @param currentItem
		 */
		public void onIndicatorFragmentChange(int preItem, int currentItem);
	}

	/**
	 * 切换至指定的页面
	 * 
	 * @param item
	 *            页面的索引
	 * @param anim
	 *            是否动画效果
	 */
	public void setCurrentItem(int item, boolean anim) {
		// viewPager.setCurrentItem(item, anim);
		if (null != adapter) {
			adapter.notifyDataSetChanged(item);
		}
		indicatorView.setCurrentItem(item, anim);
	}

	public static abstract class IndicatorStaticFragmentAdapter {
		private final HashMap<Integer, Fragment> hashMap = new HashMap<Integer, Fragment>();
		private final ArrayList<Integer> listrefresh = new ArrayList<Integer>();
		private final FragmentManager mFragmentManager;
		private final int mFragmentContainer;
		private final IndicatorAdapter indicatorAdapter = new IndicatorAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				return IndicatorStaticFragmentAdapter.this.getViewForTab(position, convertView, parent);
			}

			@Override
			public int getCount() {
				return IndicatorStaticFragmentAdapter.this.getCount();
			}
		};

		public IndicatorStaticFragmentAdapter(FragmentManager mFragmentManager, int mFragmentContainer) {
			super();
			this.mFragmentManager = mFragmentManager;
			this.mFragmentContainer = mFragmentContainer;
		}

		public IndicatorAdapter getIndicatorAdapter() {
			return indicatorAdapter;
		}

		public void notifyDataSetChanged(int currentIndex) {
			for (Integer key : listrefresh) {
				Fragment tempFragment = hashMap.get(key);
				if (null != tempFragment) {
					mFragmentManager.beginTransaction().remove(tempFragment).commit();
				}
				hashMap.remove(key);
			}
			listrefresh.clear();
			setSelectItem(currentIndex);
		}

		public void setSelectItem(int currentIndex) {

			Set<Integer> keySet = hashMap.keySet();
			for (Integer key : keySet) {
				Fragment tempFragment = hashMap.get(key);
				if (null != tempFragment && key != currentIndex) {
					mFragmentManager.beginTransaction().hide(tempFragment).commit();
					tempFragment.setUserVisibleHint(false);

				}
			}
			Fragment tempFragment = hashMap.get(currentIndex);
			if (null != tempFragment) {
				mFragmentManager.beginTransaction().show(tempFragment).commit();
				tempFragment.setUserVisibleHint(true);
			}
			else {

				tempFragment = getFragmentForPage(currentIndex);
				hashMap.put(currentIndex, tempFragment);
				mFragmentManager.beginTransaction().add(mFragmentContainer, tempFragment, currentIndex + "").commit();
				tempFragment.setUserVisibleHint(true);
				InjectFragment injectFragment = tempFragment.getClass().getAnnotation(InjectFragment.class);
				if (null == injectFragment || !injectFragment.isRefresh()) {
					listrefresh.remove(currentIndex);
				}
				else {
					listrefresh.add(currentIndex);
				}
			}

		}

		public abstract int getCount();

		public abstract View getViewForTab(int position, View convertView, ViewGroup container);

		public abstract Fragment getFragmentForPage(int position);
	}

}
