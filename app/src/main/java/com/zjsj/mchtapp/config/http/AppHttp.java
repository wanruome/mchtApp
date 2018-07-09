package com.zjsj.mchtapp.config.http;

import com.ruomm.base.http.okhttp.DataOKHttp;
import com.ruomm.base.http.okhttp.FileOkHttp;
import com.ruomm.base.http.okhttp.TextOKHttp;
import com.ruomm.base.ioc.application.BaseApplication;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class AppHttp {

}
