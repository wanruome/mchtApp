/**
 *	@copyright 亿康通 -2015 
 * 	@author liufangcai  
 * 	@create 2015年7月17日 下午2:02:14 
 */
package com.ruomm.base.ioc.extend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

public class SmsObserver extends ContentObserver {
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	private final Activity mActivity;
	private String smsContent;
	private final Handler mHandler;
	private final EditText mVerifyText;

	public SmsObserver(Activity activity, Handler handler, EditText verifyText) {
		super(handler);
		// TODO Auto-generated constructor stub
		mActivity = activity;
		mHandler = handler;
		mVerifyText = verifyText;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Cursor cursor = null;// 光标
		// 读取收件箱中指定号码的短信
		cursor = mActivity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] { "_id", "address", "body", "read" },
				"read=?", new String[] { "0" }, "date desc");
		if (cursor != null) {// 如果短信为未读模式
			cursor.moveToFirst();
			if (cursor.moveToFirst()) {
				String smsbody = cursor.getString(cursor.getColumnIndex("body"));
				if (smsbody.contains("亿康通")) {
					String regEx = "[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(smsbody.toString());
					smsContent = m.replaceAll("").trim().toString();
					mVerifyText.setText(smsContent.substring(0, 6));
					mHandler.sendEmptyMessage(1);
				}
			}
		}
	}
}
