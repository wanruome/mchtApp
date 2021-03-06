package com.ruomm.base.ioc.activity;

import com.ruomm.base.ioc.annotation.InjectUIStyle;
import com.ruomm.base.ioc.task.BaseApplicationTask;
import com.ruomm.base.ioc.task.ResumeFormBackGroundTask;
import com.ruomm.base.ioc.task.TaskUtil;
import com.ruomm.base.tools.StatusBarUtil;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.debug.MLog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import org.bouncycastle.jce.provider.symmetric.ARC4;

import java.lang.reflect.Constructor;

public abstract class BaseActivity extends Activity {
	/**
	 * 显式声明需要调用的Context
	 */
	protected Context mContext;
	/**
	 * 显式声明需要调用的Bundle
	 */
	protected Bundle mBundle;

	/**
	 * 注入AppManager管理类和显式声明变量复制
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		mBundle = getIntent().getExtras();
		// 设置沉浸模式是否开启,可以通过在Activity上注解InjectUIStyle来调整
		boolean isEnableBarInit = BaseConfig.UIBarTint_Enable;
		InjectUIStyle injectUIStyle=this.getClass().getAnnotation(InjectUIStyle.class);
		if (isEnableBarInit && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&(null==injectUIStyle||injectUIStyle.isEnableBarInit())) {
			StatusBarUtil.setTranslucent(this,255);
			StatusBarUtil.setLightMode(this);
//			setTranslucentStatus(true);
//			SystemBarTintManager tintManager = new SystemBarTintManager(this);
//			tintManager.setStatusBarTintEnabled(true);
//			//			tintManager.setStatusBarTintColor(0xFFFF00);
//			tintManager.setStatusBarTintColor(BaseConfig.UIBarTint_Color);
			//			tintManager.setStatusBarTintResource(R.color.holo_red_dark);

		}
	}
	// 设置沉浸样式
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();

		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		}
		else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onResume() {
		super.onResume();
		doResumeFormBackGroundTask();
	}

	private void  doResumeFormBackGroundTask()
	{
		if(AppManager.isResumeFromBackGround()){
			MLog.i("执行后台返回的一些任务");
			ResumeFormBackGroundTask resumeFormBackGroundTask= TaskUtil.getTask(BaseConfig.AppResumeFormBackGroundTask);
			if(null!=resumeFormBackGroundTask)
			{
				resumeFormBackGroundTask.doTaskResumeFormBack(mContext);
			}
		}
	}
}
