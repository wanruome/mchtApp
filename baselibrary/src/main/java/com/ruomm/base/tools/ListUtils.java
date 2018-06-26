package com.ruomm.base.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：工具类 类名称：ListUtils 类描述： List工具类，可用于List常用操作 创建人：王龙能 创建时间：2014-3-13 上午11:25:07 修改人：王龙能
 * 修改时间：2014-3-13 上午11:25:07 修改备注：
 * 
 * @version
 */
public class ListUtils {

	/** 默认的分隔符r **/
	public static final String DEFAULT_JOIN_SEPARATOR = ",";

	/**
	 * 获取列表的大小
	 * 
	 * <pre>
	 * getSize(null)   =   0;
	 * getSize({})     =   0;
	 * getSize({1})    =   1;
	 * </pre>
	 * 
	 * @param <V>
	 * @param sourceList
	 * @return 如果列表为null或空，则返回0，否则返回，则为list.size（） 。
	 */
	public static <V> int getSize(List<V> sourceList) {
		return sourceList == null ? 0 : sourceList.size();
	}

	/**
	 * 为空或它的大小为0
	 * 
	 * <pre>
	 * isEmpty(null)   =   true;
	 * isEmpty({})     =   true;
	 * isEmpty({1})    =   false;
	 * </pre>
	 * 
	 * @param <V>
	 * @param sourceList
	 * @return 如果列表为空或它的大小为0，返回真，否则返回假。
	 */
	public static <V> boolean isEmpty(List<V> sourceList) {
		return sourceList == null || sourceList.size() == 0;
	}

	/**
	 * 比较两个表
	 * 
	 * <pre>
	 * isEquals(null, null) = true;
	 * isEquals(new ArrayList&lt;String&gt;(), null) = false;
	 * isEquals(null, new ArrayList&lt;String&gt;()) = false;
	 * isEquals(new ArrayList&lt;String&gt;(), new ArrayList&lt;String&gt;()) = true;
	 * </pre>
	 * 
	 * @param <V>
	 * @param actual
	 * @param expected
	 * @return
	 */
	public static <V> boolean isEquals(ArrayList<V> actual, ArrayList<V> expected) {
		if (actual == null) {
			return expected == null;
		}
		if (expected == null) {
			return false;
		}
		if (actual.size() != expected.size()) {
			return false;
		}

		for (int i = 0; i < actual.size(); i++) {
			if (!ObjectUtils.isEquals(actual.get(i), expected.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 加入列表的字符串，分隔符为","
	 * 
	 * <pre>
	 * join(null)      =   "";
	 * join({})        =   "";
	 * join({a,b})     =   "a,b";
	 * </pre>
	 * 
	 * @param list
	 * @return 加入列表的字符串，分隔符为","。如果列表为空，则返回""
	 */
	public static String join(List<String> list) {
		return join(list, DEFAULT_JOIN_SEPARATOR);
	}

	/**
	 * 加入列表的字符串
	 * 
	 * <pre>
	 * join(null, '#')     =   "";
	 * join({}, '#')       =   "";
	 * join({a,b,c}, ' ')  =   "abc";
	 * join({a,b,c}, '#')  =   "a#b#c";
	 * </pre>
	 * 
	 * @param list
	 * @param separator
	 * @return 加入列表的字符串。如果列表为空，则返回""
	 */
	public static String join(List<String> list, char separator) {
		return join(list, new String(new char[] { separator }));
	}

	/**
	 * 加入列表的字符串。如果分隔符为null，则使用DEFAULT_JOIN_SEPARATOR
	 * 
	 * <pre>
	 * join(null, "#")     =   "";
	 * join({}, "#$")      =   "";
	 * join({a,b,c}, null) =   "a,b,c";
	 * join({a,b,c}, "")   =   "abc";
	 * join({a,b,c}, "#")  =   "a#b#c";
	 * join({a,b,c}, "#$") =   "a#$b#$c";
	 * </pre>
	 * 
	 * @param list
	 * @param separator
	 * @return 加入列表，字符串分隔符。如果列表为空，则返回""
	 */
	public static String join(List<String> list, String separator) {
		if (isEmpty(list)) {
			return "";
		}
		if (separator == null) {
			separator = DEFAULT_JOIN_SEPARATOR;
		}

		StringBuilder joinStr = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			joinStr.append(list.get(i));
			if (i != list.size() - 1) {
				joinStr.append(separator);
			}
		}
		return joinStr.toString();
	}

	/**
	 * 添加不同的条目列表
	 * 
	 * @param <V>
	 * @param sourceList
	 * @param entry
	 * @return 如果条目SOURCELIST已经存在，则返回false，否则将其添加并返回true。
	 */
	public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
		return sourceList != null && !sourceList.contains(entry) ? sourceList.add(entry) : false;
	}

	/**
	 * 添加所有不同的条目从列表2至LIST1
	 * 
	 * @param <V>
	 * @param sourceList
	 * @param entryList
	 * @return 条目的计数被添加
	 */
	public static <V> int addDistinctList(List<V> sourceList, List<V> entryList) {
		if (sourceList == null || isEmpty(entryList)) {
			return 0;
		}

		int sourceCount = sourceList.size();
		for (V entry : entryList) {
			if (!sourceList.contains(entry)) {
				sourceList.add(entry);
			}
		}
		return sourceList.size() - sourceCount;
	}

	/**
	 * 在列表中删除重复的条目
	 * 
	 * @param <V>
	 * @param sourceList
	 * @return t条目的计数被删除
	 */
	public static <V> int distinctList(List<V> sourceList) {
		if (isEmpty(sourceList)) {
			return 0;
		}

		int sourceCount = sourceList.size();
		int sourceListSize = sourceList.size();
		for (int i = 0; i < sourceListSize; i++) {
			for (int j = i + 1; j < sourceListSize; j++) {
				if (sourceList.get(i).equals(sourceList.get(j))) {
					sourceList.remove(j);
					sourceListSize = sourceList.size();
					j--;
				}
			}
		}
		return sourceCount - sourceList.size();
	}

	/**
	 * 添加NOT NULL条目列出
	 * 
	 * @param sourceList
	 * @param value
	 * @return <ul>
	 *         <li>如果SOURCELIST为null，则返回false</li>
	 *         <li>如果值为null，返回false</li>
	 *         <li>返回将对List.Add（对象）</li>
	 *         </ul>
	 */
	public static <V> boolean addListNotNullValue(List<V> sourceList, V value) {
		return sourceList != null && value != null ? sourceList.add(value) : false;
	}

	/**
	 * @see {@link ArrayUtils#getLast(Object[], Object, Object, boolean)}
	 *      设置defaultValue为null，isCircle是真实的
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getLast(List<V> sourceList, V value) {
		return sourceList == null ? null : (V) ArrayUtils.getLast(sourceList.toArray(), value, true);
	}

	/**
	 * @see {@link ArrayUtils#getNext(Object[], Object, Object, boolean)}
	 *      设置defaultValue为null，isCircle是真实的
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getNext(List<V> sourceList, V value) {
		return sourceList == null ? null : (V) ArrayUtils.getNext(sourceList.toArray(), value, true);
	}

	/**
	 * 倒置列表
	 * 
	 * @param <V>
	 * @param sourceList
	 * @return
	 */
	public static <V> List<V> invertList(List<V> sourceList) {
		if (isEmpty(sourceList)) {
			return sourceList;
		}

		List<V> invertList = new ArrayList<V>(sourceList.size());
		for (int i = sourceList.size() - 1; i >= 0; i--) {
			invertList.add(sourceList.get(i));
		}
		return invertList;
	}

	/**
	 * List的分页显示
	 */
	public static <V> int getListPageSize(List<V> sourceList, int perPageSize) {

		int sizeList = getSize(sourceList);
		if (sizeList % perPageSize == 0) {
			return sizeList / perPageSize;
		}
		else {
			return sizeList / perPageSize + 1;
		}
	}

	public static <V> List<V> getPageSpitList(List<V> sourceList, int pageIndex, int perPageSize) {
		int sizeList = getSize(sourceList);
		int indexStart = pageIndex * perPageSize;
		int indexEnd = (pageIndex + 1) * perPageSize;
		if (indexStart >= sizeList) {
			return null;
		}
		else {
			if (indexEnd > sizeList) {
				indexEnd = sizeList;
			}
			List<V> listTemp = new ArrayList<V>();
			listTemp.addAll(sourceList.subList(indexStart, indexEnd));
			if (isEmpty(sourceList)) {
				return null;
			}
			else {
				return listTemp;
			}
		}
	}
}
