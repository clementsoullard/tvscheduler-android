package com.clement.tvscheduler;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clement.tvscheduler.task.CreditTask;
import com.clement.tvscheduler.task.PunitionTask;
import com.clement.tvscheduler.task.TVStatusTask;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    private Button tvOn;
    private Button tvOff;
    private Button tvCredit30;
    private Button punition;
    private Button prive;
    private Button recompense;
    private Button tvCredit60;
    private TextView remainingTimeView;
    private TextView relayStatusView;

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
        punition=(Button) findViewById(R.id.button_punition);
        prive=(Button) findViewById(R.id.button_prive);
        recompense=(Button) findViewById(R.id.button_recompense);
        remainingTimeView = (TextView) findViewById(R.id.remainingTime_view);
        relayStatusView = (TextView) findViewById(R.id.relayStatus_view);

        tvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditTask creditTask = new CreditTask(MainActivity.this);
                Log.i(TAG, "Click sur TV ON");
                creditTask.execute(-2);
            }
        });
        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditTask creditTask = new CreditTask(MainActivity.this);
                Log.i(TAG, "Click sur TV Off");
                creditTask.execute(-1);
            }
        });
        tvCredit30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditTask creditTask = new CreditTask(MainActivity.this);
                Log.i(TAG, "Click sur TV 30");
                creditTask.execute(1800);
            }
        });
        tvCredit60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditTask creditTask = new CreditTask(MainActivity.this);
                Log.i(TAG, "Click sur TV 60");
                creditTask.execute(3600);
            }
        });
        punition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PunitionTask puntionTask = new PunitionTask(MainActivity.this);
                Log.i(TAG, "Click sur punition");
                puntionTask.execute(-20);
            }
        });
        prive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PunitionTask puntionTask = new PunitionTask(MainActivity.this);
                Log.i(TAG, "Click sur punition");
                puntionTask.execute(-1000);
            }
        });

        recompense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PunitionTask puntionTask = new PunitionTask(MainActivity.this);
                Log.i(TAG, "Click sur punition");
                puntionTask.execute(20);
            }
        });

        TVStatusTask tvStatusTask=new TVStatusTask(this);
        tvStatusTask.execute();
    }

    public void credited(String message) {
        Context context = getApplicationContext();
        CharSequence text =message;
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
}
