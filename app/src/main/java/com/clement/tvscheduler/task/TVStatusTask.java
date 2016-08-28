package com.clement.tvscheduler.task;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.clement.tvscheduler.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clément on 09/07/2016.
 */
public class TVStatusTask extends AsyncTask<Integer, Integer, Long> {

    private static final String TAG = "TVStatus";

    private MainActivity mainActivity;

    private String messageRetour;

    TVStatus tvStatus;

    public TVStatusTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected Long doInBackground(Integer... params) {
        try {
            Log.i(TAG, "Execution avant");
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://192.168.1.20/tvscheduler/tvstatus").openConnection();
            InputStream is=urlConnection.getInputStream();
            tvStatus=readJsonStream(is);
            messageRetour="Succès";
            Log.i(TAG, "Execution après");
            return 0L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageRetour="Service non disponible";
        return null;
    }


    @Override
    protected void onPostExecute(Long aLong) {
        mainActivity.credited(messageRetour);
        mainActivity.setTimeRemaining(tvStatus.getRemainingTime());
        if(tvStatus.getStatusRelay()) {
            mainActivity.setRelayStatus("Tele autorisée");
        }else{
            mainActivity.setRelayStatus("Tele non autorisée");
        }
    }

    public TVStatus readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {

            return readTvStatus(reader);
        } finally {
            reader.close();
        }
    }



    public List<Double> readDoublesArray(JsonReader reader) throws IOException {
        List<Double> doubles = new ArrayList<Double>();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    public TVStatus readTvStatus(JsonReader reader) throws IOException {
        Log.i(TAG, "Decryptage du TV status");

        TVStatus tvStatus=new TVStatus();

        String remainingTime = null;
        Boolean relayStatus = false;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.i(TAG, "Decryptage du TV status cahpite name "+name);

            if (name.equals("remainingTime")) {
                remainingTime = reader.nextString();
            } else if (name.equals("relayStatus")) {
                relayStatus = reader.nextBoolean();
            } else {
                reader.skipValue();
            }
        }
        tvStatus.setRemainingSecond(remainingTime);
        tvStatus.setStatusRelay(relayStatus);
        reader.endObject();
        return tvStatus;
    }
}