package com.ruomm.resource.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.activity.AppManager;
import com.ruomm.base.ioc.activity.BaseFragmentActivity;
import com.ruomm.base.ioc.annotation.InjectUIStyle;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StatusBarUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.resource.R;
import com.ruomm.resource.dialog.DialogLoadingListener;
import com.ruomm.resource.dialog.DialogUtil;

/**
 * Created by Niu on 2017/8/2.
 */
@InjectUIStyle
public abstract  class AppMultiActivity extends FragmentActivity implements DialogLoadingListener {
    private boolean isShowDialog00 = false;
    private boolean isShowDialog01 = false;
    protected BaseApplication app = BaseApplication.getApplication();

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return DialogUtil.createLoadingDialog(mContext, true);
        }
        if (id == 1) {
            return DialogUtil.createLoadingDialog(mContext, false);
        }
        return super.onCreateDialog(id);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle data) {
        // TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, data);
        String textDialog = null;
        if (null != data && data.containsKey("text")) {
            textDialog = data.getString("text");
        }
        if (id == 0 || id == 1) {
            TextView dialogloading_text = (TextView) dialog.findViewById(R.id.dialogloading_text);
            if (TextUtils.isEmpty(textDialog)) {
                dialogloading_text.setText(R.string.loading_waiting_text);
            }
            else {
                dialogloading_text.setText(textDialog);
            }

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        // TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog);
        if (id == 0 || id == 1) {
            TextView dialogloading_text = (TextView) dialog.findViewById(R.id.dialogloading_text);
            dialogloading_text.setText(R.string.loading_waiting_text);

        }
    }

    @SuppressWarnings("deprecation")
    private void showDialog(int id, String textDialog) {
        if (TextUtils.isEmpty(textDialog)) {
            if (!isShowDialog01) {
                showDialog(id);
                if (id == 0) {
                    isShowDialog00 = true;
                }
                else if (id == 1) {
                    isShowDialog01 = true;
                }
            }
        }
        else {
            if (!isShowDialog01) {
                Bundle data = new Bundle();
                data.putString("text", textDialog);
                showDialog(id, data);
                if (id == 0) {
                    isShowDialog00 = true;
                }
                else if (id == 1) {
                    isShowDialog01 = true;
                }
            }
        }
    }

    private void showDialog(int id, int textRes) {
        String textDialog = StringUtils.nullStrToEmpty(mContext.getResources().getString(textRes));
        showDialog(id, textDialog);
    }

    @Override
    public void showProgressDialog(int textRes) {
        showDialog(0, textRes);

    }

    @Override
    public void showProgressDialog(String textDialog) {
        showDialog(0, textDialog);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void dismissProgressDialog() {
        // dismissLoading();
        if (isShowDialog00) {
            dismissDialog(0);
            isShowDialog00 = false;
        }

    }

    @Override
    public void showLoading() {
        showDialog(1, "");

    }

    @Override
    public void showLoading(int textRes) {
        showDialog(1, textRes);

    }

    @Override
    public void showLoading(String textDialog) {
        showDialog(1, textDialog);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void dismissLoading() {
        if (isShowDialog01) {
            dismissDialog(1);
            isShowDialog01 = false;
        }

    }
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
    protected MenuTopView mymenutop;
    protected FrameLayout container_baseact;

    /**
     * 注入AppManager管理类和显式声明变量复制
     */
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        AppManager.onCreate(this);
        mContext = this;
        mFManager = getSupportFragmentManager();
        mBundle = getIntent().getExtras();
        boolean isEnableBarInit = BaseConfig.UIBarTint_Enable;
        if (isEnableBarInit && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtil.setTranslucent(this,255);
            StatusBarUtil.setLightMode(this);
        }
        setContentView(R.layout.main_app_frame_act);
        mymenutop = (MenuTopView) findViewById(R.id.mymenutop);
        container_baseact = (FrameLayout) findViewById(R.id.container_baseact);
    }

    /**
     * 注入AppManager管理类
     */
    @Override
    public void finish() {
        super.finish();
        AppManager.onFinish(this);
    }


    protected void setInitContentView(int layoutResID) {
        container_baseact.removeAllViews();
        container_baseact.addView(LayoutInflater.from(mContext).inflate(layoutResID, null));
        BaseUtil.initInjectAll(this);
    }

    protected void setInitContentView(View initContentView) {
        container_baseact.removeAllViews();
        container_baseact.addView(initContentView);
        BaseUtil.initInjectAll(this);
    }

    protected void hideMenuTopView() {
        mymenutop.setVisibility(View.GONE);
    }

    protected void showMenuTopView() {
        mymenutop.setVisibility(View.VISIBLE);
    }
}
