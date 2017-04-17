package com.clement.tvscheduler.activity.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.TVSchedulerConstants;
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

    static int positionStartSwiping = -1;

    static float positionStartSwipingX = -1F;


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
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
        final View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mainActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.todo_item, null);
            rowView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = MotionEventCompat.getActionMasked(event);

                    switch (action) {
                        case (MotionEvent.ACTION_DOWN):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Action was DOWN in " + position);
                            positionStartSwiping = position;
                            positionStartSwipingX = event.getX();

                            return true;
                        case (MotionEvent.ACTION_MOVE):
                            //     Log.d(DEBUG_TAG, "Action was MOVE for  " + position + " at X=" + event.getX());
                            return true;
                        case (MotionEvent.ACTION_UP):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Action was UP in " + position);
                            if (position == positionStartSwiping) {
                                if (event.getX() - positionStartSwipingX > 0) {
                                    Log.d(TVSchedulerConstants.DEBUG_TAG, "The swipe was done left to right");
                                    Todo todo = todos.get(position);
                                    Log.d(TVSchedulerConstants.DEBUG_TAG, "Suppression de " + todo.getName());
                                    mainActivity.askConfirmationBeforeRemoving(todo.getId(), todo.getName());
                                }
                            }
                            return true;
                        case (MotionEvent.ACTION_CANCEL):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Action was CANCEL in " + position);
                            /** In case the swipe could not end, we reset the swipe*/
                            positionStartSwiping = -1;
                            return true;
                        case (MotionEvent.ACTION_OUTSIDE):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Movement occurred outside bounds " +
                                    "of current screen element");
                            /** In case the swipe could not end, we reset the swipe*/
                            positionStartSwiping = -1;
                            return true;
                        default:
                            return rowView.onTouchEvent(event);
                    }
                }
            });
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
                UpdateTodoTask updateTodoTask = new UpdateTodoTask(mainActivity, todo);
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
     *
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
