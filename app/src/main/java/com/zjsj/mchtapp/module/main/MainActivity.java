package com.zjsj.mchtapp.module.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.ruomm.base.ioc.annotation.InjectUIStyle;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.base.view.percentview.LinearLayout_PercentHeight;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;
public class MainActivity extends AppSimpleActivity{
    @InjectAll
    Views views = new Views();
    class Views {
        @InjectView(id=R.id.drawer_layout)
        DrawerLayout drawer_layout;
        @InjectView(id=R.id.main_content_container)
        FrameLayout main_content_container;
        @InjectView(id=R.id.mymenutop)
        MenuTopView mymenutop;
        @InjectView(id=R.id.ly_ylqr)
        LinearLayout_PercentHeight ly_ylqr;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.main_drawerlayout);
        setMainContentView();
        views.drawer_layout.openDrawer(Gravity.START);

    }

    protected void setMainContentView() {
        int displayWidth= DisplayUtil.getDispalyWidth(mContext);
        float height=displayWidth*0.4f;
        float maxHeight=mContext.getResources().getDimension(R.dimen.dpx320);
        if(height>maxHeight)
        {
            height=maxHeight;
        }
        float heightPercent=height/displayWidth;

        float viewFlaot=height/displayWidth;
        views.main_content_container.removeAllViews();
        views.main_content_container.addView(LayoutInflater.from(mContext).inflate(R.layout.main_content_lay, null));
        BaseUtil.initInjectAll(this);
        views.mymenutop.setCenterTextRes(R.string.main_title_name);
        views.mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID==R.id.menutop_left)
                {
                    if(!views.drawer_layout.isDrawerOpen(Gravity.LEFT))
                    {
                        views.drawer_layout.openDrawer(Gravity.LEFT,true);
                    }
                }
            }
        });

        views.ly_ylqr.setHeightPercent(heightPercent);
    }
}
