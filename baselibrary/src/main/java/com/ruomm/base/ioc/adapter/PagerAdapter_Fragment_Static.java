package com.ruomm.base.ioc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 以Fragment构建的StatePagerAdapter
 * 
 * @author Ruby
 */
public class PagerAdapter_Fragment_Static extends FragmentStatePagerAdapter {
	/**
	 * Fragment集合
	 */
	List<Fragment> listfrg;
	/**
	 * pager的标题集合
	 */
	List<String> listtitle;

	/**
	 * 构造函数，pager没有标题模式
	 * 
	 * @param fm
	 * @param listfrg
	 */
	public PagerAdapter_Fragment_Static(FragmentManager fm, List<Fragment> listfrg) {
		super(fm);
		this.listfrg = listfrg;
		listtitle = null;
	}

	/**
	 * 构造函数，pager有标题模式
	 * 
	 * @param fm
	 * @param listfrg
	 * @param listtitle
	 */
	public PagerAdapter_Fragment_Static(FragmentManager fm, ArrayList<Fragment> listfrg, ArrayList<String> listtitle) {
		super(fm);
		if (null != listtitle && null != listfrg) {
			for (int temp = listtitle.size(); temp < listfrg.size(); temp++) {
				listtitle.add("页卡" + String.format("%02d", temp));
			}
		}
		this.listfrg = listfrg;
		this.listtitle = listtitle;
	}

	/**
	 * 返回Item对应的Fragment
	 */
	@Override
	public Fragment getItem(int arg0) {
		return listfrg.get(arg0);
	}

	/**
	 * 计算PagerAdapter的页面数量
	 */
	@Override
	public int getCount() {
		return null == listfrg ? 0 : listfrg.size();
	}

	/**
	 * 返回对应位置pager的标题
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return null == listtitle ? "页卡" + String.format("%02d", position) : listtitle.get(position);
	}

}
