/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月30日 下午2:19:10
 */
package com.ruomm.base.http.okhttp;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.ruomm.base.tools.EncryptUtils;

import android.text.TextUtils;
import android.util.Log;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * OkHttp请求配置，提供网络请求Client配置，网络请求参数设置等
 *
 * @author Ruby
 */
class OkHttpConfig {
	private static OkHttpClient mOkHttpClient;

	public static OkHttpClient getOkHttpClient() {
		// if (BaseConfig.HttpEnable_OKHttp) {
		if (null == mOkHttpClient) {

			mOkHttpClient = new OkHttpClient();
			mOkHttpClient.newBuilder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS);
			//
			//
			// mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
			// mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
			// mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
		}
		return mOkHttpClient;
		// }
		// else {
		// mOkHttpClient = null;
		// return null;
		// }
	}

	public static void logRequestParam(boolean isDebug, String debugTag, HashMap<String, String> params) {
		if (!isDebug || null == params || params.size() <= 0) {
			return;
		}
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();
		for (Entry<String, String> param : entrys) {
			Log.i(debugTag, "参数String@" + param.getKey() + ":" + param.getValue());
		}
	}

	public static String getKeyString(String url, String tag, HashMap<String, String> params) {

		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		basestring.append(url);
		if (null != tag && tag.length() > 0) {
			basestring.append(tag);
		}
		if (null != params && params.size() > 0) {
			// 先将参数以其参数名的字典序升序进行排序
			TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
			Set<Entry<String, String>> entrys = sortedParams.entrySet();
			for (Entry<String, String> param : entrys) {
				if (!"timestamp".equals(param.getKey()) && !"sign".equals(param.getKey())) {
					basestring.append(param.getKey()).append("=").append(param.getValue()).append("&");
				}
			}
		}
		String keyString = EncryptUtils.EncodingMD5(basestring.toString());
		if (TextUtils.isEmpty(keyString)) {
			return "";
		}
		else {
			return keyString;
		}

	}

	public static boolean isTrueOkHttpTag(Object tag) {
		if (null == tag) {
			return false;
		}
		if (tag instanceof String) {
			if (TextUtils.isEmpty((String) tag)) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}

	// private static String getSignString(String unsigned) {
	// byte[] bytes = null;
	// try {
	// MessageDigest md5 = MessageDigest.getInstance("MD5");
	// bytes = md5.digest(unsigned.getBytes("UTF-8"));
	//
	// }
	// catch (Exception ex) {
	// return "";
	// }
	//
	// // 将MD5输出的二进制结果转换为小写的十六进制
	// StringBuilder sign = new StringBuilder();
	// for (int i = 0; i < bytes.length; i++) {
	// String hex = Integer.toHexString(bytes[i] & 0xFF);
	// if (hex.length() == 1) {
	// sign.append("0");
	// }
	// sign.append(hex);
	// }
	// return sign.toString();
	// }

	public static Request getOkHttpRequest(String url, HashMap<String, String> hashMap, boolean isPost) {
		if (isPost) {
			return getOkHttpRequestPost(url, hashMap);
		}
		else {
			return getOkHttpRequestGet(url, hashMap);
		}
	}

	public static Request getOkHttpRequestByRequestBody(String url, RequestBody mRequestBody, boolean isPost) {
		if (isPost) {
			try {
				if (null != mRequestBody) {
					return new Request.Builder().url(url).header("Connection","close").post(mRequestBody).build();
				}
				else {
					return null;
				}
			}
			catch (Exception e) {
				return null;
			}

		}
		else {
			try {
				String realUrl = attachFormRequestUrl(url, null);
				if (null != mRequestBody) {
					return new Request.Builder().url(realUrl).put(mRequestBody).get().build();

				}
				else {
					return new Request.Builder().url(realUrl).get().build();
				}
			}
			catch (Exception e) {
				return null;
			}

		}
	}

	private static Request getOkHttpRequestPost(String url, HashMap<String, String> hashMap) {
		try {
			RequestBody body = attachFormRequestForamtBody(hashMap);
			return new Request.Builder().url(url).header("Connection","close").post(body).build();
		}
		catch (Exception e) {
			return null;
		}

	}

	private static Request getOkHttpRequestGet(String url, HashMap<String, String> hashMap) {
		try {
			String realUrl = attachFormRequestUrl(url, hashMap);
			return new Request.Builder().url(realUrl).get().build();
		}
		catch (Exception e) {
			return null;
		}

	}

	// 获取真实的Get请求路径
	private static String attachFormRequestUrl(String url, HashMap<String, String> hashMap) {
		if (null == hashMap || hashMap.size() <= 0) {
			return url;
		}
		return url + "?" + attachFormRequestFormatString(hashMap);
	}

	// 构建Post请求参数
	private static RequestBody attachFormRequestForamtBody(HashMap<String, String> hashMap) {

		Builder mBuilder = new FormBody.Builder();
		Set<String> sets = hashMap.keySet();
		for (String key : sets) {
			String value = hashMap.get(key);
			if (TextUtils.isEmpty(value)) {
				mBuilder.add(key, "");
			}
			else {
				mBuilder.add(key, value);
			}

		}
		return mBuilder.build();
	}

	// 构建Get请求参数
	private static String attachFormRequestFormatString(HashMap<String, String> hashMap) {
		StringBuilder buf = new StringBuilder();
		Set<String> sets = hashMap.keySet();
		int sizeSets = sets.size();
		int index = 0;
		for (String key : sets) {
			index++;
			buf.append(key).append("=");
			String value = hashMap.get(key);
//			value= URLEncoder.encode(value);
			if (!TextUtils.isEmpty(value)) {
				buf.append(value);
			}
			if (index != sizeSets) {
				buf.append("&");
			}
		}
		return buf.toString();
	}

}
