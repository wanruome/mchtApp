package com.zjsj.mchtapp.config;

import android.content.Intent;

public class IntentFactory {
    public static final int Request_FingerPrintActivity=109;
    public static final int Request_GestureLockActivity=110;
    public static final int Request_ScreenLockActivity=111;
    public static Intent getCommonWebInfoActivity()
    {
        return new Intent("webmodule.CommonWebInfoActivity");
    }
    public static Intent getWelcomeActivity()
    {
        return new Intent("welcome.WelcomeActivity");
    }
    public static Intent getMainActivity()
    {
        return new Intent("main.MainActivity");
    }
    public static Intent getLoinActivity()
    {
        return new Intent("userinfo.LoginActivity");
    }

    public static Intent getRegisterActivity()
    {
        return new Intent("userinfo.RegisterActivity");
    }
    public static Intent getFindPwdActivity()
    {
        return new Intent("userinfo.FindPwdActivity");
    }
    public static Intent getSettingActivity()
    {
        return new Intent("settting.SettingActivity");
    }
    public static Intent getPayInfoSetPwdActivity()
    {
        return new Intent("payinfo.PayInfoSetPwdActivity");
    }
    public static Intent getPayInfoModifyPwdActivity()
    {
        return new Intent("payinfo.PayInfoModifyPwdActivity");
    }
    public static Intent getPayInfoFindPwdActivity()
    {
        return new Intent("payinfo.PayInfoFindPwdActivity");
    }
    public static Intent getGestureLockActivity()
    {
        return new Intent("gesturelock.GestureLockActivity");
    }
    public static Intent getFingerPrintActivity()
    {
        return new Intent("fingerprint.FingerPrintActivity");
    }
    public static Intent getLockScreenActivity(){
        return new Intent("lockscreen.LockScreenActivity");
    }
    public static Intent getBankCardListActivity(){
        return  new Intent("repayment.BankCardListActivity");
    }
    public static Intent getBindCardActivity(){
        return  new Intent("repayment.BindCardActivity");
    }
    public static Intent getRepaymentOrderActivity(){
        return  new Intent("repayment.RepaymentOrderActivity");
    }
    public static Intent getRepaymentOrderDetailActivity(){
        return  new Intent("repayment.RepaymentOrderDetailActivity");
    }








}
