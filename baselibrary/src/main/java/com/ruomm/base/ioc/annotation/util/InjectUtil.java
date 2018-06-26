package com.ruomm.base.ioc.annotation.util;

import com.ruomm.base.ioc.annotation.InjectEntity;
import com.ruomm.base.ioc.annotation.InjectFragment;
import com.ruomm.base.ioc.annotation.InjectLayer;
import com.ruomm.base.ioc.annotation.InjectUIStyle;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;

public class InjectUtil {
	/**
	 * 获取Bean模型的Key
	 * 
	 * @param cls
	 *            数据类型
	 * @return
	 */
	public static String getBeanKey(Class<?> cls) {
		InjectEntity injectEntity = cls.getAnnotation(InjectEntity.class);

		if (null == injectEntity || TextUtils.isEmpty(injectEntity.beanKey())) {
			return cls.getSimpleName();
		}
		else {
			return injectEntity.beanKey();
		}
	}

	/**
	 * 获取Bean模型的List的Key
	 * 
	 * @param cls
	 *            数据类型
	 * @return
	 */
	public static String getBeanListKey(Class<?> cls) {
		InjectEntity injectEntity = cls.getAnnotation(InjectEntity.class);

		if (null == injectEntity || TextUtils.isEmpty(injectEntity.beanKey())) {
			return cls.getSimpleName() + "List";
		}
		else {
			return injectEntity.beanKey() + "List";
		}
	}

	/**
	 * 获取需要存储读取Bean模型Value值的真正Key
	 * 
	 * @param key
	 *            自定义的key；
	 * @param cls
	 *            数据对象模型；
	 * @return 真正的Key值；
	 */
	public static String getRealBeanKey(String key, Class<?> cls) {
		if (!TextUtils.isEmpty(key)) {
			return key;
		}
		else {
			return getBeanKey(cls);
		}
	}

	/**
	 * 判断给出的字符串是否和Bean类型的Key值相同
	 * 
	 * @param tag
	 * @param cls
	 * @return
	 */
	public static boolean isBeanKeyEqual(String tag, Class<?> cls) {
		if (TextUtils.isEmpty(tag) || null == cls) {
			return false;
		}
		else {
			return tag.equals(getBeanKey(cls));
		}
	}

	/**
	 * 获取注入时候真实的Layout布局文件ID
	 * 
	 * @param cls
	 * @return
	 */
	public static int getLayoutCurrent(Class<?> cls) {
		InjectLayer injectLayer = cls.getAnnotation(InjectLayer.class);
		if (null != injectLayer) {
			return injectLayer.value();
		}
		else {
			return 0;
		}
	}

	/**
	 * 获取注入时候真实的布局样式注解
	 * 
	 * @param cls
	 *            注解的类
	 * @return
	 */

	public static InjectUIStyle getInjectUIStyle(Class<?> cls) {
		InjectUIStyle injectLayer = cls.getAnnotation(InjectUIStyle.class);
		return injectLayer;
	}

	/**
	 * 获取注入时候真实的布局样式注解
	 * 
	 * @param cls
	 *            注解的类
	 * @param desCls
	 *            最终的类
	 * @return
	 */
	public static InjectUIStyle getInjectUIStyle(Class<?> cls, Class<?> desCls) {
		if (null == desCls) {
			return getInjectUIStyle(cls);
		}
		Class<?> currentCls = null;
		InjectUIStyle injectLayer = null;
		while (null == injectLayer) {
			if (null == currentCls) {
				currentCls = cls;
			}
			else {
				currentCls = currentCls.getSuperclass();
			}
			injectLayer = getInjectUIStyle(currentCls);
			if (null != injectLayer || null == currentCls || currentCls.getName().endsWith(desCls.getName())) {
				break;
			}

		}

		return injectLayer;
	}

	/**
	 * 返回ViewPager内嵌的Fragment的刷新参数，PagerAdapter.
	 * POSITION_UNCHANGED是PagerAdapter在notifyDataSetChanged时候不刷新 PagerAdapter.POSITION_NONE
	 * 是PagerAdapter在notifyDataSetChanged时候刷新
	 * 
	 * @param object
	 * @return
	 */
	public static int getPagerAdapterRefreshStatus(Object object) {
		if (null == object) {
			return PagerAdapter.POSITION_UNCHANGED;
		}
		InjectFragment injectFragment = object.getClass().getAnnotation(InjectFragment.class);
		if (null == injectFragment) {
			return PagerAdapter.POSITION_UNCHANGED;
		}
		if (injectFragment.isRefresh()) {
			return PagerAdapter.POSITION_NONE;
		}
		else {
			return PagerAdapter.POSITION_UNCHANGED;
		}
	}

}
