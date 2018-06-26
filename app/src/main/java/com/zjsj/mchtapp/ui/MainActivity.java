package com.zjsj.mchtapp.ui;

import android.os.Bundle;

import com.ruomm.base.ioc.activity.BaseFragmentActivity;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.view.slidingmenulib.SlidingMenu;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.ui.BaseAppActivity;

public class MainActivity extends BaseFragmentActivity{
    @InjectAll
    Views views = new Views();

    class Views {
        @InjectView(id = R.id.slidingmenumain)
        SlidingMenu mySlidingMenu;

    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.slidingmenumain);
        BaseUtil.initInjectAll(this);
        // 设置侧滑菜单的类型
        views.mySlidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        views.mySlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        views.mySlidingMenu.setShadowWidthRes(R.dimen.shadow_width);

        views.mySlidingMenu.setShadowDrawable(R.drawable.slidingmenu_bg);

        // 设置滑动菜单视图的宽度
        views.mySlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        views.mySlidingMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content section of the
         * SlidingMenu, while SLIDING_CONTENT does not.
         */
        // views.mySlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 为侧滑菜单设置布局
        // views.mySlidingMenu.setMenu(R.layout.fragment_menu);

        views.mySlidingMenu.setMenu(R.layout.main_menu_lay);
        views.mySlidingMenu.setContent(R.layout.main_homepage_lay);
        views.mySlidingMenu.getBehindOffset();

    }
}
