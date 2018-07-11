package com.zjsj.mchtapp.util.fingerprint;


import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;

public class FingerprintUtil {
    private FingerprintManager mFingerprintManager;
    private Activity mActivity;
    private KeyguardManager mKeyManager;
    private CancellationSignal mCancellationSignal;
    private IFingerprintResultListener fingerListener=null;

    public FingerprintUtil(Context ctx) {
        mActivity = (Activity) ctx;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintManager = mActivity.getSystemService(FingerprintManager.class);
            mKeyManager = (KeyguardManager) mActivity.getSystemService(Context.KEYGUARD_SERVICE);
        }
    }
    public boolean isSupporFingerPrint(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean flag;
            try {
                if (!isHardwareDetected()) {
                    flag=false;
                }
                else if (!isHasEnrolledFingerprints()) {
                    flag=false;
                }
                else if (!isKeyguardSecure()) {
                    flag=false;
                }
                else {
                    flag=true;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                flag=false;
            }
            return flag;
        }
        else {
            return false;
        }
    };
    public void callFingerPrintVerify(final IFingerprintResultListener listener) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            this.fingerListener=listener;
            boolean isSupport=isSupporFingerPrint();
            if(null!=this.fingerListener)
            {
                this.fingerListener.onSupport(isSupport);
            }
            if(!isSupport)
            {
                return;
            }

            if (listener != null) {
                this.fingerListener.onAuthenticateStart();
            }
            if (mCancellationSignal == null) {
                mCancellationSignal = new CancellationSignal();
            }

            mFingerprintManager.authenticate(null,mCancellationSignal,0,mAuthenticationCallback,null);
        }
        else {
            if(null!=this.fingerListener)
            {
                this.fingerListener.onSupport(false);
            }
        }

    }
    private FingerprintManager.AuthenticationCallback mAuthenticationCallback=new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if (fingerListener != null) {
                fingerListener.onAuthenticateError(errorCode, errString);
            }
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            if (fingerListener != null) {
                fingerListener.onAuthenticateHelp(helpCode, helpString);
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            if (fingerListener != null) {
                fingerListener.onAuthenticateSucceeded(result);
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            if (fingerListener != null) {
                fingerListener.onAuthenticateFailed();
            }
        }
    };

    /**
     * 是否录入指纹，有些设备上即使录入了指纹，可是没有开启锁屏password的话此方法还是返回false
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isHasEnrolledFingerprints() {
        try {
            return mFingerprintManager.hasEnrolledFingerprints();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否有指纹识别硬件支持
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isHardwareDetected() {
        try {
            return mFingerprintManager.isHardwareDetected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 推断是否开启锁屏password
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isKeyguardSecure() {
        try {
            return mKeyManager.isKeyguardSecure();
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 指纹识别回调接口
     */
    public interface IFingerprintResultListener {
        void onSupport(boolean isSupport);

        void onAuthenticateStart();

        void onAuthenticateError(int errMsgId, CharSequence errString);

        void onAuthenticateFailed();

        void onAuthenticateHelp(int helpMsgId, CharSequence helpString);

        void onAuthenticateSucceeded(FingerprintManager.AuthenticationResult result);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void cancelAuthenticate() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    public void onDestroy() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cancelAuthenticate();
            mKeyManager = null;
            mFingerprintManager = null;
        }
        else {
            mKeyManager = null;
            mFingerprintManager = null;
        }

    }
}
