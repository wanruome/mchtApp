package com.zjsj.mchtapp.module.main;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.view.slidingmenulib.SlidingMenu;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;

public class MainActivitySlidingMenu extends AppSimpleActivity{
    @InjectAll
    Views views = new Views();

    class Views {
        @InjectView(id = R.id.slidingmenumain)
        SlidingMenu mySlidingMenu;
        @InjectView(id = R.id.main_content_container)
        FrameLayout main_content_container;
        @InjectView(id = R.id.main_menu_container)
        FrameLayout main_menu_container;

    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.main_slidingmenu);
        BaseUtil.initInjectAll(this);
        // 设置侧滑菜单的类型
        views.mySlidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        views.mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        views.mySlidingMenu.setShadowWidthRes(R.dimen.shadow_width);

//        views.mySlidingMenu.setShadowDrawable(R.drawable.slidingmenu_bg);

        // 设置滑动菜单视图的宽度
        views.mySlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        views.mySlidingMenu.setFadeDegree(0.35f);
        // views.mySlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        views.mySlidingMenu.setContent(R.layout.main_content_lay);
        views.mySlidingMenu.setMenu(R.layout.main_menu_container);
        views.mySlidingMenu.getBehindOffset();
        mFManager.beginTransaction().add(R.id.main_menu_container,new MainMenuFragment()).commit();
    }
}
