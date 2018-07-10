package com.ruomm.base.ioc.iocutil;

import android.content.Context;

import com.ruomm.baseconfig.BaseConfig;

public class AppStoreUtil {
    private static final String SAFESTORE_APPEND="";
    /**
     * 对象写入SharedPreferences存储中
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param object
     *            SharedPreferences存储的对象
     */
    public static void saveBean(Context mContext, String nameProperty, String key, Object object) {
        AppStoreUnSafe.saveBean(mContext, nameProperty, key, object, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中读取对象
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param  <T>
     *            SharedPreferences存储的数据类型，SharedPreferences存储读取完毕后会自动解析，key为空的时候依照此取得读取的key值
     * @return 解析好的执行类型的对象T
     */
    public static <T> T getBean(Context mContext, String nameProperty, String key, Class<T> t) {
        return AppStoreUnSafe.getBean(mContext, nameProperty, key, t, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param cls
     *            数据类型
     */
    public static void delBean(Context mContext, String nameProperty, String key, Class<?> cls) {
        AppStoreUnSafe.delBean(mContext, nameProperty, key, cls, Context.MODE_PRIVATE);
    }



    /**
     * String内容写入SharedPreferences存储中
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key
     * @param value
     *            SharedPreferences存储的值
     */
    public static void saveString(Context mContext, String nameProperty, String key, String value) {
        AppStoreUnSafe.saveString(mContext, nameProperty, key, value, Context.MODE_PRIVATE);
    }
    /**
     * 从SharedPreferences获取存储的String内容
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key
     * @return 获取到的String内容
     */
    public static String getString(Context mContext, String nameProperty, String key) {
        return AppStoreUnSafe.getString(mContext, nameProperty, key, Context.MODE_PRIVATE);
    }
    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     */
    public static void delString(Context mContext, String nameProperty, String key) {
        AppStoreUnSafe.delete(mContext, nameProperty, key, Context.MODE_PRIVATE);
    }
    /**
     * 清理SharedPreferences存储
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     */
    public static void clearSharedPreferences(Context mContext, String nameProperty) {
        AppStoreUnSafe.clearSharedPreferences(mContext, nameProperty, Context.MODE_PRIVATE);
    }
    /**
     * 对象写入SharedPreferences存储中
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param object
     *            SharedPreferences存储的对象
     */
    public static void safeSaveBean(Context mContext, String nameProperty, String key, Object object) {
        AppStoreSafe.saveBean(mContext, nameProperty, key, object, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中读取对象
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param  <T>
     *            SharedPreferences存储的数据类型，SharedPreferences存储读取完毕后会自动解析，key为空的时候依照此取得读取的key值
     * @return 解析好的执行类型的对象T
     */
    public static <T> T safeGetBean(Context mContext, String nameProperty, String key, Class<T> t) {
        return AppStoreSafe.getBean(mContext, nameProperty, key, t, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param cls
     *            数据类型
     */
    public static void safeDelBean(Context mContext, String nameProperty, String key, Class<?> cls) {
        AppStoreSafe.delBean(mContext, nameProperty, key, cls, Context.MODE_PRIVATE);
    }



    /**
     * String内容写入SharedPreferences存储中
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key
     * @param value
     *            SharedPreferences存储的值
     */
    public static void safeSaveString(Context mContext, String nameProperty, String key, String value) {
        AppStoreSafe.saveString(mContext, nameProperty, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences获取存储的String内容
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key
     * @return 获取到的String内容
     */
    public static String safeGetString(Context mContext, String nameProperty, String key) {
        return AppStoreSafe.getString(mContext, nameProperty, key, Context.MODE_PRIVATE);
    }
    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param nameProperty
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     */
    public static void safeDelString(Context mContext, String nameProperty, String key) {
        AppStoreSafe.delete(mContext, nameProperty, key, Context.MODE_PRIVATE);
    }
    /**
     * 对象写入SharedPreferences存储中
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param object
     *            SharedPreferences存储的对象
     */
    public static void saveBean(Context mContext,  String key, Object object) {
        AppStoreUnSafe.saveBean(mContext, BaseConfig.Property_Space_Object, key, object, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中读取对象
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param  <T>
     *            SharedPreferences存储的数据类型，SharedPreferences存储读取完毕后会自动解析，key为空的时候依照此取得读取的key值
     * @return 解析好的执行类型的对象T
     */
    public static <T> T getBean(Context mContext, String key, Class<T> t) {
        return AppStoreUnSafe.getBean(mContext,BaseConfig.Property_Space_Object, key, t, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param cls
     *            数据类型
     */
    public static void delBean(Context mContext,  String key, Class<?> cls) {
        AppStoreUnSafe.delBean(mContext, BaseConfig.Property_Space_Object, key, cls, Context.MODE_PRIVATE);
    }

    /**
     * String内容写入SharedPreferences存储中
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key
     * @param value
     *            SharedPreferences存储的值
     */
    public static void saveString(Context mContext, String key, String value) {
        AppStoreUnSafe.saveString(mContext, BaseConfig.Property_Space_String, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences获取存储的String内容
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key
     * @return 获取到的String内容
     */
    public static String getString(Context mContext,  String key) {
        return AppStoreUnSafe.getString(mContext, BaseConfig.Property_Space_String, key, Context.MODE_PRIVATE);
    }
    /**
     * 从SharedPreferences存储中删除值
     *
     * @param mContext
     *            SharedPreferences存储命名空间，即最终xml文件的名称
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     */
    public static void delString(Context mContext,  String key) {
        AppStoreUnSafe.delete(mContext, BaseConfig.Property_Space_String, key, Context.MODE_PRIVATE);
    }
    /**
     * 清理SharedPreferences存储
     *
     * @param mContext
     */
    public static void clearSharedPreferences(Context mContext) {
        AppStoreUnSafe.clearSharedPreferences(mContext, BaseConfig.Property_Space_String, Context.MODE_PRIVATE);
        AppStoreUnSafe.clearSharedPreferences(mContext, BaseConfig.Property_Space_Object, Context.MODE_PRIVATE);
    }
    /**
     * 对象写入SharedPreferences存储中
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param object
     *            SharedPreferences存储的对象
     */
    public static void safeSaveBean(Context mContext,  String key, Object object) {
        AppStoreSafe.saveBean(mContext, BaseConfig.Property_Space_Object+SAFESTORE_APPEND, key, object, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中读取对象
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param <T>
     *            SharedPreferences存储的数据类型，SharedPreferences存储读取完毕后会自动解析，key为空的时候依照此取得读取的key值
     * @return 解析好的执行类型的对象T
     */
    public static <T> T safeGetBean(Context mContext,  String key, Class<T> t) {
        return AppStoreSafe.getBean(mContext, BaseConfig.Property_Space_Object+SAFESTORE_APPEND, key, t, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     * @param cls
     *            数据类型
     */
    public static void safeDelBean(Context mContext,  String key, Class<?> cls) {
        AppStoreSafe.delBean(mContext, BaseConfig.Property_Space_Object+SAFESTORE_APPEND, key, cls, Context.MODE_PRIVATE);
    }

    /**
     * String内容写入SharedPreferences存储中
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key
     * @param value
     *            SharedPreferences存储的值
     */
    public static void safeSaveString(Context mContext,  String key, String value) {
        AppStoreSafe.saveString(mContext, BaseConfig.Property_Space_String+SAFESTORE_APPEND, key, value, Context.MODE_PRIVATE);
    }

    /**
     * 从SharedPreferences获取存储的String内容
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key
     * @return 获取到的String内容
     */
    public static String safeGetString(Context mContext, String key) {
        return AppStoreSafe.getString(mContext, BaseConfig.Property_Space_String+SAFESTORE_APPEND, key, Context.MODE_PRIVATE);
    }
    /**
     * 从SharedPreferences存储中删除对象
     *
     * @param mContext
     * @param key
     *            SharedPreferences存储的key，key值为空的时候，按照对象的数据类型的SimpleName或者InjectEntity来获取存储的key值
     */
    public static void safeDelString(Context mContext, String key) {
        AppStoreSafe.delete(mContext, BaseConfig.Property_Space_String+SAFESTORE_APPEND, key, Context.MODE_PRIVATE);
    }

    /**
     * 清理SharedPreferences存储
     *
     * @param mContext
     */
    public static void safeClearSharedPreferences(Context mContext) {
        AppStoreSafe.clearSharedPreferences(mContext, BaseConfig.Property_Space_String+SAFESTORE_APPEND, Context.MODE_PRIVATE);
        AppStoreSafe.clearSharedPreferences(mContext, BaseConfig.Property_Space_Object+SAFESTORE_APPEND, Context.MODE_PRIVATE);
    }

    public static void setAppStoreSafeInterface(AppStoreSafeInterface appStoreSafeInterface)
    {
        AppStoreSafe.setAppStoreSafeInterface(appStoreSafeInterface);
    }

}
