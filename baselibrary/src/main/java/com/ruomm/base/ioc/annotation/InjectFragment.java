package com.ruomm.base.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在fragment上 标记了 当前fragment在ViewPager中的刷新状态，若是isRefresh为True则fragment会刷新，否则不会刷新
 * 
 * @author Ruby
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectFragment {

	/**
	 * 在ViewPager的Fragment变换时候是否刷新Framgment
	 * 
	 * @return
	 */
	public boolean isRefresh() default false;
}
