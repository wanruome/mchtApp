package com.ruomm.base.ioc.iocutil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.ruomm.base.ioc.annotation.view.EventClickListener;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

class App_ViewBind {
	/**
	 * 获取包名
	 * 
	 * @param mContext
	 * @return 应用程序包名
	 */
	private static String getPackageName(Context mContext) {
		final String pagename = mContext.getPackageName();
		return pagename;
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
	public static void initInjectAllViews(Object source, Class<?> sourceClass, View sourceView, boolean isForce) {
		Context mContext = sourceView.getContext();
		String mPackageName = getPackageName(mContext);
		Field[] fields = sourceClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {

			for (Field field : fields) {
				try {

					field.setAccessible(true);

					// if (field.get(source) != null) {
					// continue;
					// }

					InjectAll allviewInject = field.getAnnotation(InjectAll.class);
					if (allviewInject != null) {
						Object objinstance = field.get(source);

						String clsSimpleName = field.getType().getSimpleName();
						if (null == objinstance) {
							if (field.getType().getName().endsWith("$" + clsSimpleName)) {

								Constructor<?> constructor = field.getType().getDeclaredConstructor(sourceClass);
								if (null != constructor) {
									constructor.setAccessible(true);
									objinstance = constructor.newInstance(source);
								}
								if (null != objinstance) {
									field.set(source, objinstance);

								}

							}
							// 外部类逻辑
							else {

								Constructor<?> constructor = field.getType().getDeclaredConstructor();
								if (null != constructor) {
									constructor.setAccessible(true);
									objinstance = constructor.newInstance();
								}
								if (null != objinstance) {
									field.setAccessible(true);
									field.set(source, objinstance);

								}

							}
						}
						if (null != objinstance) {
							initInjectView(objinstance, field.getType(), sourceView, source, sourceClass, isForce);
						}
						// 内部类逻辑
						// if (field.getType().getName().endsWith("$" + clsSimpleName)) {
						//
						// Constructor<?> constructor =
						// field.getType().getDeclaredConstructor(sourceClass);
						// if (null != constructor) {
						// constructor.setAccessible(true);
						// objinstance = constructor.newInstance(source);
						// }
						// if (null != objinstance) {
						// field.set(source, objinstance);
						// initInjectView(objinstance, field.getType(), sourceView, source,
						// sourceClass);
						// }
						//
						// }
						// // 外部类逻辑
						// else {
						//
						// Constructor<?> constructor = field.getType().getDeclaredConstructor();
						// if (null != constructor) {
						// constructor.setAccessible(true);
						// objinstance = constructor.newInstance();
						// }
						// if (null != objinstance) {
						// field.setAccessible(true);
						// field.set(source, objinstance);
						// initInjectView(objinstance, field.getType(), sourceView, source,
						// sourceClass);
						// }
						//
						// }

					}
					InjectView viewInject = field.getAnnotation(InjectView.class);
					if (viewInject != null) {
						field.setAccessible(true);
						if (field.get(source) != null) {
							if (isForce) {
								field.set(source, null);
							}
							else {
								continue;
							}
						}
						int viewId = viewInject.id();

						if (viewId == 0) {
							String viewName = viewInject.idName();
							if (TextUtils.isEmpty(viewName)) {
								viewName = field.getName();
							}
							viewId = mContext.getResources().getIdentifier(viewName, "id", mPackageName);

						}

						View v = sourceView.findViewById(viewId);
						field.set(source, v);
						if (!TextUtils.isEmpty(viewInject.click())) {
							setClickListener(v, source, sourceClass, viewInject.click());
						}
					}
				}
				catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param source
	 *            注入的对象，为包含InjectView注解的对象；
	 * @param sourceClass
	 *            source所在对象的类的类型，主要是为了支持继承类的父类；
	 * @param sourceView
	 *            注入的时候View赋值和绑定的来源View；
	 * @param methodSource
	 *            绑定OnClick事件的方法名称
	 * @param methodClass
	 *            绑定OnClick事件的方法所在的类
	 * @param isFroce
	 *            是否强制注入，强制注入则在Field不为空的时候置空并注入，否则则只进行Field为空的时候的注入，若是View多次重新绘制界面则必须为强制模式；
	 */
	public static void initInjectView(Object source, Class<?> sourceClass, View sourceView, Object methodSource,
			Class<?> methodClass, boolean isFroce) {
		Context mContext = sourceView.getContext();
		String mPackageName = getPackageName(mContext);
		Field[] fields = sourceClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {

			for (Field field : fields) {
				try {

					field.setAccessible(true);
					if (field.get(source) != null) {
						if (isFroce) {
							field.set(source, null);
						}
						else {
							continue;
						}
					}
					InjectView viewInject = field.getAnnotation(InjectView.class);
					if (viewInject != null) {
						int viewId = viewInject.id();

						if (viewId == 0) {
							String viewName = viewInject.idName();
							if (TextUtils.isEmpty(viewName)) {
								viewName = field.getName();
							}
							viewId = mContext.getResources().getIdentifier(viewName, "id", mPackageName);

						}
						field.setAccessible(true);
						View v = sourceView.findViewById(viewId);
						field.set(source, v);
						if (!TextUtils.isEmpty(viewInject.click())) {
							setClickListener(v, methodSource, methodClass, viewInject.click());
						}

					}
				}
				catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private static void setClickListener(View v, Object methodSource, Class<?> methodClass, String methodname) {
		if (null != v) {
			v.setOnClickListener(new EventClickListener(methodSource, methodClass).click(methodname));
		}
	}
}
