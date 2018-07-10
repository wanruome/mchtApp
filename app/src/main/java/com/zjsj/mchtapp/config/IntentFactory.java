package com.zjsj.mchtapp.config;

import android.content.Intent;

public class IntentFactory {
    public static Intent getWelcomeActivityIntent()
    {
        return new Intent("welcome.WelcomeActivity");
    }
    public static Intent getMainActivityIntent()
    {
        return new Intent("main.MainActivity");
    }
    public static Intent getLoinActityIntent()
    {
        return new Intent("userinfo.LoginActivity");
    }

    public static Intent getRegisterActivityIntent()
    {
        return new Intent("userinfo.RegisterActivity");
    }
    public static Intent getFindPwdActivityIntent()
    {
        return new Intent("userinfo.FindPwdActivity");
    }

}
