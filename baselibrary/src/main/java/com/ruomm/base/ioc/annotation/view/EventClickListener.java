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

/**
 * View注解模式事件注入辅助类
 * 
 * @author Ruby
 */
public class EventClickListener implements OnClickListener {

	/**
	 * view注入事件所在的对象
	 */
	private final Object handler;
	/**
	 * view注入事件所在的对象类模型
	 */
	private final Class<?> handlercls;
	/**
	 * view注入事件的方法名称
	 */
	private String clickMethod;

	public EventClickListener(Object handler, Class<?> handlercls) {
		this.handler = handler;
		this.handlercls = handlercls;
	}

	public EventClickListener click(String method) {
		this.clickMethod = method;
		return this;
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

}
