/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruomm.base.ioc.annotation.view;

import java.lang.reflect.Method;

import com.ruomm.base.ioc.exception.ViewException;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * View注解绑定事件的处理类
 * 
 * @author Ruby
 */
public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener,
		OnItemSelectedListener, OnItemLongClickListener {
	/**
	 * view注入事件所在的对象
	 */
	private final Object handler;
	/**
	 * view注入事件所在的对象类模型
	 */
	private final Class<?> handlercls;
	/**
	 * view注入onClick事件的方法名称
	 */
	private String clickMethod;
	/**
	 * view注入onLongClick事件的方法名称
	 */
	private String longClickMethod;
	/**
	 * view注入onItemClick事件的方法名称
	 */
	private String itemClickMethod;
	/**
	 * view注入onItemSelect事件的方法名称
	 */
	private String itemSelectMethod;
	/**
	 * view注入onNothingSelected事件的方法名称
	 */
	private String nothingSelectedMethod;
	/**
	 * view注入onItemLongClick事件的方法名称
	 */
	private String itemLongClickMehtod;

	public EventListener(Object handler, Class<?> handlercls) {
		this.handler = handler;
		this.handlercls = handlercls;
	}

	public EventListener click(String method) {
		this.clickMethod = method;
		return this;
	}

	public EventListener longClick(String method) {
		this.longClickMethod = method;
		return this;
	}

	public EventListener itemLongClick(String method) {
		this.itemLongClickMehtod = method;
		return this;
	}

	public EventListener itemClick(String method) {
		this.itemClickMethod = method;
		return this;
	}

	public EventListener select(String method) {
		this.itemSelectMethod = method;
		return this;
	}

	public EventListener noSelect(String method) {
		this.nothingSelectedMethod = method;
		return this;
	}

	/**
	 * onLongClick方法，调用注入方法
	 */
	@Override
	public boolean onLongClick(View v) {
		return invokeLongClickMethod(handler, handlercls, longClickMethod, v);
	}

	/**
	 * onItemLongClick方法，调用注入方法
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		return invokeItemLongClickMethod(handler, handlercls, itemLongClickMehtod, arg0, arg1, arg2, arg3);
	}

	/**
	 * onItemSelected方法，调用注入方法
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		invokeItemSelectMethod(handler, handlercls, itemSelectMethod, arg0, arg1, arg2, arg3);
	}

	/**
	 * onNothingSelected方法，调用注入方法
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		invokeNoSelectMethod(handler, handlercls, nothingSelectedMethod, arg0);
	}

	/**
	 * onItemClick方法，调用注入方法
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		invokeItemClickMethod(handler, handlercls, itemClickMethod, arg0, arg1, arg2, arg3);
	}

	/**
	 * OnClick方法，调用注入方法
	 */
	@Override
	public void onClick(View v) {

		invokeClickMethod(handler, handlercls, clickMethod, v);
	}

	/**
	 * View的OnClick事件注入
	 * 
	 * @param handler
	 *            view注入事件所在的对象
	 * @param handlercls
	 *            view注入事件所在的对象类模型
	 * @param methodName
	 *            view注入事件的方法名称
	 * @param params
	 *            需要注入的事件的View对象
	 * @return
	 */
	private static Object invokeClickMethod(Object handler, Class<?> handlercls, String methodName, Object... params) {
		if (handler == null) {
			return null;
		}
		Method method = null;
		try {
			method = handlercls.getDeclaredMethod(methodName, View.class);

			if (method != null) {
				method.setAccessible(true);
				return method.invoke(handler, params);
			}
			else {
				throw new ViewException("no such method:" + methodName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * View的OnLongClick事件注入
	 */

	private static boolean invokeLongClickMethod(Object handler, Class<?> handlercls, String methodName,
			Object... params) {
		if (handler == null) {
			return false;
		}
		Method method = null;
		try {
			// public boolean onLongClick(View v)
			method = handlercls.getDeclaredMethod(methodName, View.class);
			if (method != null) {
				method.setAccessible(true);
				Object obj = method.invoke(handler, params);
				return obj == null ? false : Boolean.valueOf(obj.toString());
			}
			else {
				throw new ViewException("no such method:" + methodName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * View的OnItemClick事件注入
	 */
	private static Object invokeItemClickMethod(Object handler, Class<?> handlercls, String methodName,
			Object... params) {
		if (handler == null) {
			return null;
		}
		Method method = null;
		try {
			// /onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			method = handlercls.getDeclaredMethod(methodName, AdapterView.class, View.class, int.class, long.class);
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(handler, params);
			}
			else {
				throw new ViewException("no such method:" + methodName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * View的OnItemLongClick事件注入
	 */
	private static boolean invokeItemLongClickMethod(Object handler, Class<?> handlercls, String methodName,
			Object... params) {
		if (handler == null) {
			throw new ViewException("invokeItemLongClickMethod: handler is null :");
		}
		Method method = null;
		try {
			// /onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			method = handlercls.getDeclaredMethod(methodName, AdapterView.class, View.class, int.class, long.class);
			if (method != null) {
				method.setAccessible(true);
				Object obj = method.invoke(handler, params);
				return Boolean.valueOf(obj == null ? false : Boolean.valueOf(obj.toString()));
			}
			else {
				throw new ViewException("no such method:" + methodName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * View的OnItemSelected事件注入
	 */
	private static Object invokeItemSelectMethod(Object handler, Class<?> handlercls, String methodName,
			Object... params) {
		if (handler == null) {
			return null;
		}
		Method method = null;
		try {
			// /onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			method = handlercls.getDeclaredMethod(methodName, AdapterView.class, View.class, int.class, long.class);
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(handler, params);
			}
			else {
				throw new ViewException("no such method:" + methodName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * View的onNothingSelected事件注入
	 */
	private static Object invokeNoSelectMethod(Object handler, Class<?> handlercls, String methodName, Object... params) {
		if (handler == null) {
			return null;
		}
		Method method = null;
		try {
			// onNothingSelected(AdapterView<?> arg0)
			method = handlercls.getDeclaredMethod(methodName, AdapterView.class);
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(handler, params);
			}
			else {
				throw new ViewException("no such method:" + methodName);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
