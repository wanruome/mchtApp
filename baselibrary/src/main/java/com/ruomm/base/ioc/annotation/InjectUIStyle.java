package com.ruomm.base.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Activity辅助注解
 *
 * @author Ruby
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectUIStyle {

	/**
	 * UI的样式layout文件的ID，需要自定义Activity基类里面实现不同的UI布局样式的逻辑
	 *
	 * @author Ruby
	 * @return 0,表示默认样式,
	 * @return int
	 */
	public int valueUIStyle() default 0;
	public boolean isEnableBarInit() default true;

}
