/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年5月10日 上午9:39:02 
 */
package com.ruomm.base.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>代码混淆时候的加上此注解可以不混淆</h1>
 * <p>
 * 如需使用请网络搜索"Proguard 部分类不混淆的技巧"或者"NotProguard"
 * 
 * @author Ruby
 */
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
public @interface NotProguard {

}
