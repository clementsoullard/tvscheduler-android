package com.clement.tvscheduler.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.clement.tvscheduler.activity.adapter.CoursesAdapter;
import com.clement.tvscheduler.R;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.achat.AddAchatTask;
import com.clement.tvscheduler.task.achat.ListAchatTask;
import com.clement.tvscheduler.task.achat.ListSuggestAchatTask;
import com.clement.tvscheduler.task.achat.RemoveAchatTask;

import java.util.List;

public class ListeCourseActivity extends AppCompatActivity implements ConnectedActivityI {

    public final static String TAG = "TvPcActivity";

    private ListView listViewAchats;
    /**
     * the button to add the purchase
     */
    private Button achatAjoutBtn;
    /**
     * The drop down to list the potential items
     */
    private AutoCompleteTextView achatAjoutEdt;

    /**
     * The suggested list for the pruchase
     */
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_course);
        init();
        Log.d(TAG, "Passage sur on create");
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.my_child_toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListAchatTask listAchatTask = new ListAchatTask(this);
        listAchatTask.execute();
        getSuggest();
    }

    /**
     * Intitialistation des composants
     */
    private void init() {
        listViewAchats = (ListView) findViewById(R.id.list_courses_lst);
        achatAjoutEdt = (AutoCompleteTextView) findViewById(R.id.achat_ajout_edt);
    }

    /**
     * Called once the achat are retrived from the database
     *
     * @param achats
     */

    public void setAchats(List<Achat> achats) {
        ListAdapter listAdapter = new CoursesAdapter(achats, this, listViewAchats);

        listViewAchats.setAdapter(listAdapter);
        listViewAchats.setEmptyView(findViewById(R.id.empty_courses_view));
        achatAjoutBtn = (Button) findViewById(R.id.achat_ajout_btn);
        achatAjoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click sur le bouton ajout");
                Achat achat = new Achat();
                achat.setName(achatAjoutEdt.getText().toString());
                AddAchatTask addAchatTask = new AddAchatTask(ListeCourseActivity.this, achat);
                addAchatTask.execute();
            }
        });
    }

    /**
     * Called once the suggestion list is retrived from the database
     *
     * @param suggestions
     */

    public void setSuggestAchats(List<String> suggestions) {
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, suggestions);
        achatAjoutEdt.setAdapter(adapter);
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
     * This asks a confirmation before removing an item
     *
     * @param achatId
     * @param achatName
     */
    public void askConfirmationBeforeRemoving(final String achatId, String achatName) {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Etes vous sur de vouloir supprimer " + achatName + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RemoveAchatTask removeAchatTask = new RemoveAchatTask(ListeCourseActivity.this, achatId);
                        removeAchatTask.execute();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * This retrieve the purchase recorded
     */
    public void refreshListeAchat() {
        ListAchatTask listAchatTask = new ListAchatTask(this);
        listAchatTask.execute();
        achatAjoutEdt.setText("");
    }

    /**
     * This retrieve the purchase recorded
     */
    public void getSuggest() {
        ListSuggestAchatTask listAchatTask = new ListSuggestAchatTask(this);
        listAchatTask.execute();

    }

    /**
     *
     */
    public void achatTermine() {
        ListAchatTask listAchatTask = new ListAchatTask(this);
        listAchatTask.execute();
    }


}

