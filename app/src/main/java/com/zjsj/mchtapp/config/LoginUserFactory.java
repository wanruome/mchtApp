package com.zjsj.mchtapp.config;

import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.DbStoreSafe;
import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.dal.response.PayInfoDto;
import com.zjsj.mchtapp.dal.response.UserInfoDto;
import com.zjsj.mchtapp.dal.store.LastLoginUserInfo;
import com.zjsj.mchtapp.dal.store.UserFingerPrint;
import com.zjsj.mchtapp.dal.store.UserGesturesInfo;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LoginUserFactory {
    private static final SimpleDateFormat TOKEN_FORAMT=new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static UserInfoDto mLoginUserInfo=null;
    private static PayInfoDto mPayInfoDto=null;
    public static boolean isLogin()
    {
        return null==mLoginUserInfo?false:true;
    }

    public static UserInfoDto getLoginUserInfo(){
        if(null==mLoginUserInfo)
        {
            return new UserInfoDto();
        }
        else{
            return mLoginUserInfo;
        }
    }
    public static void doLoginByStore(){
        UserInfoDto userInfoDto= AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null, UserInfoDto.class);
        boolean isTokeValid=isTokenValid(userInfoDto);
        if(isTokeValid)
        {
            doLogin(userInfoDto);
        }
        else{
            if(null!=userInfoDto) {
                AppStoreUtil.safeDelBean(BaseApplication.getApplication(), null, UserInfoDto.class);
            }
        }
    }
    public  static void doLogin(UserInfoDto loginUserInfo){
        mLoginUserInfo=loginUserInfo;
        if(null!=mLoginUserInfo)
        {
            BaseApplication app=BaseApplication.getApplication();
            LastLoginUserInfo lastLoginUserInfo=new LastLoginUserInfo();
            lastLoginUserInfo.account=loginUserInfo.mobile;
            lastLoginUserInfo.accountType="1";
            AppStoreUtil.safeSaveBean(app,null,lastLoginUserInfo);
            AppStoreUtil.safeSaveBean(app, null, loginUserInfo);
            //发送EventBus事件
            LoginEvent loginEvent=new LoginEvent();
            loginEvent.loninStatus=true;
            EventBus.getDefault().post(loginEvent);
        }
    }
    public static void doLogout()
    {
        mLoginUserInfo=null;
        mPayInfoDto=null;
        AppStoreUtil.safeDelBean(BaseApplication.getApplication(), null, UserInfoDto.class);
        //发送EventBus事件
        LoginEvent loginEvent=new LoginEvent();
        loginEvent.loninStatus=false;
        EventBus.getDefault().post(loginEvent);
    }
    public static void setPayInfoDto(PayInfoDto payInfoDto)
    {
        mPayInfoDto=payInfoDto;
    }
    public static PayInfoDto getPayInfo(){
        return mPayInfoDto;
    }

//    public static UserGesturesInfo getUserGesturesInfo(){
//        if(!isLogin())
//        {
//            return null;
//        }
//        UserGesturesInfo userGesturesInfo= AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null,UserGesturesInfo.class);
//        if(null==userGesturesInfo)
//        {
//            return null;
//        }
//        if(getLoginUserInfo().userId.equals(userGesturesInfo.userId))
//        {
//            return userGesturesInfo;
//        }
//        else{
//            AppStoreUtil.delBean(BaseApplication.getApplication(),null,UserGesturesInfo.class);
//            return null;
//        }
//    }
//    public static void setUserGesturesInfo(List<Integer> gestures)
//    {
//        if(null==gestures||gestures.size()<=0)
//        {
//            return;
//        }
//        if(!isLogin())
//        {
//            return;
//        }
//        UserGesturesInfo userGesturesInfo=new UserGesturesInfo();
//        userGesturesInfo.userId=getLoginUserInfo().userId;
//        userGesturesInfo.gestures=gestures;
//        AppStoreUtil.safeSaveBean(BaseApplication.getApplication(),null,userGesturesInfo);
//    }

    public static UserGesturesInfo getUserGesturesInfo(){
        if(!isLogin())
        {
            return null;
        }
        return DbStoreSafe.getInstance().getBean(null,getLoginUserInfo().userId,UserGesturesInfo.class);
    }
    public static boolean saveUserGesturesInfo(List<Integer> gestures)
    {
        if(!isLogin())
        {
            return false;
        }
        UserGesturesInfo userGesturesInfo=new UserGesturesInfo();
        userGesturesInfo.userId=getLoginUserInfo().userId;
        userGesturesInfo.gestures=gestures;
        if (null!=gestures&gestures.size()>0)
        {
            userGesturesInfo.isEnable=true;
        }
        else {
            userGesturesInfo.isEnable=false;
        }
        return DbStoreSafe.getInstance().saveBean(null,getLoginUserInfo().userId,userGesturesInfo);
    }
    public static void delUserGesturesInfo(){
        AppStoreUtil.delBean(BaseApplication.getApplication(),null,UserGesturesInfo.class);
    }
    public static UserFingerPrint getUserFingerPrint(){
        if(!isLogin())
        {
            return null;
        }
        return DbStoreSafe.getInstance().getBean(null,getLoginUserInfo().userId,UserFingerPrint.class);
    }

    public static boolean saveUserFingerPrint(boolean isFingerPrintEnable)
    {
        if(!isLogin())
        {
            return false;
        }
        UserFingerPrint userFingerPrint=new UserFingerPrint();
        userFingerPrint.isEnable =isFingerPrintEnable;
        userFingerPrint.userId=getLoginUserInfo().userId;
        return DbStoreSafe.getInstance().saveBean(null,getLoginUserInfo().userId,userFingerPrint);
    }

    public static boolean isTokenValid(UserInfoDto userInfoDto)
    {
            if(null==userInfoDto|| StringUtils.isEmpty(userInfoDto.validTime))
            {
                return false;
            }
            boolean flag=false;
            try{
                Date dateNow=new Date();
                Date dateToken=TOKEN_FORAMT.parse(userInfoDto.validTime);
                long timeRemind=dateToken.getTime()-dateNow.getTime();
                if(timeRemind<1000*3600l)
                {
                   flag= false;
                }
                else{
                    flag= true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                flag= false;

            }
            return flag;
    }

}
