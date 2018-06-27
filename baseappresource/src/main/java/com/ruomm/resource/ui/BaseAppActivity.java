package com.ruomm.resource.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ruomm.base.ioc.activity.BaseFragmentActivity;
import com.ruomm.base.ioc.annotation.InjectUIStyle;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.resource.R;

/**
 * Created by Niu on 2017/8/2.
 */
@InjectUIStyle
public abstract  class BaseAppActivity extends BaseFragmentActivity {
    protected MenuTopView mymenutop;
    protected FrameLayout container_baseact;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.main_app_frame_act);
        mymenutop = (MenuTopView) findViewById(R.id.mymenutop);
        container_baseact = (FrameLayout) findViewById(R.id.container_baseact);
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
