package com.zjsj.mchtapp.module.welcome;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;

import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.activity.AppManager;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.base.tools.permission.PermissionBean;
import com.ruomm.base.tools.permission.PermissionHelper;
import com.ruomm.base.tools.permission.PermissionHelperCallBack;
import com.ruomm.base.tools.rootcheck.RootCheck;
import com.ruomm.base.tools.rootcheck.RootCheckCallBack;
import com.ruomm.base.view.dialog.BaseDialogClickListener;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.dialog.MessageDialog;
import com.ruomm.resource.dialog.dal.DialogValue;
import com.ruomm.resource.ui.AppSimpleActivity;
import com.zjsj.mchtapp.R;
import com.zjsj.mchtapp.config.http.ApiConfig;
import com.zjsj.mchtapp.config.impl.TextHttpCallBack;
import com.zjsj.mchtapp.dal.response.KeyPairDto;
import com.zjsj.mchtapp.dal.response.base.ResultFactory;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends AppSimpleActivity {
//    @InjectView(id=R.id.view_img)
//    ImageView view_img;
    PermissionHelper permissionHelper=null;
    boolean isGrant=false;
    String uuid=null;
    KeyPair keyPair=RSAUtils.generateRSAKeyPair();
//    boolean isFinish=false;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setInitContentView(R.layout.welcome_act);
        uuid= TelePhoneUtil.getUtdid(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isGrant) {
            if (null == permissionHelper) {
                permissionHelper = new PermissionHelper(mContext, permissionHelperCallBack);
                permissionHelper.setPermissions(new String[][]{
                        {Manifest.permission.READ_EXTERNAL_STORAGE, "存储卡"},
                        {Manifest.permission.ACCESS_FINE_LOCATION, "定位"},
//                        {android.Manifest.permission.READ_PHONE_STATE, "手机状态"},
//                        {android.Manifest.permission.CAMERA, "相机"},
//                        {android.Manifest.permission.CALL_PHONE, "拨打电话"}
                });
                permissionHelper.checkPermissions();
            }
            else{
                permissionHelper.checkPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    PermissionHelperCallBack permissionHelperCallBack=new PermissionHelperCallBack() {
        @Override
        public void grantedCallBack(List<PermissionBean> listPermissionBeans,String dialogMsg, boolean isAllGranted) {
            MLog.i("权限申请完毕");
            isGrant=isAllGranted;
            if(isGrant)
            {
//                getPublicKey();
                doCheckRoot();
            }
            else {
                MessageDialog messageDialog=new MessageDialog(mContext);
                messageDialog.setDialogValue(new DialogValue("提示",dialogMsg,"退出","设置"));
                messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                    @Override
                    public void onDialogItemClick(View v, Object tag) {
                        if(v.getId()==R.id.dialog_confirm){
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                            mContext.startActivity(intent);
                        }
                        else if(v.getId()==R.id.dialog_cancle)
                        {
                            AppManager.exitApp();
                        }
                    }
                });
                messageDialog.show();
            }
        }
    };

    @Override
    public void finish() {
        super.finish();

    }


    private void gotoMainActivity(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(3000);
                startMainActivity();
            }
        }.start();
    }
    private void startMainActivity(){
        if(isFinishing())
        {
            return;
        }
//        Intent intent=new Intent("fingerprint.FingerPrintActivity");
        Intent intent=new Intent("main.MainActivity");
        startActivity(intent);
        finish();
    }
    private void doCheckRoot()
    {
        new RootCheck().doRootCheck(new RootCheckCallBack() {
            @Override
            public void checkStart() {
                showLoading("检查手机环境安全");
            }

            @Override
            public void checkResult(boolean isRoot) {
                dismissLoading();
                if(isRoot)
                {
                    MessageDialog messageDialog=new MessageDialog(mContext);
                    DialogValue dialogValue=new DialogValue("提示","手机被root过了，环境不安全。","退出应用","  ");
                    messageDialog.setDialogValue(dialogValue);
                    messageDialog.setAutoDisMisss(false);
                    messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                        @Override
                        public void onDialogItemClick(View v, Object tag) {
                            if(v.getId()==R.id.dialog_cancle)
                            {
                                dismissLoading();
                                AppManager.exitApp();

                            }
                            else if(v.getId()==R.id.dialog_confirm)
                            {

                            }
                        }
                    });
                    messageDialog.show();
                }
                else {
                    getPublicKey();
                }
            }
        });
    }
    private void getPublicKey()
    {
//        new DataOKHttp().setUrl().setRequestBody()
        if(!ApiConfig.isKeyPairNeedUpdate())
        {
            gotoMainActivity();
            return;
        }
        showLoading();
        Map<String,String> map=new HashMap<>();
        map.put("uuid", ApiConfig.getAppUUID());
        map.put("keyType", ApiConfig.TRANSMIT_KEYTYPE);
        map.put("rasPublicKey", Base64.encode(keyPair.getPublic().getEncoded()));
        map.put("timeStamp", System.currentTimeMillis() + "");
        new TextOKHttp().setUrl(ApiConfig.BASE_URL+"app/keypair/getPublicKeyByUuid").setRequestBodyText(map).doHttp(KeyPairDto.class, new TextHttpCallBack() {
            @Override
            public void httpCallBack(Object resultObject, String resultString, int status) {
                if(null== ResultFactory.getErrorTip(resultObject,status)){
                    KeyPairDto keyPairDto=ResultFactory.getResult(resultObject,status);
                    keyPairDto.keyType=ApiConfig.TRANSMIT_KEYTYPE;
                    String pubKeyStr=new String(RSAUtils.decryptDataBig(Base64.decode(keyPairDto.publicKey),keyPair.getPrivate()));
                    keyPairDto.publicKey=pubKeyStr;
                    ApiConfig.loadTransmitKey(keyPairDto);
                    AppStoreUtil.safeSaveBean(mContext,null,keyPairDto);

                }
                dismissLoading();
                if(!ApiConfig.isKeyPairOk())
                {
                    MessageDialog messageDialog=new MessageDialog(mContext);
                    DialogValue dialogValue=new DialogValue("提示","请求传输密钥失败，请重新尝试获取秘钥","退出应用","重试");
                    messageDialog.setDialogValue(dialogValue);
                    messageDialog.setBaseDialogClick(new BaseDialogClickListener() {
                        @Override
                        public void onDialogItemClick(View v, Object tag) {
                            if(v.getId()==R.id.dialog_cancle)
                            {
                                AppManager.exitApp();
                            }
                            else{
                                getPublicKey();
                            }
                        }
                    });
                    messageDialog.show();
                }
                else {
                    startMainActivity();
                }
            }

        });
    }

}
