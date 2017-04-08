package com.clement.tvscheduler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.object.Todo;
import com.clement.tvscheduler.task.todo.AddTodoTask;

public class CreateTaskActivity extends AppCompatActivity implements ConnectedActivity {

    public final static String TAG = "MainActivity";


    private Button createTodoBtn;

    private EditText todoAjoutEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
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
    }

    /**
     * Intitialistation des composants
     */
    private void init() {
        createTodoBtn = (Button) findViewById(R.id.todo_ajout_btn);

        todoAjoutEdt = (EditText) findViewById(R.id.todo_ajout_edt);
        createTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo todo = new Todo();
                todo.setName(todoAjoutEdt.getText().toString());
                AddTodoTask addTodoTask = new AddTodoTask(CreateTaskActivity.this, todo);
                addTodoTask.execute();
            }
        });
    }


    public void todoEnregistre() {
        todoAjoutEdt.setText("");
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);

    }

    @Override
    public void showMessage(String message) {

    }
}
