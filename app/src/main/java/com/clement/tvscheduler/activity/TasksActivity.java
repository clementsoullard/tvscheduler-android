package com.clement.tvscheduler.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.activity.adapter.TodosAdapter;
import com.clement.tvscheduler.object.Todo;
import com.clement.tvscheduler.task.todo.AddTodoTask;
import com.clement.tvscheduler.task.todo.ListTodoTask;
import com.clement.tvscheduler.task.todo.RemoveTodoTask;

import java.util.List;

public class TasksActivity extends AppCompatActivity implements ConnectedActivityI, TaskListActivityI {

    public final static String TAG = "TasksActivity";


    private Button createTodoBtn;

    private EditText todoAjoutEdt;

    private ListView listViewTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
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
        refreshTaskList();

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
                AddTodoTask addTodoTask = new AddTodoTask(TasksActivity.this, todo);
                addTodoTask.execute();
            }
        });

        listViewTasks = (ListView) findViewById(R.id.listTasks);

    }


    public void todoEnregistre() {
        todoAjoutEdt.setText("");
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, upIntent);

    }

    public void setTasks(List<Todo> todos) {
        ListAdapter listAdapter = new TodosAdapter(todos, this, listViewTasks);
        listViewTasks.setAdapter(listAdapter);
        listViewTasks.setEmptyView(findViewById(R.id.empty_todos_view));
    }

    @Override
    public void askConfirmationBeforeRemoving(final String id, String name) {
        {
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Etes vous sur de vouloir supprimer " + name + " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            RemoveTodoTask removeAchatTask = new RemoveTodoTask(TasksActivity.this, id);
                            removeAchatTask.execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public void showMessage(String message) {
    }

    @Override
    public void refreshTaskList() {
        ListTodoTask listTodoTask = new ListTodoTask(this, "home");
        listTodoTask.execute();
    }

    @Override
    public void setTodos(List<Todo> todos) {
        ListAdapter listAdapter = new TodosAdapter(todos, this, listViewTasks);
        listViewTasks.setAdapter(listAdapter);
        listViewTasks.setEmptyView(findViewById(R.id.empty_todos_view));
    }
}
