package com.clement.tvscheduler.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.clement.tvscheduler.activity.TvPcActivity;
import com.clement.tvscheduler.R;
import com.clement.tvscheduler.task.BaseTask;

/**
 *
 */
public class PinDialog extends android.support.v4.app.DialogFragment {

    private String midPin;

    private BaseTask baseTask;


    public void setMidPin(String midPin) {
        this.midPin = midPin;
    }

    public void setTaskToExecuteAfterCorrectPin(BaseTask baseTask) {
        this.baseTask = baseTask;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //   TextView textView=(TextView) container.findViewById(R.id.midPin);
        Log.i(TvPcActivity.TAG, "On Create View " + container);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TvPcActivity.TAG, "On Create Dialog");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_pin, null);
        TextView tv = (TextView) v.findViewById(R.id.midPin);
        final EditText et = (EditText) v.findViewById(R.id.editTextPin);
        tv.setText(midPin);
        builder.setView(v)
                .setPositiveButton(R.string.pin, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TvPcActivity tvPcActivity = (TvPcActivity) getActivity();
                        tvPcActivity.checkPin(midPin, et.getText().toString(), baseTask);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        final Dialog dialog = builder.create();
        return dialog;
    }


}