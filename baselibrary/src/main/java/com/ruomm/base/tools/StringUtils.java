package com.ruomm.base.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruomm.base.ioc.extend.StringValueOnAppend;

import android.text.TextUtils;

/**
 * 项目名称：工具类 类名称：StringUtils 类描述： 可用于常见字符串操作 创建人：王龙能 创建时间：2014-3-11 下午2:07:11 修改人：王龙能 修改时间：2014-3-11
 * 下午2:07:11 修改备注：
 *
 * @version
 */
public class StringUtils {
	/**
	 * 公共静态布尔ISBLANK 为空或长度为0或它是由空间中进行
	 *
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = true;
	 * isBlank(&quot;a&quot;) = false;
	 * isBlank(&quot;a &quot;) = false;
	 * isBlank(&quot; a&quot;) = false;
	 * isBlank(&quot;a b&quot;) = false;
	 * </pre>
	 *
	 * @param str
	 * @return 如果字符串为null或它的大小为0，或它是由空间中进行的，返回true，否则返回false。
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 为空或长度为0
	 *
	 * <pre>
	 * isEmpty(null) = true;
	 * isEmpty(&quot;&quot;) = true;
	 * isEmpty(&quot;  &quot;) = false;
	 * </pre>
	 *
	 * @param str
	 * @return 如果字符串为null或它的大小为0，返回真，否则返回假。
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isZhCNString(String chs) {
		if (TextUtils.isEmpty(chs)) {
			return false;
		}
		else {
			boolean isChs = false;
			for (int i = 0; i < chs.length(); i++) {
				String key = chs.substring(i, i + 1);
				if (key.getBytes().length >= 2) {
					isChs = true;
					break;
				}
			}
			return isChs;
		}
	}

	public static int getLength(String str) {
		return null == str ? 0 : str.length();
	}

	/**
	 * 是否是有效的适合Picasso 的图片路径
	 *
	 * @param str
	 * @return
	 */
	public static boolean isAvailablePicassoUrl(String str) {
		if (isEmpty(str) || isEmpty(str.replaceAll(" ", ""))) {
			return false;
		}
		return true;
	}

	//
	public static String getPicassoUrl(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl)) {
			return "";
		}
		else if (imageUrl.startsWith(File.separator)) {
			return "file://" + imageUrl;
		}
		else {
			return imageUrl;
		}
	}

	/**
	 * 比较两个字符串
	 *
	 * @param actual
	 * @param expected
	 * @return 如果两者都为空，则返回true 否则 返回实际
	 */
	public static boolean isEquals(String actual, String expected) {

		return ObjectUtils.isEquals(actual, expected);
	}

	public static boolean isEqaulsNullEmptySame(String actual, String expected) {
		return nullStrToEmpty(actual).equals(nullStrToEmpty(expected));

	}

	/**
	 * 空字符串为空字符串
	 *
	 * <pre>
	 * nullStrToEmpty(null) = &quot;&quot;;
	 * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
	 * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String nullStrToEmpty(String str) {
		return str == null ? "" : str;
	}

	public static String CharSequenceToString(CharSequence charSequence) {
		return charSequence == null ? "" : charSequence.toString();
	}

	public static String fillStringOnEnd(CharSequence charSequence, int length) {
		return fillString(charSequence, length, null, false);
	}

	public static String fillStringOnEnd(CharSequence charSequence, int length, String fillTag) {
		return fillString(charSequence, length, fillTag, false);
	}

	public static String fillStringOnStart(CharSequence charSequence, int length) {
		return fillString(charSequence, length, null, true);
	}

	public static String fillStringOnStart(CharSequence charSequence, int length, String fillTag) {
		return fillString(charSequence, length, fillTag, true);
	}

	public static String fillStringOnCenter(CharSequence charSequence, int length) {
		return fillStringOnCenter(charSequence, length, null);
	}

	public static String fillStringOnCenter(CharSequence charSequence, int length, String fillTag) {
		String text = null;
		if (!TextUtils.isEmpty(charSequence)) {
			text = charSequence.toString();
		}
		int size = getLength(text);
		int sizeOff = length - size;
		if (sizeOff <= 0) {
			return text;
		}
		StringBuilder stringBuilderLeft = new StringBuilder();
		StringBuilder stringBuilderRight = new StringBuilder();
		int sizeLeft = sizeOff / 2;
		int sizeRight = sizeOff - sizeLeft;
		String tag = null;
		if (!TextUtils.isEmpty(fillTag)) {
			tag = fillTag;
		}
		else {
			tag = " ";
		}
		for (int i = 0; i < sizeLeft; i++) {
			stringBuilderLeft.append(tag);
		}
		for (int i = 0; i < sizeRight; i++) {
			stringBuilderRight.append(tag);
		}
		return stringBuilderLeft.toString() + text + stringBuilderRight.toString();

	}

	private static String fillString(CharSequence charSequence, int length, String fillTag, boolean isAppendStart) {

		String text = null;
		if (!TextUtils.isEmpty(charSequence)) {
			text = charSequence.toString();
		}

		int size = getLength(text);
		int sizeOff = length - size;
		if (sizeOff <= 0) {
			return text;
		}
		StringBuilder stringBuilder = new StringBuilder();
		String tag = null;
		if (!TextUtils.isEmpty(fillTag)) {
			tag = fillTag;
		}
		else {
			tag = " ";
		}
		for (int i = 0; i < sizeOff; i++) {
			stringBuilder.append(tag);
		}

		if (isAppendStart) {
			return stringBuilder.toString() + text;
		}
		else {
			return text + stringBuilder.toString();
		}
	}

	/**
	 * 大写第一个字母
	 *
	 * <pre>
	 * capitalizeFirstLetter(null)     =   null;
	 * capitalizeFirstLetter("")       =   "";
	 * capitalizeFirstLetter("2ab")    =   "2ab"
	 * capitalizeFirstLetter("a")      =   "A"
	 * capitalizeFirstLetter("ab")     =   "Ab"
	 * capitalizeFirstLetter("Abc")    =   "Abc"
	 * </pre>
	 *
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
		if (isEmpty(str)) {
			return str;
		}

		char c = str.charAt(0);
		return !Character.isLetter(c) || Character.isUpperCase(c) ? str : new StringBuilder(str.length())
				.append(Character.toUpperCase(c)).append(str.substring(1)).toString();
	}

	/**
	 * 编码为UTF-8
	 *
	 * <pre>
	 * utf8Encode(null)        =   null
	 * utf8Encode("")          =   "";
	 * utf8Encode("aa")        =   "aa";
	 * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
	 * </pre>
	 *
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 *             if an error occurs
	 */
	public static String utf8Encode(String str) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	/**
	 * 编码为UTF-8
	 *
	 * <pre>
	 * utf8Encode(null)        =   null
	 * utf8Encode("")          =   "";
	 * utf8Encode("aa")        =   "aa";
	 * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
	 * </pre>
	 *
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 *             if an error occurs
	 */
	public static String utf8EncodeAll(String str) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				str = URLEncoder.encode(str, "UTF-8");
				str = str.replaceAll(",", "%2C");
				str = str.replaceAll("/", "%2F");
				return str;
			}
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	public static String stringToUtf8Encode(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		String strString = "";
		String strUTF8 = "";

		try {
			strString = new String(sb.toString().getBytes("UTF-8"));
			strUTF8 = URLEncoder.encode(strString, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
		}
		return strUTF8;
	}

	/**
	 * e编码为UTF-8，如果异常，返回defultReturn
	 *
	 * @param str
	 * @param defultReturn
	 * @return
	 */
	public static String utf8Encode(String str, String defultReturn) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				return defultReturn;
			}
		}
		return str;
	}

	/**
	 * 从HREF得到的innerHTML
	 *
	 * <pre>
	 * getHrefInnerHtml(null)                                  = ""
	 * getHrefInnerHtml("")                                    = ""
	 * getHrefInnerHtml("mp3")                                 = "mp3";
	 * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
	 * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
	 * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
	 * </pre>
	 *
	 * @param href
	 * @return <ul>
	 *         <li>如果HREF为null，则返回""</li>
	 *         <li>如果不匹配REGX，返回源</li>
	 *         <li>返回匹配REGX的最后一个字符串</li>
	 *         </ul>
	 */
	public static String getHrefInnerHtml(String href) {
		if (isEmpty(href)) {
			return "";
		}

		String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
		Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
		Matcher hrefMatcher = hrefPattern.matcher(href);
		if (hrefMatcher.matches()) {
			return hrefMatcher.group(1);
		}
		return href;
	}

	/**
	 * 在html中处理特殊字符
	 *
	 * <pre>
	 * htmlEscapeCharsToString(null) = null;
	 * htmlEscapeCharsToString("") = "";
	 * htmlEscapeCharsToString("mp3") = "mp3";
	 * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
	 * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
	 * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
	 * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
	 * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
	 * </pre>
	 *
	 * @param source
	 * @return
	 */
	public static String htmlEscapeCharsToString(String source) {
		return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}

	/**
	 * 变换半宽字符到全角字符
	 *
	 * <pre>
	 * fullWidthToHalfWidth(null) = null;
	 * fullWidthToHalfWidth("") = "";
	 * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
	 * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
	 * </pre>
	 *
	 * @param s
	 * @return
	 */
	public static String fullWidthToHalfWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
				// } else if (source[i] == 12290) {
				// source[i] = '.';
			}
			else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char) (source[i] - 65248);
			}
			else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * 变换全宽字符到半角字符
	 *
	 * <pre>
	 * halfWidthToFullWidth(null) = null;
	 * halfWidthToFullWidth("") = "";
	 * halfWidthToFullWidth(" ") = new String(new char[] {12288});
	 * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
	 * </pre>
	 *
	 * @param s
	 * @return
	 */
	public static String halfWidthToFullWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == ' ') {
				source[i] = (char) 12288;
				// } else if (source[i] == '.') {
				// source[i] = (char)12290;
			}
			else if (source[i] >= 33 && source[i] <= 126) {
				source[i] = (char) (source[i] + 65248);
			}
			else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	public static String getNumberString(int nums, int perValue, String tag) {
		if (perValue < 2) {
			return String.valueOf(nums);
		}

		if (nums >= perValue) {
			double dn = nums * 1.0 / perValue;
			BigDecimal bigDecimal = new BigDecimal(dn);
			double value = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			// return String.format("%.01d", dn) + "千";
			return value + tag;

		}
		else {
			return String.valueOf(nums);
		}
	}

	// /**
	// * 数量转换为k比例 当值大于999时，以k为单位。如：1000显示为1.0k,1001至1100显示为1.1k,11000显示为11.0k ,10001至10999显示为10.1k
	// *
	// * @param nums
	// * @return 创建人：王龙能 2015年1月5日 下午4:06:59
	// */
	// public static String getKtointValue(int nums) {
	// return getNumberString(nums, 1000, "k");
	// }

	public static String getNumberStringByKB(int nums) {
		return getNumberString(nums, 1000, "k");
	}

	/**
	 * 正则验证表达式
	 *
	 * @param string
	 *            源字符串
	 * @param regex
	 *            验证规则
	 * @return
	 */
	public static boolean doRegex(String string, String regex) {
		if (null == string || string.length() <= 0) {
			return false;
		}
		else if (null == regex || regex.length() <= 0) {
			return true;
		}
		else {
			return string.matches(regex);
		}
	}

	public static ArrayList<String> getListString(String arg, String split) {
		ArrayList<String> list = new ArrayList<String>();
		if (TextUtils.isEmpty(arg)) {
			return list;
		}
		else {
			String[] strings = arg.split(split);
			if(null!=strings) {
				for (String string : strings) {
					if (!TextUtils.isEmpty(string)) {
						list.add(string);
					}
				}
			}
		}
		return list;
	}

	public static ArrayList<Integer> getListInteger(String arg, String split) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (TextUtils.isEmpty(arg)) {
			return list;
		}
		else {
			String[] strings = arg.split(split);
			if(null!=strings) {
				for (String string : strings) {
					if (!TextUtils.isEmpty(string)) {
						try {
							Integer value = Integer.valueOf(string);
							list.add(value);
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
		}
		return list;
	}

	public static ArrayList<Long> getListLong(String arg, String split) {
		ArrayList<Long> list = new ArrayList<Long>();
		if (TextUtils.isEmpty(arg)) {
			return list;
		}
		else {
			String[] strings = arg.split(split);
			if(null!=strings) {
				for (String string : strings) {
					if (!TextUtils.isEmpty(string)) {
						try {
							Long value = Long.valueOf(string);
							list.add(value);
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * @param lists
	 *            args :字符串拼接集合
	 * @param appendString
	 *            ：字符串拼接的分隔符默认没有分隔符；
	 * @return
	 *         appendString({"as","cd","xyz"})="ascdxyz"，appendString({"as","cd","xyz"}，"-")="as-cd-xyz"
	 */
	public static String appendString(String[] args) {
		if (null == args || args.length == 0) {
			return "";
		}
		StringBuilder strbuf = new StringBuilder();
		for (String string : args) {
			strbuf.append(string);
		}
		return new String(strbuf);
	}

	public static String appendString(String[] args, String appendString) {
		if (null == args || args.length == 0) {
			return "";
		}
		int arrsize = args.length;
		StringBuilder bufString = new StringBuilder();
		for (int index = 0; index < arrsize; index++) {
			bufString.append(args[index]);
			if (index < arrsize - 1 && !TextUtils.isEmpty(appendString)) {
				bufString.append(appendString);
			}
		}
		String string = new String(bufString);
		return string;
	}

	public static <V> String appendString(List<V> lists) {
		return appendString(lists, null, null);
	}

	public static <V> String appendString(List<V> lists, String appendString) {
		return appendString(lists, appendString, null);
	}

	public static <V> String appendString(List<V> lists, String appendString, String tag) {
		if (null == lists || lists.size() == 0) {
			return "";
		}
		int listsize = lists.size();
		StringBuilder bufString = new StringBuilder();
		for (int index = 0; index < listsize; index++) {
			V v = lists.get(index);
			if (v instanceof String || v instanceof Boolean || v instanceof Character || v instanceof Integer
					|| v instanceof Long || v instanceof Double || v instanceof Float) {
				bufString.append(v);
			}
			else {
				StringValueOnAppend item = null;
				try {
					item = (StringValueOnAppend) v;
				}
				catch (Exception e) {
					item = null;
				}
				if (null != item) {
					bufString.append(item.getAppendStringValue(tag));
				}
				else {
					bufString.append(v);
				}
			}
			if (index < listsize - 1 && !TextUtils.isEmpty(appendString)) {
				bufString.append(appendString);
			}

		}
		String string = new String(bufString);
		return string;
	}
}
