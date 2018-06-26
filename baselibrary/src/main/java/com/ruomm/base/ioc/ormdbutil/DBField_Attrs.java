package com.ruomm.base.ioc.ormdbutil;

import java.lang.reflect.Field;

/**
 * 数据库Orm模型的数据库字段属性
 * 
 * @author Ruby
 */
class DBField_Attrs {
	public Field field;
	public String column;
	public String dataType;
	public Class<?> typecls;
}
