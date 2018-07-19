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
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.KeyboardSafeImpl;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.event.LoginEvent;
import com.zjsj.mchtapp.dal.response.MsgSendDto;
import com.zjsj.mchtapp.dal.response.UserInfoDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.LastLoginUserInfo;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class LoginActivity extends AppMultiActivity {

    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.edt_name)
        EditText edt_name;
        @InjectView(id=R.id.edt_pwd)
        EditText edt_pwd;
        @InjectView(id=R.id.ly_verifyCode)
        LinearLayout ly_verifyCode;
        @InjectView(id=R.id.edt_verifyCode)
        EditText edt_verifyCode;
        @InjectView(id=R.id.btn_verifyCode)
        Button btn_verifyCode;
        @InjectView(id=R.id.text_findpwd)
        TextView text_findpwd;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
        @InjectView(id=R.id.ly_fast_register)
        LinearLayout ly_fast_register;
    }
    private KeyboardUtil keyboardUtil;
    private VerifyCodeThread verifyCodeThread=null;
    private LastLoginUserInfo lastLoginUserInfo=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideMenuTopView();
        setInitContentView(R.layout.login_act);
        views.ly_verifyCode.setVisibility(View.GONE);
        updateUiByLastLoginUserInfo();
        keyboardUtil=new KeyboardUtil(this,views.edt_pwd).setSymbolEnable(ApiConfig.PWD_SYMBOL_ENABLE).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
        views.btn_submit.setOnClickListener(myOnClickListener);
        views.btn_verifyCode.setOnClickListener(myOnClickListener);
        views.ly_fast_register.setOnClickListener(myOnClickListener);
        views.text_findpwd.setOnClickListener(myOnClickListener);
    }
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
//                   verifyCodeThread=new VerifyCodeThread();
//                   verifyCodeThread.start();
                   doSendMsgVerifyCode();
               }
            }
            else if(vID==R.id.text_findpwd)
            {
                startActivity(IntentFactory.getFindPwdActivity());
            }
            else if(vID==R.id.ly_fast_register)
            {
                startActivity(IntentFactory.getRegisterActivity());
            }
        }
    };
    private void updateUiByLastLoginUserInfo()
    {
        lastLoginUserInfo=AppStoreUtil.safeGetBean(mContext,null,LastLoginUserInfo.class);
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
            return;
        }
        boolean flag= RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegexSize(views.edt_pwd,1,32,"请输入正确的密码").builder();
        if(!flag)
        {
            return;
        }
        String verifyCode=null;
        if(views.ly_verifyCode.getVisibility()==View.VISIBLE)
        {
            verifyCode=views.edt_verifyCode.getText().toString();
            if(StringUtils.isEmpty(verifyCode))
            {
                ToastUtil.makeFailToastThr(mContext,"请填写验证码");
                return;
            }
        }
        showLoading();
        String pwdLocalEncrypt=keyboardUtil.getEncryptStr();
        String pwdParse=ApiConfig.decryptByApp(pwdLocalEncrypt);
        String pwd=ApiConfig.getPassWord(pwdParse,ApiConfig.TRANSMIT_KEYTYPE);
        Map<String,String> map=ApiConfig.createRequestMap(false);
        map.put("account", account);
        map.put("accountType", "1");
        map.put("uuidEncrypt", ApiConfig.getPassWordEncrypt(false));
        map.put("pwdEncrypt", ApiConfig.getPassWordEncrypt(true));
        map.put("pwd",  ApiConfig.getPassWord(pwdParse, ApiConfig.getPassWordEncrypt(true)));
        map.put("termType", "1");
        map.put("msgVerifyCode",verifyCode);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/userAccount/doLogin").setRequestBodyText(map).doHttp(UserInfoDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errTip=ResultFactory.getErrorTip(resultObject,status);
                ResultDto resultDto=(ResultDto)resultObject;
                if(null!=resultDto&&ResultFactory.ERR_NEED_VERIFYCODE.equals(resultDto.code)){

                    views.ly_verifyCode.setVisibility(View.VISIBLE);
                }
                if(!StringUtils.isEmpty(errTip))
                {
                    ToastUtil.makeFailToastThr(mContext,errTip);
                }
                else {
                    UserInfoDto userInfoDto = ResultFactory.getResult(resultObject, status);
                    if (null != userInfoDto) {
                        ToastUtil.makeOkToastThr(mContext, "登录成功");
                        LoginUserFactory.doLogin(userInfoDto);
                        finish();
                    } else {
                        ToastUtil.makeFailToastThr(mContext, "登录失败");
                    }
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
            return;
        }
        showLoading();
        Map<String, String> map = ApiConfig.createRequestMap(false);
        map.put("msgFunction", "3");
        map.put("msgAddr", account);
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
    class VerifyCodeThread extends Thread_CanStop{
        @Override
        public void run() {
            super.run();
            long timeStart= SystemClock.elapsedRealtime();
            long timeOut=timeStart+ ApiConfig.VERIFYCODE_COUNTDOWN;
            long timeRemain=timeOut- SystemClock.elapsedRealtime();
            while(timeRemain>0&&!isStopTask()){
                final long timeRemind=timeOut- SystemClock.elapsedRealtime();
                LoginActivity.this.runOnUiThread(new Runnable() {
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
                LoginActivity.this.runOnUiThread(new Runnable() {
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
