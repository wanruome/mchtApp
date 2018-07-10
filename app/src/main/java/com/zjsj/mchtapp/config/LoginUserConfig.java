package com.zjsj.mchtapp.config;

import com.zjsj.mchtapp.dal.store.LoginUserInfo;

public class LoginUserConfig {

    private static LoginUserInfo mLoginUserInfo=null;
    public static LoginUserInfo getLoginUserInfo(){
        if(null==mLoginUserInfo)
        {
            return new LoginUserInfo();
        }
        else{
            return mLoginUserInfo;
        }
    }
    public  static void doLogin(LoginUserInfo loginUserInfo){
        mLoginUserInfo=loginUserInfo;
    }
    public static void doLogout()
    {
        mLoginUserInfo=null;
    }

}
