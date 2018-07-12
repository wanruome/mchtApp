package com.zjsj.mchtapp.module.fingerprint;
import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;

import com.ruomm.base.ioc.iocutil.DbStoreSafe;
import com.ruomm.base.view.menutopview.MenuTopListener;

import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;

import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.dal.store.UserFingerPrint;
import com.zjsj.mchtapp.util.fingerprint.FingerprintUtil;

public class FingerPrintActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id= R.id.text_error)
        TextView text_error;
        @InjectView(id= R.id.text_tip)
        TextView text_tip;
        @InjectView(id= R.id.ly_container)
        LinearLayout ly_container;
    }
    FingerprintUtil fingerprintUtil=null;
    boolean isOnVerify=false;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.fingerprint_act_lay);
        setMenuTop();
        views.text_error.setVisibility(View.INVISIBLE);
        views.ly_container.setVisibility(View.VISIBLE);
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
                    views.ly_container.setVisibility(View.INVISIBLE);
                    views.text_error.setVisibility(View.VISIBLE);
                }
                else {
                    views.ly_container.setVisibility(View.VISIBLE);
                    views.text_error.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onAuthenticateStart() {
                MLog.i("onAuthenticateStart");
                showFingerprintAuthMsg("验证指纹");
            }

            @Override
            public void onAuthenticateError(int errMsgId, CharSequence errString) {
                MLog.i("onAuthenticateError"+errMsgId+errString);
                showFingerprintAuthMsg(errString);
            }

            @Override
            public void onAuthenticateFailed() {
                MLog.i("onAuthenticateFailed");
                showFingerprintAuthMsg("指纹验证失败，请稍后重试");
                isOnVerify=false;
            }

            @Override
            public void onAuthenticateHelp(int helpMsgId, CharSequence helpString) {
                MLog.i("onAuthenticateHelp"+helpMsgId+helpString);
            }

            @Override
            public void onAuthenticateSucceeded(FingerprintManager.AuthenticationResult result) {
                MLog.i("onAuthenticateSucceeded"+result.toString());
                showFingerprintAuthMsg("指纹验证成功");
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
                        boolean tmp=LoginUserFactory.saveUserFingerPrint(true);
                        if(tmp){
                            setResult(Activity.RESULT_OK);
                        }
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
    }

    private void showFingerprintAuthMsg(CharSequence contentStr)
    {
        views.text_tip.setText(contentStr);
    }
}
