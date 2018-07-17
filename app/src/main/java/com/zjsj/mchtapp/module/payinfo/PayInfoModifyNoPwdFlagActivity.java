package com.zjsj.mchtapp.module.payinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.extend.Thread_CanStop;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.KeyboardSafeImpl;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.core.PassWordService;
import com.zjsj.mchtapp.dal.response.MsgSendDto;
import com.zjsj.mchtapp.dal.response.PayInfoDto;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.dal.store.LastLoginUserInfo;
import com.zjsj.mchtapp.dal.ui.PayInfoModifyNoPwdFlagModel;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import java.util.Map;

public class PayInfoModifyNoPwdFlagActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{

        @InjectView(id=R.id.text_pwd_old_tip)
        TextView text_pwd_old_tip;
        @InjectView(id=R.id.edt_pwd_old)
        EditText edt_pwd_old;
        @InjectView(id=R.id.ly_verifyCode)
        LinearLayout ly_verifyCode;
        @InjectView(id=R.id.edt_verifyCode)
        EditText edt_verifyCode;
        @InjectView(id=R.id.btn_verifyCode)
        Button btn_verifyCode;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
    }
    private KeyboardUtil keyboardUtilOld;
    private VerifyCodeThread verifyCodeThread=null;
    private LastLoginUserInfo lastLoginUserInfo=null;
    PayInfoModifyNoPwdFlagModel payInfoModifyNoPwdFlagModel=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mymenutop.setMenuClickListener(myMenutopListener);
        mymenutop.setCenterText("修改免密状态");
        setInitContentView(R.layout.paypwd_modifynopwdflag_act);
        payInfoModifyNoPwdFlagModel= BaseUtil.serializableGet(getIntent(),PayInfoModifyNoPwdFlagModel.class);
        if(null==payInfoModifyNoPwdFlagModel)
        {
            payInfoModifyNoPwdFlagModel=new PayInfoModifyNoPwdFlagModel();
            payInfoModifyNoPwdFlagModel.isUsePayPwd=false;
            payInfoModifyNoPwdFlagModel.noPwdFlag="0";
        }
        if(payInfoModifyNoPwdFlagModel.isUsePayPwd)
        {
            views.text_pwd_old_tip.setText("支付密码");
            keyboardUtilOld=new KeyboardUtil(this,views.edt_pwd_old).setLetterEnable(false).setSymbolEnable(false).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.NUMBER);
        }
        else{
            views.text_pwd_old_tip.setText("用户密码");
            keyboardUtilOld=new KeyboardUtil(this,views.edt_pwd_old).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
        }


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
    private void doHttpTask(){

        String verifyCode= views.edt_verifyCode.getText().toString();
        String pwdLocalEncryptOld=keyboardUtilOld.getEncryptStr();
        String pwdParseOld= ApiConfig.decryptByApp(pwdLocalEncryptOld);
        if(StringUtils.isEmpty(pwdParseOld)){
            ToastUtil.makeFailToastThr(mContext,"请填写旧的支付密码");
            return;
        }
        boolean flag= RegexText.with(new RegexCallBack() {
            @Override
            public void errorRegex(TextView v, String value, String errorInfo) {
                ToastUtil.makeFailToastThr(mContext,errorInfo);
            }
        }).doRegexSize(views.edt_verifyCode,4,10,"请填写验证码").builder();
        if(!flag)
        {
            return;
        }

        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
        if(payInfoModifyNoPwdFlagModel.isUsePayPwd)
        {
            map.put("payPwdEncrypt", ApiConfig.getPassWordEncrypt(true));
            map.put("payPwd",  ApiConfig.getPassWord(pwdParseOld, ApiConfig.getPassWordEncrypt(true)));
        }
        else {
            map.put("pwdEncrypt", ApiConfig.getPassWordEncrypt(true));
            map.put("pwd",  ApiConfig.getPassWord(pwdParseOld, ApiConfig.getPassWordEncrypt(true)));
        }
        map.put("msgVerifyCode",verifyCode);
        map.put("noPwdFlag",payInfoModifyNoPwdFlagModel.noPwdFlag);
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/repaymentPayInfo/doModifyNoPwdFlag").setRequestBodyText(map).doHttp(PayInfoDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errTip= ResultFactory.getErrorTip(resultObject,status);
                if(!StringUtils.isEmpty(errTip))
                {
                    ToastUtil.makeFailToastThr(mContext,errTip);

                }
                else {
                    PayInfoDto payInfoDtoResult=ResultFactory.getResult(resultObject,status);
                    if(null!=payInfoDtoResult){
                        ToastUtil.makeOkToastThr(mContext, "修改免密状态成功");
                        LoginUserFactory.getPayInfo().noPwdFlag=payInfoDtoResult.noPwdFlag;
                        LoginUserFactory.savePayInfoDto(LoginUserFactory.getPayInfo());
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                    else{
                        ToastUtil.makeFailToastThr(mContext,"修改免密状态失败");
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
        showLoading();
        Map<String, String> map = ApiConfig.createRequestMap(true);
        map.put("msgFunction", "10");
        map.put("msgAddr", "1");
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
                PayInfoModifyNoPwdFlagActivity.this.runOnUiThread(new Runnable() {
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
                PayInfoModifyNoPwdFlagActivity.this.runOnUiThread(new Runnable() {
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
