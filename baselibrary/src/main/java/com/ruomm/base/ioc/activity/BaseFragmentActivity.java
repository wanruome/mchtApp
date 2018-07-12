package com.ruomm.base.ioc.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

import com.ruomm.base.ioc.annotation.InjectUIStyle;
import com.ruomm.base.ioc.task.ResumeFormBackGroundTask;
import com.ruomm.base.ioc.task.TaskUtil;
import com.ruomm.base.tools.StatusBarUtil;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.debug.MLog;

@SuppressLint("NewApi")
public abstract class BaseFragmentActivity extends FragmentActivity {
	/**
	 * 显式声明需要调用的Context
	 */
	protected Context mContext;
	/**
	 * 显式声明需要调用的Bundle
	 */
	protected Bundle mBundle;
	/**
	 * 显式声明需要调用的FragmentManager类
	 */
	protected FragmentManager mFManager;

	/**
	 * 注入AppManager管理类和显式声明变量复制
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		mFManager = getSupportFragmentManager();
		mBundle = getIntent().getExtras();
		boolean isEnableBarInit = BaseConfig.UIBarTint_Enable;
		InjectUIStyle injectUIStyle=this.getClass().getAnnotation(InjectUIStyle.class);
		if (isEnableBarInit && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&(null==injectUIStyle||injectUIStyle.isEnableBarInit())) {
			StatusBarUtil.setTranslucent(this);
            StatusBarUtil.setLightMode(this);
//			setTranslucentStatus(true);
//			SystemBarTintManager tintManager = new SystemBarTintManager(this);
//			tintManager.setStatusBarTintEnabled(true);
//			tintManager.setStatusBarTintColor(BaseConfig.UIBarTint_Color);
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
