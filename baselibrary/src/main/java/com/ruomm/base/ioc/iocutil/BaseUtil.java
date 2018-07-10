package com.ruomm.base.ioc.iocutil;

import java.io.Serializable;

import com.ruomm.base.ioc.annotation.util.InjectUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * SharedPreferences存储工具，注解模式View赋值和绑定OnClick事件，序列化serializable对象的Bundle、Intent传递
 *
 * @author Ruby
 */
public class BaseUtil {

//	/**
//	 * 对象写入SharedPreferences存储中
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 * @param key
//	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
//	 * @param object
//	 *            SharedPreferences存储的对象
//	 */
//	public static void saveBean(Context mContext, String key, Object object) {
//		AppStoreProperty.saveBean(mContext, nameProperty, key, object, Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * 从SharedPreferences存储中读取对象
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 * @param key
//	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
//	 * @param T
//	 *            SharedPreferences存储的数据类型，SharedPreferences存储读取完毕后会自动解析，key为空的时候依照此取得读取的key值
//	 * @return 解析好的执行类型的对象T
//	 */
//	public static <T> T getBean(Context mContext, String nameProperty, String key, Class<T> t) {
//		return AppStoreProperty.getBean(mContext, nameProperty, key, t, Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * 从SharedPreferences存储中删除对象
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 * @param key
//	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
//	 * @param cls
//	 *            数据类型
//	 */
//	public static void delBean(Context mContext, String nameProperty, String key, Class<?> cls) {
//		AppStoreProperty.delBean(mContext, nameProperty, key, cls, Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * 从SharedPreferences存储中删除对象
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 * @param key
//	 *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
//	 */
//	public static void delByKey(Context mContext, String nameProperty, String key) {
//		AppStoreProperty.delete(mContext, nameProperty, key, Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * String内容写入SharedPreferences存储中
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 * @param key
//	 *            SharedPreferences存储的key
//	 * @param value
//	 *            SharedPreferences存储的值
//	 */
//	public static void saveString(Context mContext, String nameProperty, String key, String value) {
//		AppStoreProperty.saveString(mContext, nameProperty, key, value, Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * 从SharedPreferences获取存储的String内容
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 * @param key
//	 *            SharedPreferences存储的key
//	 * @return 获取到的String内容
//	 */
//	public static String getString(Context mContext, String nameProperty, String key) {
//		return AppStoreProperty.getString(mContext, nameProperty, key, Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * 清理SharedPreferences存储
//	 *
//	 * @param mContext
//	 * @param nameProperty
//	 *            SharedPreferences存储命名空间，即最终xml文件的名称
//	 */
//	public static void clearSharedPreferences(Context mContext, String nameProperty) {
//		AppStoreProperty.clearSharedPreferences(mContext, nameProperty, Context.MODE_PRIVATE);
//	}

	/**
	 * 放入序列化对象到 Intent
	 *
	 * @param intent
	 * @param serializable
	 *            序列化对象的实例;放入时候的key值为通过对象的数据类型自动获取到的key值，默认为数据类型的SimpleName，
	 *            若是要改变需要添加InjectEntity注解来改变
	 */
	public static void serializablePut(Intent intent, Serializable serializable) {
		if (null == intent || null == serializable) {

			return;
		}
		intent.putExtra(InjectUtil.getBeanKey(serializable.getClass()), serializable);
	}

	/**
	 * 放入序列化对象到 Bundle
	 *
	 * @param mBundle
	 * @param serializable
	 *            序列化对象的实例;放入时候的key值为通过对象的数据类型自动获取到的key值，默认为数据类型的SimpleName，
	 *            若是要改变需要添加InjectEntity注解来改变
	 */
	public static void serializablePut(Bundle mBundle, Serializable serializable) {
		if (null == mBundle || null == serializable) {
			return;
		}
		mBundle.putSerializable(InjectUtil.getBeanKey(serializable.getClass()), serializable);
	}

	/**
	 * 从Bundle读取序列化对象
	 *
	 * @param mBundle
	 * @param T
	 *            对象的数据类型；读取时候的key值为通过对象的数据类型自动获取到的key值，默认为数据类型的SimpleName，
	 *            若是要改变需要添加InjectEntity注解来改变
	 */
	@SuppressWarnings("unchecked")
	public static <T> T serializableGet(Bundle mBundle, Class<T> T) {
		if (null == mBundle || !mBundle.containsKey(InjectUtil.getBeanKey(T))) {
			return null;
		}
		T t = null;
		try {
			t = (T) mBundle.get(InjectUtil.getBeanKey(T));
		}
		catch (Exception e) {
			t = null;
		}
		return t;
	}

	/**
	 * 从Intent读取序列化对象
	 *
	 * @param intent
	 * @param T
	 *            对象的数据类型；读取时候的key值为通过对象的数据类型自动获取到的key值，默认为数据类型的SimpleName，
	 *            若是要改变需要添加InjectEntity注解来改变
	 */
	@SuppressWarnings("unchecked")
	public static <T> T serializableGet(Intent intent, Class<T> T) {
		if (null == intent) {
			return null;
		}
		T t = null;
		try {
			t = (T) intent.getSerializableExtra(InjectUtil.getBeanKey(T));
		}
		catch (Exception e) {
			t = null;
		}
		return t;
	}

	/**
	 * Activity下的注解模式View赋值和绑定OnClick事件
	 *
	 * @param mActivity
	 *            在Activity对象里面的带有InjectView和InjectAll注解的Field会自动赋值和绑定Onlick事件
	 */
	public static void initInjectAll(Activity mActivity) {
		App_ViewBind.initInjectAllViews(mActivity, mActivity.getClass(), mActivity.getWindow().getDecorView(), false);
	}

	/**
	 * Fragment下的注解模式View赋值和绑定OnClick事件
	 *
	 * @param fragment
	 *            在Fragment对象里面带的有InjectView和InjectAll注解的Field会自动赋值和绑定Onlick事件
	 * @param sourceView
	 *            注入的时候View赋值和绑定的来源View；
	 */
	public static void initInjectAll(Fragment fragment, View sourceView) {
		App_ViewBind.initInjectAllViews(fragment, fragment.getClass(), sourceView, true);
	}

	/**
	 * Activity下的注解模式View赋值和绑定OnClick事件
	 *
	 * @param mActivity
	 *            在Activity对象里面的带有InjectView和InjectAll注解的Field会自动赋值和绑定Onlick事件，
	 *            若是绑定的事件在最终运行的类的父类里面需要声明sourceClass类型(即含有injectAll和InjectView注解的Filed的类的类型)
	 * @param sourceClass
	 *            source(mActivity)所在对象的类的类型，主要是为了支持继承类的父类；
	 */
	public static void initInjectAll(Activity mActivity, Class<?> sourceClass) {
		App_ViewBind.initInjectAllViews(mActivity, sourceClass, mActivity.getWindow().getDecorView(), false);
	}

	/**
	 * 注解模式View赋值和绑定OnClick事件
	 *
	 * @param source
	 *            注入的对象，为包含InjectAll和InjectView注解的对象；
	 * @param sourceClass
	 *            source所在对象的类的类型，主要是为了支持继承类的父类；
	 * @param sourceView
	 *            注入的时候View赋值和绑定的来源View；
	 * @param isForce
	 *            是否强制注入，强制注入则在Field不为空的时候置空并注入，否则则只进行Field为空的时候的注入，若是View多次重新绘制界面则必须为强制模式；
	 */
	public static void initInjectAll(Object source, Class<?> sourceClass, View sourceView) {
		App_ViewBind.initInjectAllViews(source, sourceClass, sourceView, false);
	}

	/**
	 * Activity下的注解模式View赋值和绑定OnClick事件
	 *
	 * @param mActivity
	 *            在Activity对象里面的带有InjectView的Field会自动赋值和绑定Onlick事件
	 */
	public static void initInjectedView(Activity mActivity) {
		App_ViewBind.initInjectView(mActivity, mActivity.getClass(), mActivity.getWindow().getDecorView(), mActivity,
				mActivity.getClass(), false);
	}

	/**
	 * Activity下的注解模式View赋值和绑定OnClick事件
	 *
	 * @param mActivity
	 *            在Activity对象里面的带有InjectView的Field会自动赋值和绑定Onlick事件
	 * @param sourceClass
	 *            source(mActivity)所在对象的类的类型，主要是为了支持继承类的父类；
	 */
	public static void initInjectedView(Activity mActivity, Class<?> sourceClass) {
		App_ViewBind.initInjectView(mActivity, sourceClass, mActivity.getWindow().getDecorView(), mActivity,
				sourceClass, false);
	}

	/**
	 * @param source
	 *            注入的对象，为包含InjectView注解的对象；
	 * @param sourceClass
	 *            source所在对象的类的类型，主要是为了支持继承类的父类；
	 * @param sourceView
	 *            注入的时候View赋值和绑定的来源View；
	 */
	public static void initInjectedView(Object source, Class<?> sourceClass, View sourceView) {
		App_ViewBind.initInjectView(source, sourceClass, sourceView, source, sourceClass, false);
	}

}
