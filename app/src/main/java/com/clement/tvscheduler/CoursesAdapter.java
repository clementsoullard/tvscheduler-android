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

import com.clement.tvscheduler.activity.ListeCourseActivity;
import com.clement.tvscheduler.activity.MainActivity;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.UpdateAchatTask;

import java.util.List;

/**
 * Created by cleme on 30/10/2016.
 */
public class CoursesAdapter implements ListAdapter {

    List<Achat> achats;

    ListeCourseActivity listeCourseActivity;

    ListView listView;

    public CoursesAdapter(List<Achat> achats, ListeCourseActivity listeCourseActivity, ListView parentView) {
        this.achats = achats;
        this.listeCourseActivity = listeCourseActivity;
        this.listView = parentView;
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
        return achats.size();
    }

    @Override
    public Object getItem(int position) {
        return achats.get(position);
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
            LayoutInflater inflater = (LayoutInflater) listeCourseActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.todo_item, null);
        } else {
            rowView = convertView;
        }

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(achats.get(position).getName());
        final Achat achat = achats.get(position);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        checkBox.setChecked(achat.getDone());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                achat.setDone(checkBox.isChecked());
                UpdateAchatTask updateAchatTask=new UpdateAchatTask(listeCourseActivity,achat);
                updateAchatTask.execute();
                Log.i(MainActivity.TAG, "Click sur la tâche " + achat.getId());
            }
        });

        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return achats.size();
    }

    @Override
    public boolean isEmpty() {
        return achats.isEmpty();
    }
}
