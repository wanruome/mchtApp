/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月30日 下午2:19:10
 */
package com.ruomm.base.http.okhttp;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.http.config.ResponseParse;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.baseconfig.BaseConfig;

import android.text.TextUtils;
import android.util.Log;

import org.bouncycastle.jce.provider.symmetric.ARC4;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
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

//	public static OkHttpClient getOkHttpClient() {
//		// if (BaseConfig.HttpEnable_OKHttp) {
//		if (null == mOkHttpClient) {
//			mOkHttpClient = new OkHttpClient();
//			mOkHttpClient.newBuilder().connectTimeout(BaseConfig.OkHttp_Connect_Time, TimeUnit.SECONDS).writeTimeout(BaseConfig.OkHttp_Write_Time, TimeUnit.SECONDS)
//			.readTimeout(BaseConfig.OkHttp_Read_Time, TimeUnit.SECONDS);
//		}
//		return mOkHttpClient;
//	}
	public static OkHttpClient getOkHttpClient(){
		if(null!= mOkHttpClient){
			return mOkHttpClient;
		}
		if(BaseConfig.OkHttp_SSL_Safe&&!TextUtils.isEmpty(BaseConfig.OkHttp_SSL_Path)){
			return getSafeOkHttpClient();
		}
		else{
			return getUnSafeOkHttpClient();
		}
	}
	private static OkHttpClient getUnSafeOkHttpClient(){
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						@Override
						public void checkClientTrusted(X509Certificate[] certs, String authType) {
						}

						@Override
						public void checkServerTrusted(X509Certificate[] certs, String authType) {
						}

						@Override
						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[]{};
						}
					}
			};
			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			// Create a trust manager that does not validate certificate chains
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.hostnameVerifier(new NullHostNameVerifier());
			builder.sslSocketFactory(sslSocketFactory);
			builder.connectTimeout(BaseConfig.OkHttp_Connect_Time, TimeUnit.SECONDS);
			builder.writeTimeout(BaseConfig.OkHttp_Write_Time,TimeUnit.SECONDS);
			builder.readTimeout(BaseConfig.OkHttp_Read_Time, TimeUnit.SECONDS);
			//            builder.retryOnConnectionFailure(false);
			//            builder.followRedirects(false);
			//            builder.followSslRedirects(false);
			mOkHttpClient = builder.build();
			return mOkHttpClient;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	private static OkHttpClient getSafeOkHttpClient(){
		InputStream in=null;
		try{
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			//
			in = BaseApplication.getApplication().getAssets().open(BaseConfig.OkHttp_SSL_Path);  //从assets文件读取证书
			Certificate ca = cf.generateCertificate(in);

			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//                keystore.load(null, null);
			keystore.load(null,null);
			keystore.setCertificateEntry("ca", ca);
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keystore);
			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tmf.getTrustManagers(), null);
			// Create an ssl socket factory with our all-trusting manager
			final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			// Create a trust manager that does not validate certificate chains
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.hostnameVerifier(new NullHostNameVerifier());
			builder.sslSocketFactory(sslSocketFactory);
			builder.connectTimeout(BaseConfig.OkHttp_Connect_Time, TimeUnit.SECONDS);
			builder.writeTimeout(BaseConfig.OkHttp_Write_Time,TimeUnit.SECONDS);
			builder.readTimeout(BaseConfig.OkHttp_Read_Time, TimeUnit.SECONDS);
			//            builder.retryOnConnectionFailure(false);
			//            builder.followRedirects(false);
			//            builder.followSslRedirects(false);
			mOkHttpClient = builder.build();

		} catch (Exception e) {
			e.printStackTrace();
			mOkHttpClient=null;
		}
		finally {
			try{
				if(null!=in)
				{
					in.close();
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		return mOkHttpClient;
	}
	private static ResponseParse responseParse=null;

	public  static ResponseParse getResponseParse()
	{
		if(TextUtils.isEmpty(BaseConfig.Http_ResponseParse))
		{
			return null;
		}
        if (null == responseParse) {
            try {
                Class<?> onwClass = OkHttpConfig.class.getClassLoader().loadClass(BaseConfig.Http_ResponseParse);
                Constructor<?> constructor = onwClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object object = constructor.newInstance();
				responseParse = (ResponseParse) object;

            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseParse;
	}

	public static void logRequestParam(boolean isDebug, String debugTag,Map<String, String> params) {
		if (!isDebug || null == params || params.size() <= 0) {
			return;
		}
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();
		for (Entry<String, String> param : entrys) {
			Log.i(debugTag, "参数String@" + param.getKey() + ":" + param.getValue());
		}
	}

	public static String getKeyString(String url, String tag,Map<String, String> params) {

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
		String keyString = EncryptUtils.encodingMD5(basestring.toString());
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
	public static RequestBody getOkHttpRequestByText(Object bodyParameters) {
		RequestBody requestBody = null;

		String stringBody = "";
		if (bodyParameters != null) {

			if (bodyParameters instanceof String) {
				stringBody = (String) bodyParameters;
				requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), stringBody);
			}
			else {
				stringBody = JSON.toJSONString(bodyParameters);
				requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), stringBody);
			}
		}
		return requestBody;
	}
	public static Request getOkHttpRequest(String url, Map<String, String> hashMap, boolean isPost) {
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

	private static Request getOkHttpRequestPost(String url, Map<String, String> hashMap) {
		try {
			RequestBody body = attachFormRequestForamtBody(hashMap);
			return new Request.Builder().url(url).header("Connection","close").post(body).build();
		}
		catch (Exception e) {
			return null;
		}

	}

	private static Request getOkHttpRequestGet(String url, Map<String, String> hashMap) {
		try {
			String realUrl = attachFormRequestUrl(url, hashMap);
			return new Request.Builder().url(realUrl).get().build();
		}
		catch (Exception e) {
			return null;
		}

	}

	// 获取真实的Get请求路径
	private static String attachFormRequestUrl(String url, Map<String, String> hashMap) {
		if (null == hashMap || hashMap.size() <= 0) {
			return url;
		}
		return url + "?" + attachFormRequestFormatString(hashMap);
	}

	// 构建Post请求参数
	private static RequestBody attachFormRequestForamtBody(Map<String, String> hashMap) {

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
	private static String attachFormRequestFormatString(Map<String, String> hashMap) {
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
