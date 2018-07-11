package com.zjsj.mchtapp.module.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.ruomm.base.ioc.adapter.PagerAdapter_View;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dottabstripview.DotTabStripListener;
import com.ruomm.base.view.dottabstripview.DotTabStripView;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.base.view.percentview.FrameLayout_PercentHeight;
import com.ruomm.base.view.percentview.LinearLayout_PercentHeight;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.squareup.picasso.Picasso;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.module.main.adapter.MainFunctionAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
        @InjectView(id=R.id.img_scan)
        ImageView img_scan;
        @InjectView(id=R.id.img_paymentcode)
        ImageView img_paymentcode;
        @InjectView(id=R.id.ly_homead)
        FrameLayout_PercentHeight ly_homead;
        @InjectView(id=R.id.mViewPager)
        ViewPager mViewPager;
        @InjectView(id=R.id.dotTabStripView)
        DotTabStripView dotTabStripView;
        @InjectView(id=R.id.mGridView)
        GridView mGridView;
        @InjectView(id=R.id.ly_login)
        View ly_login;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        setInitContentView(R.layout.main_drawerlayout);
        setMainContentView();
        views.drawer_layout.openDrawer(Gravity.START);


    }

    protected void setMainContentView() {
        int displayWidth= DisplayUtil.getDispalyWidth(mContext);
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
        float height=displayWidth*0.4f;
        float maxHeight=mContext.getResources().getDimension(R.dimen.dpx320);
        if(height>maxHeight)
        {
            height=maxHeight;
        }
        float heightPercent=height/displayWidth;
        views.ly_ylqr.setHeightPercent(heightPercent);
        int imageHeight=(int)(height*0.9f);
        Picasso.with(mContext).load(R.mipmap.scan).resize(imageHeight,imageHeight).into(views.img_scan);
        Picasso.with(mContext).load(R.mipmap.paymentcode).resize(imageHeight,imageHeight).into(views.img_paymentcode);
        views.ly_homead.setHeightPercent(0.45f);
        int[] hotmeAd=new int[]{R.mipmap.home_ad01,R.mipmap.home_ad02};
        List<View> lstViewPagerViews=new ArrayList<View>() ;
        for (int i=0;i<hotmeAd.length;i++) {
           LayoutInflater layoutInflater=LayoutInflater.from(this);
            View view= layoutInflater.inflate(R.layout.main_viewpager_ad,null);
            ImageView vp_img=view.findViewById(R.id.vp_img);
            // Picasso.with(mContext).load(hotmeAd[i]).transform(new RoundedTransformationBuilder().cornerRadius(10).build()).into(vp_img);
            vp_img.setImageResource(hotmeAd[i]);
            lstViewPagerViews.add(view);
        }
        PagerAdapter_View pagerAdapter_view=new PagerAdapter_View(lstViewPagerViews);
        views.mViewPager.setAdapter(pagerAdapter_view);
        views.dotTabStripView.setDotCount(hotmeAd.length,0);
        views.dotTabStripView.setListener(new DotTabStripListener() {
            @Override
            public void onDotViewSelect(int currentItem) {
                views.mViewPager.setCurrentItem(currentItem);
            }
        });
        views.dotTabStripView.setCycleTask(3000);
        views.mGridView.setAdapter(new MainFunctionAdapter(mContext));
        updateUiByUserInfo();
        //设置监听
        views.ly_login.setOnClickListener(myOnClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEventMainThrend(LoginEvent event){
        updateUiByUserInfo();
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.ly_login)
            {
                parseIsLogin();
            }
            else if(vID==R.id.img_scan)
            {
                ToastUtil.makeOkToastThr(mContext,"此功能还没开放");
            }
            else if(vID==R.id.img_paymentcode)
            {
                if(!LoginUserFactory.isLogin())
                {

                }
            }
        }
    };
    private void updateUiByUserInfo()
    {
        if(LoginUserFactory.isLogin())
        {
            views.ly_login.setVisibility(View.GONE);
        }
        else{
            views.ly_login.setVisibility(View.VISIBLE);

        }
    }
    private boolean parseIsLogin(){
        if(LoginUserFactory.isLogin())
        {
            return true;
        }
        else{
            startActivity(IntentFactory.getLoinActivity());
            return false;
        }
    }

}
