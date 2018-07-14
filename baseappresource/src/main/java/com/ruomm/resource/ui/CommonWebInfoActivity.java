/**
 *	@copyright 亿康通 -2015 
 * 	@author liufangcai  
 * 	@create 2015年8月6日 下午5:33:18 
 */
package com.ruomm.resource.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.R;
import com.ruomm.resource.ui.dal.WebUrlInfo;

public class CommonWebInfoActivity extends AppMultiActivity implements MenuTopListener {
	private WebView mywebview;
	private WebUrlInfo mWebUrlInfo;

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setInitContentView(R.layout.common_webview);
		mywebview = (WebView) findViewById(R.id.common_webview);
		mWebUrlInfo = BaseUtil.serializableGet(mBundle, WebUrlInfo.class);
		setWebView();
		showWebInfo();
	}

	// 显示Web内容
	private void showWebInfo() {

		if (!TextUtils.isEmpty(mWebUrlInfo.getTitle())) {
			mymenutop.setCenterText(mWebUrlInfo.getTitle());
		}
		else {
			mymenutop.setCenterText("商户服务App");
		}
		if(!StringUtils.isEmpty(mWebUrlInfo.getWebData())){
			mywebview.loadData(mWebUrlInfo.getWebData(), "text/html", "UTF-8");
		}
		else {
		// post构造方法
			if (mWebUrlInfo.isPost()) {
				byte[] postdata = mWebUrlInfo.parseWebPostParams();
				if (null != postdata) {
					mywebview.postUrl(mWebUrlInfo.getUrl(), postdata);
				}
				else {
					mywebview.postUrl(mWebUrlInfo.getUrl(), null);
				}
			}
			// get构造方法
			else {
				mywebview.loadUrl(mWebUrlInfo.getUrl() + mWebUrlInfo.parseWebGetParams());

			}
		}
	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	private void setWebView() {

		WebSettings wSet = mywebview.getSettings();
		if (mWebUrlInfo.isZoom()) {
			wSet.setBuiltInZoomControls(true);
		}
		wSet.setJavaScriptEnabled(true);
		wSet.setDomStorageEnabled(true);
		wSet.setDefaultFontSize(16);
		wSet.setDisplayZoomControls(true);

		wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		WebChromeClient wcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);

			}

		};
		mywebview.setWebChromeClient(wcc);

		mywebview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;

			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}
		});
		mywebview.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					hideImput();
				}
				return false;
			}
		});

	}

	private void hideImput() {
		InputMethodManager inputMethodManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(mywebview.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onMenuTopClick(View v, int vID) {
		// TODO Auto-generated method stub
		if (vID == R.id.menutop_left) {
			if (mywebview.canGoBack()) {
				mywebview.goBack();
			}
			else {
				finish();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mywebview.canGoBack()) {
				mywebview.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
