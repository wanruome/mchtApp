/**
 *	@copyright 视秀科技-2015 
 * 	@author wanruome  
 * 	@create 2015年3月10日 下午4:17:44 
 */
package com.ruomm.resource.ui.dal;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class WebUrlInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8931642961893660934L;
	private String url;
	private String title;
	private String webData;
	private HashMap<String, String> params;
//	private ArrayList<String> JsHandlers;
	private boolean isPost = false;
	private boolean isZoom = true;

	public String getUrl() {
		return url;
	}

	public void setZoom(boolean isZoom) {
		this.isZoom = isZoom;
	}

	public boolean isZoom() {
		return isZoom;
	}

	public WebUrlInfo setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public WebUrlInfo setTitle(String title) {
		this.title = title;
		return this;
	}

	public HashMap<String, String> getParams() {

		return params;

	}

	public WebUrlInfo setParams(HashMap<String, String> params) {
		if (null != params) {
			if (null != this.params) {
				this.params.clear();
			}
			else {
				this.params = new HashMap<String, String>();
			}

			this.params.putAll(params);

		}
		else {
			if (null != this.params) {
				this.params.clear();
			}
			this.params.clear();
			// this.params.putAll(params);
		}
		return this;
	}

//	public WebUrlInfo setJsHandlers(String... args) {
//		if (null == args || args.length == 0) {
//			if (null != JsHandlers) {
//				JsHandlers.clear();
//			}
//		}
//		else {
//			if (null != JsHandlers) {
//				JsHandlers.clear();
//			}
//			else {
//				JsHandlers = new ArrayList<String>();
//			}
//			for (String value : args) {
//				JsHandlers.add(value);
//			}
//		}
//		return this;
//	}
//
//	public ArrayList<String> getJsHandlers() {
//		return JsHandlers;
//	}

	public WebUrlInfo putParams(String key, String value) {
		if (null == params) {
			params = new HashMap<String, String>();
		}
		params.put(key, value);
		return this;
	}

	public boolean isPost() {
		return isPost;
	}

	public WebUrlInfo setPost(boolean isPost) {
		this.isPost = isPost;
		return this;
	}

	public String getWebData() {
		return webData;
	}

	public void setWebData(String webData) {
		this.webData = webData;
	}

	public byte[] parseWebPostParams() {
		if (null != params && params.size() > 0) {
			StringBuilder buf = new StringBuilder();
			Set<String> setParmas = params.keySet();
			int size = setParmas.size();
			int index = 0;
			for (String key : setParmas) {
				index++;
				buf.append(key + "=" + params.get(key));
				if (index != size) {
					buf.append("&");
				}
			}
			return com.ruomm.base.tools.Base64.encode(new String(buf).getBytes()).getBytes();
//			return EncodingUtils.getBytes(new String(buf), "BASE64");
		}
		else {
			return null;
		}
	}

	public String parseWebGetParams() {
		if (null == params || params.size() == 0) {
			return "";
		}
		else {
			StringBuilder buf = new StringBuilder();
			buf.append("?");
			Set<String> setParmas = params.keySet();
			int size = setParmas.size();
			int index = 0;
			for (String key : setParmas) {
				index++;
				buf.append(key + "=" + params.get(key));
				if (index != size) {
					buf.append("&");
				}
			}
			return buf.toString();
		}
	}

}
