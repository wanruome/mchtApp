package com.zjsj.mchtapp.module.fingerprint;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.androidkeystore.AndroidKeyStoreRSAUtils;
import com.ruomm.base.tools.androidkeystore.KeyStoreHelper;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.module.userinfo.FindPwdActivity;
import com.zjsj.mchtapp.util.fingerprint.FingerprintUtil;
import com.zjsj.mchtapp.view.FingerPrintDialog;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;

public class FingerPrintActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id= R.id.text_error)
        TextView text_error;
    }
    FingerprintUtil fingerprintUtil=null;
    FingerPrintDialog fingerPrintDialog=null;
    boolean isOnVerify=false;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.fingerprint_act_lay);
        setMenuTop();
        views.text_error.setVisibility(View.GONE);
        fingerPrintDialog=new FingerPrintDialog(mContext);
        fingerprintUtil=new FingerprintUtil(mContext);
        doVerifyFingerprint();

    }
    private void setMenuTop(){
        mymenutop.setCenterText("设置指纹密码");
        mymenutop.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                if(vID==R.id.menutop_left)
                {
                    finish();
                }
            }
        });
    }
//    private void setClickListener(){
//        views.text_tip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doVerifyFingerprint();
//            }
//        });
//    }
    public void doVerifyFingerprint(){
        if(isOnVerify)
        {
            return;
        }
        isOnVerify=true;
        fingerprintUtil.callFingerPrintVerify(new FingerprintUtil.IFingerprintResultListener() {
            @Override
            public void onSupport(boolean isSupport) {
                MLog.i("onSupport"+isSupport);
                if(!isSupport)
                {
                    isOnVerify=false;
                    views.text_error.setVisibility(View.VISIBLE);
                }
                else {
                    views.text_error.setVisibility(View.GONE);
                }
            }
            @Override
            public void onAuthenticateStart() {
                MLog.i("onAuthenticateStart");
                showDialog("验证指纹");
            }

            @Override
            public void onAuthenticateError(int errMsgId, CharSequence errString) {
                MLog.i("onAuthenticateError"+errMsgId+errString);
                showDialog(errString);
            }

            @Override
            public void onAuthenticateFailed() {
                MLog.i("onAuthenticateFailed");
                showDialog("指纹验证失败，请稍后重试");
                isOnVerify=false;
            }

            @Override
            public void onAuthenticateHelp(int helpMsgId, CharSequence helpString) {
                MLog.i("onAuthenticateHelp"+helpMsgId+helpString);
            }

            @Override
            public void onAuthenticateSucceeded(FingerprintManager.AuthenticationResult result) {
                MLog.i("onAuthenticateSucceeded"+result.toString());
                showDialog("指纹验证成功");
                isOnVerify=false;
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        SystemClock.sleep(300);
//                        FingerPrintActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                fingerPrintDialog=null;
//                                setResult(Activity.RESULT_OK);
//                                finish();
//                            }
//                        });
//                    }
//                }.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(null!=fingerPrintDialog&&fingerPrintDialog.isShowing())
                        {
                            fingerPrintDialog.dismiss();
                        }
                        fingerPrintDialog=null;
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                },300);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fingerprintUtil.onDestroy();
        fingerPrintDialog=null;
    }

    private void showDialog(CharSequence contentStr)
    {
        if(null==fingerPrintDialog)
        {
            return;
        }
        fingerPrintDialog.setMessageContent(contentStr);
        fingerPrintDialog.show();
    }



}
