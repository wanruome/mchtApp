package com.ruomm.base.ioc.ormdbutil;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ruomm.base.ioc.annotation.InjectDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

/**
 * DBUtil相关处理工具类
 * 
 * @author Ruby
 */
class DbTool_Core {
	/**
	 * 依据数据库查询结果集为对象赋值
	 * 
	 * @param mCursor
	 *            当前游标
	 * @param o
	 *            要赋值的对象
	 * @param dbField_Attrs
	 *            数据库表和对象模型的关系
	 */
	@SuppressLint("SimpleDateFormat")
	public static void setObjectAttrs(Cursor mCursor, Object o, DBField_Attrs dbField_Attrs) {
		try {
			dbField_Attrs.field.setAccessible(true);
			Object value = null;
			int index = mCursor.getColumnIndex(dbField_Attrs.column);
			if (index < 0) {
				return;
			}
			if (dbField_Attrs.typecls == int.class || dbField_Attrs.typecls == Integer.class) {
				value = mCursor.getInt(index);
			}
			else if (dbField_Attrs.typecls == long.class || dbField_Attrs.typecls == Long.class) {
				value = mCursor.getLong(index);
			}
			else if (dbField_Attrs.typecls == float.class || dbField_Attrs.typecls == Float.class) {
				value = mCursor.getFloat(index);
			}
			else if (dbField_Attrs.typecls == double.class || dbField_Attrs.typecls == Double.class) {
				value = mCursor.getDouble(index);
			}
			else if (dbField_Attrs.typecls == short.class || dbField_Attrs.typecls == Short.class) {
				value = mCursor.getShort(index);
			}
			else if (dbField_Attrs.typecls == String.class) {
				value = mCursor.getString(index);
			}
			else if (dbField_Attrs.typecls == byte[].class) {
				value = mCursor.getBlob(index);
			}
			else if (dbField_Attrs.typecls == Date.class) {

				Date date;
				if (InjectDB.valueDateFormat_Mill.equals(dbField_Attrs.dataType)) {
					long time = mCursor.getLong(index);
					date = new Date();
					date.setTime(time);
				}
				else if (InjectDB.valueDateFormat_Second.equals(dbField_Attrs.dataType)) {
					long time = mCursor.getLong(index);
					date = new Date();
					date.setTime(time * 1000);
				}
				else if (InjectDB.valueDateFormat_Minute.equals(dbField_Attrs.dataType)) {
					long time = mCursor.getLong(index);
					date = new Date();
					date.setTime(time * 1000 * 60);
				}
				else if (TextUtils.isEmpty(dbField_Attrs.dataType)) {
					long time = mCursor.getLong(index);
					date = new Date();
					date.setTime(time);
				}
				else {
					SimpleDateFormat sdf = new SimpleDateFormat(dbField_Attrs.dataType);
					try {
						date = sdf.parse(mCursor.getString(index));
					}
					catch (Exception e) {
						date = null;
					}
				}
				value = date;
			}

			dbField_Attrs.field.set(o, value);
		}
		catch (Exception e) {
		}
	}

	/**
	 * 把单个对象模型的Field值转换成ContentValues值
	 * 
	 * @param contentValues
	 *            ContentValues值
	 * @param obj
	 *            对象的实例
	 * @param dbField_Attrs
	 *            需要转换的Filed值和数据库表的关系
	 */
	@SuppressLint("SimpleDateFormat")
	public static void putContentValue(ContentValues contentValues, Object obj, DBField_Attrs dbField_Attrs) {
		try {
			dbField_Attrs.field.setAccessible(true);
			if (dbField_Attrs.typecls == int.class || dbField_Attrs.typecls == Integer.class) {
				int value = dbField_Attrs.field.getInt(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == long.class || dbField_Attrs.typecls == Long.class) {
				long value = dbField_Attrs.field.getLong(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == float.class || dbField_Attrs.typecls == Float.class) {
				float value = dbField_Attrs.field.getFloat(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == double.class || dbField_Attrs.typecls == Double.class) {
				double value = dbField_Attrs.field.getDouble(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == short.class || dbField_Attrs.typecls == Short.class) {
				short value = dbField_Attrs.field.getShort(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == String.class) {
				String value = (String) dbField_Attrs.field.get(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == byte[].class) {
				byte[] value = (byte[]) dbField_Attrs.field.get(obj);
				contentValues.put(dbField_Attrs.column, value);
			}
			else if (dbField_Attrs.typecls == Date.class) {

				Date time = (Date) dbField_Attrs.field.get(obj);
				if (InjectDB.valueDateFormat_Mill.equals(dbField_Attrs.dataType)) {
					if (null == time) {
						contentValues.put(dbField_Attrs.column, 0);
					}
					else {
						long value = time.getTime();
						contentValues.put(dbField_Attrs.column, value);
					}
				}
				else if (InjectDB.valueDateFormat_Second.equals(dbField_Attrs.dataType)) {
					if (null == time) {
						contentValues.put(dbField_Attrs.column, 0);
					}
					else {
						long value = time.getTime() / 1000;
						contentValues.put(dbField_Attrs.column, value);
					}
				}
				else if (InjectDB.valueDateFormat_Minute.equals(dbField_Attrs.dataType)) {
					if (null == time) {
						contentValues.put(dbField_Attrs.column, 0);
					}
					else {
						long value = time.getTime() / (1000 * 60);
						contentValues.put(dbField_Attrs.column, value);
					}
				}
				else if (TextUtils.isEmpty(dbField_Attrs.dataType)) {
					if (null == time) {
						contentValues.put(dbField_Attrs.column, 0);
					}
					else {
						long value = time.getTime();
						contentValues.put(dbField_Attrs.column, value);
					}
				}
				else {
					SimpleDateFormat sdf = new SimpleDateFormat(dbField_Attrs.dataType);
					if (null == time) {
						contentValues.putNull(dbField_Attrs.column);
					}
					else {
						String value = sdf.format(time);
						contentValues.put(dbField_Attrs.column, value);
					}

				}
			}
		}
		catch (Exception e) {

		}
	}

	/**
	 * 依据一个对象类型创建一个对象
	 * 
	 * @param T
	 *            对象类型
	 * @return 对象实例
	 */
	public static <T> T getNewInstance(Class<T> T) {
		try {
			T o = T.newInstance();
			return o;
		}
		catch (Exception e) {
			return null;
		}
	}
}
