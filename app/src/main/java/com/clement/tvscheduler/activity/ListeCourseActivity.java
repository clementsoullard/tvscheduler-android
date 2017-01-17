package com.clement.tvscheduler.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.clement.tvscheduler.CoursesAdapter;
import com.clement.tvscheduler.R;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.ListAchatTask;

import java.util.List;

public class ListeCourseActivity extends AppCompatActivity implements ConnectedActivity {

    public final static String TAG = "MainActivity";

    private ListView listViewAchats;
    private Button achatAjoutBtn;
    private EditText achatAjoutEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_course);
        init();
        Log.d(TAG, "Passage sur on create");
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.my_child_toolbar);
        // setSupportActionBar(myChildToolbar);

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

    }

    /**
     * Intitialistation des composants
     */
    private void init() {
        listViewAchats = (ListView) findViewById(R.id.list_courses_lst);

    }


    public void setAchats(List<Achat> achats) {
        ListAdapter listAdapter = new CoursesAdapter(achats, this, listViewAchats);
        listViewAchats.setAdapter(listAdapter);
        listViewAchats.setEmptyView(findViewById(R.id.empty_courses_view));
        achatAjoutBtn = (Button) findViewById(R.id.achat_ajout_btn);
        achatAjoutEdt = (EditText) findViewById(R.id.achat_ajout_edt);
        achatAjoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click sur le bouton ajout");
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


}
