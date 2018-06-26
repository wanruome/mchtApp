/**
 *	@copyright 婉若小雪-2015
 * 	@author wanruome
 * 	@create 2015年3月19日 下午4:42:51
 */
package com.ruomm.base.ioc.extend;

import com.ruomm.base.ioc.iocutil.BaseServiceUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Service重写类，加入操作类型判定，service在onStart的时候可以依照不同的getOptValue值来执行不同的逻辑代码，需要传入key值BaseServiceUtil.
 * OptType，类型为Integer的Value的数据值
 * 
 * @author Ruby
 */
public abstract class BaseService extends Service {
	/**
	 * 显式声明Context
	 */
	protected Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}

	/**
	 * 获取Service的启动类型，在onStart的时候可以依照不同的getOptValue值来执行不同的逻辑代码
	 * 
	 * @param intent
	 * @return
	 */
	protected int getOptValue(Intent intent) {
		try {
			Bundle data = intent.getExtras();
			if (null != data && data.containsKey(BaseServiceUtil.OptType)) {
				return data.getInt(BaseServiceUtil.OptType, BaseServiceUtil.OptValueDefault);

			}
			else {
				return BaseServiceUtil.OptValueDefault;
			}
		}
		catch (Exception e) {
			return BaseServiceUtil.OptValueDefault;
		}

	}
}
