package com.clement.tvscheduler;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clement.tvscheduler.dialog.PinDialog;
import com.clement.tvscheduler.task.CreditTask;
import com.clement.tvscheduler.task.PunitionTask;
import com.clement.tvscheduler.task.TVStatusTask;

import java.text.DecimalFormat;

public class MainActivity extends FragmentActivity {

    public final static String TAG = "MainActivity";
    public static final String HTTP_RESEAU_LOCAL = "http://192.168.1.20/";

    public static final String HTTP_RESEAU_INET = "http://www.cesarsuperstar.com/";

    private Button tvOn;

    private Button tvOff;

    private Button tvCredit30;

    private Button punition;

    private Button prive;

    private Button recompense;

    private Button tvCredit60;

    private TextView remainingTimeView;

    private TextView relayStatusView;

    DecimalFormat df=new DecimalFormat("##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
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

        TVStatusTask tvStatusTask = new TVStatusTask(this);
        tvStatusTask.execute();
    }

    public void credited(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void setTimeRemaining(String timeRemaining) {
        remainingTimeView.setText(timeRemaining);
    }

    public void setRelayStatus(String relayStatus) {
        relayStatusView.setText(relayStatus);
    }

    /**
     * Reauest a credit to the server
     *
     * @param credit
     */
    void requestServerCredit(int credit) {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
              int netType = info.getType();

        if (netType == ConnectivityManager.TYPE_WIFI) {
            Log.i(TAG, "Network is ok");
            CreditTask creditTask = new CreditTask(MainActivity.this,HTTP_RESEAU_LOCAL);
            enterPin(creditTask);
        }

        else if(netType == ConnectivityManager.TYPE_MOBILE) {
            CreditTask creditTask = new CreditTask(MainActivity.this,HTTP_RESEAU_INET);
            enterPin(creditTask);

            Toast toast = Toast.makeText(this, "Pas de reseau", Toast.LENGTH_LONG);
            toast.show();
            Log.i(TAG, "There is no network doing nothing");
        }
    }

    /**
     * Reauest a credit to the server
     *
     * @param punition
     */
    void requestServerPunition(int punition) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            Log.i(TAG, "Network is ok");
            PunitionTask puntionTask = new PunitionTask(MainActivity.this, HTTP_RESEAU_LOCAL);
             enterPin(puntionTask);

        } else {
            PunitionTask puntionTask = new PunitionTask(MainActivity.this, HTTP_RESEAU_INET);
            enterPin(puntionTask);
            Log.i(TAG, "There is no network doing nothing");
        }

    }

    /**
     * This creates a dialog to enter the pin
     */
    public void enterPin(AsyncTask asyncTask) {
        Double d=Math.ceil(Math.random()*100);
        int random=d.intValue();
        String midPin=df.format(random);
        PinDialog newFragment = new PinDialog();
        newFragment.setMidPin(midPin);
        newFragment.setAsyncTask(asyncTask);
        FragmentManager fm=getSupportFragmentManager();
        newFragment.show(fm,"pin");
    }

    public void checkPin(String midPin,String valueEntered,AsyncTask asyncTask) {
        String expectedResult="1"+midPin+"1";
        if(expectedResult.equals(valueEntered)){
            Log.i(TAG, "La bonne valeur a ete entree");
            asyncTask.execute();
        }else{
            Log.i(TAG, "La mauvaise valeur a ete entree");
        }
    }
}
