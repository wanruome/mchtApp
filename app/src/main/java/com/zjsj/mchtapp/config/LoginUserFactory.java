package com.zjsj.mchtapp.config;

import android.view.View;

import com.ruomm.base.ioc.adapter.BaseAdapter_T;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.DbStoreSafe;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.resource.ui.dal.ScreenSercureConfig;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.dal.response.PayInfoDto;
import com.zjsj.mchtapp.dal.response.RepaymentBankCard;
import com.zjsj.mchtapp.dal.response.UserInfoDto;
import com.zjsj.mchtapp.dal.store.AppScreenSecure;
import com.zjsj.mchtapp.dal.store.LastLoginUserInfo;
import com.zjsj.mchtapp.dal.store.UserBankCardForQrCode;
import com.zjsj.mchtapp.dal.store.UserBankCardStore;
import com.zjsj.mchtapp.dal.store.UserFingerPrint;
import com.zjsj.mchtapp.dal.store.UserGesturesInfo;
import com.zjsj.mchtapp.dal.store.UserPayInfoStore;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginUserFactory {
    private static final SimpleDateFormat TOKEN_FORAMT=new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static UserInfoDto mLoginUserInfo=null;
    private static PayInfoDto mPayInfoDto=null;
//    private static AppScreenSecure mAppScreenSecure=null;
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
    public static void saveLoginForModify()
    {
        if(isLogin()){
            AppStoreUtil.safeSaveBean(BaseApplication.getApplication(), null, mLoginUserInfo);
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
        savePayInfoDto(null);
        //发送EventBus事件
        LoginEvent loginEvent=new LoginEvent();
        loginEvent.loninStatus=false;
        EventBus.getDefault().post(loginEvent);
    }
    public static void savePayInfoDto(PayInfoDto payInfoDto)
    {
        BaseApplication app=BaseApplication.getApplication();
        if(null==payInfoDto)
        {
            AppStoreUtil.safeDelBean(app,null, UserPayInfoStore.class);
        }
        UserPayInfoStore userPayInfoStore=new UserPayInfoStore();
        userPayInfoStore.userId=getLoginUserInfo().userId;
        userPayInfoStore.payInfoDto=payInfoDto;
        AppStoreUtil.safeSaveBean(BaseApplication.getApplication(),null,userPayInfoStore);
        mPayInfoDto=payInfoDto;
    }
    public static PayInfoDto getPayInfo(){
        if(null==mPayInfoDto)
        {
            UserPayInfoStore userPayInfoStore=AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null,UserPayInfoStore.class);
            if(null!=userPayInfoStore&&null!=userPayInfoStore.userId&&userPayInfoStore.userId.equals(getLoginUserInfo().userId))
            {
                mPayInfoDto=userPayInfoStore.payInfoDto;
            }
        }
        return mPayInfoDto;
    }
    public static List<RepaymentBankCard> getBankCardInfo(){
        UserBankCardStore userBankCardStore=AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null,UserBankCardStore.class);
        if(null==userBankCardStore||!userBankCardStore.userId.equals(getLoginUserInfo().userId))
        {
            return null;
        }
        else{
            return userBankCardStore.list;
        }
    }
    public static void saveBankCardInfo(List<RepaymentBankCard> list){
        UserBankCardStore userBankCardStore=new UserBankCardStore();
        userBankCardStore.userId= LoginUserFactory.getLoginUserInfo().userId;
        if (null == list || list.size() <= 0) {
            userBankCardStore.list=null;
        }
        else{

            userBankCardStore.list=new ArrayList<RepaymentBankCard>();
            userBankCardStore.list.addAll(list);
        }
        AppStoreUtil.safeSaveBean(BaseApplication.getApplication(),null,userBankCardStore);
    }
    public static RepaymentBankCard getBankCardForQrCode( List<RepaymentBankCard> listBankCards){
        UserBankCardForQrCode userBankCardForQrCode=AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null,UserBankCardForQrCode.class);
        if(null==listBankCards||listBankCards.size()<=0)
        {
            return null;
        }
        if(null==userBankCardForQrCode||!userBankCardForQrCode.userId.equals(getLoginUserInfo().userId)){
            RepaymentBankCard repaymentBankCard=listBankCards.get(0);
            saveBankCardForQrCode(repaymentBankCard);
            return repaymentBankCard;
        }
        else{
            RepaymentBankCard repaymentBankCard=null;
            for(RepaymentBankCard tmp:listBankCards)
            {
                if(tmp.cardIndex.equals(userBankCardForQrCode.cardIndex))
                {
                    repaymentBankCard=tmp;
                    break;
                }
            }
            if(null==repaymentBankCard)
            {
                repaymentBankCard=listBankCards.get(0);
                saveBankCardForQrCode(repaymentBankCard);
            }
            return repaymentBankCard;
        }
    }
    public static RepaymentBankCard getBankCardForQrCode(){
       return getBankCardForQrCode(getBankCardInfo());
    }
    public static void saveBankCardForQrCode(RepaymentBankCard repaymentBankCard)
    {
        if(null==repaymentBankCard)
        {
            return;
        }
        UserBankCardForQrCode userBankCardForQrCode=new UserBankCardForQrCode();
        userBankCardForQrCode.userId=getLoginUserInfo().userId;
        userBankCardForQrCode.cardIndex=repaymentBankCard.cardIndex;
        AppStoreUtil.safeSaveBean(BaseApplication.getApplication(),null,userBankCardForQrCode);
    }
    public static boolean isAppScreenSecure()
    {
        boolean isAppScreenSecure=getAppScreenSecure().isScreenSecrue;
        ScreenSercureConfig.isAppScreenSecure=isAppScreenSecure;
        return isAppScreenSecure;
    }

    private static AppScreenSecure getAppScreenSecure() {
            AppScreenSecure  mAppScreenSecure=AppStoreUtil.safeGetBean(BaseApplication.getApplication(),null,AppScreenSecure.class);
            if(null==mAppScreenSecure)
            {
                mAppScreenSecure=new AppScreenSecure();
                mAppScreenSecure.isScreenSecrue=false;
            }
            return mAppScreenSecure;

    }
    public static void saveAppScreenSecure(AppScreenSecure appScreenSecure)
    {
        if(null==appScreenSecure)
        {
            return;
        }
        AppStoreUtil.safeSaveBean(BaseApplication.getApplication(),null,appScreenSecure);
        ScreenSercureConfig.isAppScreenSecure=appScreenSecure.isScreenSecrue;
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
//    public static void delUserGesturesInfo(){
//        AppStoreUtil.delBean(BaseApplication.getApplication(),null,UserGesturesInfo.class);
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
        if (null!=gestures&&gestures.size()>0)
        {
            userGesturesInfo.isEnable=true;
        }
        else {
            userGesturesInfo.isEnable=false;
        }
        return DbStoreSafe.getInstance().saveBean(null,getLoginUserInfo().userId,userGesturesInfo);
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
