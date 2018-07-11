package com.zjsj.mchtapp.module.payinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.KeyboardSafeImpl;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.core.PassWordService;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import java.util.Map;

public class PayInfoSetPwdActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.edt_pwd)
        EditText edt_pwd;
        @InjectView(id=R.id.edt_pwd_repeat)
        EditText edt_pwd_repeat;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
    }
    private KeyboardUtil keyboardUtil;
    private KeyboardUtil keyboardUtilRepeat;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mymenutop.setMenuClickListener(myMenutopListener);
        mymenutop.setCenterText("设置支付密码");
        setInitContentView(R.layout.paypwd_setpwd_act);
        keyboardUtil=new KeyboardUtil(this,views.edt_pwd).setSymbolEnable(false).setLetterEnable(false).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.NUMBER);
        keyboardUtilRepeat=new KeyboardUtil(this,views.edt_pwd_repeat).setSymbolEnable(false).setLetterEnable(false).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.NUMBER);
        views.btn_submit.setOnClickListener(myOnClickListener);
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
        }
    };
    private void doHttpTask(){

        String pwdLocalEncrypt=keyboardUtil.getEncryptStr();
        String pwdParse= ApiConfig.decryptByApp(pwdLocalEncrypt);
        String pwdLocalEncryptRepeat=keyboardUtilRepeat.getEncryptStr();
        String pwdParseRepeat= ApiConfig.decryptByApp(pwdLocalEncryptRepeat);

        if(null==pwdParse||null==pwdParseRepeat||!pwdParse.equals(pwdParseRepeat))
        {
            ToastUtil.makeFailToastThr(mContext,"密码和确认密码不一致");
            return;
        }
        if(!PassWordService.isPayPwdRuleOk(pwdParse)){
            ToastUtil.makeFailToastThr(mContext,PassWordService.parsePayPwdRuleToString());
            return;
        }

        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
        map.put("payPwdEncrypt", ApiConfig.getPassWordEncrypt(true));
        map.put("payPwd",  ApiConfig.getPassWord(pwdParse, ApiConfig.getPassWordEncrypt(true)));
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"/app/repaymentPayInfo/doSetPayPwd").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {

                String errTip= ResultFactory.getErrorTip(resultObject,status);
                ResultDto resultDto=(ResultDto)resultObject;
                if(!StringUtils.isEmpty(errTip))
                {
                    ToastUtil.makeFailToastThr(mContext,errTip);
                }
                else {
                    ToastUtil.makeOkToastThr(mContext, "设置支付密码成功");
                    finish();
                }
                dismissLoading();

            }
        });
    }
}
