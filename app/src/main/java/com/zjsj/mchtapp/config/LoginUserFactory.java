package com.zjsj.mchtapp.config;

import com.zjsj.mchtapp.dal.response.UserInfoDto;

public class LoginUserFactory {

    private static UserInfoDto mLoginUserInfo=null;
    public static UserInfoDto getLoginUserInfo(){
        if(null==mLoginUserInfo)
        {
            return new UserInfoDto();
        }
        else{
            return mLoginUserInfo;
        }
    }
    public  static void doLogin(UserInfoDto loginUserInfo){
        mLoginUserInfo=loginUserInfo;
    }
    public static void doLogout()
    {
        mLoginUserInfo=null;
    }

}
