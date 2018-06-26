package com.ruomm.base.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在acitivty上 标记了 当前activity的布局 来代替setContentView，需要是想相关的逻辑
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLayer {

	/**
	 * 注入的View布局的id
	 * 
	 * @return
	 */
	public int value() default 0;

}
