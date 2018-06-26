/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年5月20日 下午8:07:30
 */
package com.ruomm.baseconfig.http;

import com.ruomm.base.http.config.TextCacheGetListener;
import com.ruomm.base.tools.StringUtils;

import android.text.TextUtils;

/**
 * @author Ruby Json请求缓存自动读取
 */
public class JsonCacheGetHandler implements TextCacheGetListener {
	private String readKey=null;
	private String cacheString=null;
	private long cacheTime=0l;
	private boolean isCacheStringRead=false;

	private boolean isLruCache = true;
	private boolean isDiskLruCache = true;
	private final long cacheOffset;
	private boolean isDoRealHttp=false;
	private boolean isCacheExecuteToHttpCallBack=true;
	private boolean isCacheOk=false;

	/**
	 * @param cacheOffset
	 *            缓存过期时间
	 */
	public JsonCacheGetHandler(long cacheOffset) {
		super();
		this.isLruCache = true;
		this.isDiskLruCache = true;
		this.cacheOffset = cacheOffset;
	}

	public JsonCacheGetHandler(long cacheOffset, boolean isLruCache, boolean isDiskLruCache) {
		super();
		this.isLruCache = isLruCache;
		this.isDiskLruCache = isDiskLruCache;
		this.cacheOffset = cacheOffset;
	}
	private void readCache(String key)
	{
		if(TextUtils.isEmpty(key))
		{
			readKey=null;
			isCacheStringRead=false;
			cacheString=null;
			cacheTime=0l;
			return;
		}
		if(TextUtils.isEmpty(readKey)||!readKey.equals(key))
		{
			readKey=null;
			isCacheStringRead=false;
			cacheString=null;
			cacheTime=0l;
		}
		if(isCacheStringRead)
		{
			return;
		}
		isCacheStringRead=true;
		readKey=key;
		try{
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(key)) {
				return;
			}
			String buf = null;
			if (isLruCache) {
				buf = StringLruCache.getString(key);
			}
			if (TextUtils.isEmpty(buf) && isDiskLruCache) {
				buf = HttpDiskLruCache.getString(key);
				if (!TextUtils.isEmpty(buf)) {
					StringLruCache.putString(key, buf);
				}
			}
			if (null == buf) {
				return;
			}

			else {
				int index = buf.indexOf(":");
				if (index <= 0) {
					return;
				}
				else {
					try {
						cacheTime = Long.valueOf(buf.substring(0, index));
					}
					catch (Exception e) {
						cacheTime = 0l;
					}
					if (cacheTime <= 0) {
						return;
					}
					else {
						if (Math.abs(System.currentTimeMillis() / 1000 - cacheTime) < cacheOffset) {
							cacheString=buf.substring(index + 1);
							return;
						}
						else {
							return;
						}
					}

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			cacheString=null;
			cacheTime=0l;
		}
	}
	/**
	 * 根据Key来获取缓存的数据，若过期则返回null，不过去返回缓存的数据
	 */
	@Override
	public String getCacheString(String key) {
		readCache(key);
		return cacheString;
	}

	/**
	 * 返回负值表示会自动调用HttpCallBack的方法;
	 */
	@Override
	public long getCacheTime() {
		readCache(this.readKey);
		return this.cacheTime;
	}
	//设置缓存否有效
	@Override
	public void setCacheOk(boolean isCacheOk){
		this.isCacheOk=isCacheOk;
	}
	//缓存是否有效
	@Override
	public boolean isCacheOk(){
		return this.isCacheOk;
	}
	//是否使用httpCallBack返回数据，若使用则调用httpCallBack返回数据，否则使用httpCallBackCache返回数据
	@Override
	public TextCacheGetListener setCacheExecuteToHttpCallBack(boolean isCacheExecuteToHttpCallBack){
		this.isCacheExecuteToHttpCallBack=isCacheExecuteToHttpCallBack;
		return this;
	}
	//是否使用httpCallBack返回数据，若使用则调用httpCallBack返回数据，否则使用httpCallBackCache返回数据
	@Override
	public boolean isCacheExecuteToHttpCallBack(){
		return this.isCacheExecuteToHttpCallBack;
	}
	//设置cache有效时候是否继续执行网络请求
	@Override
	public TextCacheGetListener setDoRealHttp(boolean isDoRealHttp){
		this.isDoRealHttp=isDoRealHttp;
		return this;
	}
	//cache有效时候是否继续执行网络请求
	@Override
	public boolean isDoRealHttp(){
		return this.isDoRealHttp;
	}

	/**
	 * 返回True则表示不继续进行网络请求
	 */
	@Override
	public void httpCallBackCache(Object resultObject, byte[] resultBytes, int status, long cacheTime) {
		if(null==resultObject)
		{
			setCacheOk(false);
		}
		else{
			setCacheOk(true);
		}
	}
}
