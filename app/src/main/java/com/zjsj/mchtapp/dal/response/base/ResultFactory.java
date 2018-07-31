package com.zjsj.mchtapp.dal.response.base;

import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.http.config.ResponseText;
import com.ruomm.base.tools.StringUtils;

import java.util.List;

public class ResultFactory {
    public static String ERR_PRARM = "e401";
    public static String ERR_CORE = "e402";
    public static String ERR_DB = "e403";
    public static String ERR_PWD_WRONG = "e404";
    public static String ERR_PWD_PARSE = "e405";
    public static String ERR_FILE_NOFIND = "e406";
    public static String ERR_NEED_VERIFYCODE = "e409";
    public static String ERR_PARSE_REQUEST = "e410";
    public static String ERR_TOKEN_INVALID = "e411";
    public static String ERR_MSGCODE_INVALID = "e412";
    public static String ERR_CLIENT_TIME = "e413";
    public static String ERR_LOCATION_CHANGE = "e416";
    public static String ERR_REPAYMENT = "e420";
    public static String ERR_UNKNOWN = "e499";
    public static String SUCESS_CODE = "0000";
    public static boolean isTrueResult(Object resultObject,int status)
    {

        if(status!= HttpConfig.Success){
            return false;
        }
        if(null==resultObject||!(resultObject instanceof ResultDto))
        {
            return false;
        }
        ResultDto result=(ResultDto) resultObject;
        if(SUCESS_CODE.equals(result.code))
        {
            return true;
        }
        else{
            return false;
        }
    }
    public static String getErrorTip(Object resultObject,int status){
        if (status==HttpConfig.Success_Filter)
        {
            return "登录已经失效，请重新登录";
        }
        if (status==HttpConfig.Success_ParseError)
        {
            return "请求解析错误";
        }
        if(status!= HttpConfig.Success)
        {
            return "网络连接错误";
        }
        if(null==resultObject||!(resultObject instanceof ResultDto))
        {
                return "请求解析错误";
        }
        ResultDto result=(ResultDto) resultObject;
        if(SUCESS_CODE.equals(result.code))
            {
                return null;
            }
        else{
                String msg=result.msg;
                if(StringUtils.isEmpty(msg))
                {
                    return "请求失败";
                }
                else{
                    return result.msg;
                }
            }
    }

    public static <T> T  getResult(Object resultObject,int status){
        if(status!= HttpConfig.Success)
        {
            return null;
        }
        if(null==resultObject||!(resultObject instanceof ResultDto)){
            return null;
        }
        T t=null;
        try {
            ResultDto result=(ResultDto) resultObject;
           t=(T) result.data;
        }catch (Exception e)
        {
            e.printStackTrace();
            t=null;

        }
        return t;
    }
//    public static <T> List<T>  getResult(Object resultObject, int status){
//        if(status!= HttpConfig.Success)
//        {
//            return null;
//        }
//        if(null==resultObject||!(resultObject instanceof ResultDto)){
//            return null;
//        }
//        ResultDto result=(ResultDto) resultObject;
//        return List<T> result.data;
//    }
    public static boolean isTrueResult(ResponseText responseText)
    {
        if(null==responseText)
        {
            return false;
        }
        if(responseText.status!= HttpConfig.Success){
            return false;
        }
        if(null==responseText.resultObject||!(responseText.resultObject instanceof ResultDto))
        {
            return false;
        }
        ResultDto result=(ResultDto) responseText.resultObject;
        if(SUCESS_CODE.equals(result.code))
        {
            return true;
        }
        else{
            return false;
        }
    }
    public static String getErrorTip(ResponseText responseText){
        if(null==responseText)
        {
            return "网络连接错误";
        }
        if(responseText.status!= HttpConfig.Success){
            return "网络连接错误";
        }
        if(null==responseText.resultObject||!(responseText.resultObject instanceof ResultDto))
        {
            return "请求解析错误";
        }
        ResultDto result=(ResultDto) responseText.resultObject;
        if(SUCESS_CODE.equals(result.code))
        {
            return null;
        }
        else{
            String msg=result.msg;
            if(StringUtils.isEmpty(msg))
            {
                return "请求失败";
            }
            else{
                return result.msg;
            }
        }
    }
    public static <T> T  getResult(ResponseText responseText){
        if(null==responseText)
        {
            return null;
        }
        if(responseText.status!= HttpConfig.Success){
            return null;
        }
        if(null==responseText.resultObject||!(responseText.resultObject instanceof ResultDto))
        {
            return null;
        }
        T t=null;
        try {
            ResultDto result=(ResultDto) responseText.resultObject;
            t=(T) result.data;
        }catch (Exception e)
        {
            e.printStackTrace();
            t=null;

        }
        return t;
    }

}
