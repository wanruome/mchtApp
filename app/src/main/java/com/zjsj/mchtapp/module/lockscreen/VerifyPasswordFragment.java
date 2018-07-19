package com.zjsj.mchtapp.module.lockscreen;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.ToastUtil;
import com.ruomm.base.tools.regextool.RegexCallBack;
import com.ruomm.base.tools.regextool.RegexText;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.IntentFactory;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.KeyboardSafeImpl;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.core.PassWordService;
import com.zjsj.mchtapp.dal.response.base.ResultDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;
import com.zjsj.mchtapp.module.payinfo.PayInfoModifyPwdActivity;
import com.zjsj.mchtapp.util.keyboard.KeyboardUtil;

import java.util.Map;

public class VerifyPasswordFragment extends AppFragment {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id=R.id.edt_pwd)
        EditText edt_pwd;
        @InjectView(id=R.id.btn_submit)
        Button btn_submit;
        @InjectView(id=R.id.text_findpwd)
        TextView text_findpwd;
    }
    private KeyboardUtil keyboardUtil;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=LayoutInflater.from(mContext).inflate(R.layout.lockscreen_verifypwd_lay,null);
        BaseUtil.initInjectAll(this,mView);
        keyboardUtil=new KeyboardUtil(getActivity(),views.edt_pwd).setSymbolEnable(ApiConfig.PWD_SYMBOL_ENABLE).setSafeInterFace(new KeyboardSafeImpl()).bulider(KeyboardUtil.KEYMODE.LETTER_LOWER);
        views.btn_submit.setOnClickListener(myOnClickListener);
        views.text_findpwd.setOnClickListener(myOnClickListener);
        return mView;
    }
    private View.OnClickListener myOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vID=v.getId();
            if(vID==R.id.btn_submit){
                doHttpTask();
            }
            else if(vID==R.id.text_findpwd)
            {
                startActivity(IntentFactory.getFindPwdActivity());
            }

        }
    };
    private void doHttpTask(){
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
        String pwdLocalEncrypt=keyboardUtil.getEncryptStr();
        String pwdParse= ApiConfig.decryptByApp(pwdLocalEncrypt);
        if(StringUtils.isEmpty(pwdParse))
       {
           ToastUtil.makeFailToastThr(mContext,"请输入正确的密码");
           return;
       }


        showLoading();
        Map<String,String> map=ApiConfig.createRequestMap(true);
        map.put("pwdEncrypt", ApiConfig.getPassWordEncrypt(true));
        map.put("pwd",  ApiConfig.getPassWord(pwdParse, ApiConfig.getPassWordEncrypt(true)));
        ApiConfig.signRequestMap(map);
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/userAccount/doVerifyPassword").setRequestBodyText(map).doHttp(String.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                String errMsg=ResultFactory.getErrorTip(resultObject,status);
                if(StringUtils.isEmpty(errMsg))
                {
                    ToastUtil.makeOkToastThr(mContext,"密码验证成功，清除了指纹和手势密码");
                    LoginUserFactory.saveUserGesturesInfo(null);
                    LoginUserFactory.saveUserFingerPrint(false);
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                else {
                    ToastUtil.makeFailToastThr(mContext,"请输入正确的密码");
                }
                dismissLoading();

            }
        });
    }
}
