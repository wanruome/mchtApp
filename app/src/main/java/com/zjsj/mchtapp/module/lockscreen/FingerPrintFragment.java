package com.zjsj.mchtapp.module.lockscreen;

import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.view.gesturelock.GestureLock;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppFragment;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.LoginUserFactory;
import com.zjsj.mchtapp.module.fingerprint.FingerPrintActivity;
import com.zjsj.mchtapp.util.fingerprint.FingerprintUtil;

public class FingerPrintFragment extends AppFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=LayoutInflater.from(mContext).inflate(R.layout.lockscreen_fingerprint_lay,null);
        BaseUtil.initInjectAll(this,mView);
        views.text_error.setVisibility(View.INVISIBLE);
        views.ly_container.setVisibility(View.VISIBLE);
        fingerprintUtil=new FingerprintUtil(getActivity());
        doVerifyFingerprint();
        return  mView;
    }
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
                getActivity().finish();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fingerprintUtil.onDestroy();
    }

    private void showFingerprintAuthMsg(CharSequence contentStr)
    {
        views.text_tip.setText(contentStr);
    }
}
