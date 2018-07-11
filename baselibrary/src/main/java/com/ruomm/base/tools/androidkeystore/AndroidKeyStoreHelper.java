package com.ruomm.base.tools.androidkeystore;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.tools.Base64;
import com.ruomm.base.tools.DesUtil;
import com.ruomm.base.tools.RSAUtils;
import com.ruomm.base.tools.StringUtils;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.security.auth.x500.X500Principal;

public class AndroidKeyStoreHelper {
    public static final int DEFAULT_KEY_SIZE = 1024;
    private static final String TAG=AndroidKeyStoreHelper.class.getSimpleName();
    private static final String desKeyStr="GdaXf5FF38IvUpL4UUzqIEr0Kf3aGc4v";
    KeyStore keyStore=null;
    public void initKeyStore() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M||null!=keyStore){
            return;
        }
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasAlias(String alias) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            String data=AppStoreUtil.getString(BaseApplication.getApplication(),alias);
            if(StringUtils.isEmpty(data))
            {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            try {
                initKeyStore();
                return keyStore != null && keyStore.containsAlias(alias);
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void createKeyPair(String alias) {
        if (hasAlias(alias)) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try{
                //使用别名来检索的key 。这是一个key 的key !
                KeyPair keyPair=RSAUtils.generateRSAKeyPair(DEFAULT_KEY_SIZE);
                String pubKeyStr= Base64.encode(keyPair.getPublic().getEncoded());
                String priKeyStr= Base64.encode(keyPair.getPrivate().getEncoded());
                KeyPairStore keyPairStore=new KeyPairStore();
                keyPairStore.privateKey=priKeyStr;
                keyPairStore.publicKey=pubKeyStr;
                AppStoreUtil.saveString(BaseApplication.getApplication(),alias, DesUtil.encryptString(JSON.toJSONString(keyPairStore),desKeyStr));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } else {
            try{
                Calendar start = new GregorianCalendar();
                Calendar end = new GregorianCalendar();
                end.add(Calendar.YEAR, 10);//往后加10年
                AlgorithmParameterSpec spec;
                //Android 6.0(或者以上)使用KeyGenparameterSpec.Builder 方式来创建,
                // 允许你自定义允许的的关键属性和限制
                spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN
                        | KeyProperties.PURPOSE_ENCRYPT
                        | KeyProperties.PURPOSE_DECRYPT)
                        .setKeySize(DEFAULT_KEY_SIZE)
                        .setUserAuthenticationRequired(false)
                        .setCertificateSubject(new X500Principal("CN=" + alias))
                        //, KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA384,
                        // KeyProperties.DIGEST_SHA512, KeyProperties.DIGEST_MD5)
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA1)
                        .setCertificateNotBefore(start.getTime())
                        .setCertificateNotAfter(end.getTime())
                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build();
                KeyPairGenerator kpGenerator = KeyPairGenerator
                        .getInstance(SecurityConstants.TYPE_RSA,
                                SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
                kpGenerator.initialize(spec);
                kpGenerator.generateKeyPair();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


    }
    public KeyPair getTargetKeyPair(String alias) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            createKeyPair(alias);
            String data=AppStoreUtil.getString(BaseApplication.getApplication(),alias);
            String dataDecypt=DesUtil.decryptString(data,desKeyStr);
            KeyPairStore keyPairStore=JSON.parseObject(dataDecypt,KeyPairStore.class);
            PublicKey publicKey=RSAUtils.loadPublicKey(keyPairStore.publicKey);
            PrivateKey privateKey=RSAUtils.loadPrivateKey(keyPairStore.privateKey);
            Log.d(TAG, "getTargetKeyPair>>privateKey:" + privateKey + ",publicKey:" + publicKey);
            Log.d(TAG, publicKey.toString());
            Log.d(TAG, privateKey.toString());
            return new KeyPair(publicKey,privateKey);
        }
        else {
            createKeyPair(alias);
            try {
                KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                PublicKey publicKey = entry.getCertificate().getPublicKey();
                PrivateKey privateKey = entry.getPrivateKey();
                Log.d(TAG, "getTargetKeyPair>>privateKey:" + privateKey + ",publicKey:" + publicKey);
                Log.d(TAG, publicKey.toString());
                Log.d(TAG, privateKey.toString());
                return new KeyPair(publicKey, privateKey);
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (UnrecoverableEntryException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
