package com.jsz.peini.utils;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.presenter.IpConfig;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static Retrofit retrofit;
    private static Retrofit httpsRetrofit;
    public static boolean isUpdateIp = false;
    private static final int mCertificate = R.raw.pn;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    private static final OkHttpClient httpsClient = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(new StethoInterceptor())
            .hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            })
            .sslSocketFactory(getSSLSocketFactory(PeiNiApp.context))
            .build();

    public static <T> T createService(Class<T> clazz) {
        if (retrofit == null || isUpdateIp) {
            synchronized (RetrofitUtil.class) {
                Retrofit.Builder builder = new Retrofit.Builder();
                retrofit = builder
                        .baseUrl(IpConfig.HttpPeiniIp)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                isUpdateIp = false;
            }
        }
        return retrofit.create(clazz);
    }

    public static <T> T createHttpsService(Class<T> clazz) {
        if (httpsRetrofit == null) {
            synchronized (RetrofitUtil.class) {
                Retrofit.Builder builder = new Retrofit.Builder();
                httpsRetrofit = builder
                        .baseUrl(IpConfig.HttpsPeiniIp)
                        .client(httpsClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return httpsRetrofit.create(clazz);
    }

    private static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            //读取本地证书
            InputStream is = context.getResources().openRawResource(mCertificate);
            keyStore.setCertificateEntry("ca", certificateFactory.generateCertificate(is));

            if (is != null) {
                is.close();
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}