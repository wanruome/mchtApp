package com.zjsj.mchtapp.module.login;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruomm.base.http.config.impl.TextHttpCallBack;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.extend.Thread_CanStop;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TimeUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.base.tools.regextool.RegexUtil;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.keyboard.KeyboardSafeImpl;
import com.zjsj.mchtapp.core.MsgVerifyCodeSend;
import com.zjsj.mchtapp.dal.event.MsgSendEvent;
import com.zjsj.mchtapp.dal.response.MsgSendDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.util.keyboard.KeyboardSafeInterface;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
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
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideMenuTopView();
        setInitContentView(R.layout.login_act);
        views.ly_verifyCode.setVisibility(View.GONE);
        keyboardUtil=new KeyboardUtil(this,views.edt_pwd).setSymbolEnable(false).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
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
                doLogin();
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
    private void doLogin(){
        boolean flag= RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegex(views.edt_name,RegexUtil.MOBILE_NUM,"手机号不正确").doRegexSize(views.edt_pwd,1,32,"请输入正确的密码").builder();
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
        String account=views.edt_name.getText().toString();
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
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/userAccount/doLogin").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                MLog.i(resultString);
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
                    ToastUtil.makeFailToastThr(mContext,"登录成功");
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
        map.put("msgFunction", "3");
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
    class VerifyCodeThread extends Thread_CanStop{
        @Override
        public void run() {
            super.run();
            long timeStart= SystemClock.elapsedRealtime();
            long timeOut=timeStart+ 60*1000;
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
                SystemClock.sleep(1000);
                MLog.i(timeRemain+"时间");
            }
            if(!isStopTask())
            {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        views.btn_verifyCode.setText("获取验证码");
                    }
                });
                MLog.i(timeRemain+"停止");
            }
            verifyCodeThread=null;
        }
    }


}
