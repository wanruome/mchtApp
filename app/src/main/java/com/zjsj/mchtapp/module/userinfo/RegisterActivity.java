package com.zjsj.mchtapp.module.userinfo;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.http.config.impl.TextHttpCallBack;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.extend.Thread_CanStop;
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
import com.zjsj.mchtapp.core.PassWordService;
import com.zjsj.mchtapp.dal.response.MsgSendDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import java.util.Map;

public class RegisterActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id= R.id.edt_name)
        EditText edt_name;
        @InjectView(id=R.id.edt_pwd)
        EditText edt_pwd;
        @InjectView(id=R.id.edt_pwd_repeat)
        EditText edt_pwd_repeat;
        @InjectView(id=R.id.edt_nickname)
        EditText edt_nickname;
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
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mymenutop.setMenuClickListener(myMenutopListener);
        mymenutop.setCenterText("用户注册");
        setInitContentView(R.layout.userinfo_register_act);
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
//                   verifyCodeThread=new VerifyCodeThread();
//                   verifyCodeThread.start();
                    doSendMsgVerifyCode();
                }
            }
        }
    };
    private void doHttpTask(){
        boolean flag= RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegex(views.edt_name, RegexUtil.MOBILE_NUM,"手机号不正确")
                .doRegexSize(views.edt_pwd,6,16,"密码长度为6-16位")
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
        String account=views.edt_name.getText().toString();
        String pwd=ApiConfig.getPassWord(pwdParse,ApiConfig.TRANSMIT_KEYTYPE);
        String nickName=views.edt_nickname.getText().toString();
        Map<String,String> map=ApiConfig.createRequestMap(false);
        map.put("account", account);
        map.put("accountType", "1");
        map.put("uuidEncrypt", ApiConfig.getPassWordEncrypt(false));
        map.put("pwdEncrypt", ApiConfig.getPassWordEncrypt(true));
        map.put("pwd",  ApiConfig.getPassWord(pwdParse, ApiConfig.getPassWordEncrypt(true)));
        map.put("termType", "1");
        map.put("msgVerifyCode",verifyCode);
        map.put("nickName",nickName);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/userAccount/doRegister").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
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
                    ToastUtil.makeOkToastThr(mContext, "注册成功");
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
        map.put("msgFunction", "1");
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
                RegisterActivity.this.runOnUiThread(new Runnable() {
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
                RegisterActivity.this.runOnUiThread(new Runnable() {
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
