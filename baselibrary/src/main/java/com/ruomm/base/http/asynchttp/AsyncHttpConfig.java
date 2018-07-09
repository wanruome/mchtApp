/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年1月6日 上午11:43:43
 */
package com.ruomm.base.http.asynchttp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.baseconfig.BaseConfig;

import android.text.TextUtils;
import android.util.Log;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.client.params.ClientPNames;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * AsyncHttpClient请求配置，提供网络请求Client配置，网络请求参数设置等
 *
 * @author Ruby
 */
@SuppressWarnings("deprecation")
public class AsyncHttpConfig {
	// 异步请求，可用于UI主线程
	private static AsyncHttpClient clientAsync;
	// 同步请求，可用于Thread线程
	private static SyncHttpClient clientSync;

	/**
	 * 获取异步请求客户端，依据配置来决定是否支持Https协议
	 *
	 * @return
	 */
	public static AsyncHttpClient getClientAsync() {

		// if (BaseConfig.HttpEnable_AsyncHttp) {
		if (null == clientAsync) {
			if (BaseConfig.AsyncHttpClient_Enable_HTTPS) {
				clientAsync = new AsyncHttpClient(getSchemeRegistry());
			}
			else {
				clientAsync = new AsyncHttpClient();
			}
			clientAsync.setLoggingEnabled(false);
			clientAsync.setTimeout(HttpConfig.HTTP_CLIENT_TIMEOUT); // 设置链接超时，如果不设置，默认为10s
			// 重定向次数
			// clientSync.getHttpClient().getParams().setParameter(ClientPNames.MAX_REDIRECTS,
			// 3);
			// clientAsync.getHttpClient().getParams().setParameter(ClientPNames.MAX_REDIRECTS,
			// 3);
			// 允许重定向
			clientAsync.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		}
		return clientAsync;
	}

	/**
	 * 获取同步请求客户端，依据配置来决定是否支持Https协议
	 *
	 * @return
	 */
	public static SyncHttpClient getClientSync() {
		// if (BaseConfig.HttpEnable_SyncHttp) {
		if (null == clientSync) {
			if (BaseConfig.AsyncHttpClient_Enable_HTTPS) {
				clientSync = new SyncHttpClient(getSchemeRegistry());
			}
			else {
				clientSync = new SyncHttpClient();
			}
			clientSync.setLoggingEnabled(false);
			clientSync.setTimeout(HttpConfig.HTTP_CLIENT_TIMEOUT);
			clientSync.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		}
		return clientSync;
	}

	/**
	 * https协议设定
	 *
	 * @return https协议设定参数
	 */
	private static SchemeRegistry getSchemeRegistry() {
		try {

			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			// MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			new SSLSocketFactory(trustStore);
			// SSLSocketFactory sf = new SSLSocketFactory(trustStore);
			SSLSocketFactory sf = new AsyncSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));
			return registry;
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 是否有效的Http请求Tag
	 *
	 * @param tag
	 * @return 是否有效的Http请求Tag
	 */
	public static boolean isTrueHttpTag(Object tag) {
		// PlainSocketFactory.getSocketFactory();
		// cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory.getSocketFactory();
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

	/**
	 * 设置请求参数
	 *
	 * @param params
	 *            请求参数主体
	 * @param hashMap
	 *            请求参数集合，String类型
	 * @param isDebug
	 *            是否是调试模式
	 * @param debugTag
	 *            调试模式的输出tag标识
	 * @return
	 */
	public static boolean setRequestParams(RequestParams params, HashMap<String, String> hashMap, boolean isDebug,
			String debugTag) {
		return setRequestParams(params, hashMap, null, null, isDebug, debugTag);
	}

	/**
	 * 设置请求参数
	 *
	 * @param params
	 *            请求参数主体
	 * @param hashMap
	 *            请求参数集合，String类型
	 * @param fileMap
	 *            请求参数集合，文件类型
	 * @param isDebug
	 *            是否是调试模式
	 * @param debugTag
	 *            调试模式的输出tag标识
	 * @return 请求参数主体
	 */
	public static boolean setRequestParams(RequestParams params, HashMap<String, String> hashMap,
			HashMap<String, File> fileMap, boolean isDebug, String debugTag) {
		return setRequestParams(params, hashMap, fileMap, null, isDebug, debugTag);
	}

	/**
	 * 设置请求参数
	 *
	 * @param params
	 *            请求参数主体
	 * @param hashMap
	 *            请求参数集合，多类型，放置的时候进行类型判定
	 * @return 请求参数主体
	 */
	public static boolean setRequestParamsByObject(RequestParams params, HashMap<String, Object> hashMap,
			boolean isDebug, String debugTag) {
		if (null == params) {
			return true;
		}
		if (null == hashMap || hashMap.isEmpty()) {
			return true;
		}
		boolean isTrue = true;
		// APP的通用请求参数设置
		Set<String> set = hashMap.keySet();
		for (String key : set) {

			Object mapValue = hashMap.get(key);
			if (null == mapValue) {
				params.put(key, "");
				if (isDebug) {
					Log.i(debugTag, "参数null@" + key + ":" + "\"NullPointer\"");
				}
			}
			else if (mapValue instanceof Integer) {
				Integer value = (Integer) mapValue;
				params.put(key, value);
				if (isDebug) {
					Log.i(debugTag, "参数Integer@" + key + ":" + value);
				}
			}
			else if (mapValue instanceof Long) {
				Long value = (Long) mapValue;
				params.put(key, value);
				if (isDebug) {
					Log.i(debugTag, "参数Long@" + key + ":" + value);
				}
			}
			else if (mapValue instanceof String) {
				String value = (String) mapValue;
				params.put(key, value);
				if (isDebug) {
					Log.i(debugTag, "参数String@" + key + ":" + value);
				}
			}
			else if (mapValue instanceof Float) {
				String value = (String) mapValue;
				params.put(key, value);
				if (isDebug) {
					Log.i(debugTag, "参数Float@" + key + ":" + value);
				}
			}
			else if (mapValue instanceof Double) {
				String value = (String) mapValue;
				params.put(key, value);
				if (isDebug) {
					Log.i(debugTag, "参数Double@" + key + ":" + value);
				}
			}
			else if (mapValue instanceof File) {
				params.setAutoCloseInputStreams(false);
				File value = (File) mapValue;

				try {
					params.put(key, value);
					if (isDebug) {
						Log.i(debugTag, "参数File@" + key + ":文件路径" + value.getPath());

					}
				}
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					isTrue = false;
					if (isDebug) {
						Log.i(debugTag, "参数File@" + key + ":文件没有找到");
					}
				}

			}
			else if (mapValue instanceof File[]) {
				File[] value = (File[]) mapValue;
				try {
					params.put(key, value);
					if (isDebug) {
						int size = value.length;

						StringBuilder buf = new StringBuilder();
						buf.append(":文件个数:" + size + ";" + "文件路径:");
						for (int i = 0; i < size; i++) {
							buf.append(value[i].getPath());
							if (i != size - 1) {
								buf.append(",");
							}
						}
						Log.i(debugTag, "参数File数组@" + key + buf.toString());
					}
				}
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					isTrue = false;
					if (isDebug) {
						Log.i(debugTag, "参数File数组@" + key + ":部分文件没有找到");
					}
				}

			}
			else if (mapValue instanceof InputStream) {
				InputStream value = (InputStream) mapValue;
				params.put(key, value);
				if (isDebug) {
					Log.i(debugTag, "参数InputStream@" + key + ":输入流");
				}
			}
			else {
				params.put(key, mapValue);
				if (isDebug) {
					Log.i(debugTag, "参数Object@" + key + ":输入流");
				}
			}

		}
		return isTrue;
	}

	/**
	 * 设置请求参数
	 *
	 * @param params
	 *            请求参数主体
	 * @param hashMap
	 *            请求参数集合，String类型
	 * @param fileMap
	 *            请求参数集合，文件类型
	 * @param fileListMap
	 *            请求参数集合，多文件类型
	 * @param isDebug
	 *            是否是调试模式
	 * @param debugTag
	 *            调试模式的输出tag标识
	 * @return 请求参数主体
	 */
	public static boolean setRequestParams(RequestParams params, HashMap<String, String> hashMap,
			HashMap<String, File> fileMap, HashMap<String, File[]> fileListMap, boolean isDebug, String debugTag) {
		if (null == params) {
			return true;
		}
		if ((null == hashMap || hashMap.isEmpty()) && (null == fileMap || fileMap.isEmpty())
				&& (null == fileListMap || fileListMap.isEmpty())) {
			return true;
		}
		// http请求参数
		boolean isTrue = true;
		// APP的通用请求参数设置
		if (null != hashMap && !hashMap.isEmpty()) {
			Set<String> set = hashMap.keySet();
			for (String key : set) {

				String value = hashMap.get(key);
				if (null == value) {
					params.put(key, "");
					if (isDebug) {
						Log.i(debugTag, "参数String@" + key + ":" + "\"NullPointer\"");
					}
				}
				else {
					params.put(key, value);
					if (isDebug) {
						Log.i(debugTag, "参数String@" + key + ":" + value);
					}
				}
			}
		}
		if (null != fileMap && !fileMap.isEmpty()) {
			Set<String> set = fileMap.keySet();
			for (String key : set) {

				File value = fileMap.get(key);
				if (null != value) {
					try {
						params.put(key, value);
						if (isDebug) {
							Log.i(debugTag, "参数File@" + key + ":文件路径" + value.getPath());

						}
					}
					catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						isTrue = false;
						if (isDebug) {
							Log.i(debugTag, "参数File@" + key + ":文件没有找到");
						}
					}

				}

			}
		}
		if (null != fileListMap && !fileListMap.isEmpty()) {
			Set<String> set = fileListMap.keySet();
			for (String key : set) {

				File[] value = fileListMap.get(key);
				try {
					params.put(key, value);
					if (isDebug) {
						int size = value.length;

						StringBuilder buf = new StringBuilder();
						buf.append(":文件个数:" + size + ";" + "文件路径:");
						for (int i = 0; i < size; i++) {
							buf.append(value[i].getPath());
							if (i != size - 1) {
								buf.append(",");
							}
						}
						Log.i(debugTag, "参数File数组@" + key + buf.toString());
					}
				}
				catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					isTrue = false;
					if (isDebug) {
						Log.i(debugTag, "参数File数组@" + key + ":部分文件没有找到");
					}
				}

			}
		}

		return isTrue;
	}

	/**
	 * 获取网络请求的Key值，作为唯一性和同样性校验
	 *
	 * @param url
	 *            请求Url路径
	 * @param mObjectMap
	 *            参数合集
	 * @return 网络请求Key值
	 */
	public static String getKeyString(String url, HashMap<String, Object> mObjectMap) {
		StringBuilder basestring = new StringBuilder();
		basestring.append(url);
		if (null != mObjectMap && mObjectMap.size() > 0) {
			// 先将参数以其参数名的字典序升序进行排序
			TreeMap<String, Object> sortedParams = new TreeMap<String, Object>(mObjectMap);
			Set<Entry<String, Object>> entrys = sortedParams.entrySet();
			for (Entry<String, Object> param : entrys) {
				if ("timestamp".equals(param.getKey()) || "sign".equals(param.getKey())) {
					continue;
				}
				Object mapValue = param.getValue();
				if (null == mapValue) {
					basestring.append(param.getKey()).append("=");
				}
				else if (mapValue instanceof Integer) {
					Integer value = (Integer) mapValue;
					basestring.append(param.getKey()).append("=").append(value);
				}
				else if (mapValue instanceof Long) {
					Long value = (Long) mapValue;
					basestring.append(param.getKey()).append("=").append(value);
				}
				else if (mapValue instanceof String) {
					String value = (String) mapValue;
					basestring.append(param.getKey()).append("=").append(value);
				}
				else if (mapValue instanceof Float) {
					String value = (String) mapValue;
					basestring.append(param.getKey()).append("=").append(value);
				}
				else if (mapValue instanceof Double) {
					String value = (String) mapValue;
					basestring.append(param.getKey()).append("=").append(value);
				}
				else if (mapValue instanceof File) {

					File value = (File) mapValue;
					basestring.append(param.getKey()).append("=").append(value);

				}
				else if (mapValue instanceof File[]) {
					File[] value = (File[]) mapValue;
					basestring.append(param.getKey()).append("=").append(value);

				}
				else if (mapValue instanceof InputStream) {
					InputStream value = (InputStream) mapValue;
					basestring.append(param.getKey()).append("=").append(value);
				}
				else {
					basestring.append(param.getKey()).append("=").append(mapValue);
				}

			}
		}
		String keyString = EncryptUtils.encodingMD5(basestring.toString());
		if (TextUtils.isEmpty(keyString)) {
			return "";
		}
		else {
			return keyString;
		}
	}
}
