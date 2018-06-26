/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月22日 上午11:28:22 
 */
package com.ruomm.base.ioc.iocutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.common.greendao.BaseDaoSession;
import com.ruomm.base.common.greendao.DBEntryValue;
import com.ruomm.base.common.greendao.DBEntryValueDao;
import com.ruomm.base.common.greendao.DBEntryValueDao.Properties;
import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.tools.ListUtils;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 对象化的数据存储类
 * <p>
 * 3级tag数据库模型，第一级Tag为key，第二级Tag为tag，第三级Tag为valueTag
 * <p>
 * 第一级tag为必须值不能为空，主要用来区分不同的数据类型，要存储用户订单、用户收货地址是2个不同的东西，就需要不同tag，在存储String内容的时候第一级Tag(key值)必须设置，
 * 存储对象时候第一级Tag(key值)若是为空时候会自动按照对象的数据类型获取第一级Tag，第一级Tag使用对象的数据类型获取的时候可以通过InjectEntity注解来改变key值；
 * <p>
 * 存储只支持精确存储，没有的tag自动按照空值来处理
 * <p>
 * 删除拥有精确删除和列表删除功能，分别对应del和delList，精确删除方法调用时候没有的tag自动按照空值来处理;
 * <p>
 * 获取用户精确获取和集合获取功能，分别对应get和getList，精确获取方法调用时候没有的tag自动按照空值来处理;
 * <p>
 * 示例：saveBean(key,tag,valueTag,value)；要存储一个订单信息，可以这样存储，第一级Tag(key)为：orderinfo;第二级Tag(tag)为：userID;
 * 第三级Tag(valueTag)为orderID，这样就可以精确区分一个订单了
 * 
 * @author Ruby
 */
public class AppStoreDBProperty {
	/**
	 * 单例模式的对象
	 */
	private static AppStoreDBProperty instance;
	/**
	 * Context环境
	 */
	private static Context appContext;
	/**
	 * 数据库Session
	 */
	private BaseDaoSession mDaoSession;
	/**
	 * 数据库Dao
	 */
	private DBEntryValueDao entryDBModelDao;

	/**
	 * 私有构造函数
	 */
	private AppStoreDBProperty() {

	}

	/**
	 * 获取数据库对象化存储单例
	 * 
	 * @return 数据库对象化存储单例
	 */
	public static AppStoreDBProperty getInstance() {
		if (instance == null) {
			instance = new AppStoreDBProperty();
			if (appContext == null) {
				appContext = BaseApplication.getApplication();
			}
		}
		instance.mDaoSession = BaseApplication.getBaseDaoSession();
		if (null == instance.mDaoSession) {
			instance.entryDBModelDao = null;
		}
		else {

			instance.entryDBModelDao = instance.mDaoSession.getDBEntryValueDao();
		}
		return instance;
	}

	/**
	 * 3级存储获取一个存储数据
	 * 
	 * @param realKey
	 *            key值，第一级Tag，建议为存储类型的名称
	 * @param tag
	 *            tag值，第二级Tag，建议为登录的用户名
	 * @param valueTag
	 *            valueTag,第三级Tag，建议为具体存储值的标识
	 * @return 存储的数据
	 */
	private DBEntryValue getEntryValue(String realKey, String tag, String valueTag) {
		if (null == instance.mDaoSession) {
			return null;
		}
		if (TextUtils.isEmpty(realKey)) {
			return null;
		}
		List<DBEntryValue> list = null;
		QueryBuilder<DBEntryValue> qb = entryDBModelDao.queryBuilder();
		if (TextUtils.isEmpty(tag) && TextUtils.isEmpty(valueTag)) {
			list = qb.where(qb.and(Properties.Key.eq(realKey), Properties.Tag.isNull(), Properties.ValueTag.isNull()))
					.list();
		}
		else if (TextUtils.isEmpty(tag)) {
			list = qb.where(
					qb.and(Properties.Key.eq(realKey), Properties.Tag.isNull(), Properties.ValueTag.eq(valueTag)))
					.list();
		}
		else if (TextUtils.isEmpty(valueTag)) {
			list = qb.where(qb.and(Properties.Key.eq(realKey), Properties.Tag.eq(tag), Properties.ValueTag.isNull()))
					.list();
		}
		else {
			list = qb.where(
					qb.and(Properties.Key.eq(realKey), Properties.Tag.eq(tag), Properties.ValueTag.eq(valueTag)))
					.list();
		}
		int listSize = ListUtils.getSize(list);
		if (listSize > 1) {
			for (int i = 1; i < list.size(); i++) {
				entryDBModelDao.delete(list.get(i));
			}
		}
		if (listSize > 0) {
			return list.get(0);
		}
		else {
			return null;
		}

	}

	/**
	 * 删除整个数据，依照第一级Tag
	 * <p>
	 * 第二级Tag为任意值，第三级Tag为任意值
	 * 
	 * @param realKey
	 *            第一级Tag;
	 * @return 删除结果boolean
	 */
	private boolean deleteEntryList(String realKey) {
		if (null == instance.mDaoSession) {
			return false;
		}
		if (TextUtils.isEmpty(realKey)) {
			return false;
		}
		SQLiteDatabase sq = entryDBModelDao.getDatabase();
		sq.beginTransaction();

		int deleteNumber = sq.delete(entryDBModelDao.getTablename(), Properties.Key.columnName + "=?",
				new String[] { realKey });
		sq.setTransactionSuccessful();
		sq.endTransaction();
		if (deleteNumber > 0) {
			return true;
		}
		else {
			return false;
		}

	}

	/**
	 * 删除整个数据结果集，依照第一级Tag和第二级Tag
	 * <p>
	 * 第三级Tag为任意值
	 * 
	 * @param realKey
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @return 删除结果boolean
	 */
	private boolean deleteEntryList(String realKey, String tag) {
		if (null == instance.mDaoSession) {
			return false;
		}
		if (TextUtils.isEmpty(realKey)) {
			return false;
		}
		int deleteNumber = 0;

		SQLiteDatabase sq = entryDBModelDao.getDatabase();
		sq.beginTransaction();
		if (!TextUtils.isEmpty(tag)) {
			deleteNumber = sq.delete(entryDBModelDao.getTablename(), Properties.Key.columnName + "=? and "
					+ Properties.Tag.columnName + "=?", new String[] { realKey, tag });
		}
		else {
			deleteNumber = sq.delete(entryDBModelDao.getTablename(), Properties.Key.columnName + "=? and "
					+ Properties.Tag.columnName + " is NULL", new String[] { realKey });
		}

		sq.setTransactionSuccessful();
		sq.endTransaction();
		if (deleteNumber > 0) {
			return true;
		}
		else {
			return false;
		}

	}

	/**
	 * 获取整个数据库结果集，依照第一级Tag
	 * <p>
	 * 第二级Tag为任意值，第三级Tag为任意值
	 * 
	 * @param realKey
	 *            第一级Tag
	 * @return 数据库结果集
	 */
	private List<DBEntryValue> getEntryValueList(String realKey) {
		if (null == instance.mDaoSession) {
			return null;
		}
		if (TextUtils.isEmpty(realKey)) {
			return null;
		}
		List<DBEntryValue> list = null;
		QueryBuilder<DBEntryValue> qb = entryDBModelDao.queryBuilder();
		list = qb.where(Properties.Key.eq(realKey)).list();

		if (null != list && !list.isEmpty()) {
			return list;

		}
		else {
			return null;
		}
	}

	/**
	 * 获取整个数据库结果集，依照第一级Tag和第二级Tag
	 * <p>
	 * 第三级Tag为任意值
	 * 
	 * @param realKey
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @return 数据库结果集
	 */
	private List<DBEntryValue> getEntryValueList(String realKey, String tag) {
		if (null == instance.mDaoSession) {
			return null;
		}
		if (TextUtils.isEmpty(realKey)) {
			return null;
		}
		List<DBEntryValue> list = null;
		QueryBuilder<DBEntryValue> qb = entryDBModelDao.queryBuilder();
		if (TextUtils.isEmpty(tag)) {
			list = qb.where(qb.and(Properties.Key.eq(realKey), Properties.Tag.isNull())).list();
		}
		else {
			list = qb.where(qb.and(Properties.Key.eq(realKey), Properties.Tag.eq(tag))).list();
		}

		if (null != list && !list.isEmpty()) {
			return list;
		}
		else {
			return null;
		}

	}

	/**
	 * 获取数据库中的内容，依照第一级Tag
	 * <p>
	 * 第二级Tag为空值，第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @return 数据库存储中的结果
	 */
	public String getValue(String key) {
		return getValue(key, null, null);
	}

	/**
	 * 获取数据库中的内容，依照第一级Tag和第二级Tag
	 * <p>
	 * 第三级Tag为空
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @return 数据库存储中的结果
	 */
	public String getValue(String key, String tag) {
		return getValue(key, tag, null);
	}

	/**
	 * 获取数据库中的内容，依照全部Tag
	 * <p>
	 * 第二级Tag为空值，第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @return 数据库存储中的结果
	 */
	public String getValue(String key, String tag, String valueTag) {
		DBEntryValue entryValue = getEntryValue(key, tag, valueTag);
		if (null == entryValue) {
			return null;
		}
		else {
			return entryValue.getValue();
		}
	}

	/**
	 * 存储数据内容到数据库中，按照第一级Tag来存储
	 * <p>
	 * 第二级Tag为空值，第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param value
	 *            要存储的数据内容
	 * @return 数据库存储结果boolean
	 */
	public boolean saveValue(String key, String value) {
		return saveValue(key, null, null, value);
	}

	/**
	 * 存储数据内容到数据库中，按照第一级Tag和第二级Tag来存储
	 * <p>
	 * 第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param value
	 *            要存储的数据内容
	 * @return 数据库存储结果boolean
	 */
	public boolean saveValue(String key, String tag, String value) {
		return saveValue(key, tag, null, value);
	}

	/**
	 * 存储数据内容到数据库中，按照3级Tag来存储
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @param value
	 *            要存储的数据内容
	 * @return 数据库存储结果boolean
	 */
	public boolean saveValue(String key, String tag, String valueTag, String value) {
		if (null == instance.mDaoSession) {
			return false;
		}
		if (TextUtils.isEmpty(key)) {
			return false;
		}
		DBEntryValue entryValue = getEntryValue(key, tag, valueTag);

		if (null != entryValue) {
			entryValue.setValue(value);
			entryValue.setUpdateTime(new Date());
			entryDBModelDao.update(entryValue);
			return true;
		}
		else {
			entryValue = new DBEntryValue();
			entryValue.setKey(key);

			entryValue.setUpdateTime(new Date());

			entryValue.setValue(value);
			if (!TextUtils.isEmpty(tag)) {
				entryValue.setTag(tag);
			}
			if (!TextUtils.isEmpty(valueTag)) {
				entryValue.setValueTag(valueTag);
			}
			long id = entryDBModelDao.insert(entryValue);
			if (id > 0) {
				entryValue.setId(id);
				return true;
			}
			else {
				return false;
			}
		}

	}

	/**
	 * 删除数据库存储中的内容，按照第一级Tag来存储
	 * <p>
	 * 第二级Tag为空值，第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @return 数据库删除结果boolean
	 */
	public boolean delValue(String key) {
		return delValue(key, null, null);
	}

	/**
	 * 删除数据库存储中的内容，按照第一级Tag和第二级Tag来存储
	 * <p>
	 * 第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @return 数据库删除结果boolean
	 */
	public boolean delValue(String key, String tag) {
		return delValue(key, tag, null);
	}

	/**
	 * 删除数据库存储中的内容，按照3级Tag来存储
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @return 数据库删除结果boolean
	 */
	public boolean delValue(String key, String tag, String valueTag) {
		if (null == instance.mDaoSession) {
			return false;
		}
		if (TextUtils.isEmpty(key)) {
			return false;
		}
		DBEntryValue delEntryValue = getEntryValue(key, tag, valueTag);
		if (null != delEntryValue) {
			entryDBModelDao.delete(delEntryValue);
		}
		return true;
	}

	/**
	 * 获取数据库存储的结果集，依照第一级Tag
	 * <p>
	 * 第二级Tag为任意值，第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @return 数据库存储的结果集List
	 */
	public List<String> getValueList(String key) {
		List<DBEntryValue> list = getEntryValueList(key);
		if (null == list) {
			return null;
		}
		else {
			List<String> listString = new ArrayList<String>();
			for (DBEntryValue entryValue : list) {
				listString.add(entryValue.getValue());

			}
			return listString;
		}

	}

	/**
	 * 获取数据库存储的结果集，依照第一级Tag和第二级Tag
	 * <p>
	 * 第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @return 数据库存储的结果集List
	 */
	public List<String> getValueList(String key, String tag) {
		List<DBEntryValue> list = getEntryValueList(key, tag);
		if (null == list) {
			return null;
		}
		else {
			List<String> listString = new ArrayList<String>();
			for (DBEntryValue entryValue : list) {
				listString.add(entryValue.getValue());

			}
			return listString;
		}
	}

	/**
	 * 删除数据库存储的结果集，依照第一级Tag
	 * <p>
	 * 第二级Tag为任意值，第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @return 数据库存储的结果集删除结果boolean
	 */
	public boolean delValueList(String key) {

		return deleteEntryList(key);

	}

	/**
	 * 删除数据库存储的结果集，依照第一级Tag和第二级Tag
	 * <p>
	 * 第三节Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @return 数据库存储的结果集删除结果boolean
	 */
	public boolean delValueList(String key, String tag) {
		return deleteEntryList(key, tag);
	}

	/**
	 * 获取数据库中存储的对象，依照对象模型(第一级Tag通过对象模型获取)
	 * <p>
	 * 第二级Tag为空值，第三节Tag为空值
	 * 
	 * @param cls
	 *            对象类型，可以依照此获取第一级Tag和解析结果内容，tag标识可以使用InjectEntity注解来改变
	 * @return 数据库对象实例(自动解析)
	 */
	public <T> T getBean(Class<T> cls) {
		return getBean(null, null, null, cls);

	}

	/**
	 * 获取数据库中存储的对象，依照第一级Tag或对象类型(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第二级Tag为空值，第三节Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库对象实例(自动解析)
	 */
	public <T> T getBean(String key, Class<T> cls) {
		return getBean(key, null, null, cls);

	}

	/**
	 * 获取数据库中存储的对象，依照第一级Tag和第二级Tag(第一级Tag为空时候第一级Tag按照对象模型自动获取)
	 * <p>
	 * 第三节Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库对象实例(自动解析)
	 */
	public <T> T getBean(String key, String tag, Class<T> cls) {
		return getBean(key, tag, null, cls);

	}

	/**
	 * 获取数据库中存储的对象，依照3级Tag(第一级Tag为空时候第一级Tag按照对象模型自动获取)
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库对象实例(自动解析)
	 */
	public <T> T getBean(String key, String tag, String valueTag, Class<T> cls) {
		String realKey = InjectUtil.getRealBeanKey(key, cls);
		DBEntryValue entryProperty = getEntryValue(realKey, tag, valueTag);
		if (null == entryProperty) {
			return null;
		}
		else {
			try {
				return JSON.parseObject(entryProperty.getValue(), cls);
			}
			catch (Exception e) {
				return null;
			}
		}

	}

	/**
	 * 删除数据库中存储的对象，依照对象模型(第一级Tag通过对象模型获取)
	 * <p>
	 * 第二级Tag为空值，第三节Tag为空值
	 * 
	 * @param cls
	 *            对象类型，可以依照此获取第一级Tag，tag标识可以使用InjectEntity注解来改变
	 * @return 数据库对象删除结果boolean
	 */
	public boolean delBean(Class<?> cls) {
		return delBean(null, null, null, cls);
	}

	/**
	 * 删除数据库中存储的对象，依照第一级Tag或对象类型(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第二级Tag为空值，第三节Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库对象删除结果boolean
	 */
	public boolean delBean(String key, Class<?> cls) {
		return delBean(key, null, null, cls);
	}

	/**
	 * 删除数据库中存储的对象，依照第一级Tag和第二级Tag(第一级Tag为空时候第一级Tag按照对象模型自动获取)
	 * <p>
	 * 第三节Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库对象删除结果boolean
	 */
	public boolean delBean(String key, String tag, Class<?> cls) {
		return delBean(key, tag, null, cls);
	}

	/**
	 * 删除数据库中存储的对象，依照3级Tag(第一级Tag为空时候第一级Tag按照对象模型自动获取)
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库对象删除结果boolean
	 */
	public boolean delBean(String key, String tag, String valueTag, Class<?> cls) {
		if (null == instance.mDaoSession) {
			return false;
		}
		String realKey = InjectUtil.getRealBeanKey(key, cls);
		if (TextUtils.isEmpty(realKey)) {
			return false;
		}
		DBEntryValue delEntryProperty = getEntryValue(realKey, tag, valueTag);
		if (null != delEntryProperty) {
			entryDBModelDao.delete(delEntryProperty);
			return true;
		}
		else {
			return false;
		}

	}

	/**
	 * 存储对象到数据库中，依照数据模型自动存储，第一级Tag为对象的数据类型的SimlpeName，可以通过InjectEntity注解来改变
	 * <p>
	 * 第二级Tag为空值，第三节Tag为空值
	 * 
	 * @param value
	 *            对象数据实例
	 * @return 数据库对象存储结果boolean
	 */
	public boolean saveBean(Object value) {
		return saveBean(null, null, null, value);
	}

	/**
	 * 储对象到数据库中，依照第一级Tag或对象类型(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第二级Tag为空值，第三节Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param value
	 *            对象数据实例
	 * @return 数据库对象存储结果boolean
	 */
	public boolean saveBean(String key, Object value) {
		return saveBean(key, null, null, value);
	}

	/**
	 * 储对象到数据库中，依照第一级Tag和第二级Tag和(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第三级Tag为空值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param value
	 *            对象数据实例
	 * @return 数据库对象存储结果boolean
	 */
	public boolean saveBean(String key, String tag, Object value) {
		return saveBean(key, tag, null, value);
	}

	/**
	 * 储对象到数据库中，依照3级Tag(第一级Tag为空的时候用对象模型获取)
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param valueTag
	 *            第三级Tag
	 * @param value
	 *            对象数据实例
	 * @return 数据库对象存储结果boolean
	 */
	public boolean saveBean(String key, String tag, String valueTag, Object value) {
		if (null == instance.mDaoSession) {
			return false;
		}
		if (null == value) {
			return false;
		}
		String realKey = InjectUtil.getRealBeanKey(key, value.getClass());
		if (TextUtils.isEmpty(realKey)) {
			return false;
		}
		DBEntryValue entryValue = getEntryValue(realKey, tag, valueTag);
		if (null != entryValue) {
			String json = JSON.toJSONString(value);
			entryValue.setValue(json);
			entryValue.setUpdateTime(new Date());
			entryDBModelDao.update(entryValue);
			return true;
		}
		else {
			entryValue = new DBEntryValue();
			String json = JSON.toJSONString(value);
			entryValue.setKey(realKey);
			entryValue.setUpdateTime(new Date());
			entryValue.setValue(json);
			if (!TextUtils.isEmpty(tag)) {
				entryValue.setTag(tag);
			}
			if (!TextUtils.isEmpty(valueTag)) {
				entryValue.setValueTag(valueTag);
			}
			long id = entryDBModelDao.insert(entryValue);
			if (id > 0) {
				entryValue.setId(id);
				return true;
			}

			else {
				return false;
			}
		}

	}

	/**
	 * 获取数据库存储的对象结果集，依照数据模型自动获取，第一级Tag为对象的数据类型的SimlpeName，可以通过InjectEntity注解来改变
	 * <p>
	 * 第二级和第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库中的对象结果集List
	 */
	public <T> List<T> getBeanList(Class<T> cls) {
		String realKey = InjectUtil.getRealBeanKey(null, cls);
		List<DBEntryValue> list = getEntryValueList(realKey);
		if (null == list) {
			return null;
		}
		else {
			List<T> listT = new ArrayList<T>();
			for (DBEntryValue entryValue : list) {
				try {
					T t = JSON.parseObject(entryValue.getValue(), cls);
					listT.add(t);
				}
				catch (Exception e) {
					// TODO: handle exception
				}

			}
			return listT;

		}

	}

	/**
	 * 获取数据库存储的对象结果集，依据第一级Tag或数据模型(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第二级和第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库中的对象结果集List
	 */
	public <T> List<T> getBeanList(String key, Class<T> cls) {
		String realKey = InjectUtil.getRealBeanKey(key, cls);
		List<DBEntryValue> list = getEntryValueList(realKey);
		if (null == list) {
			return null;
		}
		else {
			List<T> listT = new ArrayList<T>();
			for (DBEntryValue entryValue : list) {
				try {
					T t = JSON.parseObject(entryValue.getValue(), cls);
					listT.add(t);
				}
				catch (Exception e) {
					// TODO: handle exception
				}

			}
			return listT;

		}

	}

	/**
	 * 获取数据库存储的对象结果集，依据第一级Tag和第二级Tag(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库中的对象结果集List
	 */
	public <T> List<T> getBeanList(String key, String tag, Class<T> cls) {
		String realKey = InjectUtil.getRealBeanKey(key, cls);
		List<DBEntryValue> list = getEntryValueList(realKey, tag);
		if (null == list) {
			return null;
		}
		else {
			List<T> listT = new ArrayList<T>();
			for (DBEntryValue entryValue : list) {
				try {
					T t = JSON.parseObject(entryValue.getValue(), cls);
					listT.add(t);
				}
				catch (Exception e) {
					// TODO: handle exception
				}

			}
			return listT;

		}
	}

	/**
	 * 删除数据库存储的对象结果集，依照数据模型自动删除，第一级Tag为对象的数据类型的SimlpeName，可以通过InjectEntity注解来改变
	 * <p>
	 * 第二级和第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库中的对象集删除结果boolean
	 */
	public boolean delBeanList(Class<?> cls) {
		String realKey = InjectUtil.getRealBeanKey(null, cls);
		return deleteEntryList(realKey);
	}

	/**
	 * 删除数据库存储的对象结果集，依据第一级Tag或数据模型(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第二级和第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库中的对象集删除结果boolean
	 */
	public boolean delBeanList(String key, Class<?> cls) {
		String realKey = InjectUtil.getRealBeanKey(key, cls);
		return deleteEntryList(realKey);

	}

	/**
	 * 删除数据库存储的对象结果集，依据第一级Tag和第二级Tag(第一级Tag为空的时候用对象模型获取)
	 * <p>
	 * 第三级Tag为任意值
	 * 
	 * @param key
	 *            第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @param cls
	 *            对象类型
	 * @return 数据库中的对象集删除结果boolean
	 */
	public boolean delBeanList(String key, String tag, Class<?> cls) {
		String realKey = InjectUtil.getRealBeanKey(key, cls);
		return deleteEntryList(realKey, tag);

	}

	/**
	 * 删除数据库存储的对象结果集，第一级Tag通过对象类型自动获取，第二级Tag为用户获取
	 * <p>
	 * 第三级Tag为任意值
	 * 
	 * @param cls
	 *            对象类型，通过此获取第一级Tag
	 * @param tag
	 *            第二级Tag
	 * @return
	 */
	public boolean delBeanListWithTag(Class<?> cls, String tag) {
		String realKey = InjectUtil.getRealBeanKey(null, cls);
		return deleteEntryList(realKey, tag);
	}

}
