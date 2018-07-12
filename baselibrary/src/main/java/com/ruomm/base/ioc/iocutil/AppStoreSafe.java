package com.ruomm.base.ioc.iocutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.base.tools.FileUtils;

import java.io.File;

/**
 * 对象化SharedPreferences存储类
 *
 * @author Ruby
 */
class AppStoreSafe {
	private static final String TAG=AppStoreSafe.class.getSimpleName();
	private static AppStoreSafeInterface mAppStoreSafeInterface=null;
	public static void setAppStoreSafeInterface(AppStoreSafeInterface appStoreSafeInterface)
	{
		mAppStoreSafeInterface=appStoreSafeInterface;
	}
	/**
	 * String内容写入SharedPreferences存储中
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param key
	 *            SharedPreferences存储的key
	 * @param value
	 *            SharedPreferences存储的值
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 */
	public static void saveString(Context mContext, String nameProperty, String key, String value, int PropertyMODE) {
		if(null==mAppStoreSafeInterface)
		{
			Log.i(TAG,"请先设置安全接口解密方式");
			return;
		}
		if (TextUtils.isEmpty(nameProperty)) {
			return;
		}
		if (TextUtils.isEmpty(key)) {
			return;
		}
		if (null == value) {
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, PropertyMODE);
			SharedPreferences.Editor editor = properties.edit();
			editor.remove(key);
			editor.commit();
		}
		else {
			String realValue=mAppStoreSafeInterface.encryptStr(value);
			if(TextUtils.isEmpty(realValue)){
				return;
			}
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, PropertyMODE);
			SharedPreferences.Editor editor = properties.edit();
			editor.putString(key,mAppStoreSafeInterface.encryptStr(value));
			editor.commit();
		}
	}

	/**
	 * 从SharedPreferences获取存储的String内容
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param key
	 *            SharedPreferences存储的key
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 * @return 获取到的String内容
	 */
	public static String getString(Context mContext, String nameProperty, String key, int PropertyMODE) {
		if(null==mAppStoreSafeInterface)
		{
			Log.i(TAG,"请先设置安全接口解密方式");
			return null;
		}
		if (TextUtils.isEmpty(nameProperty)) {
			return null;
		}
		if (!TextUtils.isEmpty(key)) {
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, PropertyMODE);
			String value = properties.getString(key, null);
			return mAppStoreSafeInterface.decryptStr(value);

		}
		return null;
	}

	/**
	 * 从SharedPreferences删除存储的String内容
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param key
	 *            SharedPreferences存储的key
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 */
	public static void delete(Context mContext, String nameProperty, String key, int PropertyMODE) {
		if(null==mAppStoreSafeInterface)
		{
			Log.i(TAG,"请先设置安全接口解密方式");
			return;
		}
		if (TextUtils.isEmpty(nameProperty)) {
			return;
		}
		if (TextUtils.isEmpty(key)) {
			return;
		}
		SharedPreferences properties = mContext.getSharedPreferences(nameProperty, PropertyMODE);
		SharedPreferences.Editor editor = properties.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 对象写入SharedPreferences存储中
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param key
	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
	 * @param object
	 *            SharedPreferences存储的对象
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 */
	public static void saveBean(Context mContext, String nameProperty, String key, Object object, int PropertyMODE) {
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
		saveString(mContext, nameProperty, tempKey, json, PropertyMODE);
	}

	/**
	 * 从SharedPreferences存储中读取对象
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param key
	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
	 * @param T
	 *            SharedPreferences存储的数据类型，SharedPreferences存储读取完毕后会自动解析，key为空的时候依照此取得读取的key值
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 * @return 解析好的执行类型的对象T
	 */
	public static <T> T getBean(Context mContext, String nameProperty, String key, Class<T> T, int PropertyMODE) {
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
			String json = getString(mContext, nameProperty, tempKey, PropertyMODE);
			return JSON.parseObject(json, T);
		}
		catch (Exception e) {
			return null;
		}

	}

	/**
	 * 从SharedPreferences存储中删除对象
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param key
	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
	 * @param cls
	 *            数据类型
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 */
	public static void delBean(Context mContext, String nameProperty, String key, Class<?> cls, int PropertyMODE) {
		String tempKey = null;
		if (TextUtils.isEmpty(key)) {
			tempKey = InjectUtil.getBeanKey(cls);
		}
		else {
			tempKey = key;
		}
		delete(mContext, nameProperty, tempKey, PropertyMODE);
	}

	/**
	 * 清理SharedPreferences存储
	 *
	 * @param mContext
	 * @param nameProperty
	 *            SharedPreferences存储命名空间，即最终xml文件的名称
	 * @param PropertyMODE
	 *            SharedPreferences存储的权限
	 */
	public static void clearSharedPreferences(Context mContext, String nameProperty, int PropertyMODE) {
		if (TextUtils.isEmpty(nameProperty)) {
			return;
		}
		try {
			SharedPreferences properties = mContext.getSharedPreferences(nameProperty, PropertyMODE);
			SharedPreferences.Editor editor = properties.edit();
			if (null != editor) {
				editor.clear();
				editor.commit();
			}
			File file = new File(
					FileUtils.getPathContext(mContext, "shared_prefs" + File.separator + nameProperty + ".xml"));
			if (null != file && file.exists()) {
				file.delete();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

}
