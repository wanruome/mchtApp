package com.zjsj.mchtapp.config.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ruomm.base.ioc.task.ResumeFormBackGroundTask;
import com.ruomm.baseconfig.debug.MLog;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.dal.store.UserFingerPrint;
import com.zjsj.mchtapp.dal.store.UserGesturesInfo;
import com.zjsj.mchtapp.module.gesturelock.GestureLockActivity;
import com.zjsj.mchtapp.module.lockscreen.LockScreenActivity;

import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;

public class ResumeFormBackGroundTaskImpl implements ResumeFormBackGroundTask{
    @Override
    public void doTaskResumeFormBack(Context mContext) {
        Activity mActivity=(Activity)mContext;
        if(mActivity instanceof LockScreenActivity)
        {
            return;
        }
        UserFingerPrint userFingerPrint=LoginUserFactory.getUserFingerPrint();
        UserGesturesInfo userGesturesInfo=LoginUserFactory.getUserGesturesInfo();
        boolean isGestureEnable=false;
        boolean isFingerEnable=false;
        if(null!=userFingerPrint&&userFingerPrint.isEnable)
        {
            isFingerEnable=true;
        }
        if(null!=userGesturesInfo&&userGesturesInfo.isEnable)
        {
            isGestureEnable=true;
        }
        if(!isGestureEnable&&!isFingerEnable)
        {
            return;
        }

        Intent intent=IntentFactory.getLockScreenActivity();
        if(isGestureEnable){
            ArrayList<Integer> lst=new ArrayList<>();
            lst.addAll(userGesturesInfo.gestures);
            intent.putIntegerArrayListExtra("gestures",lst);
            intent.putExtra("isGestureEnable",true);
        }
        if(isFingerEnable)
        {
            intent.putExtra("isFingerEnable",true);
        }
        mContext.startActivity(intent);
    }
    public Intent getScreenLockForLoginIntent(Context mContext)
    {
        Activity mActivity=(Activity)mContext;
        if(mActivity instanceof LockScreenActivity)
        {
            return null;
        }
        UserFingerPrint userFingerPrint=LoginUserFactory.getUserFingerPrint();
        UserGesturesInfo userGesturesInfo=LoginUserFactory.getUserGesturesInfo();
        boolean isGestureEnable=false;
        boolean isFingerEnable=false;
        if(null!=userFingerPrint&&userFingerPrint.isEnable)
        {
            isFingerEnable=true;
        }
        if(null!=userGesturesInfo&&userGesturesInfo.isEnable)
        {
            isGestureEnable=true;
        }
        if(!isGestureEnable&&!isFingerEnable)
        {
            return null;
        }

        Intent intent=IntentFactory.getLockScreenActivity();
        if(isGestureEnable){
            ArrayList<Integer> lst=new ArrayList<>();
            lst.addAll(userGesturesInfo.gestures);
            intent.putIntegerArrayListExtra("gestures",lst);
            intent.putExtra("isGestureEnable",true);
        }
        if(isFingerEnable)
        {
            intent.putExtra("isFingerEnable",true);
        }
        return intent;
    }
}
