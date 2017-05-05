package com.clement.tvscheduler.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.activity.adapter.TasksAdapter;
import com.clement.tvscheduler.dialog.PinDialog;
import com.clement.tvscheduler.task.BaseTask;
import com.clement.tvscheduler.task.tvpc.ChangeLoginAuthorizationTask;
import com.clement.tvscheduler.task.tvpc.CreditTask;
import com.clement.tvscheduler.task.task.ListTodoTask;
import com.clement.tvscheduler.task.tvpc.PunitionTask;
import com.clement.tvscheduler.task.tvpc.TVStatusTask;
import com.clement.tvscheduler.object.Task;
import com.clement.tvscheduler.task.task.RemoveTodoTask;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The main display containing the essential feature to display
 */
public class TvPcActivity extends AppCompatActivity implements ConnectedActivityI {

    public final static String TAG = "TvPcActivity";

    public static final DateFormat datFormatSimple;

    static {
        datFormatSimple = new SimpleDateFormat("EEE dd, HH:mm", Locale.FRANCE);
        datFormatSimple.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    }


    DecimalFormat midPinFormat = new DecimalFormat("00");

    private Button tvOn;

    private Button tvOff;

    private Button tvOnPc;

    private Button tvOffPc;

    private Button tvCredit30;

    private Button punition;

    private Button prive;

    private Button recompense;

    private Button tvCredit60;

    private TextView remainingTimeView;

    private TextView relayStatusView;

    private TextView tvStatusView;

    private TextView userConnectedView;

    private TextView nextCreditView;

    private TextView consumedTodayView;

    //  private ListView listViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        TVStatusTask tvStatusTask = new TVStatusTask(this);
        Log.d(TAG, "Passage sur on create");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        /**
         * Création de la toolbar
         */
        myToolbar.setTitle("Distribaffe");
        myToolbar.setSubtitle("Pour enfants gentils et méchants");
        setSupportActionBar(myToolbar);
        //  Cheecking tasks
        tvStatusTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.create_new:
                i = new Intent(TvPcActivity.this, TasksActivity.class);
                startActivity(i);
                return true;
            case R.id.liste_course:
                i = new Intent(TvPcActivity.this, ListeCourseActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TVStatusTask tvStatusTask = new TVStatusTask(this);
        Log.d(TAG, "Passage sur on resume");
        tvStatusTask.execute();

        //refreshTaskList();

    }

    /**
     * Refresh the tasks from the server
     */
//    public void refreshTaskList() {
//        ListTodoTask listTodoTask = new ListTodoTask(this, "César");
//        listTodoTask.execute();
//    }

    /**
     * Intitialistation des composants
     */
    private void init() {
        tvOn = (Button) findViewById(R.id.button_on);
        tvOnPc = (Button) findViewById(R.id.button_onpc);
        tvOff = (Button) findViewById(R.id.button_off);
        tvOffPc = (Button) findViewById(R.id.button_offpc);
        tvCredit30 = (Button) findViewById(R.id.button_30);
        tvCredit60 = (Button) findViewById(R.id.button_60);
        punition = (Button) findViewById(R.id.button_punition);
        prive = (Button) findViewById(R.id.button_prive);
        recompense = (Button) findViewById(R.id.button_recompense);
        remainingTimeView = (TextView) findViewById(R.id.remainingTime_view);
        relayStatusView = (TextView) findViewById(R.id.relayStatus_view);
        nextCreditView = (TextView) findViewById(R.id.nextCredit_view);
        userConnectedView = (TextView) findViewById(R.id.connectedUser_view);
        consumedTodayView = (TextView) findViewById(R.id.consumedToday_view);
        tvStatusView = (TextView) findViewById(R.id.tvStatus_view);
        //  listViewTasks = (ListView) findViewById(R.id.listTodos);

        tvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV ON");
                creditTv(-2);

            }
        });
        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV Off");
                creditTv(-1);
            }
        });
        tvOnPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click Enable Pc");
                enableUser(true);
            }
        });
        tvOffPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click diablse");
                enableUser(false);
            }
        });
        tvCredit30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV 30");
                creditTv(1800);
            }
        });
        tvCredit60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV 60");
                creditTv(3600);
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
    public void showMessage(String message) {
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

    public void setConsumedToday(String relayStatus) {
        consumedTodayView.setText(relayStatus);
    }

    public void setNextCredit(Date nextCredit, Integer numberOfMinutes) {
        nextCreditView.setText(numberOfMinutes.toString() + "mn credite le " + datFormatSimple.format(nextCredit));
    }

    public void setConnectedUser(String connectedUser) {
        userConnectedView.setText(connectedUser);
    }

    public void setTvStatus(String tvStatus) {
        tvStatusView.setText(tvStatus);
    }


    /**
     * Reauest a credit to the server
     *
     * @param credit
     */
    void creditTv(int credit) {
        int netType;
        CreditTask creditTask = new CreditTask(TvPcActivity.this, credit);
        enterPin(creditTask);
    }

    /**
     * Enable or diable a user on a PC
     *
     * @param enable
     */
    void enableUser(boolean enable) {
        int netType;
        ChangeLoginAuthorizationTask creditTask = new ChangeLoginAuthorizationTask(TvPcActivity.this, enable);
        enterPin(creditTask);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Reauest a credit to the server
     *
     * @param punition
     */
    void requestServerPunition(int punition) {
        PunitionTask puntionTask = new PunitionTask(TvPcActivity.this, punition);
        enterPin(puntionTask);
    }

    /**
     * This creates a dialog to enter the pin
     */
    public void enterPin(BaseTask asyncTask) {
        Double d = Math.ceil(Math.random() * 100);
        int random = d.intValue();
        String midPin = midPinFormat.format(random);
        PinDialog pinFragment = new PinDialog();
        pinFragment.setMidPin(midPin);
        pinFragment.setTaskToExecuteAfterCorrectPin(asyncTask);
        FragmentManager fm = getSupportFragmentManager();
        pinFragment.show(fm, "pin");
    }

    /**
     * This is a callback function after the correct pin has been entered.
     *
     * @param midPin
     * @param valueEntered
     * @param taskToExecuteAfterCorrectPin
     */
    public void checkPin(String midPin, String valueEntered, BaseTask taskToExecuteAfterCorrectPin) {
        String expectedResult = "1" + midPin + "1";
        if (expectedResult.equals(valueEntered)) {
            Log.i(TAG, "La bonne valeur a ete entree");
            taskToExecuteAfterCorrectPin.execute();
        } else {
            Log.i(TAG, "La mauvaise valeur a ete entree");
        }
    }


//    public void setTodos(List<Task> tasks) {
//
//        ListAdapter listAdapter = new TasksAdapter(tasks, TvPcActivity.this, listViewTasks);
//        listViewTasks.setAdapter(listAdapter);
//        listViewTasks.setEmptyView(findViewById(R.id.empty_todos_view));
//    }


}
