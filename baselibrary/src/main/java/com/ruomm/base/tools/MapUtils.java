package com.ruomm.base.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**   
*    
* 项目名称：工具类   
* 
* 类名称：MapUtils   
* 
* 类描述：   Map工具类，可用于Map常用操作
* 
* 创建人：王龙能
* 
* 联系方式：563208883  http://www.wanglongneng.cn
* 
* 创建时间：2014-3-11 下午4:25:01   
* 
* 修改人：王龙能  
* 
* 修改时间：2014-3-11 下午4:25:01   
* 
* 修改备注：   
* 
* @version    
*    
*/
public class MapUtils {
	 /** 键和值之间默认的分隔符 **/
    public static final String DEFAULT_KEY_AND_VALUE_SEPARATOR      = ":";
    /** key-value对之间默认的分隔符**/
    public static final String DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR = ",";

    /**
     * 为空或它的大小为0
     * 
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1, 2})    =   false;
     * </pre>
     * 
     * @param sourceMap
     * @return 如果map为null或者它的大小为0，返回真，否则返回假。
     */
    public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
        return (sourceMap == null || sourceMap.size() == 0);
    }

    /**
     * 添加键值对映射，而关键需要不为空或空 
     * 
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null or empty, return false</li>
     * <li>return {@link Map#put(Object, Object)}</li>
     * </ul>
     */
    public static boolean putMapNotEmptyKey(Map<String, String> map, String key, String value) {
        if (map == null || StringUtils.isEmpty(key)) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 添加键值对映射，无论是键和值不一定为零或空
     * 
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null or empty, return false</li>
     * <li>if value is null or empty, return false</li>
     * <li>return {@link Map#put(Object, Object)}</li>
     * </ul>
     */
    public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value) {
        if (map == null || StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 添加键值对映射，关键需要不为空或空
     * 
     * @param map
     * @param key
     * @param value
     * @param defaultValue
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null or empty, return false</li>
     * <li>if value is null or empty, put defaultValue, return true</li>
     * <li>if value is neither null nor empty，put value, return true</li>
     * </ul>
     */
    public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value,
                                                    String defaultValue) {
        if (map == null || StringUtils.isEmpty(key)) {
            return false;
        }

        map.put(key, StringUtils.isEmpty(value) ? defaultValue : value);
        return true;
    }

    /**
     * 添加键值对映射，关键需要不为空
     * 
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null, return false</li>
     * <li>return {@link Map#put(Object, Object)}</li>
     * </ul>
     */
    public static <K, V> boolean putMapNotNullKey(Map<K, V> map, K key, V value) {
        if (map == null || key == null) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 添加键值对映射，无论是键和值不一定空
     * 
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null, return false</li>
     * <li>if value is null, return false</li>
     * <li>return {@link Map#put(Object, Object)}</li>
     * </ul>
     */
    public static <K, V> boolean putMapNotNullKeyAndValue(Map<K, V> map, K key, V value) {
        if (map == null || key == null || value == null) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 值获取键，匹配的第一个条目从前到后
     * <ul>
     * <strong>注意事项：</strong>
     * <li>对于HashMap的，不进入相同的顺序放顺序，因此您可能需要使用TreeMap的</li>
     * </ul>
     * 
     * @param <V>
     * @param map
     * @param value
     * @return <ul>
     * <li>if map is null, return null</li>
     * <li>if value exist, return key</li>
     * <li>return null</li>
     * </ul>
     */
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        if (isEmpty(map)) {
            return null;
        }

        for (Entry<K, V> entry : map.entrySet()) {
            if (ObjectUtils.isEquals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 解析键值对映射，忽略空的关键
     * 
     * <pre>
     * parseKeyAndValueToMap("","","",true)=null
     * parseKeyAndValueToMap(null,"","",true)=null
     * parseKeyAndValueToMap("a:b,:","","",true)={(a,b)}
     * parseKeyAndValueToMap("a:b,:d","","",true)={(a,b)}
     * parseKeyAndValueToMap("a:b,c:d","","",true)={(a,b),(c,d)}
     * parseKeyAndValueToMap("a=b, c = d","=",",",true)={(a,b),(c,d)}
     * parseKeyAndValueToMap("a=b, c = d","=",",",false)={(a, b),( c , d)}
     * parseKeyAndValueToMap("a=b, c=d","=", ",", false)={(a,b),( c,d)}
     * parseKeyAndValueToMap("a=b; c=d","=", ";", false)={(a,b),( c,d)}
     * parseKeyAndValueToMap("a=b, c=d", ",", ";", false)={(a=b, c=d)}
     * </pre>
     * 
     * @param source key-value pairs
     * @param keyAndValueSeparator separator between key and value
     * @param keyAndValuePairSeparator separator between key-value pairs
     * @param ignoreSpace whether ignore space at the begging or end of key and value
     * @return
     */
    public static Map<String, String> parseKeyAndValueToMap(String source, String keyAndValueSeparator,
                                                            String keyAndValuePairSeparator, boolean ignoreSpace) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        if (StringUtils.isEmpty(keyAndValueSeparator)) {
            keyAndValueSeparator = DEFAULT_KEY_AND_VALUE_SEPARATOR;
        }
        if (StringUtils.isEmpty(keyAndValuePairSeparator)) {
            keyAndValuePairSeparator = DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR;
        }
        Map<String, String> keyAndValueMap = new HashMap<String, String>();
        String[] keyAndValueArray = source.split(keyAndValuePairSeparator);
        if (keyAndValueArray == null) {
            return null;
        }

        int seperator;
        for (String valueEntity : keyAndValueArray) {
            if (!StringUtils.isEmpty(valueEntity)) {
                seperator = valueEntity.indexOf(keyAndValueSeparator);
                if (seperator != -1) {
                    if (ignoreSpace) {
                        MapUtils.putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator).trim(),
                                                   valueEntity.substring(seperator + 1).trim());
                    } else {
                        MapUtils.putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator),
                                                   valueEntity.substring(seperator + 1));
                    }
                }
            }
        }
        return keyAndValueMap;
    }

    /**
     * 解析键值对映射，忽略空的关键
     * 
     * @param source key-value pairs
     * @param ignoreSpace 是否忽略了在键和值的乞讨或年底空间 
     * @return
     * @see {@link MapUtils#parseKeyAndValueToMap(String, String, String, boolean)}, keyAndValueSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_SEPARATOR}, keyAndValuePairSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR}
     */
    public static Map<String, String> parseKeyAndValueToMap(String source, boolean ignoreSpace) {
        return parseKeyAndValueToMap(source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR,
                                     ignoreSpace);
    }

    /**
     * 解析键值对映射，忽略空的关键，忽略了空间的key和value 开始或结束
     * 
     * @param source key-value pairs
     * @return
     * @see {@link MapUtils#parseKeyAndValueToMap(String, String, String, boolean)}, keyAndValueSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_SEPARATOR}, keyAndValuePairSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR}, ignoreSpace is true
     */
    public static Map<String, String> parseKeyAndValueToMap(String source) {
        return parseKeyAndValueToMap(source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR,
                                     true);
    }

    /**
     * 加入视图
     * 
     * @param map
     * @return
     */
    public static String toJson(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }

        StringBuilder paras = new StringBuilder();
        paras.append("{");
        Iterator<Entry<String, String>> ite = map.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, String> entry = (Entry<String, String>)ite.next();
            paras.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            if (ite.hasNext()) {
                paras.append(",");
            }
        }
        paras.append("}");
        return paras.toString();
    }
}
