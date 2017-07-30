package com.clement.tvscheduler.task;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.clement.tvscheduler.AppConstants;
import com.clement.tvscheduler.activity.ConnectedActivityI;
import com.clement.tvscheduler.activity.TvPcActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by clement on 06/09/16.
 */
public abstract class BaseTask extends AsyncTask<Integer, Integer, Long> {


    static SSLContext context;

    protected ConnectedActivityI connectedActivity;

    public BaseTask(ConnectedActivityI connectedActivity) {
        this.connectedActivity = connectedActivity;
    }

    /**
     * @param uri
     * @return
     * @throws Exception
     */
    protected HttpURLConnection getHttpUrlConnection(String uri) throws Exception {
        if (context == null) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
            AssetManager assetManager = connectedActivity.getAssets();
            InputStream caInput = assetManager.open("raspberrypi.crt");
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.d(TvPcActivity.TAG, "Ouverture du certificat " + "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {

                caInput.close();
            }
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        URL url = new URL(getBaseURL() + uri);
        if (url.toString().startsWith("https:")) {
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            return urlConnection;
        } else {
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();
            return urlConnection;
        }
    }

    /**
     * Return the url depending on the location (internet or LAN)
     * @return
     */
    protected String getBaseURL() {
        ConnectivityManager cm = (ConnectivityManager) connectedActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        int netType = info.getType();
        Log.i(TvPcActivity.TAG, "NET Type " + netType + "  " + ConnectivityManager.TYPE_MOBILE + "  " + ConnectivityManager.TYPE_MOBILE_DUN + "  " + ConnectivityManager.TYPE_WIFI + " " + info.getExtraInfo() + " " + info.getSubtypeName());
        if (netType == ConnectivityManager.TYPE_WIFI) {
            Log.i(TvPcActivity.TAG, "Network is ok");
            String extraInfo = info.getExtraInfo();
            if (extraInfo.contains("B1B6")) {
                return AppConstants.HTTP_RESEAU_LOCAL;
            }
        }
        Log.i(TvPcActivity.TAG, " on WAN");
        return AppConstants.HTTP_RESEAU_INET;


    }
}
