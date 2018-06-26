package com.ruomm.base.ioc.iocutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.ioc.annotation.util.InjectUtil;

/**
 * SharedPreferences快速存储类
 * 
 * @author Ruby
 */
class App_StoreProperty {
	/**
	 * 存储数据到SharedPreferences里，若是key为null则是删除数据
	 * 
	 * @param mContext
	 * @param nameProperty
	 *            存储数据的SharedPreferences的空间命名
	 * @param key
	 *            存储数据的Key值
	 * @param value
	 *            存储数据的Value值
	 */
	public static void saveString(Context mContext, String nameProperty, String key, String value) {
		if (TextUtils.isEmpty(nameProperty)) {
			return;
		}
		if (TextUtils.isEmpty(key)) {
			return;
		}
		if (null == value) {
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = properties.edit();
			editor.remove(key);
			editor.commit();
		}
		else {
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = properties.edit();
			editor.putString(key, value);
			editor.commit();
		}
	}

	/**
	 * 获取SharedPreferences里的数据，若是没有则返回null
	 * 
	 * @param mContext
	 * @param nameProperty
	 *            获取数据的SharedPreferences的空间命名
	 * @param key
	 *            获取数据的Key
	 * @return SharedPreferences存储的数据内容
	 */
	public static String getString(Context mContext, String nameProperty, String key) {
		if (TextUtils.isEmpty(nameProperty)) {
			return null;
		}
		if (!TextUtils.isEmpty(key)) {
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, Context.MODE_PRIVATE);
			String value = properties.getString(key, null);
			return value;

		}
		return null;
	}

	/**
	 * 删除SharedPreferences里的数据
	 * 
	 * @param mContext
	 * @param nameProperty
	 *            删除数据的SharedPreferences的空间命名
	 * @param key
	 *            删除数据的Key
	 */
	public static void delete(Context mContext, String nameProperty, String key) {
		if (TextUtils.isEmpty(nameProperty)) {
			return;
		}
		if (TextUtils.isEmpty(key)) {
			return;
		}

		SharedPreferences properties = mContext.getSharedPreferences(nameProperty, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = properties.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 存储对象到SharedPreferences里，若是object为空则不做操作 *
	 * <p>
	 * 添加InjectEntry注解可以修改传入key为空的时候真正执行此方法的key
	 * 真正执行此方法key的规则优先级别为：key不为空>cls的InjectEntry注解的非空beanKey()>object对应cls的simpleName
	 * 
	 * @param mContext
	 * @param nameProperty
	 *            存储对象的SharedPreferences的空间命名
	 * @param key
	 *            存储对象的Key值
	 * @param object
	 *            存储对象的Value值
	 */
	public static void saveBean(Context mContext, String nameProperty, String key, Object object) {
		if (TextUtils.isEmpty(nameProperty)) {
			return;
		}
		String tempKey = null;
		if (TextUtils.isEmpty(key)) {

			if (null != object) {
				tempKey = InjectUtil.getBeanKey(object.getClass());
			}
		}
		else {
			tempKey = key;
		}
		if (TextUtils.isEmpty(tempKey)) {
			return;
		}
		String json = null;
		if (null != object) {
			json = JSON.toJSONString(object);
		}
		else {
			json = null;
		}
		saveString(mContext, nameProperty, tempKey, json);
	}

	/**
	 * 泛型模式获取SharedPreferences里的对象数据，若是没有则返回null *
	 * <p>
	 * 添加InjectEntry注解可以修改传入key为空的时候真正执行此方法的key
	 * 真正执行此方法key的规则优先级别为：key不为空>cls的InjectEntry注解的非空beanKey()>cls的simpleName
	 * 
	 * @param mContext
	 * @param nameProperty
	 *            获取对象的SharedPreferences的空间命名；
	 * @param key
	 *            获取对象的Key；
	 * @param T
	 *            获取对象的的数据类型；
	 * @return SharedPreferences存储的对象数据；
	 */

	public static <T> T getBean(Context mContext, String nameProperty, String key, Class<T> T) {
		if (TextUtils.isEmpty(nameProperty)) {
			return null;
		}
		String tempKey = null;
		if (TextUtils.isEmpty(key)) {

			tempKey = InjectUtil.getBeanKey(T);
		}
		else {
			tempKey = key;
		}
		if (TextUtils.isEmpty(tempKey)) {
			return null;
		}

		try {
			String json = getString(mContext, nameProperty, tempKey);
			return JSON.parseObject(json, T);
		}
		catch (Exception e) {
			return null;
		}

	}

	/**
	 * 删除SharedPreferences里的对象数据
	 * <p>
	 * 添加InjectEntry注解可以修改传入key为空的时候真正执行此方法的key
	 * 真正执行此方法key的规则优先级别为：key不为空>cls的InjectEntry注解的非空beanKey()>cls的simpleName
	 * 
	 * @param mContext
	 * @param nameProperty
	 *            删除对象数据的SharedPreferences的空间命名
	 * @param key
	 *            删除对象数据的Key
	 * @param cls
	 *            删除对象数据的数据类型
	 */
	public static void delBean(Context mContext, String nameProperty, String key, Class<?> cls) {
		String tempKey = null;
		if (TextUtils.isEmpty(key)) {
			tempKey = InjectUtil.getBeanKey(cls);
		}
		else {
			tempKey = key;
		}
		delete(mContext, nameProperty, tempKey);
	}

}
