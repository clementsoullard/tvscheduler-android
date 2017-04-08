package com.clement.tvscheduler;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clement.tvscheduler.activity.MainActivity;
import com.clement.tvscheduler.object.Todo;
import com.clement.tvscheduler.task.todo.UpdateTodoTask;

import java.util.List;

/**
 * Created by cleme on 30/10/2016.
 */
public class TodosAdapter implements ListAdapter {

    private List<Todo> todos;

    private MainActivity mainActivity;
    private ListView listViewTodos;

    public TodosAdapter(List<Todo> todos, MainActivity mainActivity, ListView parentView) {
        this.todos = todos;
        this.mainActivity = mainActivity;
        this.listViewTodos = parentView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Object getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mainActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.todo_item, null);
        } else {
            rowView = convertView;
        }

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(todos.get(position).getName());
        final Todo todo = todos.get(position);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        checkBox.setChecked(todo.getDone());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setDone(checkBox.isChecked());
                UpdateTodoTask updateTodoTask=new UpdateTodoTask(mainActivity,todo);
                updateTodoTask.execute();
                Log.i(MainActivity.TAG, "Click sur la tâche " + todo.getId());
            }
        });

        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * Returns the number of different biew in the list (In case of similar element this is 1
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return todos.isEmpty();
    }
}
