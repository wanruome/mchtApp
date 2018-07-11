package com.zjsj.mchtapp.module.userinfo;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.extend.Thread_CanStop;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.base.tools.regextool.RegexUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.KeyboardSafeImpl;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.core.PassWordService;
import com.zjsj.mchtapp.dal.response.MsgSendDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.LastLoginUserInfo;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import java.util.Map;

public class FindPwdActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id= R.id.edt_name)
        EditText edt_name;
        @InjectView(id=R.id.edt_pwd)
        EditText edt_pwd;
        @InjectView(id=R.id.edt_pwd_repeat)
        EditText edt_pwd_repeat;
        @InjectView(id=R.id.ly_verifyCode)
        LinearLayout ly_verifyCode;
        @InjectView(id=R.id.edt_verifyCode)
        EditText edt_verifyCode;
        @InjectView(id=R.id.btn_verifyCode)
        Button btn_verifyCode;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
    }
    private KeyboardUtil keyboardUtil;
    private KeyboardUtil keyboardUtilRepeat;
    private VerifyCodeThread verifyCodeThread=null;
    private LastLoginUserInfo lastLoginUserInfo=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mymenutop.setMenuClickListener(myMenutopListener);
        mymenutop.setCenterText("找回密码");
        setInitContentView(R.layout.userinfo_findpwd_act);
        updateUiByLastLoginUserInfo();
        keyboardUtil=new KeyboardUtil(this,views.edt_pwd).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
        keyboardUtilRepeat=new KeyboardUtil(this,views.edt_pwd_repeat).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
        views.btn_submit.setOnClickListener(myOnClickListener);
        views.btn_verifyCode.setOnClickListener(myOnClickListener);
    }
    private MenuTopListener myMenutopListener=new MenuTopListener() {
        @Override
        public void onMenuTopClick(View v, int vID) {
            if(vID==R.id.menutop_left)
            {
                finish();
            }
        }
    };
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.btn_submit){
                doHttpTask();
            }
            else if(vID==R.id.btn_verifyCode)
            {
                if(null==verifyCodeThread)
                {
                    doSendMsgVerifyCode();
                }
            }
        }
    };
    private void updateUiByLastLoginUserInfo()
    {
        lastLoginUserInfo= AppStoreUtil.safeGetBean(mContext,null,LastLoginUserInfo.class);
        if(null!=lastLoginUserInfo&&RegexUtil.doRegex(lastLoginUserInfo.account,RegexUtil.MOBILE_NUM))
        {
            views.edt_name.setText(lastLoginUserInfo.account.substring(0,3)+"****"+lastLoginUserInfo.account.substring(7));
            views.edt_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    views.edt_name.removeTextChangedListener(this);
                    if(null!=lastLoginUserInfo)
                    {
                        views.edt_name.setText(null);
                        lastLoginUserInfo=null;
                    }

                }
            });
        }
        else {
            lastLoginUserInfo=null;
        }
    }
    private void doHttpTask(){
        String account=null;
        if(null!=lastLoginUserInfo){
            account=lastLoginUserInfo.account;
        }
        else {
            account=views.edt_name.getText().toString();
        }
        if(!RegexUtil.doRegex(account,RegexUtil.MOBILE_NUM))
        {
            ToastUtil.makeFailToastThr(mContext,"手机号不正确");
        }
        boolean flag= RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegexSize(views.edt_pwd,6,16,"密码长度为6-16位")
                .doRegexSize(views.edt_pwd_repeat,6,16,"确认密码长度为6-16位")
                .doRegexSize(views.edt_verifyCode,4,10,"请填写验证码").builder();
        if(!flag)
        {
            return;
        }
        String verifyCode= views.edt_verifyCode.getText().toString();
        String pwdLocalEncrypt=keyboardUtil.getEncryptStr();
        String pwdParse= ApiConfig.decryptByApp(pwdLocalEncrypt);
        String pwdLocalEncryptRepeat=keyboardUtilRepeat.getEncryptStr();
        String pwdParseRepeat= ApiConfig.decryptByApp(pwdLocalEncryptRepeat);

        if(null==pwdParse||null==pwdParseRepeat||!pwdParse.equals(pwdParseRepeat))
        {
            ToastUtil.makeFailToastThr(mContext,"密码和确认密码不一致");
            return;
        }
        if(!PassWordService.isPwdRuleOk(pwdParse)){
            ToastUtil.makeFailToastThr(mContext,PassWordService.parsePwdRuleToString());
            return;
        }

        showLoading();
        String pwd=ApiConfig.getPassWord(pwdParse,ApiConfig.TRANSMIT_KEYTYPE);
        Map<String,String> map=ApiConfig.createRequestMap(false);
        map.put("account", account);
        map.put("accountType", "1");
        map.put("uuidEncrypt", ApiConfig.getPassWordEncrypt(false));
        map.put("newPwdEncrypt", ApiConfig.getPassWordEncrypt(true));
        map.put("newPwd",  ApiConfig.getPassWord(pwdParse, ApiConfig.getPassWordEncrypt(true)));
        map.put("termType", "1");
        map.put("msgVerifyCode",verifyCode);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/userAccount/doFindPwd").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errTip= ResultFactory.getErrorTip(resultObject,status);
                ResultDto resultDto=(ResultDto)resultObject;
                if(null!=resultDto&&ResultFactory.ERR_NEED_VERIFYCODE.equals(resultDto.code)){

                    views.ly_verifyCode.setVisibility(View.VISIBLE);
                }
                if(!StringUtils.isEmpty(errTip))
                {
                    ToastUtil.makeFailToastThr(mContext,errTip);
                }
                else {
                    ToastUtil.makeOkToastThr(mContext, "找回密码成功");
                    finish();
                }
                dismissLoading();

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=verifyCodeThread)
        {
            verifyCodeThread.stopTask();
        }
    }
    private void doSendMsgVerifyCode()
    {
        boolean flag= RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegex(views.edt_name,RegexUtil.MOBILE_NUM,"手机号不正确").builder();
        if(!flag)
        {
            return;
        }
        showLoading();
        Map<String, String> map = ApiConfig.createRequestMap(false);
        map.put("msgFunction", "2");
        map.put("msgAddr", views.edt_name.getText().toString());
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/msg/doMsgSend").setRequestBodyText(map).doHttp(MsgSendDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errorTip= ResultFactory.getErrorTip(resultObject,status);

                if(StringUtils.isEmpty(errorTip))
                {
                    verifyCodeThread=new VerifyCodeThread();
                    verifyCodeThread.start();
                    ToastUtil.makeOkToastThr(mContext,"短信验证码获取成功");
                }
                else{
                    ToastUtil.makeFailToastThr(mContext,errorTip);
                }
                dismissLoading();
            }
        });
    }
    class VerifyCodeThread extends Thread_CanStop {
        @Override
        public void run() {
            super.run();
            long timeStart= SystemClock.elapsedRealtime();
            long timeOut=timeStart+ ApiConfig.VERIFYCODE_COUNTDOWN;
            long timeRemain=timeOut- SystemClock.elapsedRealtime();
            while(timeRemain>0&&!isStopTask()){
                final long timeRemind=timeOut- SystemClock.elapsedRealtime();
                FindPwdActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        views.btn_verifyCode.setText(timeRemind/1000+"秒后获取");
                    }
                });
                timeRemain=timeOut- SystemClock.elapsedRealtime();
                SystemClock.sleep(ApiConfig.VERIFYCODE_THREADSLEEP);
            }
            if(!isStopTask())
            {
                FindPwdActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        views.btn_verifyCode.setText("获取验证码");
                    }
                });
            }
            verifyCodeThread=null;
        }
    }
}
