package com.ruomm.base.ioc.ormdbutil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ruomm.base.ioc.annotation.InjectDB;
import com.ruomm.base.ioc.annotation.InjectEntity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

/**
 * 数据库Orm处理工具类
 * 
 * @author Ruby
 */
public class DBUtil {
	/**
	 * 数据库表名称
	 */
	private String tableName;
	/**
	 * 数据库表的主键名
	 */
	private String primaryKey;
	/**
	 * 数据库表和数据模型的对照关系列表，依次关联数据库表和数据模型
	 */
	private final ArrayList<DBField_Attrs> listDbField_Attrs;

	/**
	 * 构造函数
	 * 
	 * @param sourceClass
	 *            数据库表对应的数据模型
	 */
	public DBUtil(Class<?> sourceClass) {
		super();
		InjectEntity defineBean = sourceClass.getAnnotation(InjectEntity.class);
		if (null == defineBean || TextUtils.isEmpty(defineBean.tableName())) {
			tableName = new String(sourceClass.getSimpleName());
		}
		else {
			tableName = new String(defineBean.tableName());
		}
		listDbField_Attrs = new ArrayList<DBField_Attrs>();
		Field[] fields = sourceClass.getDeclaredFields();
		for (Field field : fields) {
			InjectDB dbDefine = field.getAnnotation(InjectDB.class);
			if (null != dbDefine) {
				DBField_Attrs dbField_Attrs = new DBField_Attrs();
				dbField_Attrs.field = field;
				dbField_Attrs.typecls = field.getType();
				dbField_Attrs.dataType = dbDefine.type();
				if (DBTool_String.isTrue(dbDefine.cloumn())) {
					dbField_Attrs.column = new String(dbDefine.cloumn());
				}
				else {
					dbField_Attrs.column = new String(field.getName());
				}
				if (dbDefine.primarykey()) {
					dbField_Attrs.dataType = new String(InjectDB.valueConfig_Key);
					primaryKey = new String(dbField_Attrs.column);
				}
				listDbField_Attrs.add(dbField_Attrs);
			}

		}
	}

	/**
	 * 获取数据库表名称
	 * 
	 * @return 数据库表名称
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 获取数据库表主键名
	 * 
	 * @return 数据库表主键名
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * 获取数据库表的列名；依据数据库模型定义的Field的名称获取数据库表列名
	 * 
	 * @param nameField
	 *            数据模型定义的Filed的名称
	 * @return 数据库表的列名
	 */
	public String getColumnName(String nameField) {
		for (DBField_Attrs dbAttrs : listDbField_Attrs) {
			if (dbAttrs.field.getName().equals(nameField)) {
				return new String(dbAttrs.column);
			}
		}
		return null;
	}

	/**
	 * 获取数据库模型定义的Field名称；依据数据库表的列名获取数据库模型定义的Field名称；
	 * 
	 * @param nameColumn
	 *            数据库表的列名
	 * @return 数据库模型定义的Field名称
	 */
	public String getFieldName(String nameColumn) {
		for (DBField_Attrs dbAttrs : listDbField_Attrs) {
			if (dbAttrs.column.equals(nameColumn)) {
				return new String(dbAttrs.field.getName());
			}
		}
		return null;
	}

	/**
	 * 获取查询所需的所有数据库表列名集合
	 * 
	 * @return 查询所需的所有数据库表列名集合
	 */
	public String[] getSelectString() {
		int listsize = listDbField_Attrs.size();
		String[] string = new String[listsize];
		for (int index = 0; index < listsize; index++) {
			string[index] = new String(listDbField_Attrs.get(index).column);
		}
		return string;
	}

	/**
	 * 把查询出来的数据解析为对象集合
	 * 
	 * @param mCursor
	 *            数据库游标
	 * @param T
	 *            待解析的对象类型
	 * @return 解析好的对象集合List
	 */
	@SuppressLint("SimpleDateFormat")
	public <T> ArrayList<T> parseResult(Cursor mCursor, Class<T> T) {
		ArrayList<T> listtemp = new ArrayList<T>();
		if (null == mCursor) {
			return listtemp;
		}
		while (mCursor.moveToNext()) {
			T o = DbTool_Core.getNewInstance(T);
			if (null == o) {
				continue;
			}
			for (DBField_Attrs dbField_Attrs : listDbField_Attrs) {
				DbTool_Core.setObjectAttrs(mCursor, o, dbField_Attrs);
			}
			listtemp.add(o);

		}
		return listtemp;
	}

	/**
	 * 把查询出来的数据倒序解析为对象集合
	 * 
	 * @param mCursor
	 *            数据库游标
	 * @param T
	 *            待解析的对象类型
	 * @return 倒序解析好的对象集合List
	 */
	@SuppressLint("SimpleDateFormat")
	public <T> ArrayList<T> parseResult_Reverse(Cursor mCursor, Class<T> T) {
		ArrayList<T> listtemp = new ArrayList<T>();
		if (null == mCursor) {
			return listtemp;
		}
		if (!mCursor.moveToLast()) {
			return listtemp;
		}
		do {
			T o = DbTool_Core.getNewInstance(T);
			if (null == o) {
				continue;
			}
			for (DBField_Attrs dbField_Attrs : listDbField_Attrs) {
				DbTool_Core.setObjectAttrs(mCursor, o, dbField_Attrs);
			}
			listtemp.add(o);

		}
		while (mCursor.moveToPrevious());
		// while (mCursor.moveToNext()) {
		// T o = DbTool_Core.getNewInstance(T);
		// if (null == o) {
		// continue;
		// }
		// for (DBField_Attrs dbField_Attrs : listDbField_Attrs) {
		// DbTool_Core.setObjectAttrs(mCursor, o, dbField_Attrs);
		// }
		// listtemp.add(o);
		//
		// }
		return listtemp;
	}

	/**
	 * 把查询出来的数据解析成对象，只解析第一个，适合取特定数据
	 * 
	 * @param mCursor
	 *            数据库游标
	 * @param T
	 *            待解析的对象类型
	 * @return 倒序解析好的对象
	 */
	public <T> T parseResultOne(Cursor mCursor, Class<T> T) {
		if (null == mCursor) {
			return null;
		}
		if (mCursor.moveToNext()) {
			T o = DbTool_Core.getNewInstance(T);
			if (null != o) {
				for (DBField_Attrs dbField_Attrs : listDbField_Attrs) {
					DbTool_Core.setObjectAttrs(mCursor, o, dbField_Attrs);
				}
			}
			return o;
		}
		else {
			return null;
		}
	}

	/**
	 * 把对象解析为可用于数据库操作的ContentValues值
	 * 
	 * @param t
	 *            对象
	 * @param isKeyParse
	 *            是否解析主键
	 * @return ContentValues
	 */
	public ContentValues parseContentValue(Object t, boolean isKeyParse) {
		ContentValues contentValues = new ContentValues();
		for (DBField_Attrs dbAttrs : listDbField_Attrs) {

			if (!isKeyParse && InjectDB.valueConfig_Key.equals(dbAttrs.dataType)) {
				continue;
			}
			DbTool_Core.putContentValue(contentValues, t, dbAttrs);
		}

		return contentValues;
	}

	/**
	 * 把对象解析为可用于数据库操作的ContentValues值
	 * 
	 * @param t
	 *            对象
	 * @return ContentValues
	 */
	public ContentValues parseContentValue(Object t) {
		ContentValues contentValues = new ContentValues();
		for (DBField_Attrs dbAttrs : listDbField_Attrs) {
			DbTool_Core.putContentValue(contentValues, t, dbAttrs);
		}
		return contentValues;
	}

	/**
	 * 把对象解析为可用于数据库操作的ContentValues值
	 * 
	 * @param t
	 *            对象
	 * @param listsField
	 *            需要解析的字段名称集合(Filed名称集合)
	 * @return ContentValues
	 */
	public ContentValues parseContentValue(Object t, ArrayList<String> listsField) {
		ContentValues contentValues = null;
		if (null != listsField && listsField.size() > 0) {
			contentValues = new ContentValues();
			for (DBField_Attrs dbAttrs : listDbField_Attrs) {
				if (listsField.contains(dbAttrs.field.getName())) {
					DbTool_Core.putContentValue(contentValues, t, dbAttrs);
				}
			}
		}
		return contentValues;
	}

	/**
	 * 把对象解析为可用于数据库操作的ContentValues值
	 * 
	 * @param t
	 *            对象
	 * @param listsField
	 *            需要解析的字段名称集合(Filed名称集合)
	 * @return ContentValues
	 */
	public ContentValues parseContentValue(Object t, String[] listsField) {
		ContentValues contentValues = null;
		if (null != listsField && listsField.length > 0) {
			contentValues = new ContentValues();
			for (DBField_Attrs dbAttrs : listDbField_Attrs) {
				for (String temp : listsField) {
					if (temp.equals(dbAttrs.field.getName())) {
						DbTool_Core.putContentValue(contentValues, t, dbAttrs);
					}
				}
			}
		}
		return contentValues;
	}

}
