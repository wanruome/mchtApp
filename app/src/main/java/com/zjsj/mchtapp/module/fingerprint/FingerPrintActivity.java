package com.zjsj.mchtapp.module.fingerprint;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.ruomm.base.ioc.annotation.view.InjectAll;
import com.ruomm.base.ioc.annotation.view.InjectView;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.androidkeystore.KeyStoreHelper;
import com.ruomm.base.view.menutopview.MenuTopListener;
import com.ruomm.base.view.menutopview.MenuTopView;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.resource.ui.AppMultiActivity;
import com.zjsj.mchtapp.R;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;

public class FingerPrintActivity extends AppMultiActivity {
    @InjectAll
    Views views=new Views();
    class Views{
        @InjectView(id= R.id.mymenutop)
        MenuTopView menuTopView;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        BaseUtil.initInjectAll(this);
        views.menuTopView.setMenuClickListener(new MenuTopListener() {
            @Override
            public void onMenuTopClick(View v, int vID) {
                testKeyStore();
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void testKeyStore() {
        KeyStoreHelper helper = new KeyStoreHelper();
        helper.initKeyStore();

        helper.printAliases();

        String aesAlias = "hello aes key";
        helper.createSecretKey(aesAlias);
        helper.getSecretKey(aesAlias);
        helper.printAliases();

        String rsaAlias = "hello rsa key";
        helper.createKeyPair(this,rsaAlias);
        KeyPair keyPair=helper.getTargetKeyPair(rsaAlias);
        helper.printAliases();
//        RSAPrivateKey rsaPrivateKey=(RSAPrivateKey) keyPair.getPrivate();
        byte[] data=keyPair.getPrivate().getEncoded();
        byte[] encryptData=RSAUtils.encryptData("你好".getBytes(),keyPair.getPrivate());
        String encryptStr= Base64.encode(encryptData);
        MLog.i(data);
        MLog.i(encryptStr);
    }
    public static byte[] encryptData(byte[] sourceData, PrivateKey privateKey) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            // 传入编码数据并返回编码结果
            result = cipher.doFinal(sourceData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
