package com.ruomm.base.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruomm.baseconfig.DebugConfig;

import android.text.TextUtils;

public class HttpConfig {
	public final static int HTTP_CLIENT_TIMEOUT = 10000;
	public static boolean debug_public = DebugConfig.ISDEBUG;
	public static boolean debug_autonewhttp = DebugConfig.HTTP_AUTODEBUG;
	public final static String OkHttp_Call_Tag = "okhttp";
	public final static String Method_Post = "post";
	public final static String Method_Get = "get";
	public final static String ResponseType_Json = "json";
	public final static String ResponseType_Xml = "xml";
	public final static String ResponseType_String = "string";
	public final static int Cancle_Http = 406;
	public final static int Fail_NoFullDataReturn = 407;
	public final static int Fail_TimeOut = 405;
	public final static int Fail_ParamsError = 402;
	public final static int Fail_FileNull = 404;
	public final static int Fail_FileExist = 403;
	public final static int Fail_NetError = 401;
	public final static int Fail = 400;
	public final static int Finish = 300;
	public final static int Success_Filter = 204;
	public final static int Success = 200;
	public final static int Success_ParseError = 202;
	private final static String ParseString = "parseString";
	private final static String ParseByte = "parseByte";

	public static Object parseResponseData(byte[] resourceByte, Class<?> cls) {
		if (null == cls) {
			return resourceByte;
		}
		if (String.class.getName().equals(cls.getName())) {
			return byteToString(resourceByte);
		}
		if (JSONObject.class.getName().equals(cls.getName())) {
			return parseToJSONObject(getRealJsonString(byteToString(resourceByte)));
		}
		if (JSONArray.class.getName().equals(cls.getName())) {
			return parseToJSONArray(getRealJsonString(byteToString(resourceByte)));
		}
		Method method = null;
		{
			if (null != cls) {
				try {
					method = cls.getDeclaredMethod(ParseByte, byte[].class);
				}
				catch (Exception e) {
					method = null;
				}
			}
		}
		if (null == method) {
			// 默认解析为JSON对象
			return parseTextToJson(getRealJsonString(byteToString(resourceByte)), cls);
		}
		else {
			Object objinstance = null;
			try {
				Constructor<?> constructor = cls.getDeclaredConstructor();
				if (null != constructor) {
					constructor.setAccessible(true);
					objinstance = constructor.newInstance();
				}
				method.setAccessible(true);
				method.invoke(objinstance, resourceByte);
			}
			catch (Exception e) {
				objinstance = null;
			}

			return objinstance;
		}

	}

	public static Object parseResponseText(String resourceString, Class<?> cls) {
		if (null == cls) {
			return resourceString;
		}
		if (String.class.getName().equals(cls.getName())) {
			return resourceString;
		}
		if (JSONObject.class.getName().equals(cls.getName())) {
			return parseToJSONObject(resourceString);
		}
		if (JSONArray.class.getName().equals(cls.getName())) {
			return parseToJSONArray(resourceString);
		}
		Method method = null;
		try {
			method = cls.getDeclaredMethod(ParseString, String.class);
		}
		catch (Exception e) {
			method = null;
		}
		if (null == method) {
			return parseTextToJson(resourceString, cls);
		}
		else {
			Object objinstance = null;
			try {
				Constructor<?> constructor = cls.getDeclaredConstructor();
				if (null != constructor) {
					constructor.setAccessible(true);
					objinstance = constructor.newInstance();
				}
				method.setAccessible(true);
				method.invoke(objinstance, resourceString);
			}
			catch (Exception e) {
				objinstance = null;
			}

			return objinstance;
		}
	}

	private static String byteToString(byte[] resourceByte) {
		String temp = null;
		try {
			temp = new String(resourceByte);
		}
		catch (Exception e) {
			temp = null;
		}
		return temp;
	}

	private static String getRealJsonString(String responseString) {
		String jsonString = null;
		if (!TextUtils.isEmpty(responseString) && !responseString.startsWith("\"") && !responseString.endsWith("\"")) {
			jsonString = responseString;
		}
		else if (!TextUtils.isEmpty(responseString) && responseString.startsWith("\"")
				&& responseString.endsWith("\"")) {
			try {
				jsonString = responseString.substring(1, responseString.length() - 1);
			}
			catch (Exception e) {
				jsonString = null;
			}

		}
		else if (!TextUtils.isEmpty(responseString)) {
			jsonString = responseString;
		}
		else {
			jsonString = null;
		}
		return jsonString;
	}

	private static JSONObject parseToJSONObject(String responseString) {
		String jsonString = getRealJsonString(responseString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}
		JSONObject object = null;
		try {
			object = JSON.parseObject(jsonString);
		}
		catch (Exception e) {
			object = null;
		}
		return object;
	}

	private static JSONArray parseToJSONArray(String responseString) {
		String jsonString = getRealJsonString(responseString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}
		JSONArray object = null;
		try {
			object = JSON.parseArray(jsonString);
		}
		catch (Exception e) {
			object = null;
		}
		return object;
	}

	private static Object parseTextToJson(String responseString, Class<?> cls) {
		String jsonString = getRealJsonString(responseString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}
		Object object = null;
		try {
			if (jsonString.startsWith("[")) {
				object = JSON.parseArray(jsonString, cls);
			}
			else {
				object = JSON.parseObject(jsonString, cls);
			}
		}
		catch (Exception e) {
			object = null;
		}
		return object;
	}

}
