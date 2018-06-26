package com.ruomm.base.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库Orm模型的辅助注解类
 * 
 * @author Ruby
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
@Inherited
public @interface InjectDB {
	/**
	 * 数据库对应的Key
	 */
	public static final String valueConfig_Key = "key";
	/**
	 * 数据库存储日期时间时候的存储精度级别，"mill"-毫秒，"ss"-秒，"mm"-分
	 */
	public static final String valueDateFormat_Mill = "mill";
	public static final String valueDateFormat_Second = "ss";
	public static final String valueDateFormat_Minute = "mm";

	/**
	 * 是否主键
	 * 
	 * @return
	 */
	public boolean primarykey() default false;

	/**
	 * 对象属性对应的数据库cloumn的名称
	 * 
	 * @return
	 */
	public String cloumn();

	/**
	 * 对象数据在数据库里面存储的类型，基本数据类型不需要此注解，自动识别，日期格式需要此注解来识别存储模式和存储精度
	 * 
	 * @return
	 */
	public String type() default "";
}
