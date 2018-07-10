package com.zjsj.mchtapp.core;

import com.ruomm.base.tools.StringUtils;
import com.zjsj.mchtapp.config.http.ApiConfig;

public class PassWordService {
    public static boolean isPwdRuleOk(String pwd)
    {
        return isPwdRuleOK(pwd, ApiConfig.PWD_MIN_LENGTH,ApiConfig.PWD_MAT_LENGTH,ApiConfig.PWD_MIN_RULE,true);
    }
    public static String parsePwdRuleToString(){
        return parsePwdRuleToString(ApiConfig.PWD_MIN_LENGTH,ApiConfig.PWD_MAT_LENGTH,ApiConfig.PWD_MIN_RULE);
    }
    public static boolean isPayPwdRuleOk(String pwd)
    {
        if(StringUtils.getLength(pwd)<ApiConfig.PAYPWD_MIN_LENGTH)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public static String parsePayPwdRuleToString(){
        return "支付密码至少"+ApiConfig.PAYPWD_MIN_LENGTH+"位";
    }

    private static boolean isPwdRuleOK(String pwd, int minLength, int maxLength, int minRule, boolean isRuleCheck) {
        if (!isRuleCheck) {
            return true;
        }
        if (StringUtils.isBlank(pwd)) {
            return false;
        }
        if (pwd.length() > maxLength) {
            return false;
        }
        if (StringUtils.getLength(pwd) < minLength) {
            return false;
        }

        boolean isBig = false;
        boolean isNumber = false;
        boolean isSmall = false;
        boolean isOther = false;
        if (minRule <= 0) {
            return true;
        }
        for (int i = 0; i < pwd.length(); i++) {
            char tmp = pwd.charAt(i);
            if (tmp >= 'a' && tmp <= 'z') {
                isSmall = true;
            }
            else if (tmp >= 'A' && tmp <= 'Z') {
                isBig = true;
            }
            else if (tmp >= '0' && tmp <= '9') {
                isNumber = true;
            }
            else {
                isOther = true;
            }
        }
        int i = 0;
        if (isBig) {
            i++;
        }
        if (isSmall) {
            i++;
        }
        if (isOther) {
            i++;
        }
        if (isNumber) {
            i++;
        }
        if (minRule == 1) {
            if (i == 1 && isNumber) {
                return false;
            }
            else {
                return true;
            }
        }
        else if (minRule >= 4) {
            if (i >= 4) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (i >= minRule) {
                return true;
            }
            else {
                return false;
            }
        }

    }

    private static String parsePwdRuleToString(int minLength, int maxLength, int ruleNumber) {

        StringBuilder sb = new StringBuilder();
        if (minLength > 0 && maxLength > 0) {
            sb.append("密码需要" + minLength + "-" + maxLength + "位");
            if (ruleNumber > 0) {
                sb.append("且");
            }
        }
        else if (minLength > 0) {
            sb.append("密码至少" + minLength + "位");
            if (ruleNumber > 0) {
                sb.append("且");
            }
        }
        else {
            sb.append("密码");
        }

        if (ruleNumber == 1) {
            sb.append("不能为纯数字");
        }
        else if (ruleNumber >= 2) {
            int tmp = ruleNumber > 4 ? 4 : ruleNumber;
            sb.append("需要大写、小写、数字、特殊符号" + tmp + "种组合");
        }
        return sb.toString();
    }
}
