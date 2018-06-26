package com.ruomm.base.view.viewpager;

import com.ruomm.base.view.viewpager.Indicator.IndicatorAdapter;
import com.ruomm.base.view.viewpager.Indicator.OnItemSelectedListener;
import com.ruomm.base.view.viewpager.Indicator.OnTransitionListener;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

/**
 * 将indicatorView，ViewPager联合使用
 */
public class IndicatorViewPager {

	private final Indicator indicatorView;
	private final ViewPager viewPager;
	private IndicatorPagerAdapter adapter;
	private OnIndicatorPageChangeListener onIndicatorPageChangeListener;
	private boolean isCallBackOnSetting = false;

	public IndicatorViewPager(Indicator indicator, ViewPager viewPager) {
		super();
		this.indicatorView = indicator;
		this.viewPager = viewPager;
		viewPager.setOnPageChangeListener(onPageChangeListener);
		indicatorView.setOnItemSelectListener(onItemSelectedListener);
	}

	public void setCallBakOnSetting(boolean isCallBackOnSetting) {
		this.isCallBackOnSetting = isCallBackOnSetting;
	}

	/**
	 * 设置适配器
	 * 
	 * @param adapter
	 */
	public void setAdapter(IndicatorPagerAdapter adapter) {
		this.adapter = adapter;
		viewPager.setAdapter(adapter.getPagerAdapter());
		indicatorView.setAdapter(adapter.getIndicatorAdapter());
		if (null != this.onIndicatorPageChangeListener && isCallBackOnSetting) {
			this.onIndicatorPageChangeListener.onIndicatorPageChange(getPreSelectItem(), getCurrentItem());
		}
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
		viewPager.setCurrentItem(item, anim);
		indicatorView.setCurrentItem(item, anim);
		if (null != this.onIndicatorPageChangeListener) {
			this.onIndicatorPageChangeListener.onIndicatorPageChange(getPreSelectItem(), getCurrentItem());
		}
	}

	public void setCurrentItemNoListener(int item, boolean anim) {
		viewPager.setCurrentItem(item, anim);
		indicatorView.setCurrentItem(item, anim);
	}

	/**
	 * 设置页面切换监听
	 * 
	 * @return
	 */
	public void setOnIndicatorPageChangeListener(OnIndicatorPageChangeListener onIndicatorPageChangeListener) {
		this.onIndicatorPageChangeListener = onIndicatorPageChangeListener;
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
	 * 设置缓存界面的个数，左右两边缓存界面的个数，不会被重新创建。<br>
	 * 默认是1，表示左右两边 相连的1个界面和当前界面都会被缓存住，比如切换到左边的一个界面，那个界面是不会重新创建的。
	 * 
	 * @param limit
	 */
	public void setPageOffscreenLimit(int limit) {
		viewPager.setOffscreenPageLimit(limit);
	}

	/**
	 * 设置page间的图片的宽度
	 * 
	 * @param marginPixels
	 */
	public void setPageMargin(int marginPixels) {
		viewPager.setPageMargin(marginPixels);
	}

	/**
	 * 设置page间的图片
	 * 
	 * @param d
	 */
	public void setPageMarginDrawable(Drawable d) {
		viewPager.setPageMarginDrawable(d);
	}

	/**
	 * 设置page间的图片
	 * 
	 * @param resId
	 */
	public void setPageMarginDrawable(int resId) {
		viewPager.setPageMarginDrawable(resId);
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
		return viewPager.getCurrentItem();
	}

	public IndicatorPagerAdapter getAdapter() {
		return this.adapter;
	}

	public OnIndicatorPageChangeListener getOnIndicatorPageChangeListener() {
		return onIndicatorPageChangeListener;
	}

	public Indicator getIndicatorView() {
		return indicatorView;
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	/**
	 * 通知适配器数据变化，重新加载页面
	 */
	public void notifyDataSetChanged() {
		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
		if (null != this.onIndicatorPageChangeListener && isCallBackOnSetting) {
			this.onIndicatorPageChangeListener.onIndicatorPageChange(getPreSelectItem(), getCurrentItem());
		}
	}

	private final OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(View selectItemView, int select, int preSelect) {
			viewPager.setCurrentItem(select);
			if (onIndicatorPageChangeListener != null) {
				onIndicatorPageChangeListener.onIndicatorPageChange(preSelect, select);
			}
		}
	};

	public static interface OnIndicatorPageChangeListener {
		/**
		 * 注意 preItem 可能为 -1。表示之前没有选中过,每次adapter.notifyDataSetChanged也会将preItem 设置为-1；
		 * 
		 * @param preItem
		 * @param currentItem
		 */
		public void onIndicatorPageChange(int preItem, int currentItem);
	}

	private final OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			indicatorView.setCurrentItem(position, true);
			if (onIndicatorPageChangeListener != null) {
				onIndicatorPageChangeListener.onIndicatorPageChange(indicatorView.getPreSelectItem(), position);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			indicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	public static interface IndicatorPagerAdapter {

		PagerAdapter getPagerAdapter();

		IndicatorAdapter getIndicatorAdapter();

		void notifyDataSetChanged();

	}

	/**
	 * viewpage 的每个页面是view的形式
	 * 
	 * @author Administrator
	 */
	public static abstract class IndicatorViewPagerAdapter implements IndicatorPagerAdapter {
		private boolean isOnRefreshOnChange = false;
		private final IndicatorAdapter indicatorAdapter = new IndicatorAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return IndicatorViewPagerAdapter.this.getViewForTab(position, convertView, parent);
			}

			@Override
			public int getCount() {
				return IndicatorViewPagerAdapter.this.getCount();
			}
		};

		private final RecyclingPagerAdapter pagerAdapter = new RecyclingPagerAdapter() {

			@Override
			public int getCount() {
				return IndicatorViewPagerAdapter.this.getCount();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup container) {
				return IndicatorViewPagerAdapter.this.getViewForPage(position, convertView, container);
			}

			@Override
			public int getItemPosition(Object object) {
				if (isOnRefreshOnChange) {
					return PagerAdapter.POSITION_NONE;
				}
				else {
					return PagerAdapter.POSITION_UNCHANGED;
				}

			}
		};

		public void setRefreshOnChange(boolean isOnRefreshOnChange) {
			this.isOnRefreshOnChange = isOnRefreshOnChange;
		}

		public abstract int getCount();

		public abstract View getViewForTab(int position, View convertView, ViewGroup container);

		public abstract View getViewForPage(int position, View convertView, ViewGroup container);

		@Override
		public void notifyDataSetChanged() {
			indicatorAdapter.notifyDataSetChanged();
			pagerAdapter.notifyDataSetChanged();
		}

		@Override
		public PagerAdapter getPagerAdapter() {
			return pagerAdapter;
		}

		@Override
		public IndicatorAdapter getIndicatorAdapter() {
			return indicatorAdapter;
		}

	}

	/**
	 * viewpage 的每个页面是Fragment的形式
	 * 
	 * @author Administrator
	 */
	public static abstract class IndicatorFragmentPagerAdapter implements IndicatorPagerAdapter {
		private FragmentListRefreshPageAdapter pagerAdapter;

		public IndicatorFragmentPagerAdapter(FragmentManager fragmentManager) {
			super();
			pagerAdapter = new FragmentListRefreshPageAdapter(fragmentManager) {

				@Override
				public int getCount() {
					return IndicatorFragmentPagerAdapter.this.getCount();
				}

				@Override
				public Fragment getItem(int position) {
					return IndicatorFragmentPagerAdapter.this.getFragmentForPage(position);
				}

			};
		}

		private final IndicatorAdapter indicatorAdapter = new IndicatorAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				return IndicatorFragmentPagerAdapter.this.getViewForTab(position, convertView, parent);
			}

			@Override
			public int getCount() {
				return IndicatorFragmentPagerAdapter.this.getCount();
			}
		};

		public abstract int getCount();

		public abstract View getViewForTab(int position, View convertView, ViewGroup container);

		public abstract Fragment getFragmentForPage(int position);

		@Override
		public void notifyDataSetChanged() {
			indicatorAdapter.notifyDataSetChanged();
			pagerAdapter.notifyDataSetChanged();
		}

		@Override
		public PagerAdapter getPagerAdapter() {
			return pagerAdapter;
		}

		@Override
		public IndicatorAdapter getIndicatorAdapter() {
			return indicatorAdapter;
		}

	}

}
