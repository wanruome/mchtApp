package com.zjsj.mchtapp.config;

import android.content.Intent;

public class IntentFactory {
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

}
