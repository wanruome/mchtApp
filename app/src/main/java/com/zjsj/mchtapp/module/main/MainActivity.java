package com.zjsj.mchtapp.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.adapter.PagerAdapter_View;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.service.downloadservice.DownLoadEvent;
import com.ruomm.base.service.downloadservice.DownLoadTaskUtil;
import com.ruomm.base.service.downloadservice.DownLoadValue;
import com.ruomm.base.tools.DisplayUtil;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.PackageUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.TimeUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.base.view.dottabstripview.DotTabStripListener;
import com.ruomm.base.view.dottabstripview.DotTabStripView;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.base.view.percentview.FrameLayout_PercentHeight;
import com.ruomm.base.view.percentview.LinearLayout_PercentHeight;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.UpdateAppProgressDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.squareup.picasso.Picasso;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.ResumeFormBackGroundTaskImpl;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.dal.event.TokenEvent;
import com.zjsj.mchtapp.dal.response.AppVersion;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.AppUpdateIgnore;
import com.zjsj.mchtapp.module.main.adapter.MainFunctionAdapter;
import com.zjsj.mchtapp.module.main.dal.MainFunctionItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        setMainClickListener();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownLoadEvent.UpdateAppVersion_Action);
        mContext.registerReceiver(updateAppReceiver, intentFilter);
        doScreenLockForLogin();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(updateAppReceiver);
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEventMainThrend(LoginEvent event){
        updateUiByUserInfo();
    }
    @Subscribe
    public void onEventMainThrend(TokenEvent event){
        if(event.isInValid)
        {
            updateUiByUserInfo();
            startActivity(IntentFactory.getLoinActivity());
        }
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

    }
    protected void setMainClickListener() {
        views.ly_login.setOnClickListener(myOnClickListener);
        views.img_scan.setOnClickListener(myOnClickListener);
        views.img_paymentcode.setOnClickListener(myOnClickListener);
        views.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                views.mGridView.get
                if(!isAppLogin(true))
                {
                    return;
                }
                MainFunctionItem function=(MainFunctionItem)views.mGridView.getAdapter().getItem(position);
                if(null!=function&&!StringUtils.isEmpty(function.actionName))
                {
                    Intent intent=new Intent(function.actionName);
                    startActivity(intent);
                }
            }
        });

    }


    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.ly_login)
            {
                isAppLogin(true);
            }
            else if(vID==R.id.img_scan)
            {
                ToastUtil.makeOkToastThr(mContext,"此功能还没开放");
            }
            else if(vID==R.id.img_paymentcode)
            {
                if(isAppLogin(true))
                {
                    startActivity(IntentFactory.getRepaymentQrCodeActivity());
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
    private void doScreenLockForLogin(){
        Intent intent=new ResumeFormBackGroundTaskImpl().getScreenLockForLoginIntent(mContext);
        if(null!=intent)
        {
            startActivityForResult(intent,IntentFactory.Request_ScreenLockActivity);
        }
        else {
            doHttpTaskGetAppVersion();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IntentFactory.Request_ScreenLockActivity){
            doHttpTaskGetAppVersion();
        }
    }

    private boolean isAppLogin(boolean isShowDialog){
        if(LoginUserFactory.isLogin())
        {
            return true;
        }
        else{
            if(!isShowDialog){
                startActivity(IntentFactory.getLoinActivity());
            }
            else{
                MessageDialog messageDialog=new MessageDialog(mContext);
                messageDialog.setDialogValue(new DialogValue("登录","是否登录应用"));
                messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        if(v.getId()==R.id.dialog_confirm)
                        {
                            startActivity(IntentFactory.getLoinActivity());
                        }
                    }
                });
                messageDialog.show();
            }
            return false;
        }
    }
    //更新服务
    private void doHttpTaskGetAppVersion(){
        Map<String,String> map= ApiConfig.createRequestMap(false);
        map.put("appName","商户服务APP");
        map.put("appType","1");
        map.put("appVersion", TelePhoneUtil.getPackageVersionName(mContext));
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"/app/appVersion/doGetAppVersion").setRequestBodyText(map).doHttp(AppVersion.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                AppVersion appVersion= ResultFactory.getResult(resultObject,status);
                if(null!=appVersion&&"1".equals(appVersion.needUpdate)&& !StringUtils.isEmpty(appVersion.downUrl))
                {

//                        appVersion.downUrl="http://codown.youdao.com/note/youdaonote_android_6.2.3_youdaoweb.apk";
                        mAppVersion=appVersion;
                        if(isNeedUpdate()) {
                            MessageDialog messageDialog = new MessageDialog(mContext);
                            messageDialog.setDialogValue(new DialogValue("升级提醒", "你的应用需要升级？", "稍后提醒", "马上升级"));
                            messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                                @Override
                                public void onDialogItemClick(View v, Object tag) {
                                    if (v.getId() == R.id.dialog_confirm) {
                                        doUpdateApp();
                                    } else {
                                        AppUpdateIgnore appUpdateIgnore = new AppUpdateIgnore();
                                        appUpdateIgnore.version = mAppVersion.appVersion;
                                        appUpdateIgnore.ignoreTime = System.currentTimeMillis();
                                        AppStoreUtil.safeSaveBean(mContext, null, appUpdateIgnore);
                                    }
                                }
                            });
                            messageDialog.show();
                        }
                }
            }
        });
    }
    private boolean isNeedUpdate(){

        if(null!=mAppVersion&&"1".equals(mAppVersion.needUpdate)&& !StringUtils.isEmpty(mAppVersion.downUrl)&&!StringUtils.isEmpty(mAppVersion.appVersion)){
            AppUpdateIgnore appUpdateIgnore=AppStoreUtil.safeGetBean(mContext,null,AppUpdateIgnore.class);

            if(null==appUpdateIgnore||appUpdateIgnore.ignoreTime<=0){
                return true;
            }
            if(!mAppVersion.appVersion.equals(appUpdateIgnore.version)){
                return true;
            }
            long time=Math.abs(System.currentTimeMillis()-appUpdateIgnore.ignoreTime);
            if(time> ApiConfig.APPUPDATE_NOTIFY_SKIP_TIME)
            {
                return true;
            }
            else{
                return false;
            }
        }
        else {
            return false;
        }
    }

    private AppVersion mAppVersion=null;
    private void doUpdateApp() {
        String updateApkPath = FileUtils.getPathExternalCache(mContext, "download"+File.separator+"mactApp2_" + mAppVersion.appVersion
                + ".apk");
        File file = new File(updateApkPath);
        if (null != file && file.exists() && file.isFile()) {
            try {
                PackageUtils.installNormalNew(mContext, updateApkPath,"com.zjsj.mchtapp.fileprovider");
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        else {
            DownLoadValue mDownLoadValue = new DownLoadValue(mAppVersion.downUrl, updateApkPath);
            mDownLoadValue.isSendProgressEvent = true;
            mDownLoadValue.isSendDoneEvent = true;
            mDownLoadValue.tagProgress = DownLoadEvent.UpdateAppVersion_Action;
            mDownLoadValue.tagEnd = DownLoadEvent.UpdateAppVersion_Action;
            boolean flag = DownLoadTaskUtil.addDownLoadTaskCurrentQueue(mContext, mDownLoadValue);
        }
    }


    private UpdateAppProgressDialog progress_Dialog=null;
    private DownLoadValue appDownloadValue;
    private final BroadcastReceiver updateAppReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName = intent.getAction();
            if (DownLoadEvent.UpdateAppVersion_Action.equals(actionName)) {
                if (null == mAppVersion) {
                    if (null != progress_Dialog) {
                        progress_Dialog.dismiss();
                        progress_Dialog = null;
                    }
                    finish();
                    return;
                }
                DownLoadEvent downLoadEvent = BaseUtil.serializableGet(intent, DownLoadEvent.class);
                if (null == appDownloadValue) {
                    appDownloadValue = downLoadEvent.mDownLoadValue;
                }
                setCheckUpdateStatus(downLoadEvent);

                if (null != downLoadEvent && downLoadEvent.downloadStatus == DownLoadEvent.DownLoadStatus_Sucess) {
                    try {
                        String apkPath = downLoadEvent.mDownLoadValue.fileSucess;
                        PackageUtils.installNormalNew(mContext, apkPath,"com.zjsj.mchtapp.fileprovider");
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    };

    @SuppressWarnings("unused")
    protected void setCheckUpdateStatus(DownLoadEvent downLoadEvent) {
        if (null == downLoadEvent) {
            if (null != progress_Dialog) {
                progress_Dialog.dismiss();
                progress_Dialog = null;
            }
        }
        else {
            if (downLoadEvent.downloadStatus == DownLoadEvent.DownLoadStatus_OnDownLoading) {
                NumberFormat nt = NumberFormat.getPercentInstance();
                nt.setMinimumFractionDigits(2);
                String perString = nt.format(downLoadEvent.valueProgress);
                if (null == progress_Dialog) {
                    progress_Dialog = new UpdateAppProgressDialog(mContext, new BaseDialogClickListener() {

                        @Override
                        public void onDialogItemClick(View v, Object tag) {
                            int vID = v.getId();
                            if (vID == R.id.dialog_cancle) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                                return;
                            }
                            else if (vID == R.id.dialog_confirm) {
                                DownLoadTaskUtil.removeDownLoadTask(mContext, appDownloadValue);
                                progress_Dialog.dismiss();
                                progress_Dialog = null;

                            }

                        }
                    });

                    progress_Dialog.show();
                }
                else {
                    progress_Dialog.setUpdateProgress(downLoadEvent.valueProgress);
                }
            }
            else if (downLoadEvent.downloadStatus == DownLoadEvent.DownLoadStatus_Sucess) {

                if (null != progress_Dialog) {
                    progress_Dialog.dismiss();
                    progress_Dialog = null;
                }
            }
            else if (downLoadEvent.downloadStatus == DownLoadEvent.DownLoadStatus_Fail) {
                if (null != progress_Dialog) {
                    progress_Dialog.dismiss();
                    progress_Dialog = null;
                }
            }
        }
    }

}
