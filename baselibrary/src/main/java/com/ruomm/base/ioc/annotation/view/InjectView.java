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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * View的注解,配合BaseUtil使用
 * <p>
 * 注入模式View赋值和事件绑定的注解，使用此注解会自动为有此注解的View声明对象赋值和事件注入</h1>
 * <p>
 * View注入模式辅助优先级别为 id大于0>idName不为空>(id小于等于0和idName为空)，使用id时候要和声明的View的id相同，idName要是声明的View的id的命名相同，
 * 若是id小于等于0并且idName也为空，则必须保证声明的View的fieldName和id的命名相同
 * 
 * @author Ruby
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
	/**
	 * 注入模式View的id
	 * 
	 * @return
	 */
	public int id() default 0;

	/**
	 * 注入模式View的id的名称
	 * 
	 * @return
	 */
	public String idName() default "";

	/**
	 * 注入模式View的OnClick事件名称
	 * 
	 * @return
	 */
	public String click() default "";

	// public String longClick() default "";
	//
	// public String itemClick() default "";
	//
	// public String itemLongClick() default "";
	//
	// public Select select() default @Select(selected = "");
}
