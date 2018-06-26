package com.ruomm.base.ioc.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 以View对象List集合构建的PagerAdapter
 * 
 * @author Ruby
 */
public class PagerAdapter_View extends PagerAdapter {
	/**
	 * 数据源集合
	 */
	private final List<View> listviews; //

	/**
	 * 构造方法
	 * 
	 * @param listviews
	 */
	public PagerAdapter_View(List<View> listviews) {
		this.listviews = listviews;
	}

	/**
	 * destoryItem时候ViewPager移除这个View
	 */
	@Override
	public void destroyItem(View container, int arg1, Object arg2) {
		// ((ViewPager) container).removeView(listviews.get(arg1));
		((ViewPager) container).removeView((View) arg2);
	}

	/**
	 * 计算PagerAdapter的页面数量
	 */
	@Override
	public int getCount() {

		return null == listviews ? 0 : listviews.size();
	}

	/**
	 * ViewPager加载时候返回的View
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(listviews.get(position));
		return listviews.get(position);
	}

	/**
	 * 判断对应Item的View和getItem()返回的对象是否一致
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
