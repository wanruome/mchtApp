/**
 *	@copyright wanruome-2018
 * 	@author wanruome
 * 	@create 2018年6月16日 下午4:40:40
 */
package com.zjsj.mchtapp.config.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.config.LoginUserConfig;

public class SignTools {
	public static final String REQUEST_FIELD_SIGN_INFO = "signInfo";
	public static String getSignString(Map<String, String> maps){
		String value = getKeyString(maps);
		if(maps.containsKey("userId"))
		{
			value= value+ "token=" + LoginUserConfig.getLoginUserInfo().token;
		}
		else {
			value= value+ "token=" + ApiConfig.getPublicKeyString();
		}
		return EncryptUtils.encodingMD5(value);
	}
	public static String getSignString(Map<String, String> maps, String token) {
		String value = getKeyString(maps) + "token=" + token;
		return EncryptUtils.encodingMD5(value);
	}

	public static String getSignString(JSONObject jsonObject, String token) {
		Map<String, String> maps = parseJsonToMap(jsonObject);
		maps.remove(REQUEST_FIELD_SIGN_INFO);
		String value = getKeyString(maps) + "token=" + token;
		return EncryptUtils.encodingMD5(value);
	}

	public static boolean verifySign(JSONObject jsonObject, String token) {
		if (StringUtils.isEmpty(token) || null == jsonObject) {
			return false;
		}
		String signInfo = jsonObject.getString(REQUEST_FIELD_SIGN_INFO);
		if (StringUtils.isEmpty(signInfo)) {
			return false;
		}
		// jsonObject.remove(AppConfig.REQUEST_FIELD_SIGN_INFO);
		Map<String, String> maps = parseJsonToMap(jsonObject);
		maps.remove(REQUEST_FIELD_SIGN_INFO);
		String value = getKeyString(maps) + "token=" + token;
		return signInfo.equals(EncryptUtils.encodingMD5(value));
	}

	public static Map<String, String> parseJsonToMap(JSONObject jsonObject) {
		Map<String, String> hashMap = new HashMap<>();
		for (String key : jsonObject.keySet()) {
			String strVal = jsonObject.getString(key);
			if (null == strVal || strVal.trim().length() == 0) {
				continue;
			}
			if (strVal.startsWith("{") && strVal.endsWith("}")) {
				continue;
			}
			if (strVal.startsWith("[") && strVal.endsWith("]")) {
				continue;
			}
			else {
				hashMap.put(key, strVal);
			}
		}
		return hashMap;
	}

	public static String getKeyString(Map<String, String> mapValues) {

		// 遍历排序后的字典，将所有参数按"key=value&"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		if (null != mapValues && mapValues.size() > 0) {
			// 先将参数以其参数名的字典序升序进行排序
			TreeMap<String, String> treeMapValues = new TreeMap<String, String>(mapValues);
			Set<String> keySet = treeMapValues.keySet();
			for (String key : keySet) {
				String value = treeMapValues.get(key);
				if (null == value || value.trim().length() <= 0) {
					continue;
				}
				// sb.append(treeMapValues.get(key));
				sb.append(key).append("=").append(value).append("&");
			}
		}
		return sb.toString();
	}
	// public static String getKeyString(Map<String, String> mapValues, String signKey) {
	//
	// // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	// StringBuilder sb = new StringBuilder();
	// if (null != mapValues && mapValues.size() > 0) {
	// // 先将参数以其参数名的字典序升序进行排序
	// TreeMap<String, String> treeMapValues = new TreeMap<String, String>(mapValues);
	// Set<String> keySet = treeMapValues.keySet();
	// for (String key : keySet) {
	// if (!key.equals(signKey)) {
	// sb.append(treeMapValues.get(key));
	// }
	// }
	// // Set<Entry<String, String>> treeEntrySet = treeMapValues.entrySet();
	// // for (Entry<String, String> keyValues : treeEntrySet) {
	// // if (!keyValues.getKey().equals(signKey)) {
	// // sb.append(keyValues.getValue());
	// // }
	// // }
	// }
	// return sb.toString();
	// }
}
