package com.ruomm.base.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface InjectEntity {
	/**
	 * 对象模型的Key值，AppStoreDBProperty、AppStoreProperty、BaseUtil对象化默认传参、存储的Key值
	 * 
	 * @return
	 */
	public String beanKey();

	/**
	 * 对象模型对应的数据库名称
	 * 
	 * @return
	 */
	public String tableName() default "";

}
