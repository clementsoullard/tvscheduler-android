package com.clement.tvscheduler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clement.tvscheduler.dialog.PinDialog;
import com.clement.tvscheduler.task.CreditTask;
import com.clement.tvscheduler.task.PunitionTask;
import com.clement.tvscheduler.task.TVStatusTask;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends FragmentActivity {

    public final static String TAG = "MainActivity";
    public static final String HTTP_RESEAU_LOCAL = "http://192.168.43.109/";

    public static final DateFormat dfs = new SimpleDateFormat("EEE dd, hh:mm");

    public static final String HTTP_RESEAU_INET = "https://www.cesarsuperstar.com/";
    DecimalFormat df = new DecimalFormat("##");
    private Button tvOn;
    private Button tvOff;
    private Button tvCredit30;
    private Button punition;
    private Button prive;
    private Button recompense;
    private Button tvCredit60;
    private TextView remainingTimeView;
    private TextView relayStatusView;
    private TextView nextCreditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        TVStatusTask tvStatusTask = new TVStatusTask(this, getBaseURL());
        Log.i(TAG, "Update TV status onCreate");
        tvStatusTask.execute();

    }

    /**
     * Intitialistation des composants
     */
    private void init() {
        tvOn = (Button) findViewById(R.id.button_on);
        tvOff = (Button) findViewById(R.id.button_off);
        tvCredit30 = (Button) findViewById(R.id.button_30);
        tvCredit60 = (Button) findViewById(R.id.button_60);
        punition = (Button) findViewById(R.id.button_punition);
        prive = (Button) findViewById(R.id.button_prive);
        recompense = (Button) findViewById(R.id.button_recompense);
        remainingTimeView = (TextView) findViewById(R.id.remainingTime_view);
        relayStatusView = (TextView) findViewById(R.id.relayStatus_view);
        nextCreditView = (TextView) findViewById(R.id.nextCredit_view);

        tvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV ON");
                requestServerCredit(-2);

            }
        });
        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV Off");
                requestServerCredit(-1);
            }
        });
        tvCredit30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV 30");
                requestServerCredit(1800);
            }
        });
        tvCredit60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV 60");
                requestServerCredit(3600);
            }
        });
        punition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur punition");
                requestServerPunition(-20);
            }
        });
        prive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur punition");
                requestServerPunition(-1000);

            }
        });

        recompense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur punition");
                requestServerPunition(20);
            }
        });

    }

    /**
     * @param message
     */
    public void credited(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * @param timeRemaining
     */

    public void setTimeRemaining(String timeRemaining) {
        remainingTimeView.setText(timeRemaining);
    }

    public void setRelayStatus(String relayStatus) {
        relayStatusView.setText(relayStatus);
    }

    public void setNextCredit(Date nextCredit) {
        relayStatusView.setText(dfs.format(nextCredit));
    }

    private String getBaseURL() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        int netType = info.getType();
        Log.i(TAG, "NET Type " + netType + "  " + ConnectivityManager.TYPE_MOBILE + "  " + ConnectivityManager.TYPE_MOBILE_DUN + "  " + ConnectivityManager.TYPE_WIFI);
        if (netType == ConnectivityManager.TYPE_WIFI) {
            Log.i(TAG, "Network is ok");
            return HTTP_RESEAU_LOCAL;

        } else {
            Log.i(TAG, " on WAN");
            return HTTP_RESEAU_INET;
        }

    }

    /**
     * Reauest a credit to the server
     *
     * @param credit
     */
    void requestServerCredit(int credit) {
        int netType;
        CreditTask creditTask = new CreditTask(MainActivity.this, getBaseURL());
        enterPin(creditTask);
    }


    /**
     * Reauest a credit to the server
     *
     * @param punition
     */
    void requestServerPunition(int punition) {
        PunitionTask puntionTask = new PunitionTask(MainActivity.this, getBaseURL());
        enterPin(puntionTask);
    }

    /**
     * This creates a dialog to enter the pin
     */
    public void enterPin(AsyncTask asyncTask) {
        Double d = Math.ceil(Math.random() * 100);
        int random = d.intValue();
        String midPin = df.format(random);
        PinDialog newFragment = new PinDialog();
        newFragment.setMidPin(midPin);
        newFragment.setAsyncTask(asyncTask);
        FragmentManager fm = getSupportFragmentManager();
        newFragment.show(fm, "pin");
    }

    /**
     * This is a callback function after the right pin has been entered.
     *
     * @param midPin
     * @param valueEntered
     * @param asyncTask
     */
    public void checkPin(String midPin, String valueEntered, AsyncTask asyncTask) {
        String expectedResult = "1" + midPin + "1";
        if (expectedResult.equals(valueEntered)) {
            Log.i(TAG, "La bonne valeur a ete entree");
            asyncTask.execute();
        } else {
            Log.i(TAG, "La mauvaise valeur a ete entree");
        }
    }


//
//      URL url = new URL("https://www.cesarsuperstar.com/");
//        HttpsURLConnection urlConnection =
//                (HttpsURLConnection)url.openConnection();
//      // urlConnection.setSSLSocketFactory(context.getSocketFactory());
//
//
//     InputStream in = urlConnection.getInputStream();
//
//    }
}
