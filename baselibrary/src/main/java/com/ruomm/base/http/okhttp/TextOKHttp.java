/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年11月4日 下午2:09:21
 */
package com.ruomm.base.http.okhttp;

import java.io.IOException;
import java.util.HashMap;

import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.http.config.ResponseText;
import com.ruomm.base.http.config.TextCacheGetListener;
import com.ruomm.base.http.config.TextCacheSaveListener;
import com.ruomm.base.http.config.TextHttpListener;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 使用OK-Http模式的网络请求类
 * <p>
 * 默认使用post模式的异步下载
 *
 * @author Ruby
 */
public final class TextOKHttp {
	// 网络请求的Json回调监听
	private TextHttpListener textHttpListener;
	// 网络请求的Json回调目标解析对象的类型
	private Class<?> cls;
	// 网络请求返回自动解析的数据类型
	//	private HttpConfig.RESPONSETYPE mRspType = null;
	// 网络请求的时候调试输出的tag
	private String debugTag;
	// 是否使用异步模式进行网络请求
	private boolean isAjax;
	// 是否使用Post模式设置参数进行网络请求
	private boolean isPost;
	// 是否输出调试信息
	private boolean isDebug;
	// 网络请求的路径
	private String Url;
	// 下载请求的请求Body体，可以自己设置请求的RequestBody;
	private RequestBody mRequestBody;
	// 下载请求的参数列表，String类型的自动构建RequestBody
	private HashMap<String, String> mRequestHashMap;
	// 异步请求回调所需的handler
	private Handler UIHandler = null;
	// 异步请求回调到JsonOkHttp线程的Looper
	private Looper myLooper = null;
	// 请求的Call,可以调用cancleCall()来取消网络请求
	private Call mCall;
	private String tag;
	// 同步请求响应结果
	private ResponseText mResponseJson;
	// 网络下载请求的key值，构建模式为请求路经+keyTag+参数通过MD5加密后的值
	private String key;
	// 是否把缓存的结果调用JsonHttpListener的httpCallBack方法来统一处理
	private boolean isCacheExecuteToHttp = true;
	// 网络请求前获取缓存数据，可依据缓存结果是否继续网络请求
	private TextCacheGetListener cacheGetListener;
	// 网络请求结束后存储请求结果到缓存数据
	private TextCacheSaveListener cacheSaveListener;
	private OkHttpClient okHttpClient=null;
    public TextOKHttp setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }
    public OkHttpClient getOkHttpClient(){
        if(null!=okHttpClient)
        {
            return okHttpClient;
        }
        else{
            return OkHttpConfig.getOkHttpClient();
        }
    }

	/**
	 * 构造方法
	 *
	 * @return
	 */
	public TextOKHttp() {
		this.Url = null;
		this.isAjax = true;
		this.isPost = true;
		if (HttpConfig.debug_autonewhttp) {
			setDebug();
		}

	}

	/**
	 * @param cacheGetListener
	 *            缓存读取设置
	 * @return
	 */
	public TextOKHttp setCacheGetListener(TextCacheGetListener cacheGetListener) {
		this.cacheGetListener = cacheGetListener;
		return this;
	}

	/**
	 * 是否把缓存的结果调用JsonHttpListener的httpCallBack方法来统一处理
	 *
	 * @param isCacheExecuteToHttp
	 *            是否调用JsonHttpListener的httpCallBack方法来统一处理缓存结果
	 * @return
	 */
	public TextOKHttp setCacheExecuteToHttp(boolean isCacheExecuteToHttp) {
		this.isCacheExecuteToHttp = isCacheExecuteToHttp;
		return this;
	}

	/**
	 * @param cacheSaveListener
	 *            缓存写入设置
	 * @return
	 */
	public TextOKHttp setCacheSaveListener(TextCacheSaveListener cacheSaveListener) {
		this.cacheSaveListener = cacheSaveListener;
		return this;
	}

	/**
	 * @param listener
	 *            Http请求监听
	 * @return
	 */
	public TextOKHttp setHttpListener(TextHttpListener listener) {
		this.textHttpListener = listener;
		return this;
	}

	/**
	 * 调试模式设置
	 *
	 * @return
	 */
	public TextOKHttp setDebug() {
		if (HttpConfig.debug_public) {
			this.debugTag = "OkHttpInfo";
			this.isDebug = true;
		}
		else {
			this.debugTag = null;
			this.isDebug = false;
		}

		return this;
	}

	/**
	 * 调试模式Tag设置
	 *
	 * @param debugTag
	 *            调试模式输出信息的Tag
	 * @return
	 */
	public TextOKHttp setDebug(String debugTag) {

		if (HttpConfig.debug_public) {
			if (TextUtils.isEmpty(debugTag)) {
				this.debugTag = "OkHttpInfo";
			}
			else {
				this.debugTag = debugTag;
			}
			this.isDebug = true;
		}
		else {
			this.debugTag = null;
			this.isDebug = false;
		}

		return this;
	}

	/**
	 * @param url
	 *            请求路径设置
	 * @return
	 */

	public TextOKHttp setUrl(String url) {
		this.Url = url;
		return this;
	}

	/**
	 * 请求参数设置
	 *
	 * @param hashMap
	 *            请求参数集合
	 * @return
	 */
	public TextOKHttp setRequestParams(HashMap<String, String> hashMap) {
		this.mRequestHashMap = hashMap;
		return this;
	}

	/**
	 * 设置请求RequestBody
	 *
	 * @param mRequestBody
	 *            请求RequestBody
	 * @return
	 */
	public TextOKHttp setRequestBody(RequestBody mRequestBody) {
		this.mRequestBody = mRequestBody;
		return this;
	}

	/**
	 * @param isPost
	 *            是Post请求还是Get请求
	 * @param isAjax
	 *            是否异步请求
	 * @return
	 */
	public TextOKHttp setMode(boolean isPost, boolean isAjax) {
		this.isPost = isPost;
		this.isAjax = isAjax;
		return this;
	}

	/**
	 * @param isAjax
	 *            是否异步请求
	 * @return
	 */
	public TextOKHttp setAjax(boolean isAjax) {
		this.isAjax = isAjax;
		return this;
	}

	/**
	 * @param isPost
	 *            是Post请求还是Get请求
	 * @return
	 */
	public TextOKHttp setMethodType(boolean isPost) {
		this.isPost = isPost;
		return this;
	}

	//	/**
	//	 * 网络请求数据自动解析类型，默认JSON解析
	//	 *
	//	 * @param mType
	//	 * @return
	//	 */
	//	public TextOKHttp setResponseType(HttpConfig.RESPONSETYPE mRspType) {
	//		this.mRspType = mRspType;
	//		return this;
	//	}

	/**
	 * 获取请求tag标识
	 *
	 * @return
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 设置请求tag标识
	 *
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	public void cancleCall() {
		if (null != mCall) {
			mCall.cancel();
		}
	}

	/**
	 * 获取回调函数
	 *
	 * @return
	 */
	private Callback getCallback() {
		return new Callback() {

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				mCall = null;
				parseResponse(response);

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				mCall = null;
				if (isDebug) {
					Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Fail + ";msg:失败");
				}

				httpCallBack(null, null, HttpConfig.Fail);

			}
		};

	}

	/**
	 * 解析下载结果为回调所需的数据
	 *
	 * @param response
	 */
	private void parseResponse(Response response) {
		if (null == response) {
			if (isDebug) {
				Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Fail + ";msg:失败");
			}
			httpCallBack(null, null, HttpConfig.Fail);
			return;
		}
		else {
			String responseString = null;
			try {
				responseString = response.body().string();
			}
			catch (Exception e) {
				responseString = null;
			}
			if (TextUtils.isEmpty(responseString)) {
				if (isDebug) {
					Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Fail + ";msg:失败没有数据返回");
				}
				httpCallBack(null, null, HttpConfig.Fail);
			}
			else {
				if (isDebug) {
					Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Success + ";msg:返回数据成功");
					Log.i(debugTag, "结果JSON@" + responseString);
				}
				parseHttpCallBackSucess(responseString);

			}

		}
	}

	/**
	 * 网络请求过滤器回调，若是返回true，则不执行httpCallBack方法了
	 *
	 * @param realDataString
	 *            网络请求返回的数据
	 * @return
	 */
	private boolean httpCallBackFilter(String realDataString) {
		boolean isFilter = false;
		if (null != textHttpListener) {
			isFilter = textHttpListener.httpCallBackFilter(realDataString, HttpConfig.Success);
		}
		if (isFilter) {
			if (isDebug) {
				Log.i(debugTag, "结果状态过滤拦截@" + "status:" + HttpConfig.Success_Filter + ";msg:过滤器拦截成功");
			}
		}
		return isFilter;
	}

	/**
	 * 回调网络请求结果
	 *
	 * @param resultObject
	 *            请求结果解析成的Object对象
	 * @param resultString
	 *            请求结果的原始数据
	 * @param status
	 *            请求结果的状态
	 */
	private void httpCallBack(final Object resultObject, final String resultString, final int status) {
		mCall = null;
		if (!isAjax) {
			mResponseJson = new ResponseText();
			mResponseJson.resultObject = resultObject;
			mResponseJson.status = status;
			mResponseJson.resultString = resultString;
		}
		if (null == textHttpListener) {
			if (isDebug) {
				Log.i(debugTag, "结果状态JSON解析@" + "status:" + HttpConfig.Success + ";msg:没有回调注册函数");
			}
			quitLooper();
			return;
		}
		if (null == UIHandler) {
			textHttpListener.httpCallBack(resultObject, resultString, status);
		}
		else {
			SystemClock.sleep(10);
			UIHandler.post(new Runnable() {

				@Override
				public void run() {
					textHttpListener.httpCallBack(resultObject, resultString, status);
					quitLooper();
				}
			});

		}

	}

	/**
	 * 解析网络请求结果为符合回调用的数据
	 *
	 * @param responseString
	 */
	private void parseHttpCallBackSucess(final String responseString) {
		if (httpCallBackFilter(responseString)) {
			httpCallBack(null, responseString, HttpConfig.Success_Filter);
			return;
		}
		else {
			// 对象解析开始
			Object object = HttpConfig.parseResponseText( responseString, cls);
			// 回调开始
			if (null == object) {
				if (isDebug) {
					Log.i(debugTag, "结果状态JSON解析@" + "status:" + HttpConfig.Success_ParseError + ";msg:对象解析错误");
				}
				httpCallBack(null, responseString, HttpConfig.Success_ParseError);
			}
			else {
				if (isDebug) {
					Log.i(debugTag, "结果状态JSON解析@" + "status:" + HttpConfig.Success + ";msg:对象解析成功");
				}
				if (null != cacheSaveListener) {
					cacheSaveListener.saveResulit(key,object, responseString);
				}
				httpCallBack(object, responseString, HttpConfig.Success);

			}
			// 回调结束
		}
	}

	/**
	 * 判定Url是否合法
	 *
	 * @return
	 */
	private boolean isTrueHttp() {
		if (TextUtils.isEmpty(this.Url)) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * 获取请求的key值
	 *
	 * @return
	 */
	public String getOkHttpKey() {
		if (null == this.key) {
			this.key = OkHttpConfig.getKeyString(this.Url, this.tag, mRequestHashMap);
		}

		return this.key;
	}

	/**
	 * 获取下载请求的RequestBody
	 *
	 * @return
	 */
	private Request getOkHttpRequest() {
		Request mRequest = null;
		if (null != this.mRequestBody) {
			mRequest = OkHttpConfig.getOkHttpRequestByRequestBody(this.Url, this.mRequestBody, this.isPost);
		}
		else {
			mRequest = OkHttpConfig.getOkHttpRequest(this.Url, this.mRequestHashMap, this.isPost);
		}
		return mRequest;
	}

	/**
	 * 注册Looper
	 */
	private void initLopper() {
		if (!isAjax) {
			quitLooper();
			return;
		}
		else {
			if (null == Looper.myLooper()) {
				Looper.prepare();
				myLooper = Looper.myLooper();
				UIHandler = new Handler(myLooper);
				new Thread() {
					@Override
					public void run() {
						Looper.loop();
					};
				}.start();

			}
			else {
				UIHandler = new Handler(Looper.myLooper());
			}
		}
	}

	/**
	 * 退出Lopper线程
	 */
	private void quitLooper() {
		if (null != UIHandler) {
			UIHandler = null;
		}
		if (null != myLooper) {
			myLooper.quit();
			myLooper = null;
		}
	}

	/**
	 * 执行Http请求
	 *
	 * @param cls
	 *            结果解析类型
	 * @param listener
	 *            请求CallBcak监听
	 */
	public void doHttp(Class<?> cls, TextHttpListener listener) {
		this.cls = cls;
		setHttpListener(listener);
		doHttp();

	}

	/**
	 * 执行Http同步请求
	 *
	 * @param cls
	 *            结果解析类型
	 * @param listener
	 *            请求CallBcak监听
	 */
	public ResponseText doHttpSync(Class<?> cls, TextHttpListener listener) {
		this.cls = cls;
		setAjax(false);
		setHttpListener(listener);
		doHttp();
		return mResponseJson;

	}

	private void doHttp() {
		if (isDebug) {
			Log.i(debugTag, "请求路径@" + this.Url);
			OkHttpConfig.logRequestParam(isDebug, debugTag, this.mRequestHashMap);
		}

		if (!isTrueHttp()) {
			if (isDebug) {
				Log.i(debugTag, "请求路径不正确");
			}
			httpCallBack(null, null, HttpConfig.Fail_ParamsError);
			return;
		}
		// 设置请求的Key值
		// this.key = getOkHttpKey();
		// 设置请求的Tag值
		// if (!OkHttpUtil.isTrueHttpTag(tag)) {
		// setTag(this.key);
		// }
		// 判定是否启用OKHttpClient
		if (null == getOkHttpClient()) {
			if (TextUtils.isEmpty(debugTag)) {
				Log.e("OkHttpInfo", "OKHttpClient空错误@" + "没有OKHttp的请求Client");
			}
			else {
				Log.e(debugTag, "OKHttpClient空错误@" + "没有OKHttp的请求Client");
			}
			httpCallBack(null, null, HttpConfig.Fail_ParamsError);
			return;
		}
		// 开始网络请求
		Request mRequest = getOkHttpRequest();
		if (null == mRequest) {
			if (isDebug) {
				Log.i(debugTag, "请求参数不正确不正确");
			}
			httpCallBack(null, null, HttpConfig.Fail_ParamsError);
			return;
		}
        //检查并执行请求缓存
		doCacheRead();
		if(!isContinueDoRealHttp())
		{
			return;
		}
		//开始网络请求
		if (isAjax) {
			initLopper();
			mCall = getOkHttpClient().newCall(mRequest);
			mCall.enqueue(getCallback());
		}
		else {
			try {
				mCall = getOkHttpClient().newCall(mRequest);
				Response response = mCall.execute();
				parseResponse(response);
			}
			catch (Exception e) {
				if (isDebug) {
					Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Fail + ";msg:失败");
				}
				httpCallBack(null, null, HttpConfig.Fail);
			}

		}
	}

	/**
	 * 读取缓存的网络请求，如是返回true则不进行网络请求，直接使用缓存数据
	 *
	 * @return
	 */
	private boolean doCacheRead() {

		if (null == cacheGetListener || TextUtils.isEmpty(getOkHttpKey())) {
			return false;
		}
		String cacheString = cacheGetListener.getCacheString(this.key);
		if (TextUtils.isEmpty(cacheString)) {
			return false;
		}
		Object object = HttpConfig.parseResponseText( cacheString, cls);
		if (null == object) {
			if (isDebug) {
				Log.i(debugTag, "缓存结果@" + "没有从缓存中读取到结果");
			}
			return false;
		}
		if (isDebug) {
			Log.i(debugTag, "请求结果@" + "从缓存中读取到结果了");
			Log.i(debugTag, "结果JSON缓存@" + cacheString);
		}
        if (isDebug) {
				Log.i(debugTag, "调用httpCallBackCache执行缓存业务逻辑");
			}
        cacheGetListener.httpCallBackCache(object, cacheString.getBytes(),HttpConfig.Success,cacheGetListener.getCacheTime());
		if(cacheGetListener.isCacheExecuteToHttpCallBack())
		{
			if (isDebug) {
				Log.i(debugTag, "调用httpCallBack执行缓存业务逻辑");
			}
			httpCallBack(object, cacheString, HttpConfig.Success);
		}
//		else
//		{
//			if (isDebug) {
//				Log.i(debugTag, "调用httpCallBackCache执行缓存业务逻辑");
//			}
//			cacheGetListener.httpCallBackCache(object, cacheString.getBytes(),HttpConfig.Success,cacheGetListener.getCacheTime());
//		}
		return true;
	}
    //缓存执行后是否继续执行网络请求
    private boolean isContinueDoRealHttp(){
        if(null==cacheGetListener){
            return true;
        }
        else{
            if(cacheGetListener.isCacheOk())
            {
                return cacheGetListener.isDoRealHttp();
            }
            else {
                return true;
            }
        }
    }
}
